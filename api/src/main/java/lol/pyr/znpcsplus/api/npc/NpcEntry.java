package lol.pyr.znpcsplus.api.npc;

/**
 * Base class for all NPC entries
 * An NPC entry is a wrapper around an NPC that contains additional information
 */
public interface NpcEntry {
    /**
     * Gets the ID of this NPC entry
     * @return The ID of this NPC entry
     */
    String getId();
    /**
     * Gets the NPC of this NPC entry
     * @return The {@link Npc} of this NPC entry
     */
    Npc getNpc();

    /**
     * Gets if this NPC entry is processed or not
     * @return If this NPC entry is processed or not
     */
    boolean isProcessed();
    /**
     * Sets if this NPC entry is processed or not
     * @param value If this NPC entry is processed or not
     */
    void setProcessed(boolean value);

    /**
     * @return If this NPC entry SHOULD be saved into the storage or not
     */
    boolean isSave();
    /**
     * Sets if this NPC should be saved or not
     * @param value If this NPC entry should be saved or not
     */
    void setSave(boolean value);

    /**
     * Gets if this NPC can be modified by commands
     * @return {@code true} if this NPC can be modified by commands, {@code false} otherwise
     */
    boolean isAllowCommandModification();
    /**
     * Sets if this NPC can be modified by commands
     * @param value {@code true} if this NPC can be modified by commands, {@code false} otherwise
     */
    void setAllowCommandModification(boolean value);

    /**
     * Enables everything for this NPC entry
     * That is, it makes the NPC processed, saveable, and allows command modification
     */
    void enableEverything();
}
