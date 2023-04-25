package lol.pyr.znpcsplus.skin.cache;

public class CachedId {
    private final long timestamp = System.currentTimeMillis();
    private final String id;

    public CachedId(String id) {
        this.id = id;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > 60000L;
    }

    public String getId() {
        return id;
    }
}
