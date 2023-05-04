package lol.pyr.znpcsplus.api.npc;

public interface NPCEntry {
    NPC getNpc();

    boolean isProcessed();
    void setProcessed(boolean value);

    boolean isSave();
    void setSave(boolean value);

    boolean isAllowCommandModification();
    void setAllowCommandModification(boolean value);
}
