package io.github.znetworkw.znpcservers.npc.conversation;

import lol.pyr.znpcsplus.ZNPCsPlus;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.hologram.replacer.LineReplacer;
import io.github.znetworkw.znpcservers.user.ZUser;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConversationProcessor {
    private static final Map<UUID, String> RUNNING_CONVERSATIONS = new HashMap<>();

    private static final String WHITE_SPACE = " ";

    private static final int CONVERSATION_DELAY = 20;

    private final NPC npc;

    private final ConversationModel conversationModel;

    private final Player player;

    private int conversationIndex = 0;

    private long conversationIndexDelay = System.nanoTime();

    public ConversationProcessor(NPC npc, ConversationModel conversationModel, Player player) {
        if (conversationModel.getConversation().getTexts().isEmpty())
            throw new IllegalStateException("conversation should have a text.");
        this.npc = npc;
        this.conversationModel = conversationModel;
        this.player = player;
        RUNNING_CONVERSATIONS.put(player.getUniqueId(), conversationModel.getConversationName());
        start();
    }

    public static boolean isPlayerConversing(UUID uuid) {
        return RUNNING_CONVERSATIONS.containsKey(uuid);
    }

    private void start() {
        ZNPCsPlus.SCHEDULER.runTaskTimer(new BukkitRunnable() {
            public void run() {
                if (Bukkit.getPlayer(ConversationProcessor.this.player.getUniqueId()) == null || ConversationProcessor.this
                        .conversationIndex > ConversationProcessor.this.conversationModel.getConversation().getTexts().size() - 1 || ConversationProcessor.this
                        .conversationModel.canRun(ConversationProcessor.this.npc, ConversationProcessor.this.player)) {
                    ConversationProcessor.RUNNING_CONVERSATIONS.remove(ConversationProcessor.this.player.getUniqueId());
                    cancel();
                    return;
                }
                ConversationKey conversationKey = ConversationProcessor.this.conversationModel.getConversation().getTexts().get(ConversationProcessor.this.conversationIndex);
                long conversationDelayNanos = System.nanoTime() - ConversationProcessor.this.conversationIndexDelay;
                if (ConversationProcessor.this.conversationIndex != 0 && conversationDelayNanos < 1000000000L * conversationKey
                        .getDelay())
                    return;
                ZUser user = ZUser.find(ConversationProcessor.this.player);
                conversationKey.getLines().forEach(s -> ConversationProcessor.this.player.sendMessage(LineReplacer.makeAll(user, s).replace(ConfigurationConstants.SPACE_SYMBOL, " ")));
                if (conversationKey.getActions().size() > 0)
                    conversationKey.getActions().forEach(action -> action.run(user, action.getAction()));
                if (conversationKey.getSoundName() != null && conversationKey
                        .getSoundName().length() > 0)
                    try {
                        Sound sound = Sound.valueOf(conversationKey.getSoundName().toUpperCase());
                        ConversationProcessor.this.player.playSound(ConversationProcessor.this.player.getLocation(), sound, 0.2F, 1.0F);
                    } catch (IllegalArgumentException ignored) {
                    }
                ConversationProcessor.this.conversationIndexDelay = System.nanoTime();
                ConversationProcessor.this.conversationIndex++;
            }
        }, 5, 20);
    }
}
