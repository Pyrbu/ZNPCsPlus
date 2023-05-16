package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import net.kyori.adventure.text.Component;

import java.util.Collection;

/**
 * 1.8  https://wiki.vg/index.php?title=Entity_metadata&oldid=7415
 * 1.9  https://wiki.vg/index.php?title=Entity_metadata&oldid=7968
 * 1.10 https://wiki.vg/index.php?title=Entity_metadata&oldid=8241
 * 1.11 https://wiki.vg/index.php?title=Entity_metadata&oldid=8534
 * 1.12 https://wiki.vg/index.php?title=Entity_metadata&oldid=14048
 * 1.13 https://wiki.vg/index.php?title=Entity_metadata&oldid=14800
 * 1.14 https://wiki.vg/index.php?title=Entity_metadata&oldid=15240
 * 1.15 https://wiki.vg/index.php?title=Entity_metadata&oldid=15991
 * 1.16 https://wiki.vg/index.php?title=Entity_metadata&oldid=16539
 * 1.17 https://wiki.vg/index.php?title=Entity_metadata&oldid=17521
 * 1.18 NOTHING CHANGED
 * 1.19 https://wiki.vg/index.php?title=Entity_metadata
 */
public interface MetadataFactory {
    EntityData skinLayers(boolean enabled);
    EntityData effects(boolean onFire, boolean glowing, boolean invisible);
    EntityData silent(boolean enabled);
    Collection<EntityData> name(Component name);
    EntityData noGravity();
}
