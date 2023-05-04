package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.npc.NPCEntry;

public class NPCEntryImpl implements NPCEntry {
    private final NPCImpl npc;

    private boolean process = false;
    private boolean save = false;
    private boolean modify = false;

    public NPCEntryImpl(NPCImpl npc) {
        this.npc = npc;
    }

    @Override
    public NPCImpl getNpc() {
        return npc;
    }

    @Override
    public boolean isProcessed() {
        return process;
    }

    @Override
    public void setProcessed(boolean value) {
        if (process && !value) npc.delete();
        process = value;
    }

    @Override
    public boolean isSave() {
        return save;
    }

    @Override
    public void setSave(boolean value) {
        save = value;
    }

    @Override
    public boolean isAllowCommandModification() {
        return modify;
    }

    @Override
    public void setAllowCommandModification(boolean value) {
        modify = value;
    }

    public void enableEverything() {
        setSave(true);
        setProcessed(true);
        setAllowCommandModification(true);
    }
}
