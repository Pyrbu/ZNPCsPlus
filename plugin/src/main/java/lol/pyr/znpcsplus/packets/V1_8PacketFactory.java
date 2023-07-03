package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.*;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class V1_8PacketFactory implements PacketFactory {
    protected final TaskScheduler scheduler;
    protected final MetadataFactory metadataFactory;
    protected final PacketEventsAPI<Plugin> packetEvents;
    protected final EntityPropertyRegistryImpl propertyRegistry;
    protected final LegacyComponentSerializer textSerializer;

    public V1_8PacketFactory(TaskScheduler scheduler, MetadataFactory metadataFactory, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
        this.scheduler = scheduler;
        this.metadataFactory = metadataFactory;
        this.packetEvents = packetEvents;
        this.propertyRegistry = propertyRegistry;
        this.textSerializer = textSerializer;
    }

    @Override
    public void spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        addTabPlayer(player, entity, properties).thenAccept(ignored -> {
            createTeam(player, entity, properties);
            NpcLocation location = entity.getLocation();
            sendPacket(player, new WrapperPlayServerSpawnPlayer(entity.getEntityId(),
                    entity.getUuid(), npcLocationToVector(location), location.getYaw(), location.getPitch(), Collections.emptyList()));
            sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
            sendAllMetadata(player, entity, properties);
            scheduler.runLaterAsync(() -> removeTabPlayer(player, entity), 60);
        });
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        NpcLocation location = entity.getLocation();
        EntityType type = entity.getType();
        ClientVersion clientVersion = packetEvents.getServerManager().getVersion().toClientVersion();
        sendPacket(player, type.getLegacyId(clientVersion) == -1 ?
                new WrapperPlayServerSpawnLivingEntity(entity.getEntityId(), entity.getUuid(), type, npcLocationToVector(location),
                        location.getYaw(), location.getPitch(), location.getPitch(), new Vector3d(), Collections.emptyList()) :
                new WrapperPlayServerSpawnEntity(entity.getEntityId(), Optional.of(entity.getUuid()), entity.getType(), npcLocationToVector(location),
                        location.getPitch(), location.getYaw(), location.getYaw(), 0, Optional.empty()));
        sendAllMetadata(player, entity, properties);
        createTeam(player, entity, properties);
    }

    protected Vector3d npcLocationToVector(NpcLocation location) {
        return new Vector3d(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void destroyEntity(Player player, PacketEntity entity, PropertyHolder properties) {
        sendPacket(player, new WrapperPlayServerDestroyEntities(entity.getEntityId()));
        removeTeam(player, entity);
    }

    @Override
    public void teleportEntity(Player player, PacketEntity entity) {
        NpcLocation location = entity.getLocation();
        sendPacket(player, new WrapperPlayServerEntityTeleport(entity.getEntityId(), npcLocationToVector(location), location.getYaw(), location.getPitch(), true));
        sendPacket(player, new WrapperPlayServerEntityHeadLook(entity.getEntityId(), location.getYaw()));
    }

    @Override
    public CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        if (entity.getType() != EntityTypes.PLAYER) return CompletableFuture.completedFuture(null);
        CompletableFuture<Void> future = new CompletableFuture<>();
        skinned(player, properties, new UserProfile(entity.getUuid(), Integer.toString(entity.getEntityId()))).thenAccept(profile -> {
            sendPacket(player, new WrapperPlayServerPlayerInfo(
                    WrapperPlayServerPlayerInfo.Action.ADD_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(Component.text(""),
                    profile, GameMode.CREATIVE, 1)));
            future.complete(null);
        });
        return future;
    }

    @Override
    public void removeTabPlayer(Player player, PacketEntity entity) {
        if (entity.getType() != EntityTypes.PLAYER) return;
        sendPacket(player, new WrapperPlayServerPlayerInfo(
                WrapperPlayServerPlayerInfo.Action.REMOVE_PLAYER, new WrapperPlayServerPlayerInfo.PlayerData(null,
                new UserProfile(entity.getUuid(), null), null, -1)));
    }

    @Override
    public void createTeam(Player player, PacketEntity entity, PropertyHolder properties) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.CREATE, new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.empty(), Component.empty(), Component.empty(),
                WrapperPlayServerTeams.NameTagVisibility.NEVER,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                properties.hasProperty(propertyRegistry.getByName("glow")) ? properties.getProperty(propertyRegistry.getByName("glow", NamedTextColor.class)) : NamedTextColor.WHITE,
                WrapperPlayServerTeams.OptionData.NONE
        )));
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.ADD_ENTITIES, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null,
                entity.getType() == EntityTypes.PLAYER ? Integer.toString(entity.getEntityId()) : entity.getUuid().toString()));
    }

    @Override
    public void removeTeam(Player player, PacketEntity entity) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null));
    }

    @Override
    public Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        HashMap<Integer, EntityData> data = new HashMap<>();
        add(data, metadataFactory.effects(
                properties.getProperty(propertyRegistry.getByName("fire", Boolean.class)),
                false,
                properties.getProperty(propertyRegistry.getByName("invisible", Boolean.class)),
                false,
                properties.getProperty(propertyRegistry.getByName("using_item", Boolean.class))
        ));
        add(data, metadataFactory.silent(properties.getProperty(propertyRegistry.getByName("silent", Boolean.class))));
        add(data, metadataFactory.potionColor(properties.getProperty(propertyRegistry.getByName("potion_color", Color.class)).asRGB()));
        add(data, metadataFactory.potionAmbient(properties.getProperty(propertyRegistry.getByName("potion_ambient", Boolean.class))));
        if (entity.getType().equals(EntityTypes.PLAYER)) {
            add(data, metadataFactory.skinLayers(
                    properties.getProperty(propertyRegistry.getByName("skin_cape", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_jacket", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_left_sleeve", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_right_sleeve", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_left_leg", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_right_leg", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_hat", Boolean.class))
            ));
            add(data, metadataFactory.shoulderEntityLeft(properties.getProperty(propertyRegistry.getByName("shoulder_entity_left", ParrotVariant.class))));
            add(data, metadataFactory.shoulderEntityRight(properties.getProperty(propertyRegistry.getByName("shoulder_entity_right", ParrotVariant.class))));
        }
        else if (entity.getType().equals(EntityTypes.ARMOR_STAND)) {
            add(data, metadataFactory.armorStandProperties(
                    properties.getProperty(propertyRegistry.getByName("small", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("arms", Boolean.class)),
                    !properties.getProperty(propertyRegistry.getByName("base_plate", Boolean.class))
            ));
            add(data, metadataFactory.armorStandHeadRotation(properties.getProperty(propertyRegistry.getByName("head_rotation", Vector3f.class))));
            add(data, metadataFactory.armorStandBodyRotation(properties.getProperty(propertyRegistry.getByName("body_rotation", Vector3f.class))));
            add(data, metadataFactory.armorStandLeftArmRotation(properties.getProperty(propertyRegistry.getByName("left_arm_rotation", Vector3f.class))));
            add(data, metadataFactory.armorStandRightArmRotation(properties.getProperty(propertyRegistry.getByName("right_arm_rotation", Vector3f.class))));
            add(data, metadataFactory.armorStandLeftLegRotation(properties.getProperty(propertyRegistry.getByName("left_leg_rotation", Vector3f.class))));
            add(data, metadataFactory.armorStandRightLegRotation(properties.getProperty(propertyRegistry.getByName("right_leg_rotation", Vector3f.class))));
        }
        else if (entity.getType().equals(EntityTypes.AXOLOTL)) {
            add(data, metadataFactory.axolotlVariant(properties.getProperty(propertyRegistry.getByName("axolotl_variant", Integer.class))));
            add(data, metadataFactory.playingDead(properties.getProperty(propertyRegistry.getByName("playing_dead", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BAT)) {
            add(data, metadataFactory.batHanging(properties.getProperty(propertyRegistry.getByName("hanging", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BEE)) {
            add(data, metadataFactory.beeAngry(properties.getProperty(propertyRegistry.getByName("angry", Boolean.class))));
            add(data, metadataFactory.beeHasNectar(properties.getProperty(propertyRegistry.getByName("has_nectar", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BLAZE)) {
            add(data, metadataFactory.blazeOnFire(properties.getProperty(propertyRegistry.getByName("blaze_on_fire", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.CAT)) {
            add(data, metadataFactory.catVariant(properties.getProperty(propertyRegistry.getByName("cat_variant", CatVariant.class))));
            add(data, metadataFactory.catLying(properties.getProperty(propertyRegistry.getByName("cat_lying", Boolean.class))));
            add(data, metadataFactory.catCollarColor(properties.getProperty(propertyRegistry.getByName("cat_collar_color", DyeColor.class))));
            add(data, metadataFactory.catTamed(properties.hasProperty(propertyRegistry.getByName("cat_collar_color", DyeColor.class))));
        }
        else if (entity.getType().equals(EntityTypes.CREEPER)) {
            add(data, metadataFactory.creeperState(properties.getProperty(propertyRegistry.getByName("creeper_state", CreeperState.class))));
            add(data, metadataFactory.creeperCharged(properties.getProperty(propertyRegistry.getByName("creeper_charged", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.ENDERMAN)) {
            add(data, metadataFactory.endermanHeldBlock(
                    properties.getProperty(propertyRegistry.getByName("enderman_held_block", BlockState.class)).getGlobalId())
            );
            add(data, metadataFactory.endermanScreaming(properties.getProperty(propertyRegistry.getByName("enderman_screaming", Boolean.class))));
            add(data, metadataFactory.endermanStaring(properties.getProperty(propertyRegistry.getByName("enderman_staring", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.EVOKER)) {
            add(data, metadataFactory.evokerSpell(properties.getProperty(propertyRegistry.getByName("evoker_spell", SpellType.class)).ordinal()));
        }
        else if (entity.getType().equals(EntityTypes.FOX)) {
            // Not sure if this should be in here or in 1.14 PacketFactory
            add(data, metadataFactory.foxVariant(properties.getProperty(propertyRegistry.getByName("fox_variant", FoxVariant.class)).ordinal()));
            add(data, metadataFactory.foxProperties(
                properties.getProperty(propertyRegistry.getByName("fox_sitting", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_crouching", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_sleeping", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_faceplanted", Boolean.class))
            ));
        } else if (entity.getType().equals(EntityTypes.FROG)) {
            add(data, metadataFactory.frogVariant(properties.getProperty(propertyRegistry.getByName("frog_variant", FrogVariant.class)).ordinal()));
        }

        if (properties.getProperty(propertyRegistry.getByName("dinnerbone", Boolean.class))) {
            add(data, metadataFactory.name(Component.text("Dinnerbone")));
        }
        else if (properties.hasProperty(propertyRegistry.getByName("name"))) {
            add(data, metadataFactory.name(PapiUtil.set(textSerializer, player, properties.getProperty(propertyRegistry.getByName("name", Component.class)))));
            add(data, metadataFactory.nameShown());
        }
        return data;
    }

    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        sendMetadata(player, entity, new ArrayList<>(generateMetadata(player, entity, properties).values()));
        sendEquipment(player, entity, properties);
    }

    @Override
    public void sendMetadata(Player player, PacketEntity entity, List<EntityData> data) {
        packetEvents.getPlayerManager().sendPacket(player, new WrapperPlayServerEntityMetadata(entity.getEntityId(), data));
    }

    @Override
    public void sendEquipment(Player player, PacketEntity entity, PropertyHolder properties) {
        for (Equipment equipment : generateEquipments(properties))
            sendPacket(player, new WrapperPlayServerEntityEquipment(entity.getEntityId(), Collections.singletonList(equipment)));
    }

    protected List<Equipment> generateEquipments(PropertyHolder properties) {
        HashMap<String, EquipmentSlot> equipmentSlotMap = new HashMap<>();
        equipmentSlotMap.put("helmet", EquipmentSlot.HELMET);
        equipmentSlotMap.put("chestplate", EquipmentSlot.CHEST_PLATE);
        equipmentSlotMap.put("leggings", EquipmentSlot.LEGGINGS);
        equipmentSlotMap.put("boots", EquipmentSlot.BOOTS);
        equipmentSlotMap.put("hand", EquipmentSlot.MAIN_HAND);
        equipmentSlotMap.put("offhand", EquipmentSlot.OFF_HAND);
        List<Equipment> equipements = new ArrayList<>();

        for (Map.Entry<String, EquipmentSlot> entry : equipmentSlotMap.entrySet()) {
            if (!properties.hasProperty(propertyRegistry.getByName(entry.getKey()))) continue;
            equipements.add(new Equipment(entry.getValue(), properties.getProperty(propertyRegistry.getByName(entry.getKey(), ItemStack.class))));
        }
        return equipements;
    }

    protected void sendPacket(Player player, PacketWrapper<?> packet) {
        packetEvents.getPlayerManager().sendPacket(player, packet);
    }

    protected CompletableFuture<UserProfile> skinned(Player player, PropertyHolder properties, UserProfile profile) {
        if (!properties.hasProperty(propertyRegistry.getByName("skin"))) return CompletableFuture.completedFuture(profile);
        BaseSkinDescriptor descriptor = (BaseSkinDescriptor) properties.getProperty(propertyRegistry.getByName("skin", SkinDescriptor.class));
        if (descriptor.supportsInstant(player)) {
            descriptor.fetchInstant(player).apply(profile);
            return CompletableFuture.completedFuture(profile);
        }
        CompletableFuture<UserProfile> future = new CompletableFuture<>();
        descriptor.fetch(player).thenAccept(skin -> {
            if (skin != null) skin.apply(profile);
            future.complete(profile);
        });
        return future;
    }

    protected void add(Map<Integer, EntityData> map, EntityData data) {
        map.put(data.getIndex(), data);
    }
}
