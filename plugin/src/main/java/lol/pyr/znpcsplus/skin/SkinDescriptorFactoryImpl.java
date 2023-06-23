package lol.pyr.znpcsplus.skin;

import lol.pyr.znpcsplus.api.skin.SkinDescriptor;
import lol.pyr.znpcsplus.api.skin.SkinDescriptorFactory;
import lol.pyr.znpcsplus.skin.cache.SkinCache;
import lol.pyr.znpcsplus.skin.descriptor.FetchingDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.MirrorDescriptor;
import lol.pyr.znpcsplus.skin.descriptor.PrefetchedDescriptor;

public class SkinDescriptorFactoryImpl implements SkinDescriptorFactory {
    private final SkinCache skinCache;
    private final MirrorDescriptor mirrorDescriptor;

    public SkinDescriptorFactoryImpl(SkinCache skinCache) {
        this.skinCache = skinCache;
        mirrorDescriptor = new MirrorDescriptor(skinCache);
    }

    @Override
    public SkinDescriptor createMirrorDescriptor() {
        return mirrorDescriptor;
    }

    @Override
    public SkinDescriptor createRefreshingDescriptor(String playerName) {
        return new FetchingDescriptor(skinCache, playerName);
    }

    @Override
    public SkinDescriptor createStaticDescriptor(String playerName) {
        return PrefetchedDescriptor.forPlayer(skinCache, playerName).join();
    }

    @Override
    public SkinDescriptor createStaticDescriptor(String texture, String signature) {
        return new PrefetchedDescriptor(new Skin(texture, signature));
    }
}
