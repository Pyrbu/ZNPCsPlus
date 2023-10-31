package lol.pyr.znpcsplus.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum GlowColor {
    BLACK("black", 0x000000),
    DARK_BLUE("dark_blue", 0x0000aa),
    DARK_GREEN("dark_green",0x00aa00),
    DARK_AQUA("dark_aqua", 0x00aaaa),
    DARK_RED("dark_red", 0xaa0000),
    DARK_PURPLE("dark_purple", 0xaa00aa),
    GOLD("gold", 0xffaa00),
    GRAY("gray", 0xaaaaaa),
    DARK_GRAY("dark_gray", 0x555555),
    BLUE("blue", 0x5555ff),
    GREEN("green", 0x55ff55),
    AQUA("aqua", 0x55ffff),
    RED("red", 0xff5555),
    LIGHT_PURPLE("light_purple", 0xff55ff),
    YELLOW("yellow", 0xffff55),
    WHITE("white", 0xffffff);

    private static final int BLACK_VALUE = 0x000000;
    private static final int DARK_BLUE_VALUE = 0x0000aa;
    private static final int DARK_GREEN_VALUE = 0x00aa00;
    private static final int DARK_AQUA_VALUE = 0x00aaaa;
    private static final int DARK_RED_VALUE = 0xaa0000;
    private static final int DARK_PURPLE_VALUE = 0xaa00aa;
    private static final int GOLD_VALUE = 0xffaa00;
    private static final int GRAY_VALUE = 0xaaaaaa;
    private static final int DARK_GRAY_VALUE = 0x555555;
    private static final int BLUE_VALUE = 0x5555ff;
    private static final int GREEN_VALUE = 0x55ff55;
    private static final int AQUA_VALUE = 0x55ffff;
    private static final int RED_VALUE = 0xff5555;
    private static final int LIGHT_PURPLE_VALUE = 0xff55ff;
    private static final int YELLOW_VALUE = 0xffff55;
    private static final int WHITE_VALUE = 0xffffff;

    private static final List<GlowColor> VALUES = Collections.unmodifiableList(Arrays.asList(BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE, GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, YELLOW, WHITE));
    /**
     * An index of name to color.
     *
     * @since 4.0.0
     */
    public static final Index<String, GlowColor> NAMES = Index.create(constant -> constant.name, VALUES);

    /**
     * Gets the named color exactly matching the provided color.
     *
     * @param value the color to match
     * @return the matched color, or null
     * @since 4.10.0
     */
    public static @Nullable GlowColor namedColor(final int value) {
        switch (value) {
            case BLACK_VALUE: return BLACK;
            case DARK_BLUE_VALUE: return DARK_BLUE;
            case DARK_GREEN_VALUE: return DARK_GREEN;
            case DARK_AQUA_VALUE: return DARK_AQUA;
            case DARK_RED_VALUE: return DARK_RED;
            case DARK_PURPLE_VALUE: return DARK_PURPLE;
            case GOLD_VALUE: return GOLD;
            case GRAY_VALUE: return GRAY;
            case DARK_GRAY_VALUE: return DARK_GRAY;
            case BLUE_VALUE: return BLUE;
            case GREEN_VALUE: return GREEN;
            case AQUA_VALUE: return AQUA;
            case RED_VALUE: return RED;
            case LIGHT_PURPLE_VALUE: return LIGHT_PURPLE;
            case YELLOW_VALUE: return YELLOW;
            case WHITE_VALUE: return WHITE;
            default: return null;
        }
    }

    /**
     * Gets the named color exactly matching the provided color.
     *
     * @param value the color to match
     * @return the matched color, or null
     * @since 4.0.0
     * @deprecated for removal since 4.10.0, use {@link #namedColor(int)} instead
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    public static @Nullable GlowColor ofExact(final int value) {
        switch (value) {
            case BLACK_VALUE: return BLACK;
            case DARK_BLUE_VALUE: return DARK_BLUE;
            case DARK_GREEN_VALUE: return DARK_GREEN;
            case DARK_AQUA_VALUE: return DARK_AQUA;
            case DARK_RED_VALUE: return DARK_RED;
            case DARK_PURPLE_VALUE: return DARK_PURPLE;
            case GOLD_VALUE: return GOLD;
            case GRAY_VALUE: return GRAY;
            case DARK_GRAY_VALUE: return DARK_GRAY;
            case BLUE_VALUE: return BLUE;
            case GREEN_VALUE: return GREEN;
            case AQUA_VALUE: return AQUA;
            case RED_VALUE: return RED;
            case LIGHT_PURPLE_VALUE: return LIGHT_PURPLE;
            case YELLOW_VALUE: return YELLOW;
            case WHITE_VALUE: return WHITE;
            default: return null;
        }
    }

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
