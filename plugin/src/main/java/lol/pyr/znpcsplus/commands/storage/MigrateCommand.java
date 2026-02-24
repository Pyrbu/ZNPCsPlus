package lol.pyr.znpcsplus.commands.storage;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lol.pyr.director.adventure.command.CommandContext;
import lol.pyr.director.adventure.command.CommandHandler;
import lol.pyr.director.common.command.CommandExecutionException;
import lol.pyr.znpcsplus.ZNpcsPlus;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.interaction.ActionRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcTypeRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.serialization.NpcSerializerRegistryImpl;
import lol.pyr.znpcsplus.storage.NpcStorage;
import lol.pyr.znpcsplus.storage.NpcStorageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class MigrateCommand implements CommandHandler {
  private final ConfigManager configManager;
  private final ZNpcsPlus plugin;
  private final PacketFactory packetFactory;
  private final ActionRegistryImpl actionRegistry;
  private final NpcTypeRegistryImpl typeRegistry;
  private final EntityPropertyRegistryImpl propertyRegistry;
  private final LegacyComponentSerializer textSerializer;
  private final NpcStorage currentStorage;
  private final NpcStorageType currentStorageType;
  private final NpcRegistryImpl npcRegistry;
  private final NpcSerializerRegistryImpl serializerRegistry;

  public MigrateCommand(
      ConfigManager configManager,
      ZNpcsPlus plugin,
      PacketFactory packetFactory,
      ActionRegistryImpl actionRegistry,
      NpcTypeRegistryImpl typeRegistry,
      EntityPropertyRegistryImpl propertyRegistry,
      LegacyComponentSerializer textSerializer,
      NpcStorage currentStorage,
      NpcStorageType currentStorageType,
      NpcRegistryImpl npcRegistry,
      NpcSerializerRegistryImpl serializerRegistry) {
    this.configManager = configManager;
    this.plugin = plugin;
    this.packetFactory = packetFactory;
    this.actionRegistry = actionRegistry;
    this.typeRegistry = typeRegistry;
    this.propertyRegistry = propertyRegistry;
    this.textSerializer = textSerializer;
    this.currentStorage = currentStorage;
    this.currentStorageType = currentStorageType;
    this.npcRegistry = npcRegistry;
    this.serializerRegistry = serializerRegistry;
  }

  @Override
  public void run(CommandContext context) throws CommandExecutionException {
    context.setUsage(context.getLabel() + " storage migrate <from> <to> [force]");
    NpcStorageType from = context.parse(NpcStorageType.class);
    NpcStorageType to = context.parse(NpcStorageType.class);
    boolean force = context.argSize() > 2 && context.parse(Boolean.class);
    if (from.equals(to)) {
      context.halt(Component.text("The storage types must be different.", NamedTextColor.RED));
      return;
    }
    NpcStorage fromStorage;
    if (currentStorageType == from) {
      fromStorage = currentStorage;
    } else {
      fromStorage =
          from.create(
              configManager,
              plugin,
              packetFactory,
              actionRegistry,
              typeRegistry,
              propertyRegistry,
              textSerializer,
              serializerRegistry);
      if (fromStorage == null) {
        context.halt(
            Component.text(
                "Failed to initialize the source storage. Please check the console for more information.",
                NamedTextColor.RED));
        return;
      }
    }
    Collection<NpcEntryImpl> entries;
    try {
      entries = fromStorage.loadNpcs();
    } catch (Exception e) {
      context.halt(
          Component.text("Failed to load NPCs from the source storage.", NamedTextColor.RED));
      return;
    }
    if (entries.isEmpty()) {
      context.send(Component.text("No NPCs to migrate.", NamedTextColor.YELLOW));
      return;
    }
    NpcStorage toStorage;
    if (currentStorageType == to) {
      toStorage = currentStorage;
    } else {
      toStorage =
          to.create(
              configManager,
              plugin,
              packetFactory,
              actionRegistry,
              typeRegistry,
              propertyRegistry,
              textSerializer,
              serializerRegistry);
      if (toStorage == null) {
        context.halt(
            Component.text(
                "Failed to initialize the destination storage. Please check the console for more information.",
                NamedTextColor.RED));
        return;
      }
    }

    Collection<NpcEntryImpl> existingEntries;
    try {
      existingEntries = toStorage.loadNpcs();
    } catch (Exception e) {
      context.halt(
          Component.text("Failed to load NPCs from the destination storage.", NamedTextColor.RED));
      return;
    }
    if (existingEntries.isEmpty()) {
      toStorage.saveNpcs(entries);
      context.send(
          Component.text(
                  "Migrated " + entries.size() + " NPCs from the source storage (",
                  NamedTextColor.GREEN)
              .append(Component.text(from.name(), NamedTextColor.GOLD))
              .append(Component.text(") to the destination storage (", NamedTextColor.GREEN))
              .append(Component.text(to.name(), NamedTextColor.GOLD))
              .append(Component.text(").", NamedTextColor.GREEN)));
      if (currentStorageType == to) {
        npcRegistry.reload();
      } else {
        toStorage.close();
      }
      return;
    }
    if (!force) {
      Collection<NpcEntryImpl> toSave =
          entries.stream()
              .filter(e -> existingEntries.stream().noneMatch(e2 -> e2.getId().equals(e.getId())))
              .collect(Collectors.toList());
      Collection<NpcEntryImpl> idExists =
          entries.stream()
              .filter(e -> existingEntries.stream().anyMatch(e2 -> e2.getId().equals(e.getId())))
              .collect(Collectors.toList());
      if (toSave.isEmpty()) {
        context.send(Component.text("No NPCs to migrate.", NamedTextColor.YELLOW));
        if (currentStorageType != to) {
          toStorage.close();
        }
      } else {
        toStorage.saveNpcs(toSave);
        context.send(
            Component.text(
                    "Migrated " + toSave.size() + " NPCs from the source storage (",
                    NamedTextColor.GREEN)
                .append(Component.text(from.name(), NamedTextColor.GOLD))
                .append(Component.text(") to the destination storage (", NamedTextColor.GREEN))
                .append(Component.text(to.name(), NamedTextColor.GOLD))
                .append(Component.text(").", NamedTextColor.GREEN)));
        if (currentStorageType == to) {
          npcRegistry.reload();
        } else {
          toStorage.close();
        }
      }
      if (!idExists.isEmpty()) {
        AtomicReference<Component> component =
            new AtomicReference<>(
                Component.text(
                        "The following NPCs were not migrated because their IDs already exist in the destination storage:")
                    .color(NamedTextColor.YELLOW));
        idExists.forEach(
            e -> {
              component.set(
                  component
                      .get()
                      .append(Component.newline())
                      .append(Component.text(e.getId(), NamedTextColor.RED)));
            });
        component.set(
            component
                .get()
                .append(Component.newline())
                .append(Component.text("Use the ", NamedTextColor.YELLOW))
                .append(Component.text("force", NamedTextColor.GOLD))
                .append(Component.text(" argument to overwrite them.", NamedTextColor.YELLOW)));
        context.send(component.get());
      }
    } else {
      toStorage.saveNpcs(entries);
      context.send(
          Component.text(
                  "Force migrated " + entries.size() + " NPCs from the source storage (",
                  NamedTextColor.GREEN)
              .append(Component.text(from.name(), NamedTextColor.GOLD))
              .append(Component.text(") to the destination storage (", NamedTextColor.GREEN))
              .append(Component.text(to.name(), NamedTextColor.GOLD))
              .append(Component.text(").", NamedTextColor.GREEN)));
      if (currentStorageType == to) {
        npcRegistry.reload();
      } else {
        toStorage.close();
      }
    }
  }

  @Override
  public List<String> suggest(CommandContext context) throws CommandExecutionException {
    if (context.argSize() == 1) {
      return context.suggestEnum(NpcStorageType.values());
    } else if (context.argSize() == 2) {
      NpcStorageType from = context.suggestionParse(0, NpcStorageType.class);
      if (from == null) return Collections.emptyList();
      return context.suggestCollection(
          Arrays.stream(NpcStorageType.values())
              .filter(t -> t != from)
              .map(Enum::name)
              .collect(Collectors.toList()));
    } else if (context.argSize() == 3) {
      return context.suggestLiteral("true");
    }
    return Collections.emptyList();
  }
}
