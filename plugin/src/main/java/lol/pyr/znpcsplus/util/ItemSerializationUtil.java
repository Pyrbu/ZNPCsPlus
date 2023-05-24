package lol.pyr.znpcsplus.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemSerializationUtil {
    public static byte[] itemToBytes(ItemStack item) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            new BukkitObjectOutputStream(bout).writeObject(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bout.toByteArray();
    }

    public static ItemStack itemFromBytes(byte[] bytes) {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        try {
            return (ItemStack) new BukkitObjectInputStream(bin).readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static String itemToB64(ItemStack item) {
        if (item == null) return null;
        return Base64.getEncoder().encodeToString(itemToBytes(item));
    }

    public static ItemStack itemFromB64(String str) {
        if (str == null) return null;
        return itemFromBytes(Base64.getDecoder().decode(str));
    }
}
