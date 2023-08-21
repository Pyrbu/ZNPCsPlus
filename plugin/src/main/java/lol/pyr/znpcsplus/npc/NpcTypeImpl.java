package lol.pyr.znpcsplus.npc;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import lol.pyr.znpcsplus.api.entity.EntityProperty;
import lol.pyr.znpcsplus.api.npc.NpcType;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.EntityPropertyRegistryImpl;

import java.util.*;
import java.util.logging.Logger;
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
        private final static Logger logger = Logger.getLogger("NpcTypeBuilder");

        private final EntityPropertyRegistryImpl propertyRegistry;
        private final String name;
        private final EntityType type;
        private final List<EntityPropertyImpl<?>> allowedProperties = new ArrayList<>();
        private double hologramOffset = 0;

        Builder(EntityPropertyRegistryImpl propertyRegistry, String name, EntityType type) {
            this.propertyRegistry = propertyRegistry;
            this.name = name;
            this.type = type;
        }

        public Builder addEquipmentProperties() {
            addProperties("helmet", "chestplate", "leggings", "boots");
            return addHandProperties();
        }

        public Builder addHandProperties() {
            if (PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                return addProperties("hand", "offhand");
            } else {
                return addProperties("hand");
            }
        }

        public Builder addProperties(String... names) {
            for (String name : names) {
                if (propertyRegistry.getByName(name) == null) {
                    logger.warning("Tried to register the non-existent \"" + name + "\" property to the \"" + this.name + "\" npc type");
                    continue;
                }
                allowedProperties.add(propertyRegistry.getByName(name));
            }
            return this;
        }

        public Builder setHologramOffset(double hologramOffset) {
            this.hologramOffset = hologramOffset;
            return this;
        }

        public NpcTypeImpl build() {
            ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
            addProperties("fire", "invisible", "silent", "look",
                    "using_item", "potion_color", "potion_ambient", "dinnerbone");
            if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) addProperties("glow");
            if (version.isNewerThanOrEquals(ServerVersion.V_1_14)) addProperties("pose");
            if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) addProperties("shaking");
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_AGEABLE)) {
                addProperties("baby");
            }
            return new NpcTypeImpl(name, type, hologramOffset, new HashSet<>(allowedProperties));
        }
    }
}
