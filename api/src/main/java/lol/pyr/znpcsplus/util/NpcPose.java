package lol.pyr.znpcsplus.util;

// TODO: Seperate this out into multiple classes and multiple properties depending on the npc type
// TODO: For example USING_TONGUE is only for the frog type but its usable everywhere

// TODO #2: Add some backwards compatibility to some of these, like for example CROUCHING can be done
// TODO #2: on older versions using the general Entity number 0 bitmask
public enum NpcPose {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    CROUCHING,
    LONG_JUMPING,
    DYING,
    CROAKING,
    USING_TONGUE,
    SITTING,
    ROARING,
    SNIFFING,
    EMERGING,
    DIGGING
}
