package lol.pyr.znpcsplus.entity.properties.player;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import lol.pyr.znpcsplus.entity.EntityPropertyImpl;
import lol.pyr.znpcsplus.entity.PacketEntity;
import lol.pyr.znpcsplus.packets.PacketFactory;
import lol.pyr.znpcsplus.util.PapiUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

import java.util.Map;

public class TabListDisplayNameProperty extends EntityPropertyImpl<Component> {
    private final PacketFactory packetFactory;
    private final LegacyComponentSerializer textSerializer;

    public TabListDisplayNameProperty(PacketFactory packetFactory, LegacyComponentSerializer textSerializer) {
        super("tab_list_display_name", null, Component.class);
        this.packetFactory = packetFactory;
        this.textSerializer = textSerializer;
    }

    @Override
    public void apply(Player player, PacketEntity entity, boolean isSpawned, Map<Integer, EntityData<?>> properties) {
        if (!isSpawned) return;
        Component displayName = entity.getProperty(this);
        if (displayName == null) {
            packetFactory.removeTabPlayer(player, entity);
        } else {
            displayName = PapiUtil.set(textSerializer, player, displayName);
            packetFactory.updateDisplayName(player, entity, displayName);
        }
    }
}
