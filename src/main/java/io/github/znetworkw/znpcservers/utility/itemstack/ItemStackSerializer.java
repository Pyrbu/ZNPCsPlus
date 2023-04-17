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
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(json.getAsString()));
            try {
                BukkitObjectInputStream bukkitObjectOutputStream = new BukkitObjectInputStream(byteArrayInputStream);
                try {
                    ItemStack itemStack = (ItemStack) bukkitObjectOutputStream.readObject();
                    bukkitObjectOutputStream.close();
                    byteArrayInputStream.close();
                    return itemStack;
                } catch (Throwable throwable) {
                    try {
                        bukkitObjectOutputStream.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (Throwable throwable) {
                try {
                    byteArrayInputStream.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (IOException | ClassNotFoundException e) {
            return DEFAULT;
        }
    }

    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);
                try {
                    bukkitObjectOutputStream.writeObject(src);
                    JsonPrimitive jsonPrimitive = new JsonPrimitive(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
                    bukkitObjectOutputStream.close();
                    byteArrayOutputStream.close();
                    return jsonPrimitive;
                } catch (Throwable throwable) {
                    try {
                        bukkitObjectOutputStream.close();
                    } catch (Throwable throwable1) {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            } catch (Throwable throwable) {
                try {
                    byteArrayOutputStream.close();
                } catch (Throwable throwable1) {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        } catch (IOException e) {
            throw new JsonParseException("Cannot serialize itemstack", e);
        }
    }
}
