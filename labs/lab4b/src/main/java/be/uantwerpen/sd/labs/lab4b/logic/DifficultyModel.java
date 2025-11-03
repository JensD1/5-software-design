package be.uantwerpen.sd.labs.lab4b.logic;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.Level;

public final class DifficultyModel {

    private static final AppConfig C = AppConfig.get();

    private DifficultyModel() {
    }

    public static Params paramsForLevel(int levelNumber, Level level) {
        int L = clamp(levelNumber, C.startLevel, C.maxLevel);

        // Size & crates
        int w = clamp(C.startW + (L - 1) / Math.max(1, C.stepEveryW), 5, C.maxW);
        int h = clamp(C.startH + (L - 1) / Math.max(1, C.stepEveryH), 5, C.maxH);
        int crates = clamp(C.startCrates + (L - 1) / Math.max(1, C.stepEveryCrates), 1, C.maxCrates);

        // Density
        double base = C.baseDensity + (L - 1) * C.perLevelDelta;
        double jitter = ((L * 1103515245L + 12345L) ^ (System.identityHashCode(level) * 2654435761L)) & 0xffff;
        double j = (jitter / 65535.0 - 0.5) * (2.0 * C.jitter);
        double wallDensity = clamp(base + j, C.minDensity, C.maxDensity);

        // Seed
        long seed = C.seedBase + 31L * L + 7L * System.identityHashCode(level);

        // reverse-pull steps
        int areaBonus = (w * h * C.pullsPer100Tiles) / 100;
        int minPulls = C.pullsMinBase + C.pullsMinPerCrate * crates + areaBonus
                + level.extraPullBias();
        int maxPulls = C.pullsMaxBase + C.pullsMaxPerCrate * crates + areaBonus * 2
                + level.extraPullBias();

        // Ensure sane window
        maxPulls = Math.max(maxPulls, minPulls + 2);

        return new Params(w, h, crates, wallDensity, seed, minPulls, maxPulls);
    }

    private static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    public record Params(int w, int h, int crates, double wallDensity, long seed,
                         int minPulls, int maxPulls) {
    }
}