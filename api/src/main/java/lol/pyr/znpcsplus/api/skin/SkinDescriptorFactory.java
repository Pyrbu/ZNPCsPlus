package lol.pyr.znpcsplus.api.skin;

import java.net.URL;

/**
 * Factory for creating skin descriptors.
 */
public interface SkinDescriptorFactory {
    SkinDescriptor createMirrorDescriptor();
    SkinDescriptor createRefreshingDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String texture, String signature);
    SkinDescriptor createUrlDescriptor(String url, String variant);
    SkinDescriptor createUrlDescriptor(URL url, String variant);
}
