package be.uantwerpen.sd.labs.lab4b.level;

import javafx.scene.paint.Color;

import java.util.Map;

/*
TODO: Implement the concrete builder.
Methods you will implement:
  - void buildBackground()
  - void buildFloor()
  - void buildWall()
  - void buildTarget()
  - void buildPlayer()
  - void buildBox()
  - Palette build()
TIP: You can get the correct color from the theme by using c(theme.get(string)). Look at the bottom of lab4b/src/main/resources/config.yaml for specific terminology [e.g. c(theme.get(background))]
*/
public final class ThemedPaletteBuilder {
    private Color background, floor, wall, target, player, box;
    private final Map<String, String> theme;

    public ThemedPaletteBuilder(Map<String, String> theme) {
        this.theme = theme;
    }

    private static Color c(String hex) {
        return Color.web(hex);
    }
}