package be.uantwerpen.sd.labs.lab4b.model.domain.glacier;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public final class GlacierPlayer extends Player {

    @Override
    public void render(GraphicsContext g, Palette p, int x, int y, int s, boolean isTarget) {
        Color base = p.getBox();
        if (isTarget) base = p.getBox().interpolate(p.getTarget(), 0.35);
        g.setFill(base);
        g.fillRect(x + 2, y + 2, s - 4, s - 4);
        double inset = s * 0.30;
        g.setFill(p.getPlayer());
        g.fillRoundRect(x + inset, y + inset, s - 2 * inset, s - 2 * inset, s * 0.25, s * 0.25);
    }
}
