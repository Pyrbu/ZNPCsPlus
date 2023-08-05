package lol.pyr.znpcsplus.hologram;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.entity.Player;

import java.util.Collection;

public class HologramItem extends HologramLine<ItemStack> {
    public HologramItem(EntityPropertyRegistryImpl propertyRegistry, PacketFactory packetFactory, NpcLocation location, ItemStack item) {
        super(item, packetFactory, EntityTypes.ITEM, location);
        addProperty(propertyRegistry.getByName("holo_item"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getProperty(EntityProperty<T> key) {
        if (key.getName().equalsIgnoreCase("holo_item")) return (T) getValue();
        return super.getProperty(key);
    }

    @Override
    public void setLocation(NpcLocation location, Collection<Player> viewers) {
        super.setLocation(location.withY(location.getY() + 2.05), viewers);
    }

    public static boolean ensureValidItemInput(String in) {
        if (in == null || in.isEmpty()) {
            return false;
        }

        int indexOfNbt = in.indexOf("{");
        if (indexOfNbt != -1) {
            String typeName = in.substring(0, indexOfNbt);
            ItemType type = ItemTypes.getByName("minecraft:" + typeName.toLowerCase());
            if (type == null) {
                return false;
            }
            String nbtString = in.substring(indexOfNbt);
            return ensureValidNbt(nbtString);
        } else {
            ItemType type = ItemTypes.getByName("minecraft:" + in.toLowerCase());
            return type != null;
        }
    }

    private static boolean ensureValidNbt(String nbtString) {
        JsonElement nbtJson;
        try {
            nbtJson = JsonParser.parseString(nbtString);
        } catch (JsonSyntaxException e) {
            return false;
        }
        try {
            NBTCodec.jsonToNBT(nbtJson);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public static ItemStack deserialize(String serializedItem) {
        int indexOfNbt = serializedItem.indexOf("{");
        String typeName = serializedItem;
        int amount = 1;
        NBTCompound nbt = new NBTCompound();
        if (indexOfNbt != -1) {
            typeName = serializedItem.substring(0, indexOfNbt);
            String nbtString = serializedItem.substring(indexOfNbt);
            JsonElement nbtJson = null;
            try {
                nbtJson = JsonParser.parseString(nbtString);
            } catch (Exception ignored) {
            }
            if (nbtJson != null) {
                nbt = (NBTCompound) NBTCodec.jsonToNBT(nbtJson);
                NBTNumber nbtAmount = nbt.getNumberTagOrNull("Count");
                if (nbtAmount != null) {
                    nbt.removeTag("Count");
                    amount = nbtAmount.getAsInt();
                    if (amount <= 0) amount = 1;
                    if (amount > 127) amount = 127;
                }
            }
        }
        ItemType type = ItemTypes.getByName("minecraft:" + typeName.toLowerCase());
        if (type == null) type = ItemTypes.STONE;
        return ItemStack.builder().type(type).amount(amount).nbt(nbt).build();
    }

    public String serialize() {
        NBTCompound nbt = getValue().getNBT();
        if (nbt == null) nbt = new NBTCompound();
        if (getValue().getAmount() > 1) nbt.setTag("Count", new NBTInt(getValue().getAmount()));
        if (nbt.isEmpty()) return "item:" + getValue().getType().getName().toString().replace("minecraft:", "");
        return "item:" + getValue().getType().getName().toString().replace("minecraft:", "") + NBTCodec.nbtToJson(nbt, true);
    }
}
