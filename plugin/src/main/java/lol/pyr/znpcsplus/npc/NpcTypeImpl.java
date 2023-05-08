package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;

import java.util.*;

public class NpcTypeImpl {
    private final static Map<String, NpcTypeImpl> BY_NAME = new HashMap<>();

    public static Collection<NpcTypeImpl> values() {
        return BY_NAME.values();
    }

    public static NpcTypeImpl byName(String name) {
        return BY_NAME.get(name.toUpperCase());
    }

    private final EntityType type;
    private final Set<EntityPropertyImpl<?>> allowedProperties;
    private final String name;
    private final double hologramOffset;

    private NpcTypeImpl(String name, EntityType type, double hologramOffset, Set<EntityPropertyImpl<?>> allowedProperties) {
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

    public Set<EntityPropertyImpl<?>> getAllowedProperties() {
        return allowedProperties;
    }

    private static NpcTypeImpl define(Builder builder) {
        return define(builder.build());
    }

    private static NpcTypeImpl define(NpcTypeImpl type) {
        BY_NAME.put(type.getName(), type);
        return type;
    }

    public static final NpcTypeImpl PLAYER = define(
            new Builder("player", EntityTypes.PLAYER)
                    .addProperties(EntityPropertyImpl.SKIN, EntityPropertyImpl.SKIN_LAYERS)
                    .setHologramOffset(-0.45D));

    public static final NpcTypeImpl CREEPER = define(
            new Builder("creeper", EntityTypes.CREEPER)
                    .setHologramOffset(-0.6D));

    public static final NpcTypeImpl ZOMBIE = define(
            new Builder("zombie", EntityTypes.ZOMBIE)
                    .setHologramOffset(-0.3D));

    public static final NpcTypeImpl SKELETON = define(
            new Builder("skeleton", EntityTypes.SKELETON)
                    .setHologramOffset(-0.3D));

    private static final class Builder {
        private final String name;
        private final EntityType type;
        private final List<EntityPropertyImpl<?>> allowedProperties = new ArrayList<>();
        private boolean globalProperties = true;
        private double hologramOffset = 0;

        private Builder(String name, EntityType type) {
            this.name = name;
            this.type = type;
        }

        public Builder addProperties(EntityPropertyImpl<?>... properties) {
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

        public NpcTypeImpl build() {
            if (globalProperties) {
                allowedProperties.add(EntityPropertyImpl.FIRE);
                allowedProperties.add(EntityPropertyImpl.INVISIBLE);
                allowedProperties.add(EntityPropertyImpl.SILENT);
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9))
                    allowedProperties.add(EntityPropertyImpl.GLOW);
            }
            return new NpcTypeImpl(name, type, hologramOffset, new HashSet<>(allowedProperties));
        }
    }
}
