package lol.pyr.znpcsplus.metadata;

import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import lol.pyr.znpcsplus.util.*;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;

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
 * 1.18-1.19 <a href="https://wiki.vg/index.php?title=Entity_metadata&oldid=18191">...</a>
 * 1.20 <a href="https://wiki.vg/index.php?title=Entity_metadata">...</a>
 */
public interface MetadataFactory {
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

    // Player
    EntityData skinLayers(boolean cape, boolean jacket, boolean leftSleeve, boolean rightSleeve, boolean leftLeg, boolean rightLeg, boolean hat);
    EntityData shoulderEntityLeft(ParrotVariant variant);
    EntityData shoulderEntityRight(ParrotVariant variant);

    // Armor Stand
    EntityData armorStandProperties(boolean small, boolean arms, boolean noBasePlate);
    EntityData armorStandHeadRotation(Vector3f headRotation);
    EntityData armorStandBodyRotation(Vector3f bodyRotation);
    EntityData armorStandLeftArmRotation(Vector3f leftArmRotation);
    EntityData armorStandRightArmRotation(Vector3f rightArmRotation);
    EntityData armorStandLeftLegRotation(Vector3f leftLegRotation);
    EntityData armorStandRightLegRotation(Vector3f rightLegRotation);

    // Axolotl
    EntityData axolotlVariant(int variant);
    EntityData playingDead(boolean playingDead);

    // Bat
    EntityData batHanging(boolean hanging);

    // Bee
    EntityData beeAngry(boolean angry);
    EntityData beeHasNectar(boolean hasNectar);

    // Blaze
    EntityData blazeOnFire(boolean onFire);

    // Cat
    EntityData catVariant(CatVariant variant);
    EntityData catLying(boolean lying);
    EntityData catTamed(boolean tamed);
    EntityData catCollarColor(DyeColor collarColor);

    // Creeper
    EntityData creeperState(CreeperState state);
    EntityData creeperCharged(boolean charged);

    // Enderman
    EntityData endermanHeldBlock(int heldBlock);
    EntityData endermanScreaming(boolean screaming);
    EntityData endermanStaring(boolean staring);

    // Evoker
    EntityData evokerSpell(int spell);

    // Fox
    EntityData foxVariant(int variant);
    EntityData foxProperties(boolean sitting, boolean crouching, boolean sleeping, boolean facePlanted);

    // Frog
    EntityData frogVariant(int variant);

    // Ghast
    EntityData ghastAttacking(boolean attacking);

    // Goat
    EntityData goatHasLeftHorn(boolean hasLeftHorn);
    EntityData goatHasRightHorn(boolean hasRightHorn);

    // Guardian

    // Hoglin
    EntityData hoglinImmuneToZombification(boolean immuneToZombification);

    // Villager
    EntityData villagerData(int type, int profession, int level);
}
