package lol.pyr.znpcsplus.api.skin;

import java.net.MalformedURLException;
import java.net.URL;

public interface SkinDescriptorFactory {
    SkinDescriptor createMirrorDescriptor();
    SkinDescriptor createRefreshingDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String texture, String signature);
    SkinDescriptor createUrlDescriptor(String url) throws MalformedURLException;
    SkinDescriptor createUrlDescriptor(URL url);
}
