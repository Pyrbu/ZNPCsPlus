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

    private NPCType(String name, EntityType type, Set<NPCProperty<?>> allowedProperties) {
        this.name = name.toUpperCase();
        this.type = type;
        this.allowedProperties = allowedProperties;
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

    private static void register(Builder builder) {
        register(builder.build());
    }

    private static void register(NPCType type) {
        BY_NAME.put(type.getName(), type);

    }

    static {
        register(new Builder("player", EntityTypes.PLAYER)
                .addProperties(NPCProperty.SKIN, NPCProperty.SKIN_LAYERS));
        register(new Builder("creeper", EntityTypes.CREEPER));
        register(new Builder("zombie", EntityTypes.ZOMBIE));
        register(new Builder("skeleton", EntityTypes.SKELETON));
    }

    private static final class Builder {
        private final String name;
        private final EntityType type;
        private final List<NPCProperty<?>> allowedProperties = new ArrayList<>();
        private boolean globalProperties = true;

        private Builder(String name, EntityType type) {
            this.name = name;
            this.type = type;
        }

        public Builder addProperties(NPCProperty<?>... properties) {
            allowedProperties.addAll(List.of(properties));
            return this;
        }

        public Builder setEnableGlobalProperties(boolean enabled) {
            globalProperties = enabled;
            return this;
        }

        public NPCType build() {
            if (globalProperties) {
                allowedProperties.add(NPCProperty.FIRE);
                allowedProperties.add(NPCProperty.INVISIBLE);
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9))
                    allowedProperties.add(NPCProperty.GLOW);
            }
            return new NPCType(name, type, Set.copyOf(allowedProperties));
        }
    }
}
