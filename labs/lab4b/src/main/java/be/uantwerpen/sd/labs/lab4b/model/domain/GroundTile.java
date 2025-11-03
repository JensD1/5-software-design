package be.uantwerpen.sd.labs.lab4b.model.domain;

import be.uantwerpen.sd.labs.lab4b.level.Palette;
import javafx.scene.canvas.GraphicsContext;

public abstract class GroundTile {
    public abstract boolean isSolid();

    public abstract boolean isSlippery();

    public abstract boolean isTarget();

    public abstract void render(GraphicsContext g, Palette p, int x, int y, int s);
}