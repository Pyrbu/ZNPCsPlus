package io.github.znetworkw.znpcservers.npc;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import lol.pyr.znpcsplus.ZNPCsPlus;
import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.cache.CacheRegistry;
import io.github.znetworkw.znpcservers.npc.conversation.ConversationModel;
import io.github.znetworkw.znpcservers.npc.hologram.Hologram;
import io.github.znetworkw.znpcservers.npc.packet.PacketCache;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import io.github.znetworkw.znpcservers.utility.location.ZLocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class NPC {
    private static final ConcurrentMap<Integer, NPC> NPC_MAP = new ConcurrentHashMap<>();

    private static final String PROFILE_TEXTURES = "textures";

    private static final String START_PREFIX = "[ZNPC] ";

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
        if (load)
            onLoad();
    }

    public NPC(NPCModel npcModel) {
        this(npcModel, false);
    }

    public static NPC find(int id) {
        return NPC_MAP.get(Integer.valueOf(id));
    }

    public static void unregister(int id) {
        NPC npc = find(id);
        if (npc == null)
            throw new IllegalStateException("can't find npc with id " + id);
        NPC_MAP.remove(Integer.valueOf(id));
        npc.deleteViewers();
    }

    public static Collection<NPC> all() {
        return NPC_MAP.values();
    }

    public void onLoad() {
        if (NPC_MAP.containsKey(Integer.valueOf(getNpcPojo().getId())))
            throw new IllegalStateException("npc with id " + getNpcPojo().getId() + " already exists.");
        this.gameProfile = new GameProfile(UUID.randomUUID(), "[ZNPC] " + this.npcName);
        this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo.getSkin(), this.npcPojo.getSignature()));
        changeType(this.npcPojo.getNpcType());
        updateProfile(this.gameProfile.getProperties());
        setLocation(getNpcPojo().getLocation().bukkitLocation(), false);
        this.hologram.createHologram();
        if (this.npcPojo.getPathName() != null)
            setPath(NPCPath.AbstractTypeWriter.find(this.npcPojo.getPathName()));
        this.npcPojo.getCustomizationMap().forEach((key, value) -> this.npcPojo.getNpcType().updateCustomization(this, key, value));
        NPC_MAP.put(Integer.valueOf(getNpcPojo().getId()), this);
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
                if (updateTime)
                    this.lastMove = System.nanoTime();
                this.npcPojo.setLocation(new ZLocation(location = new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getY(), location.getBlockZ() + 0.5D, location.getYaw(), location.getPitch())));
            }
            CacheRegistry.SET_LOCATION_METHOD.load().invoke(this.nmsEntity, Double.valueOf(location.getX()), Double.valueOf(location.getY()), Double.valueOf(location.getZ()), Float.valueOf(location.getYaw()), Float.valueOf(location.getPitch()));
            Object npcTeleportPacket = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR.load()).newInstance(this.nmsEntity);
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
        this.gameProfile.getProperties().put("textures", new Property("textures", this.npcPojo
                .getSkin(), this.npcPojo.getSignature()));
        updateProfile(this.gameProfile.getProperties());
        deleteViewers();
    }

    public void setSecondLayerSkin() {
        try {
            Object dataWatcherObject = CacheRegistry.GET_DATA_WATCHER_METHOD.load().invoke(this.nmsEntity);
            if (Utils.versionNewer(9)) {
                CacheRegistry.SET_DATA_WATCHER_METHOD.load().invoke(dataWatcherObject, ((Constructor) CacheRegistry.DATA_WATCHER_OBJECT_CONSTRUCTOR
                        .load()).newInstance(Integer.valueOf(this.npcSkin.getLayerIndex()), CacheRegistry.DATA_WATCHER_REGISTER_FIELD
                        .load()), Byte.valueOf(127));
            } else {
                CacheRegistry.WATCH_DATA_WATCHER_METHOD.load().invoke(dataWatcherObject, Integer.valueOf(10), Byte.valueOf(127));
            }
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public synchronized void changeType(NPCType npcType) {
        deleteViewers();
        try {
            Object nmsWorld = CacheRegistry.GET_HANDLE_WORLD_METHOD.load().invoke(getLocation().getWorld());
            boolean isPlayer = (npcType == NPCType.PLAYER);
            this.nmsEntity = isPlayer ? this.packets.getProxyInstance().getPlayerPacket(nmsWorld, this.gameProfile) : (Utils.versionNewer(14) ? npcType.getConstructor().newInstance(npcType.getNmsEntityType(), nmsWorld) : npcType.getConstructor().newInstance(nmsWorld));
            this.bukkitEntity = CacheRegistry.GET_BUKKIT_ENTITY_METHOD.load().invoke(this.nmsEntity);
            this.uuid = (UUID) CacheRegistry.GET_UNIQUE_ID_METHOD.load().invoke(this.nmsEntity, new Object[0]);
            if (isPlayer) {
                try {
                    this.tabConstructor = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load()).newInstance(CacheRegistry.ADD_PLAYER_FIELD.load(), Collections.singletonList(this.nmsEntity));
                } catch (Throwable e) {
                    this.tabConstructor = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load()).newInstance(CacheRegistry.ADD_PLAYER_FIELD.load(), this.nmsEntity);
                    this.updateTabConstructor = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_PLAYER_INFO_CONSTRUCTOR.load()).newInstance(CacheRegistry.UPDATE_LISTED_FIELD.load(), this.nmsEntity);
                }
                setSecondLayerSkin();
            }
            this.npcPojo.setNpcType(npcType);
            setLocation(getLocation(), false);
            this.packets.flushCache("spawnPacket", "removeTab");
            this.entityID = ((Integer) CacheRegistry.GET_ENTITY_ID.load().invoke(this.nmsEntity, new Object[0])).intValue();
            FunctionFactory.findFunctionsForNpc(this).forEach(function -> function.resolve(this));
            getPackets().getProxyInstance().update(this.packets);
            this.hologram.createHologram();
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public synchronized void spawn(ZUser user) {
        if (this.viewers.contains(user))
            throw new IllegalStateException(user.getUUID().toString() + " is already a viewer.");
        try {
            this.viewers.add(user);
            boolean npcIsPlayer = (this.npcPojo.getNpcType() == NPCType.PLAYER);
            if (FunctionFactory.isTrue(this, "glow") || npcIsPlayer) {
                ImmutableList<Object> scoreboardPackets = this.packets.getProxyInstance().updateScoreboard(this);
                scoreboardPackets.forEach(p -> Utils.sendPackets(user, p));
            }
            if (npcIsPlayer) {
                if (FunctionFactory.isTrue(this, "mirror"))
                    updateProfile(user.getGameProfile().getProperties());
                Utils.sendPackets(user, this.tabConstructor, this.updateTabConstructor);
            }
            Utils.sendPackets(user, this.packets.getProxyInstance().getSpawnPacket(this.nmsEntity, npcIsPlayer));
            if (FunctionFactory.isTrue(this, "holo"))
                this.hologram.spawn(user);
            updateMetadata(Collections.singleton(user));
            sendEquipPackets(user);
            lookAt(user, getLocation(), true);
            if (npcIsPlayer) {
                Object removeTabPacket = this.packets.getProxyInstance().getTabRemovePacket(this.nmsEntity);
                ZNPCsPlus.SCHEDULER.scheduleSyncDelayedTask(() -> Utils.sendPackets(user, removeTabPacket, this.updateTabConstructor), 60);
            }
        } catch (ReflectiveOperationException operationException) {
            delete(user);
            throw new UnexpectedCallException(operationException);
        }
    }

    public synchronized void delete(ZUser user) {
        if (!this.viewers.contains(user))
            throw new IllegalStateException(user.getUUID().toString() + " is not a viewer.");
        this.viewers.remove(user);
        handleDelete(user);
    }

    private void handleDelete(ZUser user) {
        try {
            if (this.npcPojo.getNpcType() == NPCType.PLAYER)
                this.packets.getProxyInstance().getTabRemovePacket(this.nmsEntity);
            this.hologram.delete(user);
            Utils.sendPackets(user, this.packets.getProxyInstance().getDestroyPacket(this.entityID));
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void lookAt(ZUser player, Location location, boolean rotation) {
        long lastMoveNanos = System.nanoTime() - this.lastMove;
        if (this.lastMove > 1L && lastMoveNanos < 1000000000L)
            return;
        Location direction = rotation ? location : this.npcPojo.getLocation().bukkitLocation().clone().setDirection(location.clone().subtract(this.npcPojo.getLocation().bukkitLocation().clone()).toVector());
        try {
            Object lookPacket = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_LOOK_CONSTRUCTOR.load()).newInstance(Integer.valueOf(this.entityID), Byte.valueOf((byte) (int) (direction.getYaw() * 256.0F / 360.0F)), Byte.valueOf((byte) (int) (direction.getPitch() * 256.0F / 360.0F)), Boolean.TRUE);
            Object headRotationPacket = ((Constructor) CacheRegistry.PACKET_PLAY_OUT_ENTITY_HEAD_ROTATION_CONSTRUCTOR.load()).newInstance(this.nmsEntity, Byte.valueOf((byte) (int) (direction.getYaw() * 256.0F / 360.0F)));
            if (player != null) {
                Utils.sendPackets(player, lookPacket, headRotationPacket);
            } else {
                this.viewers.forEach(players -> Utils.sendPackets(players, headRotationPacket));
            }
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void deleteViewers() {
        for (ZUser user : this.viewers)
            handleDelete(user);
        this.viewers.clear();
    }

    protected void updateMetadata(Iterable<ZUser> users) {
        try {
            Object metaData = this.packets.getProxyInstance().getMetadataPacket(this.entityID, this.nmsEntity);
            for (ZUser user : users) {
                Utils.sendPackets(user, metaData);
            }
        } catch (ReflectiveOperationException operationException) {
            operationException.getCause().printStackTrace();
            operationException.printStackTrace();
        }
    }

    public void updateProfile(PropertyMap propertyMap) {
        if (this.npcPojo.getNpcType() != NPCType.PLAYER)
            return;
        try {
            Object gameProfileObj = CacheRegistry.GET_PROFILE_METHOD.load().invoke(this.nmsEntity);
            Utils.setValue(gameProfileObj, "name", this.gameProfile.getName());
            Utils.setValue(gameProfileObj, "id", this.gameProfile.getId());
            Utils.setValue(gameProfileObj, "properties", propertyMap);
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void sendEquipPackets(ZUser zUser) {
        if (this.npcPojo.getNpcEquip().isEmpty())
            return;
        try {
            ImmutableList<Object> equipPackets = this.packets.getProxyInstance().getEquipPackets(this);
            equipPackets.forEach(o -> Utils.sendPackets(zUser, o));
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException.getCause());
        }
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
        if (conversation == null)
            throw new IllegalStateException("can't find conversation");
        conversation.startConversation(this, player);
    }

    public Location getLocation() {
        return (this.npcPath != null) ?
                this.npcPath.getLocation().bukkitLocation() :
                this.npcPojo.getLocation().bukkitLocation();
    }
}
