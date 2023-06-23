package lol.pyr.znpcsplus.api.skin;

public interface SkinDescriptorFactory {
    SkinDescriptor createMirrorDescriptor();
    SkinDescriptor createRefreshingDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String playerName);
    SkinDescriptor createStaticDescriptor(String texture, String signature);
}
