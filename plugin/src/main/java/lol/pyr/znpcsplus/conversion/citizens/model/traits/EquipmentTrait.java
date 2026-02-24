package lol.pyr.znpcsplus.conversion.citizens.model.traits;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import com.google.common.io.BaseEncoding;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.conversion.citizens.model.SectionCitizensTrait;
import lol.pyr.znpcsplus.npc.NpcImpl;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.jetbrains.annotations.NotNull;

public class EquipmentTrait extends SectionCitizensTrait {
  private final EntityPropertyRegistry propertyRegistry;
  private final HashMap<String, EquipmentSlot> EQUIPMENT_SLOT_MAP = new HashMap<>();

  public EquipmentTrait(EntityPropertyRegistry propertyRegistry) {
    super("equipment");
    this.propertyRegistry = propertyRegistry;
    EQUIPMENT_SLOT_MAP.put("hand", EquipmentSlot.MAIN_HAND);
    EQUIPMENT_SLOT_MAP.put("offhand", EquipmentSlot.OFF_HAND);
    EQUIPMENT_SLOT_MAP.put("helmet", EquipmentSlot.HELMET);
    EQUIPMENT_SLOT_MAP.put("chestplate", EquipmentSlot.CHEST_PLATE);
    EQUIPMENT_SLOT_MAP.put("leggings", EquipmentSlot.LEGGINGS);
    EQUIPMENT_SLOT_MAP.put("boots", EquipmentSlot.BOOTS);
  }

  @Override
  public @NotNull NpcImpl apply(NpcImpl npc, ConfigurationSection section) {
    for (String key : section.getKeys(false)) {
      EquipmentSlot slot = EQUIPMENT_SLOT_MAP.get(key);
      if (slot == null) {
        continue;
      }

      ItemStack itemStack = parseItemStack(section.getConfigurationSection(key));
      if (itemStack == null) {
        continue;
      }

      EntityProperty<ItemStack> property = propertyRegistry.getByName(key, ItemStack.class);
      npc.setProperty(property, itemStack);
    }

    return npc;
  }

  private ItemStack parseItemStack(ConfigurationSection section) {
    Material material = null;
    if (section.isString("type_key")) {
      material = Material.getMaterial(section.getString("type_key").toUpperCase());
    } else if (section.isString("type")) {
      material = Material.matchMaterial(section.getString("type").toUpperCase());
    } else if (section.isString("id")) {
      material = Material.matchMaterial(section.getString("id").toUpperCase());
    }
    if (material == null || material == Material.AIR) {
      return null;
    }
    org.bukkit.inventory.ItemStack itemStack =
        new org.bukkit.inventory.ItemStack(
            material,
            section.getInt("amount", 1),
            (short) section.getInt("durability", section.getInt("data", 0)));
    if (section.isInt("mdata")) {
      //noinspection deprecation
      itemStack.getData().setData((byte) section.getInt("mdata"));
    }
    if (section.isConfigurationSection("enchantments")) {
      ConfigurationSection enchantments = section.getConfigurationSection("enchantments");
      itemStack.addUnsafeEnchantments(deserializeEnchantments(enchantments));
    }
    if (section.isConfigurationSection("meta")) {
      ItemMeta itemMeta = deserializeMeta(section.getConfigurationSection("meta"));
      if (itemMeta != null) {
        itemStack.setItemMeta(itemMeta);
      }
    }
    return SpigotConversionUtil.fromBukkitItemStack(itemStack);
  }

  private Map<Enchantment, Integer> deserializeEnchantments(ConfigurationSection section) {
    Map<Enchantment, Integer> enchantments = new HashMap<>();
    for (String key : section.getKeys(false)) {
      Enchantment enchantment = Enchantment.getByName(key);
      if (enchantment == null) {
        continue;
      }
      enchantments.put(enchantment, section.getInt(key));
    }
    return enchantments;
  }

  private ItemMeta deserializeMeta(ConfigurationSection section) {
    if (section.isString("encoded-meta")) {
      byte[] raw = BaseEncoding.base64().decode(section.getString("encoded-meta"));
      try {
        BukkitObjectInputStream inp = new BukkitObjectInputStream(new ByteArrayInputStream(raw));
        ItemMeta meta = (ItemMeta) inp.readObject();
        inp.close();
        return meta;
      } catch (IOException | ClassNotFoundException e1) {
        e1.printStackTrace();
      }
    }
    return null;
  }
}
