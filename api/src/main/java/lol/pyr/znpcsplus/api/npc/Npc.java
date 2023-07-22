package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface Npc extends PropertyHolder {
    void setType(NpcType type);
    NpcType getType();
    NpcLocation getLocation();
    void setLocation(NpcLocation location);
    Hologram getHologram();
    void setEnabled(boolean enabled);
    boolean isEnabled();
    UUID getUuid();
    World getWorld();
    List<? extends InteractionAction> getActions();
    boolean isVisibleTo(Player player);
    void hide(Player player);
    void show(Player player);
    void respawn(Player player);
}
