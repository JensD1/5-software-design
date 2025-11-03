package be.uantwerpen.sd.labs.lab4b.logic.glacier;

import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.model.domain.Empty;

public final class GlacierMovementStrategy implements MovementStrategy {

    private final LevelKit kit;

    public GlacierMovementStrategy(LevelKit kit) {
        this.kit = kit;
    }

    @Override
    public boolean move(World w, int dx, int dy) {
        if (dx == 0 && dy == 0) return false;
        int sx = w.playerX, sy = w.playerY;
        if (!w.inBounds(sx, sy)) return false;
        var sel = w.get(sx, sy);
        if (sel.thing == null || !sel.thing.isPlayer()) {
            if (!selectFirstBox(w)) return false;
            sx = w.playerX;
            sy = w.playerY;
            sel = w.get(sx, sy);
            if (sel.thing == null || !sel.thing.isPlayer()) return false;
        }
        int x = sx, y = sy;
        boolean moved = false;
        while (true) {
            int nx = x + dx, ny = y + dy;
            if (!w.inBounds(nx, ny)) break;
            var next = w.get(nx, ny);
            if (next.ground.isSolid()) break;
            if (!next.thing.isEmpty()) break;
            next.thing = sel.thing;
            w.get(x, y).thing = Empty.instance();
            x = nx;
            y = ny;
            sel = next;
            moved = true;
        }
        if (moved) w.setPlayerPos(x, y);
        return moved;
    }

    @Override
    public boolean nextSelectable(World w) {
        int W = w.getW(), H = w.getH();
        int start = Math.max(0, w.playerY) * W + Math.max(0, w.playerX);
        int nx = -1, ny = -1;
        for (int k = 1; k <= W * H; k++) {
            int idx = (start + k) % (W * H);
            int x = idx % W, y = idx / W;
            var a = w.get(x, y).thing;
            if (a != null && a.isBox()) {
                nx = x;
                ny = y;
                break;
            }
        }
        if (nx < 0) return false;
        int px = w.playerX, py = w.playerY;
        var cur = w.get(px, py);
        var nxt = w.get(nx, ny);
        if (cur.thing != null && cur.thing.isPlayer()) cur.thing = this.kit.box();
        if (nxt.thing != null && nxt.thing.isBox()) nxt.thing = this.kit.player();
        w.setPlayerPos(nx, ny);
        return true;
    }

    private boolean selectFirstBox(World w) {
        int W = w.getW(), H = w.getH();
        int nx = -1, ny = -1;
        for (int y = 0; y < H; y++)
            for (int x = 0; x < W; x++) {
                var a = w.get(x, y).thing;
                if (a != null && a.isBox()) {
                    nx = x;
                    ny = y;
                    break;
                }
            }
        if (nx < 0) return false;
        int px = w.playerX, py = w.playerY;
        var cur = w.get(px, py);
        var nxt = w.get(nx, ny);
        if (cur.thing != null && cur.thing.isPlayer()) cur.thing = this.kit.box();
        if (nxt.thing != null && nxt.thing.isBox()) nxt.thing = this.kit.player();
        w.setPlayerPos(nx, ny);
        return true;
    }
}
