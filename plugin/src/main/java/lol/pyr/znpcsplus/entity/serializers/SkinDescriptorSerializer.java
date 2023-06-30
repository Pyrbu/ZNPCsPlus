package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.skin.BaseSkinDescriptor;
import lol.pyr.znpcsplus.skin.cache.MojangSkinCache;

public class SkinDescriptorSerializer implements PropertySerializer<SkinDescriptor> {
    private final MojangSkinCache skinCache;

    public SkinDescriptorSerializer(MojangSkinCache skinCache) {
        this.skinCache = skinCache;
    }

    @Override
    public String serialize(SkinDescriptor property) {
        return ((BaseSkinDescriptor) property).serialize();
    }

    @Override
    public SkinDescriptor deserialize(String property) {
        return BaseSkinDescriptor.deserialize(skinCache, property);
    }

    @Override
    public Class<SkinDescriptor> getTypeClass() {
        return SkinDescriptor.class;
    }
}
