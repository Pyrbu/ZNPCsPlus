package io.github.znetworkw.znpcservers.npc.packet;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.ReflectionCache;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.ItemSlot;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCType;
import io.github.znetworkw.znpcservers.utility.ReflectionUtils;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;

public interface Packet {
    int version();

    @PacketValue(keyName = "playerPacket")
    Object getPlayerPacket(Object paramObject, GameProfile paramGameProfile) throws ReflectiveOperationException;

    @PacketValue(keyName = "spawnPacket")
    Object getSpawnPacket(Object paramObject, boolean paramBoolean) throws ReflectiveOperationException;

    Object convertItemStack(int paramInt, ItemSlot paramItemSlot, ItemStack paramItemStack) throws ReflectiveOperationException;

    Object getClickType(Object paramObject) throws ReflectiveOperationException;

    Object getMetadataPacket(int paramInt, Object paramObject) throws ReflectiveOperationException;

    @PacketValue(keyName = "hologramSpawnPacket", valueType = ValueType.ARGUMENTS)
    Object getHologramSpawnPacket(Object paramObject) throws ReflectiveOperationException;

    @SuppressWarnings("SuspiciousTernaryOperatorInVarargsCall")
    @PacketValue(keyName = "destroyPacket", valueType = ValueType.ARGUMENTS)
    default Object getDestroyPacket(int entityId) throws ReflectiveOperationException {
        return ReflectionCache.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load().newInstance(ReflectionCache.PACKET_PLAY_OUT_ENTITY_DESTROY_CONSTRUCTOR.load().getParameterTypes()[0].isArray() ? new int[] {entityId} : entityId);
    }

    @PacketValue(keyName = "enumSlot", valueType = ValueType.ARGUMENTS)
    default Object getItemSlot(int slot) {
        return ReflectionCache.ENUM_ITEM_SLOT.getEnumConstants()[slot];
    }

    @PacketValue(keyName = "removeTab")
    default Object getTabRemovePacket(Object nmsEntity) throws ReflectiveOperationException {
        try {
            return ReflectionCache.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load().newInstance(ReflectionCache.REMOVE_PLAYER_FIELD.load(), Collections.singletonList(nmsEntity));
        } catch (Throwable throwable) {
            boolean useOldMethod = (ReflectionCache.PACKET_PLAY_OUT_PLAYER_INFO_REMOVE_CLASS != null);
            if (useOldMethod) return ReflectionCache.PACKET_PLAY_OUT_PLAYER_INFO_REMOVE_CONSTRUCTOR.load().newInstance(Collections.singletonList(ReflectionCache.GET_UNIQUE_ID_METHOD.load().invoke(nmsEntity)));
            return ReflectionCache.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load().newInstance(ReflectionCache.REMOVE_PLAYER_FIELD.load(), nmsEntity);
        }
    }

    @PacketValue(keyName = "equipPackets")
    ImmutableList<Object> getEquipPackets(NPC paramNPC) throws ReflectiveOperationException;

    @SuppressWarnings("unchecked")
    @PacketValue(keyName = "scoreboardPackets")
    default ImmutableList<Object> updateScoreboard(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        boolean isVersion17 = (Utils.BUKKIT_VERSION > 16);
        boolean isVersion9 = (Utils.BUKKIT_VERSION > 8);
        Object scoreboardTeamPacket = isVersion17 ? ReflectionCache.SCOREBOARD_TEAM_CONSTRUCTOR.load().newInstance(null, npc.getGameProfile().getName()) : ReflectionCache.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load().newInstance();
        if (!isVersion17) {
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 1);
        }
        builder.add(isVersion17 ? ReflectionCache.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1.load().invoke(null, scoreboardTeamPacket) : scoreboardTeamPacket);
        if (isVersion17) {
            scoreboardTeamPacket = ReflectionCache.SCOREBOARD_TEAM_CONSTRUCTOR.load().newInstance(null, npc.getGameProfile().getName());
            if (Utils.BUKKIT_VERSION > 17) {
                Utils.setValue(scoreboardTeamPacket, "d", npc.getGameProfile().getName());
                ReflectionUtils.findFieldForClassAndSet(scoreboardTeamPacket, ReflectionCache.ENUM_TAG_VISIBILITY, ReflectionCache.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
                Utils.setValue(scoreboardTeamPacket, "m", ReflectionCache.ENUM_CHAT_FORMAT_FIND.load().invoke(null, "DARK_GRAY"));
            } else {
                Utils.setValue(scoreboardTeamPacket, "e", npc.getGameProfile().getName());
                Utils.setValue(scoreboardTeamPacket, "l", ReflectionCache.ENUM_TAG_VISIBILITY_NEVER_FIELD.load());
            }
        } else {
            scoreboardTeamPacket = ReflectionCache.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.load().newInstance();
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, "e", "never");
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 0);
        }
        Collection<String> collection = isVersion17 ? (Collection<String>) ReflectionCache.SCOREBOARD_PLAYER_LIST.load().invoke(scoreboardTeamPacket) : (Collection<String>) Utils.getValue(scoreboardTeamPacket, isVersion9 ? "h" : "g");
        if (npc.getNpcPojo().getNpcType() == NPCType.PLAYER) {
            collection.add(npc.getGameProfile().getName());
        } else {
            collection.add(npc.getUUID().toString());
        }
        if (allowGlowColor() && FunctionFactory.isTrue(npc, "glow"))
            updateGlowPacket(npc, scoreboardTeamPacket);
        builder.add(isVersion17 ? ReflectionCache.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE.load().invoke(null, scoreboardTeamPacket, Boolean.TRUE) : scoreboardTeamPacket);
        return builder.build();
    }

    void updateGlowPacket(NPC paramNPC, Object paramObject) throws ReflectiveOperationException;

    boolean allowGlowColor();

    default void update(PacketCache packetCache) throws ReflectiveOperationException {
        packetCache.flushCache("scoreboardPackets");
    }
}
