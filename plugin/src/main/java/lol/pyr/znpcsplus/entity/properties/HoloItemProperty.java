package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.Map;

public class HoloItemProperty extends EntityPropertyImpl<ItemStack> {

    public HoloItemProperty() {
        super("holo_item", null, ItemStack.class);
        setPlayerModifiable(false);
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        properties.put(8, newEntityData(8, EntityDataTypes.ITEMSTACK, entity.getProperty(this)));
        properties.put(5, newEntityData(5, EntityDataTypes.BOOLEAN, true));
    }
}
