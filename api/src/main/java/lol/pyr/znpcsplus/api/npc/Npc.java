package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Base class for all NPCs
 */
public interface Npc extends PropertyHolder {
    /**
     * Sets the npc type of this NPC
     * @param type The {@link NpcType} to set
     */
    void setType(NpcType type);

    /**
     * @return The {@link NpcType} of this NPC
     */
    NpcType getType();

    /**
     * Gets the location of this NPC
     * @return The {@link NpcLocation} of this NPC
     */
    NpcLocation getLocation();

    /**
     * Sets the location of this NPC
     * @param location The {@link NpcLocation} to set
     */
    void setLocation(NpcLocation location);

    /**
     * Gets the hologram of this NPC
     * @return The {@link Hologram} of this NPC
     */
    Hologram getHologram();

    /**
     * Sets if the npc is enabled or not, i.e. if it should be visible to players
     * @param enabled If the npc should be enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Gets if the npc is enabled or not, i.e. if it should be visible to players
     * @return If the npc is enabled or not
     */
    boolean isEnabled();

    /**
     * Gets the unique ID of this NPC
     * @return The unique ID of this NPC
     */
    UUID getUuid();

    /**
     * Gets the {@link World} this NPC is in
     * Note: can be null if the world is unloaded or does not exist
     * @return The {@link World} this NPC is in
     */
    World getWorld();

    /**
     * Gets the list of actions for this NPC
     * @return The {@link List} of {@link InteractionAction}s for this NPC
     */
    List<? extends InteractionAction> getActions();

    /**
     * Gets if this NPC is visible to a player
     * @param player The {@link Player} to check
     * @return If this NPC is visible to the player
     */
    boolean isVisibleTo(Player player);

    /**
     * Hides this NPC from a player
     * @param player The {@link Player} to hide from
     */
    void hide(Player player);

    /**
     * Shows this NPC to a player
     * @param player The {@link Player} to show to
     */
    void show(Player player);

    /**
     * Respawns this NPC for a player
     * @param player The {@link Player} to respawn for
     */
    void respawn(Player player);

    /**
     * Sets the head rotation of this NPC for a player
     * @param player The {@link Player} to set the head rotation for
     * @param yaw The yaw to set
     * @param pitch The pitch to set
     */
    void setHeadRotation(Player player, float yaw, float pitch);

    /**
     * Sets the head rotation of this NPC for all players/viewers
     * @param yaw The yaw to set
     * @param pitch The pitch to set
     */
    void setHeadRotation(float yaw, float pitch);

    /**
     * @return The entity id of the packet entity that this npc object represents
     */
    int getPacketEntityId();

    /**
     * @return The set of players that can currently see this npc
     */
    Set<Player> getViewers();
}
