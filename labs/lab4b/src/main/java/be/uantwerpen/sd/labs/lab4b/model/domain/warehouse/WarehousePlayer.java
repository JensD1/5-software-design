package be.uantwerpen.sd.labs.lab4b.model.domain.warehouse;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
import javafx.scene.canvas.GraphicsContext;

public final class WarehousePlayer extends Player {
    @Override
    public void render(GraphicsContext g, Palette p, int x, int y, int s, boolean isTarget) {
        g.setFill(p.getPlayer());
        g.fillRoundRect(x, y, s, s, s * 0.22, s * 0.22);
    }
}
