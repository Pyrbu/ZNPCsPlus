package io.github.znetworkw.znpcservers.skin;

public class SkinFetcherBuilder {
    private final SkinServer apiServer;

    private final String name;

    protected SkinFetcherBuilder(SkinServer apiServer, String name) {
        this.apiServer = apiServer;
        this.name = name;
    }

    public static SkinFetcherBuilder create(SkinServer skinAPIURL, String name) {
        return new SkinFetcherBuilder(skinAPIURL, name);
    }

    public static SkinFetcherBuilder withName(String name) {
        return create(name.startsWith("http") ? SkinServer.GENERATE_API : SkinServer.PROFILE_API, name);
    }

    public SkinServer getAPIServer() {
        return this.apiServer;
    }

    public String getData() {
        return this.name;
    }

    public boolean isUrlType() {
        return (this.apiServer == SkinServer.GENERATE_API);
    }

    public boolean isProfileType() {
        return (this.apiServer == SkinServer.PROFILE_API);
    }

    public SkinFetcher toSkinFetcher() {
        return new SkinFetcher(this);
    }

    public enum SkinServer {
        PROFILE_API("GET", "https://api.ashcon.app/mojang/v2/user", "textures", "raw"),
        GENERATE_API("POST", "https://api.mineskin.org/generate/url", "data", "texture");

        private final String method;

        private final String url;

        private final String valueKey;

        private final String signatureKey;

        SkinServer(String method, String url, String valueKey, String signatureKey) {
            this.method = method;
            this.url = url;
            this.valueKey = valueKey;
            this.signatureKey = signatureKey;
        }

        public String getMethod() {
            return this.method;
        }

        public String getURL() {
            return this.url;
        }

        public String getValueKey() {
            return this.valueKey;
        }

        public String getSignatureKey() {
            return this.signatureKey;
        }
    }
}
