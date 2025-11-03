package be.uantwerpen.sd.labs.lab4b.ui;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.Cell;
import be.uantwerpen.sd.labs.lab4b.model.World;
import javafx.scene.canvas.GraphicsContext;

public final class GridRenderer {
    private final AppConfig C = AppConfig.get();
    private final Palette P;
    private final LevelKit.RendererHints H;

    public GridRenderer(Palette palette, LevelKit.RendererHints hints) {
        this.P = palette;
        this.H = hints;
    }

    public void draw(GraphicsContext g, World w) {
        final int TILE = C.tilePx, PAD = C.paddingPx;

        // 0) Background for the whole scene (fits both worlds)
        g.setFill(P.getBackground());
        g.fillRect(0, 0, w.w * TILE + PAD * 2, w.h * TILE + C.legendHeightPx + PAD * 2);

        // 1) Clear the grid area overlay so tiles are crisp
        g.clearRect(PAD, PAD, w.w * TILE, w.h * TILE);

        for (int y = 0; y < w.h; y++)
            for (int x = 0; x < w.w; x++) {
                int sx = PAD + x * TILE, sy = PAD + y * TILE;
                Cell c = w.get(x, y);
                c.ground.render(g, P, sx, sy, TILE);
                if (!c.isEmpty()) c.thing.render(g, P, sx, sy, TILE, c.isTarget());
            }
    }
}
