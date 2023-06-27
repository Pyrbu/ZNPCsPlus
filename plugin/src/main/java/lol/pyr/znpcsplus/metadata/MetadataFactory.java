package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.util.Vector3f;
import net.kyori.adventure.text.Component;

/**
 * 1.8  <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=7415">...</a>
 * 1.9  <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=7968">...</a>
 * 1.10 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=8241">...</a>
 * 1.11 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=8534">...</a>
 * 1.12 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=14048">...</a>
 * 1.13 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=14800">...</a>
 * 1.14 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=15240">...</a>
 * 1.15 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=15991">...</a>
 * 1.16 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=16539">...</a>
 * 1.17 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=17521">...</a>
 * 1.18-1.19 <a href="https://wiki.vg/index.php?title=Entity_metadata">...</a>
 */
public interface MetadataFactory {
    EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat);
    EntityData effects(boolean onFire, boolean glowing, boolean invisible, boolean usingElytra, boolean usingItemLegacy);
    EntityData silent(boolean enabled);
    EntityData name(Component name);
    EntityData nameShown();
    EntityData noGravity();
    EntityData pose(EntityPose pose);
    EntityData shaking(boolean enabled);
    EntityData usingItem(boolean enabled, boolean offhand, boolean riptide);
    EntityData potionColor(int color);
    EntityData potionAmbient(boolean ambient);
    EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate);
    EntityData armorStandHeadRotation(Vector3f headRotation);
    EntityData armorStandBodyRotation(Vector3f bodyRotation);
    EntityData armorStandLeftArmRotation(Vector3f leftArmRotation);
    EntityData armorStandRightArmRotation(Vector3f rightArmRotation);
    EntityData armorStandLeftLegRotation(Vector3f leftLegRotation);
    EntityData armorStandRightLegRotation(Vector3f rightLegRotation);
}
