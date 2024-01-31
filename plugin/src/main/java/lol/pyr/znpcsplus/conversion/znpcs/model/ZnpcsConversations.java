package lol.pyr.znpcsplus.conversion.znpcs.model;

@SuppressWarnings("unused")
public class ZnpcsConversations {

    private String name;
    private ZNpcsConversationText[] texts;
    private int radius;
    private int delay;

    public String getName() {
        return name;
    }
    public ZNpcsConversationText[] getTexts() {
        return texts;
    }
    public int getRadius() {
        return radius;
    }
    public int getDelay() {
        return delay;
    }
}
