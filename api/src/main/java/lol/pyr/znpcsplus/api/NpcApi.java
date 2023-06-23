package lol.pyr.znpcsplus.api;

import lol.pyr.znpcsplus.api.entity.EntityPropertyRegistry;
import lol.pyr.znpcsplus.api.npc.NpcRegistry;
import lol.pyr.znpcsplus.api.npc.NpcTypeRegistry;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;

public interface NpcApi {
    NpcRegistry getNpcRegistry();
    NpcTypeRegistry getNpcTypeRegistry();
    EntityPropertyRegistry getPropertyRegistry();
    SkinDescriptorFactory getSkinDescriptorFactory();
}
