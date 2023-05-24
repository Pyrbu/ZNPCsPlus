package lol.pyr.znpcsplus.entity.serializers;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.ItemSerializationUtil;

public class ItemStackPropertySerializer implements PropertySerializer<ItemStack> {
    @Override
    public String serialize(ItemStack property) {
        return ItemSerializationUtil.itemToB64(SpigotConversionUtil.toBukkitItemStack(property));
    }

    @Override
    public ItemStack deserialize(String property) {
        return SpigotConversionUtil.fromBukkitItemStack(ItemSerializationUtil.itemFromB64(property));
    }

    @Override
    public Class<ItemStack> getTypeClass() {
        return ItemStack.class;
    }
}
