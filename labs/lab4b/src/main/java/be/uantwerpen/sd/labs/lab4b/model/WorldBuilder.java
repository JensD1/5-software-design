package be.uantwerpen.sd.labs.lab4b.model;

import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.model.domain.Empty;

public final class WorldBuilder {
    private final World w;
    private final LevelKit kit;

    private WorldBuilder(int width, int height, LevelKit levelKit) {
        this.w = new World(width, height);
        this.kit = levelKit;
    }

    public static WorldBuilder ofSize(int width, int height, LevelKit kit) {
        return new WorldBuilder(width, height, kit);
    }

    public WorldBuilder fromWalls(boolean[][] walls) {
        int H = walls.length, W = walls[0].length;
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                w.set(x, y, walls[y][x] ? new Cell(kit.wall(), Empty.instance()) : new Cell(kit.floor(), Empty.instance()));
            }
        }
        return this;
    }

    public WorldBuilder target(int x, int y) {
        var c = w.get(x, y);
        w.set(x, y, new Cell(kit.target(), c.thing));
        return this;
    }

    public WorldBuilder box(int x, int y) {
        var c = w.get(x, y);
        w.set(x, y, new Cell(c.ground, kit.box()));
        return this;
    }

    public WorldBuilder playerOn(int x, int y) {
        var c = w.get(x, y);
        w.set(x, y, new Cell(c.ground, kit.player()));
        w.setPlayerPos(x, y);
        return this;
    }

    public World build() {
        return w;
    }
}
