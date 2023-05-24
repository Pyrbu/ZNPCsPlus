package lol.pyr.znpcsplus.packets;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.api.entity.PropertyHolder;
import lol.pyr.znpcsplus.entity.PacketEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PacketFactory {
    void spawnPlayer(Player player, PacketEntity entity, PropertyHolder properties);
    void spawnEntity(Player player, PacketEntity entity, PropertyHolder properties);
    void destroyEntity(Player player, PacketEntity entity, PropertyHolder properties);
    void teleportEntity(Player player, PacketEntity entity);
    CompletableFuture<Void> addTabPlayer(Player player, PacketEntity entity, PropertyHolder properties);
    void removeTabPlayer(Player player, PacketEntity entity);
    void createTeam(Player player, PacketEntity entity, PropertyHolder properties);
    void removeTeam(Player player, PacketEntity entity);
    Map<Integer, EntityData> generateMetadata(Player player, PacketEntity entity, PropertyHolder properties);
    void sendAllMetadata(Player player, PacketEntity entity, PropertyHolder properties);
    void sendMetadata(Player player, PacketEntity entity, List<EntityData> data);
    void sendEquipment(Player player, PacketEntity entity, PropertyHolder properties);
}
