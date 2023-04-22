package io.github.znetworkw.znpcservers.nms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class NMSV16 extends NMSV9 {
    public int version() {
        return 16;
    }

    public ImmutableList<Object> createEquipmentPacket(NPC npc) throws ReflectiveOperationException {
        List<Pair<?, ?>> pairs = Lists.newArrayListWithCapacity((ItemSlot.values()).length);
        for (Map.Entry<ItemSlot, ItemStack> entry : npc.getNpcPojo().getNpcEquip().entrySet())
            pairs.add(new Pair<>(getItemSlot(entry
                    .getKey().getSlot()),
                    createEntityEquipmentPacket(npc.getEntityID(), entry.getKey(), entry.getValue())));
        return ImmutableList.of(Reflections.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_V1.get().newInstance(npc.getEntityID(), pairs));
    }
}
