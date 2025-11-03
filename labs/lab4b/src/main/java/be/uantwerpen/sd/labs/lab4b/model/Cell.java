package be.uantwerpen.sd.labs.lab4b.model;

import be.uantwerpen.sd.labs.lab4b.model.domain.Empty;
import be.uantwerpen.sd.labs.lab4b.model.domain.Entity;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;

public final class Cell {
    public GroundTile ground;
    public Entity thing;

    public Cell(GroundTile g, Entity t) {
        this.ground = g;
        this.thing = (t == null ? Empty.instance() : t);
    }

    public boolean isWalkable() {
        return !ground.isSolid();
    }

    public boolean isTarget() {
        return ground.isTarget();
    }

    public boolean isEmpty() {
        return thing == null || thing.isEmpty();
    }
}
