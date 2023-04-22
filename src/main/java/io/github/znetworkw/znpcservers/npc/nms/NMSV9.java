package io.github.znetworkw.znpcservers.npc.nms;

import com.google.common.collect.ImmutableList;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class NMSV9 extends NMSV8 {
    public int version() {
        return 9;
    }

    public Object convertItemStack(int entityId, ItemSlot itemSlot, ItemStack itemStack) throws ReflectiveOperationException {
        return Reflections.AS_NMS_COPY_METHOD.get().invoke(Reflections.CRAFT_ITEM_STACK_CLASS, itemStack);
    }

    public ImmutableList<Object> getEquipPackets(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        for (Map.Entry<ItemSlot, ItemStack> stackEntry : npc.getNpcPojo().getNpcEquip().entrySet()) {
            builder.add(Reflections.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_NEWEST_OLD.get().newInstance(npc.getEntityID(),
                    getItemSlot(stackEntry.getKey().getSlot()),
                    convertItemStack(npc.getEntityID(), stackEntry.getKey(), stackEntry.getValue())));
        }
        return builder.build();
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        Object enumChatString = Reflections.ENUM_CHAT_TO_STRING_METHOD.get().invoke(npc.getGlowColor());
        if (Utils.BUKKIT_VERSION > 12) {
            Utils.setValue(packet, npc.getGlowColor(), Reflections.ENUM_CHAT_CLASS);
            Utils.setValue(packet, "c", Reflections.I_CHAT_BASE_COMPONENT_A_CONSTRUCTOR.get().newInstance(enumChatString));
        } else {
            Utils.setValue(packet, "g", Reflections.GET_ENUM_CHAT_ID_METHOD.get().invoke(npc.getGlowColor()));
            Utils.setValue(packet, "c", enumChatString);
        }
    }

    public boolean allowGlowColor() {
        return true;
    }
}
