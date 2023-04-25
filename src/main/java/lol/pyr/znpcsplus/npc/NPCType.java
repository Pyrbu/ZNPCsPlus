package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;

import java.util.*;

public class NPCType {
    private final static Map<String, NPCType> BY_NAME = new HashMap<>();

    public static Collection<NPCType> values() {
        return BY_NAME.values();
    }

    public static NPCType byName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    private final EntityType type;
    private final Set<NPCProperty<?>> allowedProperties;
    private final String name;

    private NPCType(String name, EntityType type, NPCProperty<?>... allowedProperties) {
        this.name = name.toUpperCase();
        this.type = type;
        ArrayList<NPCProperty<?>> list = new ArrayList<>(List.of(allowedProperties));
        list.add(NPCProperty.FIRE);
        if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) list.add(NPCProperty.GLOW);
        this.allowedProperties = Set.copyOf(list);
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public Set<NPCProperty<?>> getAllowedProperties() {
        return allowedProperties;
    }

    private static void register(NPCType type) {
        BY_NAME.put(type.getName(), type);

    }

    static {
        register(new NPCType("player", EntityTypes.PLAYER, NPCProperty.SKIN, NPCProperty.SKIN_LAYERS));
        register(new NPCType("creeper", EntityTypes.CREEPER));
        register(new NPCType("zombie", EntityTypes.ZOMBIE));
        register(new NPCType("skeleton", EntityTypes.SKELETON));
    }
}
