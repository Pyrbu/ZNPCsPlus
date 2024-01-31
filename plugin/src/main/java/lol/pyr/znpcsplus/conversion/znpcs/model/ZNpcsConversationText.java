package lol.pyr.znpcsplus.conversion.znpcs.model;

@SuppressWarnings("unused")
public class ZNpcsConversationText {

    private String[] lines;
    private ZNpcsAction[] actions;
    private int delay;

    public String[] getLines() {
        return lines;
    }
    public ZNpcsAction[] getActions() {
        return actions;
    }
    public int getDelay() {
        return delay;
    }
}
