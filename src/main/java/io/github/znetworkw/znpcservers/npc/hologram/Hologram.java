package io.github.znetworkw.znpcservers.npc.hologram;

import io.github.znetworkw.znpcservers.UnexpectedCallException;
import io.github.znetworkw.znpcservers.reflection.Reflections;
import io.github.znetworkw.znpcservers.configuration.Configuration;
import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.configuration.ConfigurationValue;
import io.github.znetworkw.znpcservers.npc.NPC;
import io.github.znetworkw.znpcservers.npc.hologram.replacer.LineReplacer;
import io.github.znetworkw.znpcservers.user.ZUser;
import io.github.znetworkw.znpcservers.utility.Utils;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Hologram {
    private static final boolean NEW_METHOD = (Utils.BUKKIT_VERSION > 12);
    private static final double LINE_SPACING = Configuration.CONFIGURATION.getValue(ConfigurationValue.LINE_SPACING);
    private final List<HologramLine> hologramLines = new ArrayList<>();
    private final NPC npc;

    public Hologram(NPC npc) {
        this.npc = npc;
    }

    public void createHologram() {
        this.npc.getViewers().forEach(this::delete);
        try {
            this.hologramLines.clear();
            double y = 0.0D;
            Location location = this.npc.getLocation();
            for (String line : this.npc.getNpcPojo().getHologramLines()) {
                boolean visible = !line.equalsIgnoreCase("%space%");
                Object armorStand = Reflections.ENTITY_CONSTRUCTOR.get().newInstance(Reflections.GET_HANDLE_WORLD_METHOD.get().invoke(location.getWorld()),
                        location.getX(), location.getY() - 0.15D + y, location.getZ());
                if (visible) {
                    Reflections.SET_CUSTOM_NAME_VISIBLE_METHOD.get().invoke(armorStand, true);
                    updateLine(line, armorStand, null);
                }
                Reflections.SET_INVISIBLE_METHOD.get().invoke(armorStand, true);
                this.hologramLines.add(new HologramLine(line.replace(ConfigurationConstants.SPACE_SYMBOL, " "), armorStand, (Integer) Reflections.GET_ENTITY_ID
                        .get().invoke(armorStand)));
                y += LINE_SPACING;
            }
            setLocation(location, 0.0D);
            this.npc.getPackets().flushCache("getHologramSpawnPacket");
            this.npc.getViewers().forEach(this::spawn);
        } catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    public void spawn(ZUser user) {
        this.hologramLines.forEach(hologramLine -> {
            try {
                Object entityPlayerPacketSpawn = this.npc.getPackets().getProxyInstance().getHologramSpawnPacket(hologramLine.armorStand);
                Utils.sendPackets(user, entityPlayerPacketSpawn);
            } catch (ReflectiveOperationException operationException) {
                delete(user);
            }
        });
    }

    public void delete(ZUser user) {
        this.hologramLines.forEach(hologramLine -> {
            try {
                Utils.sendPackets(user, this.npc.getPackets().getProxyInstance().getDestroyPacket(hologramLine.id));
            } catch (ReflectiveOperationException operationException) {
                throw new UnexpectedCallException(operationException);
            }
        });
    }

    public void updateNames(ZUser user) {
        for (HologramLine hologramLine : this.hologramLines) {
            try {
                updateLine(hologramLine.line, hologramLine.armorStand, user);
                Object metaData = this.npc.getPackets().getProxyInstance().getMetadataPacket(hologramLine.id, hologramLine.armorStand);
                Utils.sendPackets(user, metaData);
            } catch (ReflectiveOperationException operationException) {
                throw new UnexpectedCallException(operationException);
            }
        }
    }

    public void updateLocation() {
        this.hologramLines.forEach(hologramLine -> {
            try {
                Object packet = Reflections.PACKET_PLAY_OUT_ENTITY_TELEPORT_CONSTRUCTOR.get().newInstance(hologramLine.armorStand);
                this.npc.getViewers().forEach(player -> Utils.sendPackets(player, packet));
            }
            catch (ReflectiveOperationException operationException) {
                throw new UnexpectedCallException(operationException);
            }
        });
    }

    public void setLocation(Location location, double height) {
        location = location.clone().add(0.0, height, 0.0);
        try {
            double y = this.npc.getNpcPojo().getHologramHeight();
            for (HologramLine hologramLine : this.hologramLines) {
                Reflections.SET_LOCATION_METHOD.get().invoke(hologramLine.armorStand, location.getX(), location.getY() - 0.15 + y, location.getZ(), location.getYaw(), location.getPitch());
                y += LINE_SPACING;
            }
            this.updateLocation();
        }
        catch (ReflectiveOperationException operationException) {
            throw new UnexpectedCallException(operationException);
        }
    }

    private void updateLine(String line, Object armorStand, @Nullable ZUser user) throws InvocationTargetException, IllegalAccessException {
        if (NEW_METHOD) {
            Reflections.SET_CUSTOM_NAME_NEW_METHOD.get().invoke(armorStand, Reflections.CRAFT_CHAT_MESSAGE_METHOD.get().invoke(null, LineReplacer.makeAll(user, line)));
        } else {
            Reflections.SET_CUSTOM_NAME_OLD_METHOD.get().invoke(armorStand, LineReplacer.makeAll(user, line));
        }
    }

    private static class HologramLine {
        private final String line;

        private final Object armorStand;

        private final int id;

        protected HologramLine(String line, Object armorStand, int id) {
            this.line = line;
            this.armorStand = armorStand;
            this.id = id;
        }
    }
}
