package be.uantwerpen.sd.labs.lab4b.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public final class AppConfig {
    public final int tilePx, legendHeightPx, paddingPx;
    public final double baseDensity, perLevelDelta, jitter, minDensity, maxDensity, safetyDensity;
    public final int startLevel, maxLevel;
    public final int startW, startH, startCrates;
    public final int stepEveryW, stepEveryH, stepEveryCrates;
    public final int maxW, maxH, maxCrates;
    public final long seedBase;
    public final String fontFamily, legendTextColor, toolbarBg, toolbarText, statusBg;
    public final int legendFontPx;
    public final int pullsMinBase, pullsMinPerCrate, pullsMaxBase, pullsMaxPerCrate, pullsPer100Tiles, glacierPullBonus;
    public final Map<String, String> themeWarehouse;
    public final Map<String, String> themeGlacier;
    private AppConfig INSTANCE_REF_GUARD;

    @SuppressWarnings("unchecked")
    private AppConfig(Map<String, Object> m) {
        Map<String, Object> render = (Map<String, Object>) m.get("render");
        Map<String, Object> diff = (Map<String, Object>) m.get("difficulty");
        Map<String, Object> gen = (Map<String, Object>) m.get("generator");
        Map<String, Object> themes = (Map<String, Object>) m.get("themes");

        safetyDensity = ((Number) gen.get("max_wall_density_safety")).doubleValue();

        baseDensity = ((Number) diff.get("base_density")).doubleValue();
        perLevelDelta = ((Number) diff.get("per_level_delta")).doubleValue();
        jitter = ((Number) diff.get("jitter")).doubleValue();
        minDensity = ((Number) diff.get("min_density")).doubleValue();
        maxDensity = ((Number) diff.get("max_density")).doubleValue();

        startLevel = ((Number) diff.getOrDefault("start_level", 1)).intValue();
        maxLevel = ((Number) diff.getOrDefault("max_level", 999)).intValue();

        startW = ((Number) diff.getOrDefault("start_w", 11)).intValue();
        startH = ((Number) diff.getOrDefault("start_h", 9)).intValue();
        startCrates = ((Number) diff.getOrDefault("start_crates", 2)).intValue();

        stepEveryW = ((Number) diff.getOrDefault("step_every_levels_w", 3)).intValue();
        stepEveryH = ((Number) diff.getOrDefault("step_every_levels_h", 4)).intValue();
        stepEveryCrates = ((Number) diff.getOrDefault("step_every_levels_crates", 2)).intValue();

        maxW = ((Number) diff.getOrDefault("max_w", 25)).intValue();
        maxH = ((Number) diff.getOrDefault("max_h", 19)).intValue();
        maxCrates = ((Number) diff.getOrDefault("max_crates", 10)).intValue();

        seedBase = ((Number) diff.getOrDefault("seed_base", 12345)).longValue();

        pullsMinBase = ((Number) diff.getOrDefault("pulls_min_base", 8)).intValue();
        pullsMinPerCrate = ((Number) diff.getOrDefault("pulls_min_per_crate", 4)).intValue();
        pullsMaxBase = ((Number) diff.getOrDefault("pulls_max_base", 18)).intValue();
        pullsMaxPerCrate = ((Number) diff.getOrDefault("pulls_max_per_crate", 8)).intValue();
        pullsPer100Tiles = ((Number) diff.getOrDefault("pulls_per_100_tiles", 5)).intValue();
        glacierPullBonus = ((Number) diff.getOrDefault("glacier_pull_bonus", 6)).intValue();

        tilePx = ((Number) render.get("tile_px")).intValue();
        legendHeightPx = ((Number) render.get("legend_height_px")).intValue();
        paddingPx = ((Number) render.get("padding_px")).intValue();
        fontFamily = (String) render.getOrDefault("font_family", "System");
        legendFontPx = ((Number) render.getOrDefault("legend_font_px", 14)).intValue();
        legendTextColor = (String) render.getOrDefault("legend_text_color", "#111111");
        toolbarBg = (String) render.getOrDefault("toolbar_bg", "#f4f4f4");
        toolbarText = (String) render.getOrDefault("toolbar_text", "#000000");
        statusBg = (String) render.getOrDefault("status_bg", "#f8f8f8");

        themeWarehouse = (Map<String, String>) themes.get("warehouse");
        themeGlacier = (Map<String, String>) themes.get("glacier");

    }

    public static AppConfig get() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final AppConfig INSTANCE = load();

        private static AppConfig load() {
            Yaml yaml = new Yaml();
            try (InputStream is = AppConfig.class.getResourceAsStream("/config.yaml")) {
                if (is == null) throw new IllegalStateException("Missing /config.yaml");
                Map<String, Object> m = yaml.load(is);
                return new AppConfig(m);
            } catch (Exception e) {
                throw new RuntimeException("Config load failed", e);
            }
        }
    }
}

