package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class NpcTypeImpl implements NpcType {
    private final EntityType type;
    private final Set<EntityPropertyImpl<?>> allowedProperties;
    private final String name;
    private final double hologramOffset;

    private NpcTypeImpl(String name, EntityType type, double hologramOffset, Set<EntityPropertyImpl<?>> allowedProperties) {
        this.name = name.toLowerCase();
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
        return allowedProperties.stream().map(property -> (EntityProperty<?>) property).collect(Collectors.toSet());
    }

    protected static final class Builder {
        private final EntityPropertyRegistryImpl propertyRegistry;
        private final String name;
        private final EntityType type;
        private final List<EntityPropertyImpl<?>> allowedProperties = new ArrayList<>();
        private boolean globalProperties = true;
        private double hologramOffset = 0;

        Builder(EntityPropertyRegistryImpl propertyRegistry, String name, EntityType type) {
            this.propertyRegistry = propertyRegistry;
            this.name = name;
            this.type = type;
        }

        public Builder addProperties(EntityPropertyImpl<?>... properties) {
            allowedProperties.addAll(Arrays.asList(properties));
            return this;
        }

        public Builder addProperties(String... names) {
            for (String name : names) allowedProperties.add(propertyRegistry.getByName(name));
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
                allowedProperties.add(propertyRegistry.getByName("fire"));
                allowedProperties.add(propertyRegistry.getByName("invisible"));
                allowedProperties.add(propertyRegistry.getByName("silent"));
                allowedProperties.add(propertyRegistry.getByName("look"));
                if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9))
                    allowedProperties.add(propertyRegistry.getByName("glow"));
            }
            return new NpcTypeImpl(name, type, hologramOffset, new HashSet<>(allowedProperties));
        }
    }
}
