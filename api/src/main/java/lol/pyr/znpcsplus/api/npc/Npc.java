package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.interaction.InteractionAction;
import lol.pyr.znpcsplus.util.NpcLocation;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public interface Npc extends PropertyHolder {
    Hologram getHologram();

    void delete();

    void respawn();

    int getEntityId();

    NpcLocation getNpcLocation();

    void setNpcLocation(NpcLocation npcLocation);

    World getWorld();

    void setWorld(World world);

    NpcType getType();

    default Location getBukkitLocation() {
        return getNpcLocation().toBukkitLocation(getWorld());
    }

    List<InteractionAction> getActions();

    void removeAction(int index);

    void editAction(int index, InteractionAction newAction);

    void addAction(InteractionAction parse);

    boolean isShown(Player player);

    void hide(Player player);

    void show(Player player);
}
