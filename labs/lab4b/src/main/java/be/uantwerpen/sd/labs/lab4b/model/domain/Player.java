package be.uantwerpen.sd.labs.lab4b.model.domain;

public abstract class Player extends Entity {
    @Override
    public boolean isPlayer() {
        return true;
    }
}
