package be.uantwerpen.sd.labs.lab4b.model.domain;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import javafx.scene.canvas.GraphicsContext;

public final class Empty extends Entity {

    private Empty() {
    }

    public static Empty instance() {
        return Holder.INSTANCE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void render(GraphicsContext g, Palette p, int x, int y, int s, boolean isTarget) {
        // NOOP
    }

    private static final class Holder {
        static final Empty INSTANCE = new Empty();
    }
}
