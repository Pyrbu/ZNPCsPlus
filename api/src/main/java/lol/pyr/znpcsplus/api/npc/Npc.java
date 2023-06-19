package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.api.hologram.Hologram;

import java.util.UUID;

public interface Npc extends PropertyHolder {
    Hologram getHologram();
    UUID getUuid();
}
