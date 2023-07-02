package lol.pyr.znpcsplus.entity;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import lol.pyr.znpcsplus.util.ParrotVariant;

// Not sure where to put this or even if it's needed
public class ParrotNBTCompound {
    private final NBTCompound tag = new NBTCompound();

    public ParrotNBTCompound(ParrotVariant variant) {
        tag.setTag("id", new NBTString("minecraft:parrot"));
        tag.setTag("Variant", new NBTInt(variant.ordinal()));
        // other tags if needed, idk
    }

    public NBTCompound getTag() {
        return tag;
    }
}
