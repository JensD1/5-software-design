package be.uantwerpen.sd.labs.lab4b.logic.warehouse;

import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.Cell;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.model.domain.Empty;

public final class WarehouseMovementStrategy implements MovementStrategy {

    private void swapPlayer(World w, int px, int py, int nx, int ny) {
        Cell from = w.get(px, py);
        Cell to = w.get(nx, ny);
        to.thing = from.thing;
        from.thing = Empty.instance();
        w.playerX = nx;
        w.playerY = ny;
    }

    @Override
    public boolean move(World w, int dx, int dy) {
        int px = w.playerX, py = w.playerY;
        int nx = px + dx, ny = py + dy;
        if (!w.inBounds(nx, ny)) return false;

        Cell dest = w.get(nx, ny);
        if (dest.ground.isSolid()) return false;

        if (dest.isEmpty()) {
            swapPlayer(w, px, py, nx, ny);
            return true;
        }

        if (dest.thing.isBox()) {
            int bx = nx + dx, by = ny + dy;
            if (!w.inBounds(bx, by)) return false;
            Cell beyond = w.get(bx, by);
            if (beyond.ground.isSolid() || !beyond.isEmpty()) return false;

            beyond.thing = dest.thing;
            dest.thing = Empty.instance();

            swapPlayer(w, px, py, nx, ny);
            return true;
        }
        return false;
    }
}
