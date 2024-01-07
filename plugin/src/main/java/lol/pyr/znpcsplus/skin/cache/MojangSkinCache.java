package lol.pyr.znpcsplus.skin.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lol.pyr.znpcsplus.config.ConfigManager;
import lol.pyr.znpcsplus.reflection.Reflections;
import lol.pyr.znpcsplus.skin.SkinImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MojangSkinCache {
    private final static Logger logger = Logger.getLogger("ZNPCsPlus Skin Cache");

    private final ConfigManager configManager;

    private final Map<String, SkinImpl> cache = new ConcurrentHashMap<>();
    private final Map<String, CachedId> idCache = new ConcurrentHashMap<>();

    public MojangSkinCache(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void cleanCache() {
        for (Map.Entry<String, SkinImpl> entry : cache.entrySet()) if (entry.getValue().isExpired()) cache.remove(entry.getKey());
        for (Map.Entry<String, CachedId> entry : idCache.entrySet()) if (entry.getValue().isExpired()) cache.remove(entry.getKey());
    }

    public CompletableFuture<SkinImpl> fetchByName(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null && player.isOnline()) return CompletableFuture.completedFuture(getFromPlayer(player));

        if (idCache.containsKey(name.toLowerCase())) return fetchByUUID(idCache.get(name.toLowerCase()).getId());

        return CompletableFuture.supplyAsync(() -> {
            URL url = parseUrl("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if (obj.has("errorMessage")) return fetchByNameFallback(name).join();
                    String id = obj.get("id").getAsString();
                    idCache.put(name.toLowerCase(), new CachedId(id));
                    SkinImpl skin = fetchByUUID(id).join();
                    if (skin == null) return fetchByNameFallback(name).join();
                    return skin;
                }
            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to get uuid from player name, trying to use fallback server:");
                    exception.printStackTrace();
                }
                return fetchByNameFallback(name).join();
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    public CompletableFuture<SkinImpl> fetchByNameFallback(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null && player.isOnline()) return CompletableFuture.completedFuture(getFromPlayer(player));

        if (idCache.containsKey(name.toLowerCase())) return fetchByUUID(idCache.get(name.toLowerCase()).getId());

        return CompletableFuture.supplyAsync(() -> {
            URL url = parseUrl("https://api.ashcon.app/mojang/v2/user/" + name);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if (obj.has("error")) return null;
                    String uuid = obj.get("uuid").getAsString();
                    idCache.put(name.toLowerCase(), new CachedId(uuid));
                    JsonObject textures = obj.get("textures").getAsJsonObject();
                    String value = textures.get("raw").getAsJsonObject().get("value").getAsString();
                    String signature = textures.get("raw").getAsJsonObject().get("signature").getAsString();
                    SkinImpl skin = new SkinImpl(value, signature);
                    cache.put(uuid, skin);
                    return skin;
                }
            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to fetch skin from fallback server:");
                    exception.printStackTrace();
                }
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        });
    }

    public CompletableFuture<SkinImpl> fetchByUrl(URL url, String variant) {
        return CompletableFuture.supplyAsync(() -> {
            URL apiUrl = parseUrl("https://api.mineskin.org/generate/url");
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                OutputStream outStream = connection.getOutputStream();
                DataOutputStream out = new DataOutputStream(outStream);
                out.writeBytes("{\"variant\":\"" + variant + "\",\"url\":\"" + url.toString() + "\"}");
                out.flush();
                out.close();
                outStream.close();

                try (Reader reader = new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
                    if (obj.has("error")) return null;
                    if (!obj.has("data")) return null;
                    JsonObject texture = obj.get("data").getAsJsonObject().get("texture").getAsJsonObject();
                    return new SkinImpl(texture.get("value").getAsString(), texture.get("signature").getAsString());
                }

            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to get skin from url:");
                    exception.printStackTrace();
                }
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        });
    }

    public boolean isNameFullyCached(String s) {
        String name = s.toLowerCase();
        if (!idCache.containsKey(name)) return false;
        CachedId id = idCache.get(name);
        if (id.isExpired() || !cache.containsKey(id.getId())) return false;
        SkinImpl skin = cache.get(id.getId());
        return !skin.isExpired();
    }

    public SkinImpl getFullyCachedByName(String s) {
        String name = s.toLowerCase();
        if (!idCache.containsKey(name)) return null;
        CachedId id = idCache.get(name);
        if (id.isExpired() || !cache.containsKey(id.getId())) return null;
        SkinImpl skin = cache.get(id.getId());
        if (skin.isExpired()) return null;
        return skin;
    }

    public CompletableFuture<SkinImpl> fetchByUUID(String uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) return CompletableFuture.completedFuture(getFromPlayer(player));

        if (cache.containsKey(uuid)) {
            SkinImpl skin = cache.get(uuid);
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
                    if (obj.has("errorMessage")) return null;
                    SkinImpl skin = new SkinImpl(obj);
                    cache.put(uuid, skin);
                    return skin;
                }
            } catch (IOException exception) {
                if (!configManager.getConfig().disableSkinFetcherWarnings()) {
                    logger.warning("Failed to fetch skin, trying to use fallback server:");
                    exception.printStackTrace();
                }
            } finally {
                if (connection != null) connection.disconnect();
            }
            return null;
        });
    }

    public SkinImpl getFromPlayer(Player player) {
        try {
            Object playerHandle = Reflections.GET_PLAYER_HANDLE_METHOD.get().invoke(player);
            Object gameProfile = Reflections.GET_PROFILE_METHOD.get().invoke(playerHandle);
            Object propertyMap = Reflections.GET_PROPERTY_MAP_METHOD.get().invoke(gameProfile);
            return new SkinImpl(propertyMap);
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
