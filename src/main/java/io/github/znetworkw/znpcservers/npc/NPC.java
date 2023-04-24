package io.github.znetworkw.znpcservers.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.hologram.Hologram;
import io.github.znetworkw.znpcservers.nms.PacketCache;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import lol.pyr.znpcsplus.ZNPCsPlus;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NPC {
    private static final ConcurrentMap<Integer, NPC> NPC_MAP = new ConcurrentHashMap<>();
    private final Set<ZUser> viewers = new HashSet<>();
    private final PacketCache packets = new PacketCache();
    private final NPCModel npcPojo;
    private final Hologram hologram;
    private final String npcName;
    private final NPCSkin npcSkin;
    private long lastMove = -1L;
    private int entityID;
    private Object glowColor;
    private Object tabConstructor;
    private Object updateTabConstructor;
    private Object nmsEntity;
    private Object bukkitEntity;
    private UUID uuid;
    private GameProfile gameProfile;
    private NPCPath.PathInitializer npcPath;

    public NPC(NPCModel npcModel, boolean load) {
        this.npcPojo = npcModel;
        this.hologram = new Hologram(this);
        this.npcName = NamingType.DEFAULT.resolve(this);
        this.npcSkin = NPCSkin.forValues(npcModel.getSkin(), npcModel.getSignature());
        this.uuid = npcModel.getUuid();
        if (load)
            onLoad();
    }

    public NPC(NPCModel npcModel) {
        this(npcModel, false);
    }

    public static NPC find(int id) {
        return NPC_MAP.get(id);
    }

    public static void unregister(int id) {
        NPC npc = find(id);
        if (npc == null)
            throw new IllegalStateException("can't find npc with id " + id);
        NPC_MAP.remove(id);
        npc.deleteViewers();
    }

    public static Collection<NPC> all() {
        return NPC_MAP.values();
    }

    public void onLoad() {
        if (NPC_MAP.containsKey(getNpcPojo().getId())) throw new IllegalStateException("npc with id " + getNpcPojo().getId() + " already exists.");
        this.gameProfile = new GameProfile(this.uuid, "[ZNPC] " + this.npcName);
        this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo.getSkin(), this.npcPojo.getSignature()));
        if (this.npcPojo.getNpcType().getConstructor() == null && !this.npcPojo.getNpcType().equals(NPCType.PLAYER)) {
            this.npcPojo.setShouldSpawn(false);
            if (ConfigurationConstants.DEBUG_ENABLED) {
                ZNPCsPlus.LOGGER.warning("The NPC Type " + npcPojo.getNpcType().name() + " does not exist or is not supported in this version.");
            }
        } else {
            this.npcPojo.setShouldSpawn(true);
            changeType(this.npcPojo.getNpcType());
            updateProfile(this.gameProfile.getProperties());
            setLocation(getNpcPojo().getLocation().toBukkitLocation(), false);
            this.hologram.createHologram();
            if (this.npcPojo.getPathName() != null)
                setPath(NPCPath.AbstractTypeWriter.find(this.npcPojo.getPathName()));
            this.npcPojo.getCustomizationMap().forEach((key, value) -> this.npcPojo.getNpcType().updateCustomization(this, key, value));
        }
        NPC_MAP.put(getNpcPojo().getId(), this);
    }

    public NPCModel getNpcPojo() {
        return this.npcPojo;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public Object getBukkitEntity() {
        return this.bukkitEntity;
    }

    public Object getNmsEntity() {
        return this.nmsEntity;
    }

    public Object getGlowColor() {
        return this.glowColor;
    }

    public void setGlowColor(Object glowColor) {
        this.glowColor = glowColor;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public NPCPath.PathInitializer getNpcPath() {
        return this.npcPath;
    }

    public Hologram getHologram() {
        return this.hologram;
    }

    public Set<ZUser> getViewers() {
        return this.viewers;
    }

    public PacketCache getPackets() {
        return this.packets;
    }

    public void setLocation(Location location, boolean updateTime) {
        try {
            if (this.npcPath == null) {
                lookAt(null, location, true);
                if (updateTime) this.lastMove = System.nanoTime();
                this.npcPojo.setLocation(new ZLocation(location = new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getY(), location.getBlockZ() + 0.5D, location.getYaw(), location.getPitch())));
            }
            Reflections.SET_LOCATION_METHOD.get().invoke(this.nmsEntity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            Object npcTeleportPacket = Reflections.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR.get().newInstance(this.nmsEntity);
            this.viewers.forEach(player -> Utils.sendPackets(player, npcTeleportPacket));
            this.hologram.setLocation(location, this.npcPojo.getNpcType().getHoloHeight());
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void changeSkin(NPCSkin skinFetch) {
        this.npcPojo.setSkin(skinFetch.getTexture());
        this.npcPojo.setSignature(skinFetch.getSignature());
        this.gameProfile.getProperties().clear();
        this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo.getSkin(), this.npcPojo.getSignature()));
        updateProfile(this.gameProfile.getProperties());
        deleteViewers();
    }

    public void setSecondLayerSkin() {
        try {
            Object dataWatcherObject = Reflections.GET_DATA_WATCHER_METHOD.get().invoke(this.nmsEntity);
            if (Utils.versionNewer(9)) {
                Reflections.SET_DATA_WATCHER_METHOD.get().invoke(dataWatcherObject, Reflections.DATA_WATCHER_OBJECT_CONSTRUCTOR.get()
                                .newInstance(this.npcSkin.getLayerIndex(), Reflections.DATA_WATCHER_REGISTER_FIELD.get()), (byte) 127);
            } else {
                Reflections.WATCH_DATA_WATCHER_METHOD.get().invoke(dataWatcherObject, 10, (byte) 127);
            }
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public synchronized void changeType(NPCType npcType) {
        deleteViewers();
        try {
            Object nmsWorld = Reflections.GET_HANDLE_WORLD_METHOD.get().invoke(getLocation().getWorld());
            boolean isPlayer = (npcType == NPCType.PLAYER);
            this.nmsEntity = isPlayer ? this.packets.getNms().createPlayer(nmsWorld, this.gameProfile) : (Utils.versionNewer(14) ? npcType.getConstructor().newInstance(npcType.getNmsEntityType(), nmsWorld) : npcType.getConstructor().newInstance(nmsWorld));
            this.bukkitEntity = Reflections.GET_BUKKIT_ENTITY_METHOD.get().invoke(this.nmsEntity);
            this.uuid = (UUID) Reflections.GET_UNIQUE_ID_METHOD.get().invoke(this.nmsEntity, new Object[0]);
            if (isPlayer) {
                try {
                    this.tabConstructor = Reflections.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.get().newInstance(Reflections.ADD_PLAYER_FIELD.get(), Collections.singletonList(this.nmsEntity));
                } catch (Throwable e) {
                    this.tabConstructor = Reflections.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.get().newInstance(Reflections.ADD_PLAYER_FIELD.get(), this.nmsEntity);
                    this.updateTabConstructor = Reflections.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.get().newInstance(Reflections.UPDATE_LISTED_FIELD.get(), this.nmsEntity);
                }
                setSecondLayerSkin();
            }
            this.npcPojo.setNpcType(npcType);
            setLocation(getLocation(), false);
            this.packets.flushCache("spawnPacket", "removeTab");
            this.entityID = (Integer) Reflections.GET_ENTITY_ID.get().invoke(this.nmsEntity, new Object[0]);
            FunctionFactory.findFunctionsForNpc(this).forEach(function -> function.resolve(this));
            getPackets().getNms().update(this.packets);
            this.hologram.createHologram();
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public synchronized void spawn(ZUser user) {
        if (this.viewers.contains(user)) {
            return;
        }
        if (!getNpcPojo().getShouldSpawn()) {
            return;
        }
        try {
            this.viewers.add(user);
            boolean npcIsPlayer = (this.npcPojo.getNpcType() == NPCType.PLAYER);
            if (FunctionFactory.isTrue(this, "glow") || npcIsPlayer) {
                ImmutableList<Object> scoreboardPackets = this.packets.getNms().updateScoreboard(this);
                scoreboardPackets.forEach(p -> Utils.sendPackets(user, p));
            }

            ZLocation location = npcPojo.getLocation();
            Player player = user.toPlayer();
            if (npcIsPlayer) {
                if (FunctionFactory.isTrue(this, "mirror")) updateProfile(user.getGameProfile().getProperties());
                Utils.sendPackets(user, this.tabConstructor, this.updateTabConstructor);
                ZNPCsPlus.SCHEDULER.runTask(() -> {
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerSpawnPlayer(entityID,
                            this.gameProfile.getId(), SpigotConversionUtil.fromBukkitLocation(location.toBukkitLocation())));
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerEntityMetadata(entityID,
                            List.of(new EntityData(17, EntityDataTypes.BYTE, Byte.MAX_VALUE))));
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerEntityHeadLook(entityID, location.getYaw()));
                });
            }
            else {
                ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
                EntityType type = SpigotConversionUtil.fromBukkitEntityType(((Entity) bukkitEntity).getType());
                if (version.isNewerThanOrEquals(ServerVersion.V_1_14)) PacketEvents.getAPI().getPlayerManager().sendPacket(
                        player, new WrapperPlayServerSpawnEntity(entityID, Optional.of(uuid), type, location.toVector3d(),
                                location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
                else PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerSpawnLivingEntity(
                        entityID, uuid, type, location.toVector3d(), location.getYaw(), location.getPitch(),
                        location.getPitch(), new Vector3d(), List.of()));
            }

            if (FunctionFactory.isTrue(this, "holo")) this.hologram.spawn(user);
            updateMetadata(Collections.singleton(user));
            sendEquipPackets(user);
            lookAt(user, getLocation(), true);
            if (npcIsPlayer) ZNPCsPlus.SCHEDULER.scheduleSyncDelayedTask(() -> {
                removeFromTab(player);
                Utils.sendPackets(user, this.updateTabConstructor);
            }, 60);
        } catch (ReflectiveOperationException operationException) {
            delete(user);
            throw new UnexpectedCallException(operationException);
        }
    }

    private void removeFromTab(Player player) {
        PacketWrapper<?> packet;
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_19_3)) packet = new WrapperPlayServerPlayerInfoRemove(gameProfile.getId());
        else packet = new WrapperPlayServerPlayerInfo(WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(null, new UserProfile(gameProfile.getId(), gameProfile.getName()), null, 1));
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    public synchronized void delete(ZUser user) {
        if (!this.viewers.contains(user)) throw new IllegalStateException(user.getUUID().toString() + " is not a viewer.");
        this.viewers.remove(user);
        handleDelete(user);
    }

    private void handleDelete(ZUser user) {
        Player player = user.toPlayer();
        this.hologram.delete(user);
        if (this.npcPojo.getNpcType() == NPCType.PLAYER) removeFromTab(player);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, new WrapperPlayServerDestroyEntities(this.entityID));
    }

    public void lookAt(ZUser player, Location location, boolean rotation) {
        long lastMoveNanos = System.nanoTime() - this.lastMove;
        if (this.lastMove > 1L && lastMoveNanos < 1000000000L) return;
        Location direction = rotation ? location : this.npcPojo.getLocation().pointingTo(location);
        try {
            Object lookPacket = Reflections.PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR.get().newInstance(this.entityID, (byte) (int) (direction.getYaw() * 256.0F / 360.0F), (byte) (int) (direction.getPitch() * 256.0F / 360.0F), true);
            Object headRotationPacket = Reflections.PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR.get().newInstance(this.nmsEntity, (byte) (int) (direction.getYaw() * 256.0F / 360.0F));
            if (player != null) Utils.sendPackets(player, lookPacket, headRotationPacket);
            else this.viewers.forEach(players -> Utils.sendPackets(players, headRotationPacket));
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void deleteViewers() {
        for (ZUser user : this.viewers) handleDelete(user);
        this.viewers.clear();
    }

    protected void updateMetadata(Iterable<ZUser> users) {
        try {
            Object metaData = this.packets.getNms().createMetadataPacket(this.entityID, this.nmsEntity);
            for (ZUser user : users) Utils.sendPackets(user, metaData);
        } catch (ReflectiveOperationException operationException) {
            operationException.getCause().printStackTrace();
            operationException.printStackTrace();
        }
    }

    public void updateProfile(PropertyMap propertyMap) {
        if (this.npcPojo.getNpcType() != NPCType.PLAYER) return;
        try {
            Object gameProfileObj = Reflections.GET_PROFILE_METHOD.get().invoke(this.nmsEntity);
            Utils.setValue(gameProfileObj, "name", this.gameProfile.getName());
            Utils.setValue(gameProfileObj, "id", this.gameProfile.getId());
            Utils.setValue(gameProfileObj, "properties", propertyMap);
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void sendEquipPackets(ZUser zUser) {
        if (this.npcPojo.getNpcEquip().isEmpty()) return;
        List<Equipment> equipment = npcPojo.getNpcEquip().entrySet().stream()
                .map(entry -> new Equipment(entry.getKey(), SpigotConversionUtil.fromBukkitItemStack(entry.getValue())))
                .toList();

        if (Utils.versionNewer(16)) PacketEvents.getAPI().getPlayerManager().sendPacket(zUser.toPlayer(), new WrapperPlayServerEntityEquipment(entityID, equipment));
        else for (Equipment e : equipment) PacketEvents.getAPI().getPlayerManager().sendPacket(zUser.toPlayer(), new WrapperPlayServerEntityEquipment(entityID, List.of(e)));
    }

    public void setPath(NPCPath.AbstractTypeWriter typeWriter) {
        if (typeWriter == null) {
            this.npcPath = null;
            this.npcPojo.setPathName("none");
        } else {
            this.npcPath = typeWriter.getPath(this);
            this.npcPojo.setPathName(typeWriter.getName());
        }
    }

    public void tryStartConversation(Player player) {
        ConversationModel conversation = this.npcPojo.getConversation();
        if (conversation == null) throw new IllegalStateException("can't find conversation");
        conversation.startConversation(this, player);
    }

    public Location getLocation() {
        if (this.npcPath != null && this.npcPath.getLocation() != null) return this.npcPath.getLocation().toBukkitLocation();
        return this.npcPojo.getLocation().toBukkitLocation();
    }
}
