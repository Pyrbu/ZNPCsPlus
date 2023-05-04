package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;

public interface Npc extends PropertyHolder {
    Hologram getHologram();
}
