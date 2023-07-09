package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.Equipment;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.*;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.util.NpcLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class V1_8PacketFactory implements PacketFactory {
    protected final TaskScheduler scheduler;
    protected final PacketEventsAPI<Plugin> packetEvents;
    protected final EntityPropertyRegistryImpl propertyRegistry;
    protected final LegacyComponentSerializer textSerializer;

    public V1_8PacketFactory(TaskScheduler scheduler, PacketEventsAPI<Plugin> packetEvents, EntityPropertyRegistryImpl propertyRegistry, LegacyComponentSerializer textSerializer) {
        this.scheduler = scheduler;
        this.packetEvents = packetEvents;
        this.propertyRegistry = propertyRegistry;
        this.textSerializer = textSerializer;
    }

    @Override
    public void spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties) {
        addTabPlayer(player, entity, properties).thenAccept(ignored -> {
            createTeam(player, entity, properties.getProperty(propertyRegistry.getByName("glow", NamedTextColor.class)));
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
        createTeam(player, entity, properties.getProperty(propertyRegistry.getByName("glow", NamedTextColor.class)));
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
    public void createTeam(Player player, PacketEntity entity, NamedTextColor glowColor) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.CREATE, new WrapperPlayServerTeams.ScoreBoardTeamInfo(
                Component.empty(), Component.empty(), Component.empty(),
                WrapperPlayServerTeams.NameTagVisibility.NEVER,
                WrapperPlayServerTeams.CollisionRule.NEVER,
                glowColor == null ? NamedTextColor.WHITE : glowColor,
                WrapperPlayServerTeams.OptionData.NONE
        )));
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.ADD_ENTITIES, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null,
                entity.getType() == EntityTypes.PLAYER ? Integer.toString(entity.getEntityId()) : entity.getUuid().toString()));
    }

    @Override
    public void removeTeam(Player player, PacketEntity entity) {
        sendPacket(player, new WrapperPlayServerTeams("npc_team_" + entity.getEntityId(), WrapperPlayServerTeams.TeamMode.REMOVE, (WrapperPlayServerTeams.ScoreBoardTeamInfo) null));
    }

        /*
        List<EntityData> data = new ArrayList<>();
        data.add(metadataFactory.effects(
                properties.getProperty(propertyRegistry.getByName("fire", Boolean.class)),
                false,
                properties.getProperty(propertyRegistry.getByName("invisible", Boolean.class)),
                false,
                properties.getProperty(propertyRegistry.getByName("using_item", Boolean.class))
        ));
        data.add(metadataFactory.silent(properties.getProperty(propertyRegistry.getByName("silent", Boolean.class))));
        data.add(metadataFactory.potionColor(properties.getProperty(propertyRegistry.getByName("potion_color", Color.class)).asRGB()));
        data.add(metadataFactory.potionAmbient(properties.getProperty(propertyRegistry.getByName("potion_ambient", Boolean.class))));
        if (entity.getType().equals(EntityTypes.PLAYER)) {
            data.add(metadataFactory.skinLayers(
                    properties.getProperty(propertyRegistry.getByName("skin_cape", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_jacket", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_left_sleeve", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_right_sleeve", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_left_leg", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_right_leg", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("skin_hat", Boolean.class))
            ));
            data.add(metadataFactory.shoulderEntityLeft(properties.getProperty(propertyRegistry.getByName("shoulder_entity_left", ParrotVariant.class))));
            data.add(metadataFactory.shoulderEntityRight(properties.getProperty(propertyRegistry.getByName("shoulder_entity_right", ParrotVariant.class))));
        }
        else if (entity.getType().equals(EntityTypes.ARMOR_STAND)) {
            data.add(metadataFactory.armorStandProperties(
                    properties.getProperty(propertyRegistry.getByName("small", Boolean.class)),
                    properties.getProperty(propertyRegistry.getByName("arms", Boolean.class)),
                    !properties.getProperty(propertyRegistry.getByName("base_plate", Boolean.class))
            ));
            data.add(metadataFactory.armorStandHeadRotation(properties.getProperty(propertyRegistry.getByName("head_rotation", Vector3f.class))));
            data.add(metadataFactory.armorStandBodyRotation(properties.getProperty(propertyRegistry.getByName("body_rotation", Vector3f.class))));
            data.add(metadataFactory.armorStandLeftArmRotation(properties.getProperty(propertyRegistry.getByName("left_arm_rotation", Vector3f.class))));
            data.add(metadataFactory.armorStandRightArmRotation(properties.getProperty(propertyRegistry.getByName("right_arm_rotation", Vector3f.class))));
            data.add(metadataFactory.armorStandLeftLegRotation(properties.getProperty(propertyRegistry.getByName("left_leg_rotation", Vector3f.class))));
            data.add(metadataFactory.armorStandRightLegRotation(properties.getProperty(propertyRegistry.getByName("right_leg_rotation", Vector3f.class))));
        }
        else if (entity.getType().equals(EntityTypes.AXOLOTL)) {
            data.add(metadataFactory.axolotlVariant(properties.getProperty(propertyRegistry.getByName("axolotl_variant", Integer.class))));
            data.add(metadataFactory.playingDead(properties.getProperty(propertyRegistry.getByName("playing_dead", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BAT)) {
            data.add(metadataFactory.batHanging(properties.getProperty(propertyRegistry.getByName("hanging", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BEE)) {
            data.add(metadataFactory.beeAngry(properties.getProperty(propertyRegistry.getByName("angry", Boolean.class))));
            data.add(metadataFactory.beeHasNectar(properties.getProperty(propertyRegistry.getByName("has_nectar", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.BLAZE)) {
            data.add(metadataFactory.blazeOnFire(properties.getProperty(propertyRegistry.getByName("blaze_on_fire", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.CAT)) {
            data.add(metadataFactory.catVariant(properties.getProperty(propertyRegistry.getByName("cat_variant", CatVariant.class))));
            data.add(metadataFactory.catLying(properties.getProperty(propertyRegistry.getByName("cat_lying", Boolean.class))));
            data.add(metadataFactory.catCollarColor(properties.getProperty(propertyRegistry.getByName("cat_collar_color", DyeColor.class))));
            data.add(metadataFactory.catTamed(properties.hasProperty(propertyRegistry.getByName("cat_collar_color", DyeColor.class))));
        }
        else if (entity.getType().equals(EntityTypes.CREEPER)) {
            data.add(metadataFactory.creeperState(properties.getProperty(propertyRegistry.getByName("creeper_state", CreeperState.class))));
            data.add(metadataFactory.creeperCharged(properties.getProperty(propertyRegistry.getByName("creeper_charged", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.ENDERMAN)) {
            data.add(metadataFactory.endermanHeldBlock(
                    properties.getProperty(propertyRegistry.getByName("enderman_held_block", BlockState.class)).getGlobalId())
            );
            data.add(metadataFactory.endermanScreaming(properties.getProperty(propertyRegistry.getByName("enderman_screaming", Boolean.class))));
            data.add(metadataFactory.endermanStaring(properties.getProperty(propertyRegistry.getByName("enderman_staring", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.EVOKER)) {
            data.add(metadataFactory.evokerSpell(properties.getProperty(propertyRegistry.getByName("evoker_spell", SpellType.class)).ordinal()));
        }
        else if (entity.getType().equals(EntityTypes.FOX)) {
            // Not sure if this should be in here or in 1.14 PacketFactory
            data.add(metadataFactory.foxVariant(properties.getProperty(propertyRegistry.getByName("fox_variant", FoxVariant.class)).ordinal()));
            data.add(metadataFactory.foxProperties(
                properties.getProperty(propertyRegistry.getByName("fox_sitting", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_crouching", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_sleeping", Boolean.class)),
                properties.getProperty(propertyRegistry.getByName("fox_faceplanted", Boolean.class))
            ));
        }
        else if (entity.getType().equals(EntityTypes.FROG)) {
            data.add(metadataFactory.frogVariant(properties.getProperty(propertyRegistry.getByName("frog_variant", FrogVariant.class)).ordinal()));
        }
        else if (entity.getType().equals(EntityTypes.GHAST)) {
            data.add(metadataFactory.ghastAttacking(properties.getProperty(propertyRegistry.getByName("attacking", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.GOAT)) {
            data.add(metadataFactory.goatHasLeftHorn(properties.getProperty(propertyRegistry.getByName("has_left_horn", Boolean.class))));
            data.add(metadataFactory.goatHasRightHorn(properties.getProperty(propertyRegistry.getByName("has_right_horn", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.GUARDIAN)) {
            // TODO
        }
        else if (entity.getType().equals(EntityTypes.HOGLIN)) {
            data.add(metadataFactory.hoglinImmuneToZombification(properties.getProperty(propertyRegistry.getByName("immune_to_zombification", Boolean.class))));
        }
        else if (entity.getType().equals(EntityTypes.VILLAGER)) {
            VillagerProfession profession = properties.getProperty(propertyRegistry.getByName("villager_profession", VillagerProfession.class));
            int professionId = profession.ordinal();
            if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_14)) {
                professionId = profession.getLegacyId();
            }
            data.add(metadataFactory.villagerData(
                    properties.getProperty(propertyRegistry.getByName("villager_type", VillagerType.class)).ordinal(),
                    professionId,
                    properties.getProperty(propertyRegistry.getByName("villager_level", VillagerLevel.class)).ordinal() + 1
            ));
        }

        if (properties.getProperty(propertyRegistry.getByName("dinnerbone", Boolean.class))) {
            data.add(metadataFactory.name(Component.text("Dinnerbone")));
        }
        else if (properties.hasProperty(propertyRegistry.getByName("name"))) {
            data.add(metadataFactory.name(PapiUtil.set(textSerializer, player, properties.getProperty(propertyRegistry.getByName("name", Component.class)))));
            data.add(metadataFactory.nameShown());
        }
        return data;
         */

    @Override
    public void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties) {
        Map<Integer, EntityData> datas = new HashMap<>();
        for (EntityProperty<?> property : properties.getAppliedProperties())
            ((EntityPropertyImpl<?>) property).UNSAFE_update(properties.getProperty(property), player, entity, false, datas);
        sendMetadata(player, entity, new ArrayList<>(datas.values()));
    }

    @Override
    public void sendMetadata(Player player, PacketEntity entity, List<EntityData> data) {
        sendPacket(player, new WrapperPlayServerEntityMetadata(entity.getEntityId(), data));
    }

    @Override
    public void sendEquipment(Player player, PacketEntity entity, Equipment equipment) {
        sendPacket(player, new WrapperPlayServerEntityEquipment(entity.getEntityId(), Collections.singletonList(equipment)));
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
