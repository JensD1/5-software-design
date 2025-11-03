package be.uantwerpen.sd.labs.lab4b.level;

import javafx.scene.paint.Color;

public class Palette {
    public final Color background, floor, wall, target, player, box;

    public Palette(Color background, Color floor, Color wall, Color target, Color player, Color box) {
        this.background = background;
        this.floor = floor;
        this.wall = wall;
        this.target = target;
        this.player = player;
        this.box = box;
    }

    public Color getBackground() {
        return background;
    }

    public Color getFloor() {
        return floor;
    }

    public Color getWall() {
        return wall;
    }

    public Color getTarget() {
        return target;
    }

    public Color getPlayer() {
        return player;
    }

    public Color getBox() {
        return box;
    }
}

