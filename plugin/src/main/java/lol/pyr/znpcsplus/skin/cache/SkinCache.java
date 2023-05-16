package lol.pyr.znpcsplus.skin.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.reflection.Reflections;
import lol.pyr.znpcsplus.skin.Skin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SkinCache {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Skin Cache");

    private final ConfigManager configManager;

    private final Map<String, Skin> cache = new ConcurrentHashMap<>();
    private final Map<String, CachedId> idCache = new ConcurrentHashMap<>();

    public SkinCache(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void cleanCache() {
        for (Map.Entry<String, Skin> entry : cache.entrySet()) if (entry.getValue().isExpired()) cache.remove(entry.getKey());
        for (Map.Entry<String, CachedId> entry : idCache.entrySet()) if (entry.getValue().isExpired()) cache.remove(entry.getKey());
    }

    public CompletableFuture<Skin> fetchByName(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null && player.isOnline()) return CompletableFuture.completedFuture(getFromPlayer(player));

        if (cache.containsKey(name.toLowerCase())) return fetchByUUID(idCache.get(name.toLowerCase()).getId());

        return CompletableFuture.supplyAsync(() -> {
            URL url = parseUrl("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if (obj.has("errorMessage")) return null;
                    String id = obj.get("id").getAsString();
                    idCache.put(name.toLowerCase(), new CachedId(id));
                    return fetchByUUID(id).join();
                }
            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to uuid from player name:");
                    exception.printStackTrace();
                }
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        });
    }

    public CompletableFuture<Skin> fetchByUUID(UUID uuid) {
        return fetchByUUID(uuid.toString().replace("-", ""));
    }

    public boolean isNameFullyCached(String s) {
        String name = s.toLowerCase();
        if (!idCache.containsKey(name)) return false;
        CachedId id = idCache.get(name);
        if (id.isExpired() || !cache.containsKey(id.getId())) return false;
        Skin skin = cache.get(id.getId());
        return !skin.isExpired();
    }

    public Skin getFullyCachedByName(String s) {
        String name = s.toLowerCase();
        if (!idCache.containsKey(name)) return null;
        CachedId id = idCache.get(name);
        if (id.isExpired() || !cache.containsKey(id.getId())) return null;
        Skin skin = cache.get(id.getId());
        if (skin.isExpired()) return null;
        return skin;
    }

    public CompletableFuture<Skin> fetchByUUID(String uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) return CompletableFuture.completedFuture(getFromPlayer(player));

        if (cache.containsKey(uuid)) {
            Skin skin = cache.get(uuid);
            if (!skin.isExpired()) return CompletableFuture.completedFuture(skin);
        }

        return CompletableFuture.supplyAsync(() -> {
            URL url = parseUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    Skin skin = new Skin(obj);
                    cache.put(uuid, skin);
                    return skin;
                }
            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to fetch skin:");
                    exception.printStackTrace();
                }
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        });
    }

    public Skin getFromPlayer(Player player) {
        try {
            Object playerHandle = Reflections.GET_HANDLE_PLAYER_METHOD.get().invoke(player);
            GameProfile gameProfile = (GameProfile) Reflections.GET_PROFILE_METHOD.get().invoke(playerHandle, new Object[0]);
            return new Skin(gameProfile.getProperties());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
