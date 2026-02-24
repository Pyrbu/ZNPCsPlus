package lol.pyr.znpcsplus.conversion.fancynpcs;

import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.io.File;
import java.util.*;
import lol.pyr.znpcsplus.api.interaction.InteractionType;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.conversion.DataImporter;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.consolecommand.ConsoleCommandAction;
import lol.pyr.znpcsplus.interaction.message.MessageAction;
import lol.pyr.znpcsplus.interaction.playercommand.PlayerCommandAction;
import lol.pyr.znpcsplus.npc.*;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.SkinImpl;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;
import lol.pyr.znpcsplus.util.LookType;
import lol.pyr.znpcsplus.util.NamedColor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class FancyNpcsImporter implements DataImporter {
  private final ConfigManager configManager;
  private final BukkitAudiences adventure;
  private final TaskScheduler scheduler;
  private final PacketFactory packetFactory;
  private final LegacyComponentSerializer textSerializer;
  private final NpcTypeRegistryImpl typeRegistry;
  private final EntityPropertyRegistryImpl propertyRegistry;
  private final MojangSkinCache skinCache;
  private final File dataFile;
  private final NpcRegistryImpl npcRegistry;

  public FancyNpcsImporter(
      ConfigManager configManager,
      BukkitAudiences adventure,
      TaskScheduler taskScheduler,
      PacketFactory packetFactory,
      LegacyComponentSerializer textSerializer,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      MojangSkinCache skinCache,
      File dataFile,
      NpcRegistryImpl npcRegistry) {
    this.configManager = configManager;
    this.adventure = adventure;
    this.scheduler = taskScheduler;
    this.packetFactory = packetFactory;
    this.textSerializer = textSerializer;
    this.typeRegistry = typeRegistry;
    this.propertyRegistry = propertyRegistry;
    this.skinCache = skinCache;
    this.dataFile = dataFile;
    this.npcRegistry = npcRegistry;
  }

  @Override
  public Collection<NpcEntryImpl> importData() {
    YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
    ConfigurationSection npcsSection = config.getConfigurationSection("npcs");
    if (npcsSection == null) {
      return Collections.emptyList();
    }
    ArrayList<NpcEntryImpl> entries = new ArrayList<>();
    npcsSection
        .getKeys(false)
        .forEach(
            key -> {
              ConfigurationSection npcSection = npcsSection.getConfigurationSection(key);
              if (npcSection == null) {
                return;
              }
              String name = npcSection.getString("name", "FancyNPC");
              UUID uuid = UUID.fromString(key);
              String world = npcSection.getString("location.world");
              if (world == null) {
                world = Bukkit.getWorlds().get(0).getName();
              }
              NpcLocation location =
                  new NpcLocation(
                      npcSection.getDouble("location.x"),
                      npcSection.getDouble("location.y"),
                      npcSection.getDouble("location.z"),
                      (float) npcSection.getDouble("location.yaw"),
                      (float) npcSection.getDouble("location.pitch"));
              String typeString = npcSection.getString("type");
              NpcTypeImpl type = typeRegistry.getByName(typeString);
              if (type == null) {
                type = typeRegistry.getByName("player");
              }
              NpcImpl npc =
                  new NpcImpl(
                      uuid,
                      propertyRegistry,
                      configManager,
                      packetFactory,
                      textSerializer,
                      world,
                      type,
                      location);
              npc.getType().applyDefaultProperties(npc);

              npc.getHologram().addTextLineComponent(textSerializer.deserialize(name));
              boolean glowing = npcSection.getBoolean("glowing", false);
              if (glowing) {
                NamedColor color;
                try {
                  color = NamedColor.valueOf(npcSection.getString("glowingColor", "white"));
                } catch (IllegalArgumentException ignored) {
                  color = NamedColor.WHITE;
                }
                EntityPropertyImpl<NamedColor> property =
                    propertyRegistry.getByName("glow", NamedColor.class);
                npc.setProperty(property, color);
              }
              if (npcSection.getBoolean("turnToPlayer", false)) {
                EntityPropertyImpl<LookType> property =
                    propertyRegistry.getByName("look", LookType.class);
                npc.setProperty(property, LookType.CLOSEST_PLAYER);
              }
              if (npcSection.isConfigurationSection("skin")) {
                ConfigurationSection skinSection = npcSection.getConfigurationSection("skin");
                String texture = skinSection.getString("value");
                String signature = skinSection.getString("signature");
                npc.setProperty(
                    propertyRegistry.getByName("skin", SkinDescriptor.class),
                    new PrefetchedDescriptor(new SkinImpl(texture, signature)));
              }
              if (npcSection.isConfigurationSection("equipment")) {
                ConfigurationSection equipmentSection =
                    npcSection.getConfigurationSection("equipment");
                for (String slot : equipmentSection.getKeys(false)) {
                  ItemStack item = equipmentSection.getItemStack(slot);
                  if (item != null) {
                    npc.setProperty(
                        propertyRegistry.getByName(
                            getEquipmentPropertyName(slot),
                            com.github.retrooper.packetevents.protocol.item.ItemStack.class),
                        SpigotConversionUtil.fromBukkitItemStack(item));
                  }
                }
              }
              if (npcSection.getBoolean("mirrorSkin")) {
                npc.setProperty(
                    propertyRegistry.getByName("skin", SkinDescriptor.class),
                    new MirrorDescriptor(skinCache));
              }
              List<String> playerCommands = npcSection.getStringList("playerCommands");
              if (!playerCommands.isEmpty()) {
                long cooldown = npcSection.getLong("interactionCooldown", 0);
                for (String command : playerCommands) {
                  npc.addAction(
                      new PlayerCommandAction(
                          scheduler, command, InteractionType.ANY_CLICK, cooldown, 0));
                }
              }
              String serverCommand = npcSection.getString("serverCommand");
              if (serverCommand != null) {
                long cooldown = npcSection.getLong("interactionCooldown", 0);
                npc.addAction(
                    new ConsoleCommandAction(
                        scheduler, serverCommand, InteractionType.ANY_CLICK, cooldown, 0));
              }
              List<String> messages = npcSection.getStringList("messages");
              if (!messages.isEmpty()) {
                long cooldown = npcSection.getLong("interactionCooldown", 0);
                for (String message : messages) {
                  npc.addAction(
                      new MessageAction(
                          adventure,
                          textSerializer,
                          message,
                          InteractionType.ANY_CLICK,
                          cooldown,
                          0));
                }
              }
              String id = npcSection.getString("name");
              while (npcRegistry.getById(id) != null) {
                id += "_";
              }
              NpcEntryImpl entry = new NpcEntryImpl(id, npc);
              entry.enableEverything();
              entries.add(entry);
            });
    return entries;
  }

  private String getEquipmentPropertyName(String slot) {
    switch (slot) {
      case "MAINHAND":
        return "hand";
      case "OFFHAND":
        return "offhand";
      case "FEET":
        return "boots";
      case "LEGS":
        return "leggings";
      case "CHEST":
        return "chestplate";
      case "HEAD":
        return "helmet";
      default:
        return null;
    }
  }

  @Override
  public boolean isValid() {
    return dataFile.isFile();
  }
}
