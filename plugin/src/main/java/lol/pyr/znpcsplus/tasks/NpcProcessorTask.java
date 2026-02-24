package lol.pyr.znpcsplus.tasks;

import lol.pyr.znpcsplus.api.event.NpcDespawnEvent;
import lol.pyr.znpcsplus.api.event.NpcSpawnEvent;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;
import lol.pyr.znpcsplus.npc.NpcEntryImpl;
import lol.pyr.znpcsplus.npc.NpcImpl;
import lol.pyr.znpcsplus.npc.NpcRegistryImpl;
import lol.pyr.znpcsplus.scheduling.TaskScheduler;
import lol.pyr.znpcsplus.user.User;
import lol.pyr.znpcsplus.user.UserManager;
import lol.pyr.znpcsplus.util.LookType;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class NpcProcessorTask extends BukkitRunnable {
    private final NpcRegistryImpl npcRegistry;
    private final TaskScheduler scheduler;
    private final UserManager userManager;

    private final EntityPropertyImpl<Integer> viewDistanceProperty;
    private final EntityPropertyImpl<LookType> lookProperty;
    private final EntityPropertyImpl<Double> lookDistanceProperty;
    private final EntityPropertyImpl<Boolean> lookReturnProperty;
    private final EntityPropertyImpl<Boolean> permissionRequiredProperty;
    private final EntityPropertyImpl<String> permissionNodeProperty;
    private final EntityPropertyImpl<Boolean> playerKnockbackProperty;
    private final EntityPropertyImpl<String> playerKnockbackExemptPermissionProperty;
    private final EntityPropertyImpl<Double> playerKnockbackDistanceProperty;
    private final EntityPropertyImpl<Double> playerKnockbackVerticalProperty;
    private final EntityPropertyImpl<Double> playerKnockbackHorizontalProperty;
    private final EntityPropertyImpl<Integer> playerKnockbackCooldownProperty;
    private final EntityPropertyImpl<Boolean> playerKnockbackSoundProperty;
    private final EntityPropertyImpl<Sound> playerKnockbackSoundNameProperty;
    private final EntityPropertyImpl<Float> playerKnockbackSoundVolumeProperty;
    private final EntityPropertyImpl<Float> playerKnockbackSoundPitchProperty;

    public NpcProcessorTask(NpcRegistryImpl npcRegistry, EntityPropertyRegistryImpl propertyRegistry, UserManager userManager, TaskScheduler scheduler) {
        this.npcRegistry = npcRegistry;
        this.userManager = userManager;
        this.scheduler = scheduler;

        // viewDistanceProperty is intentionally Integer for backwards compatibility in config/property serializers
        viewDistanceProperty = propertyRegistry.getByName("view_distance", Integer.class);
        lookProperty = propertyRegistry.getByName("look", LookType.class);
        lookDistanceProperty = propertyRegistry.getByName("look_distance", Double.class);
        lookReturnProperty = propertyRegistry.getByName("look_return", Boolean.class);
        permissionRequiredProperty = propertyRegistry.getByName("permission_required", Boolean.class);
        permissionNodeProperty = propertyRegistry.getByName("premission_required_perm", String.class);
        playerKnockbackProperty = propertyRegistry.getByName("player_knockback", Boolean.class);
        playerKnockbackExemptPermissionProperty = propertyRegistry.getByName("player_knockback_exempt_permission", String.class);
        playerKnockbackDistanceProperty = propertyRegistry.getByName("player_knockback_distance", Double.class);
        playerKnockbackVerticalProperty = propertyRegistry.getByName("player_knockback_vertical", Double.class);
        playerKnockbackHorizontalProperty = propertyRegistry.getByName("player_knockback_horizontal", Double.class);
        playerKnockbackCooldownProperty = propertyRegistry.getByName("player_knockback_cooldown", Integer.class);
        playerKnockbackSoundProperty = propertyRegistry.getByName("player_knockback_sound", Boolean.class);
        playerKnockbackSoundNameProperty = propertyRegistry.getByName("player_knockback_sound_name", Sound.class);
        playerKnockbackSoundVolumeProperty = propertyRegistry.getByName("player_knockback_sound_volume", Float.class);
        playerKnockbackSoundPitchProperty = propertyRegistry.getByName("player_knockback_sound_pitch", Float.class);
    }

    @Override
    public void run() {
        ProcessingSnapshot snapshot = captureSnapshot();
        if (snapshot.npcs.isEmpty() || snapshot.players.isEmpty()) return;

        List<PendingKnockback> pendingKnockback = processSnapshot(snapshot);
        if (!pendingKnockback.isEmpty()) scheduler.runSyncGlobal(() -> applyKnockback(pendingKnockback));
    }

    private ProcessingSnapshot captureSnapshot() {
        if (Bukkit.isPrimaryThread()) return buildSnapshotSync();

        CompletableFuture<ProcessingSnapshot> future = new CompletableFuture<>();
        scheduler.runSyncGlobal(() -> {
            try {
                future.complete(buildSnapshotSync());
            } catch (Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        });
        return future.join();
    }

    private ProcessingSnapshot buildSnapshotSync() {
        Collection<NpcEntryImpl> processable = npcRegistry.getProcessable();
        if (processable.isEmpty()) return new ProcessingSnapshot(Collections.emptyList(), Collections.emptyList());

        List<NpcSnapshot> npcSnapshots = new ArrayList<>();
        Set<String> queriedPermissions = new HashSet<>();

        for (NpcEntryImpl entry : processable) {
            NpcImpl npc = entry.getNpc();
            if (!npc.isEnabled()) continue;

            LookType lookType = npc.getProperty(lookProperty);
            double lookDistance = NumberConversions.square(npc.getProperty(lookDistanceProperty));
            boolean lookReturn = npc.getProperty(lookReturnProperty);

            boolean permissionRequired = npc.getProperty(permissionRequiredProperty);
            String permissionNode = null;
            if (permissionRequired) {
                permissionNode = trimToNull(npc.getProperty(permissionNodeProperty));
                if (permissionNode == null) permissionNode = "znpcsplus.npc." + entry.getId();
                queriedPermissions.add(permissionNode);
            }

            boolean playerKnockback = npc.getProperty(playerKnockbackProperty);
            String playerKnockbackExemptPermission = null;
            double playerKnockbackDistance = 0;
            double playerKnockbackVertical = 0;
            double playerKnockbackHorizontal = 0;
            int playerKnockbackCooldown = 0;
            boolean playerKnockbackSound = false;
            Sound playerKnockbackSoundName = null;
            float playerKnockbackSoundVolume = 0;
            float playerKnockbackSoundPitch = 0;

            if (playerKnockback) {
                playerKnockbackExemptPermission = trimToNull(npc.getProperty(playerKnockbackExemptPermissionProperty));
                if (playerKnockbackExemptPermission != null) queriedPermissions.add(playerKnockbackExemptPermission);
                playerKnockbackDistance = NumberConversions.square(npc.getProperty(playerKnockbackDistanceProperty));
                playerKnockbackVertical = npc.getProperty(playerKnockbackVerticalProperty);
                playerKnockbackHorizontal = npc.getProperty(playerKnockbackHorizontalProperty);
                playerKnockbackCooldown = npc.getProperty(playerKnockbackCooldownProperty);
                playerKnockbackSound = npc.getProperty(playerKnockbackSoundProperty);
                playerKnockbackSoundName = npc.getProperty(playerKnockbackSoundNameProperty);
                playerKnockbackSoundVolume = npc.getProperty(playerKnockbackSoundVolumeProperty);
                playerKnockbackSoundPitch = npc.getProperty(playerKnockbackSoundPitchProperty);
            }

            npcSnapshots.add(new NpcSnapshot(
                    entry,
                    npc,
                    npc.getWorldName(),
                    npc.getLocation(),
                    NumberConversions.square(npc.getProperty(viewDistanceProperty)),
                    lookType,
                    lookDistance,
                    lookReturn,
                    permissionRequired,
                    permissionNode,
                    npc.getType().getHologramOffset(),
                    playerKnockback,
                    playerKnockbackExemptPermission,
                    playerKnockbackDistance,
                    playerKnockbackVertical,
                    playerKnockbackHorizontal,
                    playerKnockbackCooldown,
                    playerKnockbackSound,
                    playerKnockbackSoundName,
                    playerKnockbackSoundVolume,
                    playerKnockbackSoundPitch
            ));
        }

        if (npcSnapshots.isEmpty()) return new ProcessingSnapshot(Collections.emptyList(), Collections.emptyList());

        List<PlayerSnapshot> playerSnapshots = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Set<String> permissions = new HashSet<>();
            for (String permission : queriedPermissions) {
                if (player.hasPermission(permission)) permissions.add(permission);
            }
            playerSnapshots.add(new PlayerSnapshot(
                    player,
                    player.getUniqueId(),
                    player.getWorld().getName(),
                    new NpcLocation(player.getLocation()),
                    permissions
            ));
        }

        return new ProcessingSnapshot(npcSnapshots, playerSnapshots);
    }

    private List<PendingKnockback> processSnapshot(ProcessingSnapshot snapshot) {
        List<PendingKnockback> pendingKnockback = new ArrayList<>();

        for (NpcSnapshot npcState : snapshot.npcs) {
            double closestDist = Double.MAX_VALUE;
            PlayerSnapshot closestPlayer = null;

            for (PlayerSnapshot playerState : snapshot.players) {
                Player player = playerState.player;
                NpcImpl npc = npcState.npc;

                if (!playerState.worldName.equals(npcState.worldName)) {
                    if (npc.isVisibleTo(player)) npc.hide(player);
                    continue;
                }

                if (npcState.permissionRequired && !playerState.hasPermission(npcState.permissionNode)) {
                    if (npc.isVisibleTo(player)) npc.hide(player);
                    continue;
                }

                double distance = distanceSquared(playerState.location, npcState.location);
                boolean inRange = distance <= npcState.viewDistanceSquared;

                if (!inRange && npc.isVisibleTo(player)) {
                    NpcDespawnEvent event = new NpcDespawnEvent(player, npcState.entry);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) npc.hide(player);
                }

                if (!inRange) continue;

                if (!npc.isVisibleTo(player)) {
                    NpcSpawnEvent event = new NpcSpawnEvent(player, npcState.entry);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) continue;
                    npc.show(player);
                }

                if (distance < closestDist) {
                    closestDist = distance;
                    closestPlayer = playerState;
                }

                if (npcState.lookType == LookType.PER_PLAYER) {
                    if (npcState.lookDistanceSquared >= distance) {
                        NpcLocation expected = npcState.location.lookingAt(playerState.location.withY(playerState.location.getY() - npcState.hologramOffset));
                        npc.setHeadRotation(player, expected.getYaw(), expected.getPitch());
                    } else if (npcState.lookReturn) {
                        npc.setHeadRotation(player, npcState.location.getYaw(), npcState.location.getPitch());
                    }
                }

                if (npcState.playerKnockback && distance <= npcState.playerKnockbackDistanceSquared) {
                    if (npcState.playerKnockbackExemptPermission == null || !playerState.hasPermission(npcState.playerKnockbackExemptPermission)) {
                        pendingKnockback.add(new PendingKnockback(
                                playerState.playerUuid,
                                calculateKnockbackVector(npcState.location, playerState.location, npcState.playerKnockbackHorizontal, npcState.playerKnockbackVertical),
                                npcState.playerKnockbackCooldown,
                                npcState.playerKnockbackSound,
                                npcState.playerKnockbackSoundName,
                                npcState.playerKnockbackSoundVolume,
                                npcState.playerKnockbackSoundPitch
                        ));
                    }
                }
            }

            if (npcState.lookType == LookType.CLOSEST_PLAYER) {
                if (closestPlayer != null && npcState.lookDistanceSquared >= closestDist) {
                    NpcLocation expected = npcState.location.lookingAt(closestPlayer.location.withY(closestPlayer.location.getY() - npcState.hologramOffset));
                    if (!expected.equals(npcState.location)) npcState.npc.setHeadRotation(expected.getYaw(), expected.getPitch());
                } else if (npcState.lookReturn) {
                    npcState.npc.setHeadRotation(npcState.location.getYaw(), npcState.location.getPitch());
                }
            } else if (npcState.lookType == LookType.FIXED) {
                npcState.npc.setHeadRotation(npcState.location.getYaw(), npcState.location.getPitch());
            }
        }

        return pendingKnockback;
    }

    private void applyKnockback(List<PendingKnockback> pendingKnockback) {
        for (PendingKnockback knockback : pendingKnockback) {
            Player player = Bukkit.getPlayer(knockback.playerUuid);
            if (player == null || !player.isOnline()) continue;

            User user = userManager.get(knockback.playerUuid);
            if (!user.canKnockback(knockback.cooldown)) continue;

            player.setVelocity(player.getVelocity().add(knockback.knockback));
            if (knockback.playSound && knockback.soundName != null) {
                player.playSound(player.getLocation(), knockback.soundName, knockback.soundVolume, knockback.soundPitch);
            }
        }
    }

    static double distanceSquared(NpcLocation first, NpcLocation second) {
        double x = first.getX() - second.getX();
        double y = first.getY() - second.getY();
        double z = first.getZ() - second.getZ();
        return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
    }

    static Vector calculateKnockbackVector(NpcLocation npcLocation, NpcLocation playerLocation, double horizontal, double vertical) {
        double x = npcLocation.getX() - playerLocation.getX();
        double z = npcLocation.getZ() - playerLocation.getZ();
        double angle = Math.atan2(z, x);
        double knockbackX = -Math.cos(angle) * horizontal;
        double knockbackZ = -Math.sin(angle) * horizontal;
        return new Vector(knockbackX, vertical, knockbackZ);
    }

    private static String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private static final class ProcessingSnapshot {
        private final List<NpcSnapshot> npcs;
        private final List<PlayerSnapshot> players;

        private ProcessingSnapshot(List<NpcSnapshot> npcs, List<PlayerSnapshot> players) {
            this.npcs = npcs;
            this.players = players;
        }
    }

    private static final class NpcSnapshot {
        private final NpcEntryImpl entry;
        private final NpcImpl npc;
        private final String worldName;
        private final NpcLocation location;
        private final double viewDistanceSquared;
        private final LookType lookType;
        private final double lookDistanceSquared;
        private final boolean lookReturn;
        private final boolean permissionRequired;
        private final String permissionNode;
        private final double hologramOffset;
        private final boolean playerKnockback;
        private final String playerKnockbackExemptPermission;
        private final double playerKnockbackDistanceSquared;
        private final double playerKnockbackVertical;
        private final double playerKnockbackHorizontal;
        private final int playerKnockbackCooldown;
        private final boolean playerKnockbackSound;
        private final Sound playerKnockbackSoundName;
        private final float playerKnockbackSoundVolume;
        private final float playerKnockbackSoundPitch;

        private NpcSnapshot(
                NpcEntryImpl entry,
                NpcImpl npc,
                String worldName,
                NpcLocation location,
                double viewDistanceSquared,
                LookType lookType,
                double lookDistanceSquared,
                boolean lookReturn,
                boolean permissionRequired,
                String permissionNode,
                double hologramOffset,
                boolean playerKnockback,
                String playerKnockbackExemptPermission,
                double playerKnockbackDistanceSquared,
                double playerKnockbackVertical,
                double playerKnockbackHorizontal,
                int playerKnockbackCooldown,
                boolean playerKnockbackSound,
                Sound playerKnockbackSoundName,
                float playerKnockbackSoundVolume,
                float playerKnockbackSoundPitch
        ) {
            this.entry = entry;
            this.npc = npc;
            this.worldName = worldName;
            this.location = location;
            this.viewDistanceSquared = viewDistanceSquared;
            this.lookType = lookType;
            this.lookDistanceSquared = lookDistanceSquared;
            this.lookReturn = lookReturn;
            this.permissionRequired = permissionRequired;
            this.permissionNode = permissionNode;
            this.hologramOffset = hologramOffset;
            this.playerKnockback = playerKnockback;
            this.playerKnockbackExemptPermission = playerKnockbackExemptPermission;
            this.playerKnockbackDistanceSquared = playerKnockbackDistanceSquared;
            this.playerKnockbackVertical = playerKnockbackVertical;
            this.playerKnockbackHorizontal = playerKnockbackHorizontal;
            this.playerKnockbackCooldown = playerKnockbackCooldown;
            this.playerKnockbackSound = playerKnockbackSound;
            this.playerKnockbackSoundName = playerKnockbackSoundName;
            this.playerKnockbackSoundVolume = playerKnockbackSoundVolume;
            this.playerKnockbackSoundPitch = playerKnockbackSoundPitch;
        }
    }

    private static final class PlayerSnapshot {
        private final Player player;
        private final UUID playerUuid;
        private final String worldName;
        private final NpcLocation location;
        private final Set<String> permissions;

        private PlayerSnapshot(Player player, UUID playerUuid, String worldName, NpcLocation location, Set<String> permissions) {
            this.player = player;
            this.playerUuid = playerUuid;
            this.worldName = worldName;
            this.location = location;
            this.permissions = permissions;
        }

        private boolean hasPermission(String permission) {
            return permission != null && permissions.contains(permission);
        }
    }

    private static final class PendingKnockback {
        private final UUID playerUuid;
        private final Vector knockback;
        private final int cooldown;
        private final boolean playSound;
        private final Sound soundName;
        private final float soundVolume;
        private final float soundPitch;

        private PendingKnockback(UUID playerUuid, Vector knockback, int cooldown, boolean playSound, Sound soundName, float soundVolume, float soundPitch) {
            this.playerUuid = playerUuid;
            this.knockback = knockback;
            this.cooldown = cooldown;
            this.playSound = playSound;
            this.soundName = soundName;
            this.soundVolume = soundVolume;
            this.soundPitch = soundPitch;
        }
    }
}
