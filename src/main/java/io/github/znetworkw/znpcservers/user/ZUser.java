package io.github.znetworkw.znpcservers.user;

import com.mojang.authlib.GameProfile;
import io.github.znetworkw.znpcservers.reflection.ReflectionCache;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.NPCAction;
import io.github.znetworkw.znpcservers.npc.event.ClickType;
import io.github.znetworkw.znpcservers.npc.event.NPCInteractEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lol.pyr.znpcsplus.ZNPCsPlus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ZUser {
    private static final Map<UUID, ZUser> USER_MAP = new HashMap<>();

    private final Map<Integer, Long> lastClicked;

    private final List<EventService<?>> eventServices;

    private final UUID uuid;

    private final GameProfile gameProfile;

    private final Object playerConnection;

    private boolean hasPath = false;

    private long lastInteract = 0L;

    public ZUser(UUID uuid) {
        this.uuid = uuid;
        this.lastClicked = new HashMap<>();
        this.eventServices = new ArrayList<>();
        try {
            Object playerHandle = ReflectionCache.GET_HANDLE_PLAYER_METHOD.load().invoke(toPlayer());
            this.gameProfile = (GameProfile) ReflectionCache.GET_PROFILE_METHOD.load().invoke(playerHandle, new Object[0]);
            this.playerConnection = ReflectionCache.PLAYER_CONNECTION_FIELD.load().get(playerHandle);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("can't create user for player " + uuid.toString(), e.getCause());
        }
        if (!tryRegisterChannel()) ZNPCsPlus.SCHEDULER.runTaskTimer(new ChannelRegistrationFallbackTask(this), 3);
    }

    private static class ChannelRegistrationFallbackTask extends BukkitRunnable {
        private final ZUser user;
        private final Player player;
        private int tries = 5;

        private ChannelRegistrationFallbackTask(ZUser user) {
            this.user = user;
            this.player = user.toPlayer();
        }

        @Override
        public void run() {
            if (!player.isOnline() || user.tryRegisterChannel()) {
                cancel();
                return;
            }
            if (tries-- > 0) return;
            cancel();
            player.kick(Component.text("[ZNPCsPlus]", NamedTextColor.RED).appendNewline()
                    .append(Component.text("Couldn't inject interaction detector to channel", NamedTextColor.WHITE)).appendNewline()
                    .append(Component.text("Please report this at https://github.com/Pyrbu/ZNPCsPlus", NamedTextColor.WHITE)));
            ZNPCsPlus.LOGGER.severe("Couldn't inject interaction detector to channel for player " + player.getName() + " (" + player.getUniqueId() + ")");
        }
    }

    private boolean tryRegisterChannel() {
        try {
            Channel channel = (Channel) ReflectionCache.CHANNEL_FIELD.load().get(ReflectionCache.NETWORK_MANAGER_FIELD.load().get(this.playerConnection));
            if (channel.pipeline().names().contains("npc_interact")) channel.pipeline().remove("npc_interact");
            channel.pipeline().addAfter("decoder", "npc_interact", new ZNPCSocketDecoder());
            return true;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("illegal access exception while trying to register npc_interact channel");
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static ZUser find(UUID uuid) {
        return USER_MAP.computeIfAbsent(uuid, ZUser::new);
    }

    public static ZUser find(Player player) {
        return find(player.getUniqueId());
    }

    public static void unregister(Player player) {
        ZUser zUser = USER_MAP.get(player.getUniqueId());
        if (zUser == null)
            throw new IllegalStateException("can't find user " + player.getUniqueId());
        USER_MAP.remove(player.getUniqueId());
        NPC.all().stream()
                .filter(npc -> npc.getViewers().contains(zUser))
                .forEach(npc -> npc.delete(zUser));
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    public Object getPlayerConnection() {
        return this.playerConnection;
    }

    public boolean isHasPath() {
        return this.hasPath;
    }

    public void setHasPath(boolean hasPath) {
        this.hasPath = hasPath;
    }

    public List<EventService<?>> getEventServices() {
        return this.eventServices;
    }

    public Player toPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    class ZNPCSocketDecoder extends MessageToMessageDecoder<Object> {
        protected void decode(ChannelHandlerContext channelHandlerContext, Object packet, List<Object> out) throws Exception {
            out.add(packet);
            if (packet.getClass() == ReflectionCache.PACKET_PLAY_IN_USE_ENTITY_CLASS) {
                long lastInteractNanos = System.nanoTime() - ZUser.this.lastInteract;
                if (ZUser.this.lastInteract != 0L && lastInteractNanos < 1000000000L)
                    return;
                int entityId = ReflectionCache.PACKET_IN_USE_ENTITY_ID_FIELD.load().getInt(packet);
                NPC npc = NPC.all().stream().filter(npc1 -> (npc1.getEntityID() == entityId)).findFirst().orElse(null);
                if (npc == null)
                    return;
                ClickType clickName = ClickType.forName(npc.getPackets().getProxyInstance().getClickType(packet).toString());
                ZUser.this.lastInteract = System.nanoTime();
                ZNPCsPlus.SCHEDULER.scheduleSyncDelayedTask(() -> {
                    Bukkit.getServer().getPluginManager().callEvent(new NPCInteractEvent(ZUser.this.toPlayer(), clickName, npc));
                    List<NPCAction> actions = npc.getNpcPojo().getClickActions();
                    if (actions == null || actions.isEmpty())
                        return;
                    for (NPCAction npcAction : actions) {
                        if (npcAction.getClickType() != ClickType.DEFAULT && clickName != npcAction.getClickType())
                            continue;
                        if (npcAction.getDelay() > 0) {
                            int actionId = npc.getNpcPojo().getClickActions().indexOf(npcAction);
                            if (ZUser.this.lastClicked.containsKey(actionId)) {
                                long lastClickNanos = System.nanoTime() - ZUser.this.lastClicked.get(actionId);
                                if (lastClickNanos < npcAction.getFixedDelay())
                                    continue;
                            }
                            ZUser.this.lastClicked.put(actionId, System.nanoTime());
                        }
                        npcAction.run(ZUser.this, npcAction.getAction());
                    }
                }, 1);
            }
        }
    }
}
