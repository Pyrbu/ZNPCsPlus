package lol.pyr.znpcsplus.npc;

import lol.pyr.znpcsplus.api.npc.NpcEntry;

public class NpcEntryImpl implements NpcEntry {
    private final NpcImpl npc;

    private boolean process = false;
    private boolean save = false;
    private boolean modify = false;

    public NpcEntryImpl(NpcImpl npc) {
        this.npc = npc;
    }

    @Override
    public NpcImpl getNpc() {
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
