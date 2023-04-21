package io.github.znetworkw.znpcservers.npc.conversation;

import com.google.common.base.Splitter;
import io.github.znetworkw.znpcservers.npc.NPCAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ConversationKey {
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");
    private final List<String> lines;
    private final List<NPCAction> actions;
    private int delay = 1;
    private String soundName;

    public ConversationKey(String line) {
        this(SPACE_SPLITTER.split(line));
    }

    public ConversationKey(Iterable<String> line) {
        this
                .lines = StreamSupport.stream(line.spliterator(), false).map(String::toString).collect(Collectors.toList());
        this.actions = new ArrayList<>();
    }

    public List<String> getLines() {
        return this.lines;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public String getSoundName() {
        return this.soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public List<NPCAction> getActions() {
        return this.actions;
    }

    public String getTextFormatted() {
        if (this.lines.isEmpty())
            return "";
        String text = this.lines.iterator().next();
        int fixedLength = Math.min(text.length(), 28);
        return text.substring(0, fixedLength);
    }
}
