package lol.pyr.znpcsplus.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.HSVLike;
import net.kyori.adventure.util.Index;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class GlowColor implements TextColor {
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

    /**
     * The standard {@code black} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor BLACK = new GlowColor("black", BLACK_VALUE);
    /**
     * The standard {@code dark_blue} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_BLUE = new GlowColor("dark_blue", DARK_BLUE_VALUE);
    /**
     * The standard {@code dark_green} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_GREEN = new GlowColor("dark_green", DARK_GREEN_VALUE);
    /**
     * The standard {@code dark_aqua} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_AQUA = new GlowColor("dark_aqua", DARK_AQUA_VALUE);
    /**
     * The standard {@code dark_red} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_RED = new GlowColor("dark_red", DARK_RED_VALUE);
    /**
     * The standard {@code dark_purple} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_PURPLE = new GlowColor("dark_purple", DARK_PURPLE_VALUE);
    /**
     * The standard {@code gold} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor GOLD = new GlowColor("gold", GOLD_VALUE);
    /**
     * The standard {@code gray} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor GRAY = new GlowColor("gray", GRAY_VALUE);
    /**
     * The standard {@code dark_gray} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor DARK_GRAY = new GlowColor("dark_gray", DARK_GRAY_VALUE);
    /**
     * The standard {@code blue} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor BLUE = new GlowColor("blue", BLUE_VALUE);
    /**
     * The standard {@code green} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor GREEN = new GlowColor("green", GREEN_VALUE);
    /**
     * The standard {@code aqua} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor AQUA = new GlowColor("aqua", AQUA_VALUE);
    /**
     * The standard {@code red} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor RED = new GlowColor("red", RED_VALUE);
    /**
     * The standard {@code light_purple} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor LIGHT_PURPLE = new GlowColor("light_purple", LIGHT_PURPLE_VALUE);
    /**
     * The standard {@code yellow} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor YELLOW = new GlowColor("yellow", YELLOW_VALUE);
    /**
     * The standard {@code white} colour.
     *
     * @since 4.0.0
     */
    public static final GlowColor WHITE = new GlowColor("white", WHITE_VALUE);

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

    /**
     * Find the named colour nearest to the provided colour.
     *
     * @param any colour to match
     * @return nearest named colour. will always return a value
     * @since 4.0.0
     */
    public static @NotNull GlowColor nearestTo(final @NotNull TextColor any) {
        if (any instanceof NamedTextColor) {
            return (GlowColor) any;
        }

        return TextColor.nearestColorTo(VALUES, any);
    }

    private final String name;
    private final int value;
    private final HSVLike hsv;

    private GlowColor(final String name, final int value) {
        this.name = name;
        this.value = value;
        this.hsv = HSVLike.fromRGB(this.red(), this.green(), this.blue());
    }

    @Override
    public int value() {
        return this.value;
    }

    public NamedTextColor toNamedTextColor() {
        return NamedTextColor.namedColor(this.value);
    }

    @Override
    public @NotNull HSVLike asHSV() {
        return this.hsv;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    @Override
    public @NotNull Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.concat(
                Stream.of(ExaminableProperty.of("name", this.name)),
                TextColor.super.examinableProperties()
        );
    }
}
