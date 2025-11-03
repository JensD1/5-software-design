package be.uantwerpen.sd.labs.lab4b.model.domain.glacier;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.domain.Box;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class GlacierBox extends Box {
    @Override
    public boolean slidesOn(GroundTile tile) {
        /*
            TODO: Implement the slidesOn method.
            TIP: The provided tile object has a field defining whether or not the ground is slippery.
        */
        throw new UnsupportedOperationException("TODO: Implement the slidesOn method.");
    }

    @Override
    public void render(GraphicsContext g, Palette p, int x, int y, int s, boolean isTarget) {
        Color c = p.getBox();
        if (isTarget) {
            c = p.getBox().interpolate(p.getTarget(), 0.35);
        }
        g.setFill(c);
        g.fillRect(x + 2, y + 2, s - 4, s - 4);
    }
}
