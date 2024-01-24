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
    private final Map<EntityPropertyImpl<?>, Object> defaultProperties;
    private final String name;
    private final double hologramOffset;

    private NpcTypeImpl(String name, EntityType type, double hologramOffset, Set<EntityPropertyImpl<?>> allowedProperties, Map<EntityPropertyImpl<?>, Object> defaultProperties) {
        this.name = name.toLowerCase();
        this.type = type;
        this.hologramOffset = hologramOffset;
        this.allowedProperties = allowedProperties;
        this.defaultProperties = defaultProperties;
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

    public void applyDefaultProperties(NpcImpl npc) {
        for (Map.Entry<EntityPropertyImpl<?>, Object> entry : defaultProperties.entrySet()) {
            npc.UNSAFE_setProperty(entry.getKey(), entry.getValue());
        }
    }

    public boolean isAllowedProperty(EntityPropertyImpl<?> entityProperty) {
        return !entityProperty.isPlayerModifiable() || allowedProperties.contains(entityProperty);
    }

    protected static final class Builder {
        private final static Logger logger = Logger.getLogger("NpcTypeBuilder");

        private final EntityPropertyRegistryImpl propertyRegistry;
        private final String name;
        private final EntityType type;
        private final List<EntityPropertyImpl<?>> allowedProperties = new ArrayList<>();
        private final Map<EntityPropertyImpl<?>, Object> defaultProperties = new HashMap<>();
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
                    // Only for use in development, please comment this out in production because some properties are version-dependent
                    // logger.warning("Tried to register the non-existent \"" + name + "\" property to the \"" + this.name + "\" npc type");
                    continue;
                }
                allowedProperties.add(propertyRegistry.getByName(name));
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> Builder addDefaultProperty(String name, T value) {
            EntityPropertyImpl<T> property = (EntityPropertyImpl<T>) propertyRegistry.getByName(name);
            if (property == null) {
                // Only for use in development, please comment this out in production because some properties are version-dependent
                // logger.warning("Tried to register the non-existent \"" + name + "\" default property to the \"" + this.name + "\" npc type");
                return this;
            }
            defaultProperties.put(property, value);
            return this;
        }

        public Builder setHologramOffset(double hologramOffset) {
            this.hologramOffset = hologramOffset;
            return this;
        }

        public NpcTypeImpl build() {
            ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
            addProperties("fire", "invisible", "silent", "look", "look_distance", "view_distance",
                    "potion_color", "potion_ambient", "display_name", "permission_required");
            if (!type.equals(EntityTypes.PLAYER)) addProperties("dinnerbone");
            // TODO: make this look nicer after completing the rest of the properties
            if (version.isNewerThanOrEquals(ServerVersion.V_1_9)) addProperties("glow");
            if (version.isNewerThanOrEquals(ServerVersion.V_1_14)) {
                addProperties("pose");
                if (EntityTypes.isTypeInstanceOf(type, EntityTypes.HORSE)) {
                    addProperties("chestplate");
                }
            }
            if (version.isNewerThanOrEquals(ServerVersion.V_1_17)) addProperties("shaking");
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_AGEABLE) || EntityTypes.isTypeInstanceOf(type, EntityTypes.ZOMBIE) || EntityTypes.isTypeInstanceOf(type, EntityTypes.ZOGLIN)) {
                addProperties("baby");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_HORSE)) {
                addProperties("is_saddled", "is_eating", "is_rearing", "has_mouth_open");
            }
            if (type.equals(EntityTypes.HORSE) && version.isOlderThan(ServerVersion.V_1_14)) {
                addProperties("horse_armor");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.CHESTED_HORSE)) {
                addProperties("has_chest");
            } else if (version.isOlderThan(ServerVersion.V_1_11) && type.equals(EntityTypes.HORSE)) {
                addProperties("has_chest");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_EVO_ILLU_ILLAGER)) {
                addProperties("spell");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_PIGLIN)) {
                addProperties("piglin_immune_to_zombification");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.SLIME) || EntityTypes.isTypeInstanceOf(type, EntityTypes.PHANTOM)) {
                addProperties("size");
            }
            if (version.isOlderThan(ServerVersion.V_1_14)) {
                if (EntityTypes.isTypeInstanceOf(type, EntityTypes.OCELOT)) {
                    addProperties("ocelot_type");
                }
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.PANDA)) {
                if (version.isNewerThanOrEquals(ServerVersion.V_1_15)) {
                    addProperties("panda_rolling", "panda_sitting", "panda_on_back", "hand");
                } else {
                    addProperties("panda_eating");
                }
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.ABSTRACT_TAMEABLE_ANIMAL)) {
                addProperties("tamed", "sitting");
            }
            if (EntityTypes.isTypeInstanceOf(type, EntityTypes.GUARDIAN)) {
                addProperties("is_retracting_spikes");
            }
            return new NpcTypeImpl(name, type, hologramOffset, new HashSet<>(allowedProperties), defaultProperties);
        }
    }
}
