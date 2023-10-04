package lol.pyr.znpcsplus.util;

import org.bukkit.DyeColor;

import java.util.function.IntFunction;

public class TropicalFishVariant {

    private final TropicalFishPattern pattern;
    private final DyeColor bodyColor;
    private final DyeColor patternColor;

    public TropicalFishVariant(TropicalFishPattern pattern, DyeColor bodyColor, DyeColor patternColor) {
        this.pattern = pattern;
        this.bodyColor = bodyColor;
        this.patternColor = patternColor;
    }

    public int getVariant() {
        return pattern.getId() & '\uffff' | (bodyColor.ordinal() & 255) << 16 | (patternColor.ordinal() & 255) << 24;
    }

    public enum TropicalFishPattern {
        KOB(0, 0),
        SUNSTREAK(0, 1),
        SNOOPER(0, 2),
        DASHER(0, 3),
        BRINELY(0, 4),
        SPOTTY(0, 5),
        FLOPPER(1, 0),
        STRIPEY(1, 1),
        GLITTER(1, 2),
        BLOCKFISH(1, 3),
        BETTY(1, 4),
        CLAYFISH(1, 5);

        private final int size;
        private final int id;
        private static final IntFunction<TropicalFishPattern> BY_ID = (id) -> {
            for (TropicalFishPattern pattern : values()) {
                if (pattern.id == id) {
                    return pattern;
                }
            }
            return null;
        };

        TropicalFishPattern(int size, int pattern) {
            this.size = size;
            this.id = size | pattern << 8;
        }

        public int getSize() {
            return size;
        }

        public int getId() {
            return id;
        }

        public static TropicalFishPattern fromVariant(int variant) {
            return BY_ID.apply(variant & '\uffff');
        }
    }

    public static class Builder {
        private TropicalFishPattern pattern;
        private DyeColor bodyColor;
        private DyeColor patternColor;

        public Builder() {
            this.pattern = TropicalFishPattern.KOB;
            this.bodyColor = DyeColor.WHITE;
            this.patternColor = DyeColor.WHITE;
        }

        public Builder pattern(TropicalFishPattern pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder bodyColor(DyeColor bodyColor) {
            this.bodyColor = bodyColor;
            return this;
        }

        public Builder patternColor(DyeColor patternColor) {
            this.patternColor = patternColor;
            return this;
        }

        public static Builder fromInt(int variant) {
            Builder builder = new Builder();
            builder.pattern = TropicalFishPattern.fromVariant(variant);
            builder.bodyColor = DyeColor.values()[(variant >> 16) & 0xFF];
            builder.patternColor = DyeColor.values()[(variant >> 24) & 0xFF];
            return builder;
        }

        public TropicalFishVariant build() {
            return new TropicalFishVariant(pattern, bodyColor, patternColor);
        }
    }
}
