package be.uantwerpen.sd.labs.lab4b.model.domain.warehouse;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import javafx.scene.canvas.GraphicsContext;

public final class WarehouseFloor extends GroundTile {
    @Override
    public boolean isSolid() {
        /*
            TODO: Implement the isSolid method.
            TIP: The floor isn't solid.
        */
        throw new UnsupportedOperationException("TODO: Implement the isSolid method.");
    }

    @Override
    public boolean isSlippery() {
        /*
            TODO: Implement the isSlippery method.
            TIP: The warehouse floor is not slippery.
        */
        throw new UnsupportedOperationException("TODO: Implement the isSlippery method.");
    }

    @Override
    public boolean isTarget() {
        /*
            TODO: Implement the isTarget method.
            TIP: The floor is not a target.
        */
        throw new UnsupportedOperationException("TODO: Implement the isTarget method.");
    }

    @Override
    public void render(GraphicsContext g, Palette p, int x, int y, int s) {
        g.setFill(p.getFloor());
        g.fillRect(x, y, s, s);
    }
}
