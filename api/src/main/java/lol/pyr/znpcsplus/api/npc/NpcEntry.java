package lol.pyr.znpcsplus.api.npc;

public interface NpcEntry {
    Npc getNpc();

    boolean isProcessed();
    void setProcessed(boolean value);

    boolean isSave();
    void setSave(boolean value);

    boolean isAllowCommandModification();
    void setAllowCommandModification(boolean value);
}
