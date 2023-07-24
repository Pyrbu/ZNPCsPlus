package lol.pyr.znpcsplus.entity.properties;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.EquipmentSlot;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import org.bukkit.entity.Player;

import java.util.Map;

public class EquipmentProperty extends EntityPropertyImpl<ItemStack> {
    private final PacketFactory packetFactory;
    private final EquipmentSlot slot;

    public EquipmentProperty(PacketFactory packetFactory, String name, EquipmentSlot slot) {
        super(name, null, ItemStack.class);
        this.packetFactory = packetFactory;
        this.slot = slot;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData> properties) {
        packetFactory.sendEquipment(player, entity, new Equipment(slot, entity.getProperty(this)));
    }
}
