package be.uantwerpen.sd.labs.lab4b.model.domain;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    public boolean isBox() {
        return false;
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean slidesOn(GroundTile tile) {
        return false;
    }

    public boolean isEmpty() {
        return false;
    }

    public abstract void render(GraphicsContext g, Palette p, int x, int y, int s, boolean isTarget);
}
