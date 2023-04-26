package io.github.znetworkw.znpcservers.utility.itemstack;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Base64;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    private static final ItemStack DEFAULT = new ItemStack(Material.AIR);

    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(json.getAsString()));
        try (BukkitObjectInputStream bukkitObjectOutputStream = new BukkitObjectInputStream(byteArrayInputStream)) {
            return (ItemStack) bukkitObjectOutputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return DEFAULT;
        }
    }

    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream)) {
            bukkitObjectOutputStream.writeObject(src);
            return new JsonPrimitive(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            throw new JsonParseException("Cannot serialize itemstack", e);
        }
    }
}
