package lol.pyr.znpcsplus.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GlowColor {
    BLACK("black", NamedTextColor.BLACK.value()),
    DARK_BLUE("dark_blue", NamedTextColor.DARK_BLUE.value()),
    DARK_GREEN("dark_green",NamedTextColor.DARK_GREEN.value()),
    DARK_AQUA("dark_aqua", NamedTextColor.AQUA.value()),
    DARK_RED("dark_red", NamedTextColor.RED.value()),
    DARK_PURPLE("dark_purple", NamedTextColor.DARK_PURPLE.value()),
    GOLD("gold", NamedTextColor.GOLD.value()),
    GRAY("gray", NamedTextColor.GRAY.value()),
    DARK_GRAY("dark_gray", NamedTextColor.DARK_GRAY.value()),
    BLUE("blue", NamedTextColor.BLUE.value()),
    GREEN("green", NamedTextColor.GREEN.value()),
    AQUA("aqua", NamedTextColor.AQUA.value()),
    RED("red", NamedTextColor.RED.value()),
    LIGHT_PURPLE("light_purple", NamedTextColor.LIGHT_PURPLE.value()),
    YELLOW("yellow", NamedTextColor.YELLOW.value()),
    WHITE("white", NamedTextColor.WHITE.value());

    private static final List<GlowColor> VALUES = Collections.unmodifiableList(Arrays.asList(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE));
    /**
     * An index of name to color.
     *
     * @since 4.0.0
     */
    public static final Index<String, GlowColor> NAMES = Index.create(constant -> constant.name, VALUES);

    private final String name;
    private final int value;

    private GlowColor(final String name, final int value) {
        this.name = name;
        this.value = value;
    }

    public NamedTextColor toNamedTextColor() {
        return NamedTextColor.namedColor(this.value);
    }

    public int value() {
        return this.value;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }
}
