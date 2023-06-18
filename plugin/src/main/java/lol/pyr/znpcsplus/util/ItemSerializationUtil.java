package lol.pyr.znpcsplus.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemSerializationUtil {
    public static byte[] objectToBytes(Object obj) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            new BukkitObjectOutputStream(bout).writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bout.toByteArray();
    }

    @SuppressWarnings({"unchecked", "unused"})
    public static <T> T objectFromBytes(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        try {
            return (T) new BukkitObjectInputStream(bin).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String itemToB64(ItemStack item) {
        if (item == null) return null;
        return Base64.getEncoder().encodeToString(objectToBytes(item));
    }

    public static ItemStack itemFromB64(String str) {
        if (str == null) return null;
        return objectFromBytes(Base64.getDecoder().decode(str), ItemStack.class);
    }

    public static ItemMeta metaFromB64(String str) {
        return objectFromBytes(Base64.getDecoder().decode(str), ItemMeta.class);
    }
}
