package lol.pyr.znpcsplus.util;

public class FoliaUtil {
    private static final Boolean FOLIA = isFolia();
    public static boolean isFolia() {
        if (FOLIA != null) return FOLIA;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
