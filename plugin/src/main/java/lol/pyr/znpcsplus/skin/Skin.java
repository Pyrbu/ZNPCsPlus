package lol.pyr.znpcsplus.skin;

import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lol.pyr.znpcsplus.reflection.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Skin {
    private final long timestamp = System.currentTimeMillis();
    private final List<TextureProperty> properties;

    public Skin(String texture, String signature) {
        properties = new ArrayList<>(1);
        properties.add(new TextureProperty("textures", texture, signature));
    }

    public Skin(Collection<TextureProperty> properties) {
        this.properties = new ArrayList<>(properties);
    }

    public Skin(Object propertyMap) {
        this.properties = new ArrayList<>();
        try {
            Collection<?> properties = (Collection<?>) Reflections.PROPERTY_MAP_VALUES_METHOD.get().invoke(propertyMap);
            for (Object property : properties) {
                String name = (String) Reflections.PROPERTY_GET_NAME_METHOD.get().invoke(property);
                String value = (String) Reflections.PROPERTY_GET_VALUE_METHOD.get().invoke(property);
                String signature = (String) Reflections.PROPERTY_GET_SIGNATURE_METHOD.get().invoke(property);
                this.properties.add(new TextureProperty(name, value, signature));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Skin(JsonObject obj) {
        properties = new ArrayList<>();
        for (JsonElement e : obj.get("properties").getAsJsonArray()) {
            JsonObject o = e.getAsJsonObject();
            properties.add(new TextureProperty(o.get("name").getAsString(), o.get("value").getAsString(), o.has("signature") ? o.get("signature").getAsString() : null));
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
