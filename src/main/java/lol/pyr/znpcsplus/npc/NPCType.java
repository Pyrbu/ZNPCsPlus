package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class NPCType {
    private final static ImmutableList<NPCType> npcTypes;

    public static List<NPCType> values() {
        return npcTypes;
    }

    private final EntityType type;

    public NPCType(EntityType type) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }

    static {
        ImmutableList.Builder<NPCType> builder = new ImmutableList.Builder<>();

        builder.add(new NPCType(EntityTypes.PLAYER));
        builder.add(new NPCType(EntityTypes.CREEPER));
        builder.add(new NPCType(EntityTypes.ZOMBIE));
        builder.add(new NPCType(EntityTypes.SKELETON));

        npcTypes = builder.build();
    }
}
