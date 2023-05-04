package lol.pyr.znpcsplus.api.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.entity.EntityProperty;

import java.util.*;

public class NpcType {
    private final static Map<String, NpcType> BY_NAME = new HashMap<>();

    public static Collection<NpcType> values() {
        return BY_NAME.values();
    }

    public static NpcType byName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    private final EntityType type;
    private final Set<EntityProperty<?>> allowedProperties;
    private final String name;
    private final double hologramOffset;

    private NpcType(String name, EntityType type, double hologramOffset, Set<EntityProperty<?>> allowedProperties) {
        this.name = name.toUpperCase();
        this.type = type;
        this.hologramOffset = hologramOffset;
        this.allowedProperties = allowedProperties;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public double getHologramOffset() {
        return hologramOffset;
    }

    public Set<EntityProperty<?>> getAllowedProperties() {
        return allowedProperties;
    }

    private static NpcType define(Builder builder) {
        return define(builder.build());
    }

    private static NpcType define(NpcType type) {
        BY_NAME.put(type.getName(), type);
        return type;
    }

    public static final NpcType PLAYER = define(
            new Builder("player", EntityTypes.PLAYER)
                    .addProperties(EntityProperty.SKIN, EntityProperty.SKIN_LAYERS)
                    .setHologramOffset(-0.45D));

    public static final NpcType CREEPER = define(
            new Builder("creeper", EntityTypes.CREEPER)
                    .setHologramOffset(-0.6D));

    public static final NpcType ZOMBIE = define(
            new Builder("zombie", EntityTypes.ZOMBIE)
                    .setHologramOffset(-0.3D));

    public static final NpcType SKELETON = define(
            new Builder("skeleton", EntityTypes.SKELETON)
                    .setHologramOffset(-0.3D));

    private static final class Builder {
        private final String name;
        private final EntityType type;
        private final List<EntityProperty<?>> allowedProperties = new ArrayList<>();
        private boolean globalProperties = true;
        private double hologramOffset = 0;

        private Builder(String name, EntityType type) {
            this.name = name;
            this.type = type;
        }

        public Builder addProperties(EntityProperty<?>... properties) {
            allowedProperties.addAll(Arrays.asList(properties));
            return this;
        }

        public Builder setEnableGlobalProperties(boolean enabled) {
            globalProperties = enabled;
            return this;
        }

        public Builder setHologramOffset(double hologramOffset) {
            this.hologramOffset = hologramOffset;
            return this;
        }

        public NpcType build() {
            if (globalProperties) {
                allowedProperties.add(EntityProperty.FIRE);
                allowedProperties.add(EntityProperty.INVISIBLE);
                allowedProperties.add(EntityProperty.SILENT);
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9))
                    allowedProperties.add(EntityProperty.GLOW);
            }
            return new NpcType(name, type, hologramOffset, new HashSet<>(allowedProperties));
        }
    }
}
