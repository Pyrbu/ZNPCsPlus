package lol.pyr.znpcsplus.skin;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.PropertyMap;
import lol.pyr.znpcsplus.util.list.ListUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Skin {
    private final long timestamp = System.currentTimeMillis();
    private final List<TextureProperty> properties = new ArrayList<>();

    public Skin(String texture, String signature) {
        properties.add(new TextureProperty("textures", texture, signature));
    }

    public Skin(TextureProperty... properties) {
        this.properties.addAll(ListUtil.immutableList(properties));
    }

    public Skin(Collection<TextureProperty> properties) {
        this.properties.addAll(properties);
    }

    public Skin(PropertyMap properties) {
        this.properties.addAll(properties.values().stream()
                .map(property -> new TextureProperty(property.getName(), property.getValue(), property.getSignature()))
                .collect(Collectors.toList()));
    }

    public Skin(JsonObject obj) {
        for (JsonElement e : obj.get("properties").getAsJsonArray()) {
            JsonObject o = e.getAsJsonObject();
            properties.add(new TextureProperty(o.get("getName").getAsString(), o.get("value").getAsString(), o.has("signature") ? o.get("signature").getAsString() : null));
        }
    }

    public UserProfile apply(UserProfile profile) {
        profile.setTextureProperties(properties);
        return profile;
    }

    public List<TextureProperty> getProperties() {
        return properties;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - timestamp > 60000L;
    }
}
