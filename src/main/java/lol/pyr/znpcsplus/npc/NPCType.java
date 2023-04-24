package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NPCType {
    private final static Set<NPCType> npcTypes;

    public static Set<NPCType> values() {
        return npcTypes;
    }

    private final EntityType type;
    private final Set<NPCProperty<?>> allowedProperties;

    public NPCType(EntityType type, NPCProperty<?>... allowedProperties) {
        this.type = type;
        ArrayList<NPCProperty<?>> list = new ArrayList<>(List.of(allowedProperties));
        list.add(NPCProperty.FIRE);
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) list.add(NPCProperty.GLOW);
        this.allowedProperties = Set.copyOf(list);
    }

    public EntityType getType() {
        return type;
    }

    public Set<NPCProperty<?>> getAllowedProperties() {
        return allowedProperties;
    }

    static {
        Set<NPCType> set = new HashSet<>();
        set.add(new NPCType(EntityTypes.PLAYER, NPCProperty.SKIN, NPCProperty.SKIN_LAYERS));
        set.add(new NPCType(EntityTypes.CREEPER));
        set.add(new NPCType(EntityTypes.ZOMBIE));
        set.add(new NPCType(EntityTypes.SKELETON));
        npcTypes = Set.copyOf(set);
    }
}
