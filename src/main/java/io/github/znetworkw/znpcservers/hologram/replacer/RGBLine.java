package io.github.znetworkw.znpcservers.hologram.replacer;

import io.github.znetworkw.znpcservers.configuration.ConfigurationConstants;
import io.github.znetworkw.znpcservers.utility.Utils;
import net.md_5.bungee.api.ChatColor;

import java.util.concurrent.ThreadLocalRandom;

public class RGBLine implements LineReplacer {
    public String make(String string) {
        String rgbString = string;
        for (int i = 0; i < rgbString.length(); i++) {
            char charAt = rgbString.charAt(i);
            if (charAt == '#') {
                int endIndex = i + 6 + 1;
                boolean success = true;
                StringBuilder hexCodeStringBuilder = new StringBuilder();
                for (int i2 = i; i2 < endIndex; i2++) {
                    if (rgbString.length() - 1 < i2) {
                        success = false;
                        break;
                    }
                    char hexCode = rgbString.charAt(i2);
                    hexCodeStringBuilder.append((ConfigurationConstants.RGB_ANIMATION && hexCode != '#') ? Integer.toHexString(ThreadLocalRandom.current().nextInt(16)) : Character.valueOf(hexCode));
                }
                if (success) try {
                    rgbString = rgbString.substring(0, i) + ChatColor.of(hexCodeStringBuilder.toString()) + rgbString.substring(endIndex);
                } catch (Exception ignored) {}
            }
        }
        return rgbString;
    }

    public boolean isSupported() {
        return (Utils.BUKKIT_VERSION > 15);
    }
}
