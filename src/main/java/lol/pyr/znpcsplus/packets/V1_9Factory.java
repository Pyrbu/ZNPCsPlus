package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.metadata.MetadataFactory;
import lol.pyr.znpcsplus.npc.NPC;
import lol.pyr.znpcsplus.npc.NPCProperty;
import org.bukkit.entity.Player;

public class V1_9Factory extends V1_8Factory {
    @Override
    public void sendAllMetadata(Player player, PacketEntity entity) {
        NPC owner = entity.getOwner();
        if (entity.getType() == EntityTypes.PLAYER && owner.getProperty(NPCProperty.SKIN_LAYERS)) sendMetadata(player, entity, MetadataFactory.get().skinLayers());
        boolean glow = owner.hasProperty(NPCProperty.GLOW);
        boolean fire = owner.getProperty(NPCProperty.FIRE);
        if (glow || fire) sendMetadata(player, entity, MetadataFactory.get().effects(fire, glow));
    }

    @Override
    public void spawnEntity(Player player, PacketEntity entity) {
        super.spawnEntity(player, entity);
        if (entity.getOwner().hasProperty(NPCProperty.GLOW)) createTeam(player, entity);
    }

    @Override
    public void destroyEntity(Player player, PacketEntity entity) {
        super.destroyEntity(player, entity);
        if (entity.getType() != EntityTypes.PLAYER && entity.getOwner().hasProperty(NPCProperty.GLOW)) removeTeam(player, entity);
    }
}
