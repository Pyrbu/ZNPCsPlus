package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.util.Map;

public class PacketV8 implements Packet {
    public int version() {
        return 8;
    }

    public Object getPlayerPacket(Object nmsWorld, GameProfile gameProfile) throws ReflectiveOperationException {
        Constructor<?> constructor = (Utils.BUKKIT_VERSION > 13) ? CacheRegistry.PLAYER_INTERACT_MANAGER_NEW_CONSTRUCTOR.load() : CacheRegistry.PLAYER_INTERACT_MANAGER_OLD_CONSTRUCTOR.load();
        return ((Constructor) CacheRegistry.PLAYER_CONSTRUCTOR_OLD.load()).newInstance(new Object[]{CacheRegistry.GET_SERVER_METHOD
                .load().invoke(Bukkit.getServer()), nmsWorld, gameProfile, constructor

                .newInstance(nmsWorld)});
    }

    public Object getSpawnPacket(Object nmsEntity, boolean isPlayer) throws ReflectiveOperationException {
        return isPlayer ? ((Constructor) CacheRegistry.PACKET_PLAY_OUT_NAMED_ENTITY_CONSTRUCTOR.load()).newInstance(nmsEntity) : ((Constructor) CacheRegistry.PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR.load()).newInstance(nmsEntity);
    }

    public Object convertItemStack(int entityId, ItemSlot itemSlot, ItemStack itemStack) throws ReflectiveOperationException {
        return ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_OLD.load()).newInstance(Integer.valueOf(entityId),
                Integer.valueOf(itemSlot.getSlotOld()), CacheRegistry.AS_NMS_COPY_METHOD
                        .load().invoke(CacheRegistry.CRAFT_ITEM_STACK_CLASS, itemStack));
    }

    public Object getClickType(Object interactPacket) throws ReflectiveOperationException {
        return Utils.getValue(interactPacket, "action");
    }

    public Object getMetadataPacket(int entityId, Object nmsEntity) throws ReflectiveOperationException {
        Object dataWatcher = CacheRegistry.GET_DATA_WATCHER_METHOD.load().invoke(nmsEntity);
        try {
            return ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR.load()).newInstance(Integer.valueOf(entityId), dataWatcher, Boolean.valueOf(true));
        } catch (Exception e2) {
            return ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_META_DATA_CONSTRUCTOR_V1
                    .load())
                    .newInstance(Integer.valueOf(entityId), CacheRegistry.GET_DATAWATCHER_B_LIST
                            .load().invoke(dataWatcher));
        }
    }

    public Object getHologramSpawnPacket(Object armorStand) throws ReflectiveOperationException {
        return ((Constructor) CacheRegistry.PACKET_PLAY_OUT_SPAWN_ENTITY_CONSTRUCTOR.load()).newInstance(armorStand);
    }

    public ImmutableList<Object> getEquipPackets(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        for (Map.Entry<ItemSlot, ItemStack> stackEntry : npc.getNpcPojo().getNpcEquip().entrySet()) {
            builder.add(((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_EQUIPMENT_CONSTRUCTOR_OLD.load()).newInstance(Integer.valueOf(npc.getEntityID()),
                    Integer.valueOf(stackEntry.getKey().getSlotOld()),
                    convertItemStack(npc.getEntityID(), stackEntry.getKey(), stackEntry.getValue())));
        }
        return builder.build();
    }

    public void updateGlowPacket(NPC npc, Object packet) throws ReflectiveOperationException {
        throw new IllegalStateException("Glow color is not supported for 1.8 version.");
    }

    public boolean allowGlowColor() {
        return false;
    }
}
