package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.ItemSerializationUtil;
import org.bukkit.inventory.ItemStack;

public class ItemStackPropertySerializer implements PropertySerializer<ItemStack> {
    @Override
    public String serialize(ItemStack property) {
        return ItemSerializationUtil.itemToB64(property);
    }

    @Override
    public ItemStack deserialize(String property) {
        return ItemSerializationUtil.itemFromB64(property);
    }

    @Override
    public Class<ItemStack> getTypeClass() {
        return ItemStack.class;
    }
}
