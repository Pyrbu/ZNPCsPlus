package lol.pyr.znpcsplus.api.npc;

import lol.pyr.znpcsplus.api.hologram.Hologram;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;

public interface NPC extends PropertyHolder {
    Hologram getHologram();
}
