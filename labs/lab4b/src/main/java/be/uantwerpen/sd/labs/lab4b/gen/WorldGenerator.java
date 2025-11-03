package be.uantwerpen.sd.labs.lab4b.gen;

import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.model.World;

import java.util.*;

public abstract class WorldGenerator {
    protected static final int SCREEN_CAP_W = 25, SCREEN_CAP_H = 18;
    protected final Random rng = new Random();

    protected static int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    protected static boolean in(int W, int H, int x, int y) {
        return x >= 0 && y >= 0 && x < W && y < H;
    }

    protected static boolean in(int W, int H, P p) {
        return in(W, H, p.x, p.y);
    }

    protected static void ensureFloor(boolean[][] walls, P p) {
        if (in(walls[0].length, walls.length, p)) walls[p.y][p.x] = false;
    }

    public abstract World generate(int w, int h, int crates, double wallDensity, long seed, int minPulls, int maxPulls, LevelKit kit);

    protected void addPerimeter(boolean[][] walls) {
        int H = walls.length, W = walls[0].length;
        for (int x = 0; x < W; x++) {
            walls[0][x] = true;
            walls[H - 1][x] = true;
        }
        for (int y = 0; y < H; y++) {
            walls[y][0] = true;
            walls[y][W - 1] = true;
        }
    }

    protected void carveSparse(boolean[][] walls, double density) {
        int H = walls.length, W = walls[0].length;
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                walls[y][x] = rng.nextDouble() < density;
    }

    protected P pickRandomFree(boolean[][] walls, Set<P> boxes) {
        java.util.ArrayList<P> free = new java.util.ArrayList<>();
        int H = walls.length, W = walls[0].length;
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (!walls[y][x] && !boxes.contains(new P(x, y))) free.add(new P(x, y));
        if (free.isEmpty()) return null;
        java.util.Collections.shuffle(free, rng);
        return free.get(0);
    }

    protected void carvePolyline(List<P> pts, boolean[][] walls, boolean[][] carved) {
        if (pts == null || pts.size() < 2) return;
        for (int i = 0; i + 1 < pts.size(); i++) {
            P a = pts.get(i), b = pts.get(i + 1);
            if (a.x == b.x) carveV(a, b, walls, carved);
            else if (a.y == b.y) carveH(a, b, walls, carved);
        }
        int W = walls[0].length, H = walls.length;
        for (P p : pts)
            if (in(W, H, p)) {
                walls[p.y][p.x] = false;
                if (carved != null) carved[p.y][p.x] = true;
            }
    }

    protected void carveH(P a, P b, boolean[][] walls, boolean[][] carved) {
        int x0 = Math.min(a.x, b.x), x1 = Math.max(a.x, b.x), y = a.y;
        for (int x = x0; x <= x1; x++) {
            walls[y][x] = false;
            if (carved != null) carved[y][x] = true;
        }
    }

    protected void carveV(P a, P b, boolean[][] walls, boolean[][] carved) {
        int y0 = Math.min(a.y, b.y), y1 = Math.max(a.y, b.y), x = a.x;
        for (int y = y0; y <= y1; y++) {
            walls[y][x] = false;
            if (carved != null) carved[y][x] = true;
        }
    }

    protected List<P> interiorFloors(boolean[][] walls) {
        int H = walls.length, W = walls[0].length;
        ArrayList<P> out = new ArrayList<>();
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (!walls[y][x]) out.add(new P(x, y));
        return out;
    }

    protected int manhattan(P a, P b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    protected static final class P {
        final int x, y;

        P(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof P p && p.x == x && p.y == y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
