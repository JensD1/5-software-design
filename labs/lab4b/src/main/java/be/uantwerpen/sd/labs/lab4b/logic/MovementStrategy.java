package be.uantwerpen.sd.labs.lab4b.logic;

import be.uantwerpen.sd.labs.lab4b.model.World;

public interface MovementStrategy {
    boolean move(World w, int dx, int dy);

    default boolean nextSelectable(World w) {
        return false;
    }
}
