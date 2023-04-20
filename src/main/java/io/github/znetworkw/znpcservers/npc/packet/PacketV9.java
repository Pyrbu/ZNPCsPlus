package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import io.github.znetworkw.znpcservers.reflection.ReflectionCache;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PacketV9 extends PacketV8 {
    public int version() {
        return 9;
    }

    public Object convertItemStack(int entityId, ItemSlot itemSlot, ItemStack itemStack) throws ReflectiveOperationException {
        return ReflectionCache.AS_NMS_COPY_METHOD.load().invoke(ReflectionCache.CRAFT_ITEM_STACK_CLASS, itemStack);
    }

    public ImmutableList<Object> getEquipPackets(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        for (Map.Entry<ItemSlot, ItemStack> stackEntry : npc.getNpcPojo().getNpcEquip().entrySet()) {
            builder.add(ReflectionCache.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD.load().newInstance(npc.getEntityID(),
                    getItemSlot(stackEntry.getKey().getSlot()),
                    convertItemStack(npc.getEntityID(), stackEntry.getKey(), stackEntry.getValue())));
        }
        return builder.build();
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        Object enumChatString = ReflectionCache.ENUM_CHAT_TO_STRING_METHOD.load().invoke(npc.getGlowColor());
        if (Utils.BUKKIT_VERSION > 12) {
            Utils.setValue(packet, npc.getGlowColor(), ReflectionCache.ENUM_CHAT_CLASS);
            Utils.setValue(packet, "c", ReflectionCache.I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR.load().newInstance(enumChatString));
        } else {
            Utils.setValue(packet, "g", ReflectionCache.GET_ENUM_CHAT_ID_METHOD.load().invoke(npc.getGlowColor()));
            Utils.setValue(packet, "c", enumChatString);
        }
    }

    public boolean allowGlowColor() {
        return true;
    }
}
