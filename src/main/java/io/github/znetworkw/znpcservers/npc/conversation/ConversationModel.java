package io.github.znetworkw.znpcservers.npc.conversation;

import io.github.znetworkw.znpcservers.npc.NPC;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class ConversationModel {
    private final transient Map<UUID, Long> lastStarted = new HashMap<>();
    private String conversationName;
    private ConversationType conversationType;

    public ConversationModel(String conversationName, String conversationType) {
        this.conversationName = conversationName;
        try {
            this.conversationType = ConversationType.valueOf(conversationType.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("can't find conversation type " + conversationType);
        }
    }

    private ConversationModel() {
    }

    public String getConversationName() {
        return this.conversationName;
    }

    public ConversationType getConversationType() {
        return this.conversationType;
    }

    public Conversation getConversation() {
        return Conversation.forName(this.conversationName);
    }

    public void startConversation(NPC npc, Player player) {
        if (!Conversation.exists(this.conversationName))
            throw new IllegalStateException("can't find conversation " + this.conversationName);
        if (ConversationProcessor.isPlayerConversing(player.getUniqueId()))
            return;
        if (this.lastStarted.containsKey(player.getUniqueId())) {
            long lastConversationNanos = System.nanoTime() - this.lastStarted.get(player.getUniqueId());
            if (lastConversationNanos < 1000000000L * getConversation().getDelay())
                return;
        }
        this.lastStarted.remove(player.getUniqueId());
        if (this.conversationType.canStart(npc, getConversation(), player)) {
            new ConversationProcessor(npc, this, player);
            this.lastStarted.put(player.getUniqueId(), System.nanoTime());
        }
    }

    public boolean canRun(NPC npc, Player player) {
        return Stream.of(ConversationType.values()).anyMatch(conversationType1 -> !conversationType1.canStart(npc, getConversation(), player));
    }

    public enum ConversationType {
        RADIUS {
            public boolean canStart(NPC npc, Conversation conversation, Player player) {
                return (player.getWorld() == npc.getLocation().getWorld() && player
                        .getLocation().distance(npc.getLocation()) <= conversation.getRadius());
            }
        },
        CLICK {
            public boolean canStart(NPC npc, Conversation conversation, Player player) {
                return true;
            }
        };

        abstract boolean canStart(NPC param1NPC, Conversation param1Conversation, Player param1Player);
    }
}
