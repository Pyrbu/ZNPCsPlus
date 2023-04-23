package io.github.znetworkw.znpcservers.nms;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.npc.FunctionFactory;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCType;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.utility.ReflectionUtils;
import io.github.znetworkw.znpcservers.utility.Utils;

import java.util.Collection;

public interface NMS {
    int version();

    @PacketValue(keyName = "playerPacket")
    Object createPlayer(Object paramObject, GameProfile paramGameProfile) throws ReflectiveOperationException;

    Object createMetadataPacket(int paramInt, Object paramObject) throws ReflectiveOperationException;

    @PacketValue(keyName = "hologramSpawnPacket", valueType = ValueType.ARGUMENTS)
    Object createArmorStandSpawnPacket(Object paramObject) throws ReflectiveOperationException;

    @SuppressWarnings("unchecked")
    @PacketValue(keyName = "scoreboardPackets")
    default ImmutableList<Object> updateScoreboard(NPC npc) throws ReflectiveOperationException {
        ImmutableList.Builder<Object> builder = ImmutableList.builder();
        boolean isVersion17 = (Utils.BUKKIT_VERSION > 16);
        boolean isVersion9 = (Utils.BUKKIT_VERSION > 8);
        Object scoreboardTeamPacket = isVersion17 ? Reflections.SCOREBOARD_TEAM_CONSTRUCTOR.get().newInstance(null, npc.getGameProfile().getName()) : Reflections.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.get().newInstance();
        if (!isVersion17) {
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 1);
        }
        builder.add(isVersion17 ? Reflections.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE_V1.get().invoke(null, scoreboardTeamPacket) : scoreboardTeamPacket);
        if (isVersion17) {
            scoreboardTeamPacket = Reflections.SCOREBOARD_TEAM_CONSTRUCTOR.get().newInstance(null, npc.getGameProfile().getName());
            if (Utils.BUKKIT_VERSION > 17) {
                Utils.setValue(scoreboardTeamPacket, "d", npc.getGameProfile().getName());
                ReflectionUtils.findFieldForClassAndSet(scoreboardTeamPacket, Reflections.ENUM_TAG_VISIBILITY, Reflections.ENUM_TAG_VISIBILITY_NEVER_FIELD.get());
                Utils.setValue(scoreboardTeamPacket, "m", Reflections.ENUM_CHAT_FORMAT_FIND.get().invoke(null, "DARK_GRAY"));
            } else {
                Utils.setValue(scoreboardTeamPacket, "e", npc.getGameProfile().getName());
                Utils.setValue(scoreboardTeamPacket, "l", Reflections.ENUM_TAG_VISIBILITY_NEVER_FIELD.get());
            }
        } else {
            scoreboardTeamPacket = Reflections.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CONSTRUCTOR_OLD.get().newInstance();
            Utils.setValue(scoreboardTeamPacket, "a", npc.getGameProfile().getName());
            Utils.setValue(scoreboardTeamPacket, "e", "never");
            Utils.setValue(scoreboardTeamPacket, isVersion9 ? "i" : "h", 0);
        }
        Collection<String> collection = isVersion17 ? (Collection<String>) Reflections.SCOREBOARD_PLAYER_LIST.get().invoke(scoreboardTeamPacket) : (Collection<String>) Utils.getValue(scoreboardTeamPacket, isVersion9 ? "h" : "g");
        if (npc.getNpcPojo().getNpcType() == NPCType.PLAYER) {
            collection.add(npc.getGameProfile().getName());
        } else {
            collection.add(npc.getUUID().toString());
        }
        if (allowsGlowColor() && FunctionFactory.isTrue(npc, "glow"))
            updateGlow(npc, scoreboardTeamPacket);
        builder.add(isVersion17 ? Reflections.PACKET_PLAY_OUT_SCOREBOARD_TEAM_CREATE.get().invoke(null, scoreboardTeamPacket, Boolean.TRUE) : scoreboardTeamPacket);
        return builder.build();
    }

    void updateGlow(NPC paramNPC, Object paramObject) throws ReflectiveOperationException;

    boolean allowsGlowColor();

    default void update(PacketCache packetCache) throws ReflectiveOperationException {
        packetCache.flushCache("scoreboardPackets");
    }
}
