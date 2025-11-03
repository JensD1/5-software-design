package be.uantwerpen.sd.labs.lab4b.gen;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.model.WorldBuilder;

import java.util.*;


public final class WarehouseWorldGenerator extends WorldGenerator {

    private static boolean isCorner(boolean[][] g, P p) {
        int H = g.length, W = g[0].length;
        // Require interior
        if (p.x <= 0 || p.y <= 0 || p.x >= W - 1 || p.y >= H - 1) return false;
        if (g[p.y][p.x]) return false;
        boolean up = g[p.y - 1][p.x], dn = g[p.y + 1][p.x], lf = g[p.y][p.x - 1], rt = g[p.y][p.x + 1];
        return (up || dn) && (lf || rt);
    }

    private static boolean wouldBecomeDeepPocket(boolean[][] g, P p) {
        int H = g.length, W = g[0].length;
        if (p.x <= 0 || p.y <= 0 || p.x >= W - 1 || p.y >= H - 1) return false;
        int c = 0;
        if (g[p.y - 1][p.x]) c++;
        if (g[p.y + 1][p.x]) c++;
        if (g[p.y][p.x - 1]) c++;
        if (g[p.y][p.x + 1]) c++;
        return c >= 3;
    }

    @Override
    public World generate(int w, int h, int crates, double wallDensity, long seed, int minPulls, int maxPulls, LevelKit kit) {
        rng.setSeed(seed);

        if (w > SCREEN_CAP_W || h > SCREEN_CAP_H) {
            w = Math.min(w, SCREEN_CAP_W);
            h = Math.min(h, SCREEN_CAP_H);
        }

        double safety = 0.55;
        try {
            if (AppConfig.get() != null) {
                safety = Math.max(0.20, Math.min(0.80, AppConfig.get().safetyDensity));
            }
        } catch (Throwable ignored) {
        }
        final double safeDensity = Math.min(Math.max(0.02, wallDensity), safety);

        final int totalPullsTarget = clamp(minPulls + rng.nextInt(Math.max(1, maxPulls - minPulls + 1)), 6, 2000);
        final int attempts = 150;

        for (int attempt = 0; attempt < attempts; attempt++) {
            boolean[][] walls = new boolean[h][w];
            addPerimeter(walls);
            carveSparseWarehouse(walls, Math.min(0.18, safeDensity));

            List<P> targets = pickWarehouseTargets(walls, crates, 2);
            if (targets.size() < crates) continue;

            GenerationResult res = buildWarehouseWitnessAndRealize(walls, w, h, crates, targets, totalPullsTarget);
            if (!res.success) {
                carveConnectorToOpenUp(walls);
                continue;
            }

            P player = (res.playerStart != null) ? res.playerStart : new P(1, 1);

            if (anyBoxOnTarget(res.boxes, targets)) {
                P updated = nudgeBoxesOffTargetsOnce(walls, w, h, res.boxes, targets, player);
                if (updated == null) {
                    carveConnectorToOpenUp(walls);
                    continue;
                }
                player = updated; // keep the player where the reverse-pull left them
            }

            if (!hasAnyLegalInitialPush(walls, res.boxes, player)) {
                carveConnectorToOpenUp(walls);
                if (!hasAnyLegalInitialPush(walls, res.boxes, player)) continue;
            }

            WorldBuilder wb = WorldBuilder.ofSize(w, h, kit).fromWalls(walls);
            for (P t : targets) wb.target(t.x, t.y);
            for (P b : res.boxes) wb.box(b.x, b.y);
            wb.playerOn(player.x, player.y);
            return wb.build();
        }
        return fallback(kit, 1, 1);
    }

    private boolean hasAnyLegalInitialPush(boolean[][] walls, LinkedHashSet<P> boxes, P player) {
        int W = walls[0].length, H = walls.length;
        boolean[][] reach = flood(walls, boxes, player);
        for (P box : boxes) {
            for (Dir d : Dir.values()) {
                P pBack = new P(box.x - d.dx, box.y - d.dy);
                P dest = new P(box.x + d.dx, box.y + d.dy);
                if (!in(W, H, pBack) || !in(W, H, dest)) continue;
                if (!reach[pBack.y][pBack.x]) continue;
                if (walls[dest.y][dest.x] || boxes.contains(dest)) continue;
                return true;
            }
        }
        return false;
    }

    private List<P> pickWarehouseTargets(boolean[][] walls, int k, int minSep) {
        List<P> free = interiorFloors(walls);
        Collections.shuffle(free, rng);
        ArrayList<P> out = new ArrayList<>();
        for (P p : free) {
            if (isCorner(walls, p)) continue;
            boolean ok = true;
            for (P q : out) {
                if (manhattan(p, q) < minSep) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                out.add(p);
                if (out.size() == k) break;
            }
        }
        return out;
    }

    private GenerationResult buildWarehouseWitnessAndRealize(boolean[][] walls, int W, int H, int crates, List<P> targets, int pullsBudget) {
        LinkedHashSet<P> boxes = new LinkedHashSet<>(targets);
        HashSet<P> standSquares = new HashSet<>();

        final int stepsTarget = Math.max(pullsBudget, crates * 4);

        P player = pickRandomFree(walls, boxes);
        if (player == null) player = new P(1, 1);

        int steps = 0;
        while (steps < stepsTarget) {
            boolean[][] reach = flood(walls, boxes, player);

            ArrayList<Dir> dirs = new ArrayList<>(Arrays.asList(Dir.values()));
            ArrayList<P> curBoxes = new ArrayList<>(boxes);
            Collections.shuffle(curBoxes, rng);

            P chosenBox = null;
            Dir chosenDir = null;
            P pStand = null;
            P pBack = null;

            for (P cur : curBoxes) {
                Collections.shuffle(dirs, rng);
                for (Dir d : dirs) {
                    P s = new P(cur.x - d.dx, cur.y - d.dy);
                    P b = new P(s.x - d.dx, s.y - d.dy);
                    if (s.x <= 0 || s.y <= 0 || s.x >= W - 1 || s.y >= H - 1) continue;
                    if (b.x <= 0 || b.y <= 0 || b.x >= W - 1 || b.y >= H - 1) continue;
                    if (!in(W, H, s) || !in(W, H, b)) continue;
                    if (walls[s.y][s.x] || walls[b.y][b.x]) continue;
                    if (boxes.contains(s) || boxes.contains(b)) continue;
                    if (!reach[s.y][s.x]) continue;
                    if (wouldBecomeDeepPocket(walls, s)) continue;

                    chosenBox = cur;
                    chosenDir = d;
                    pStand = s;
                    pBack = b;
                    break;
                }
                if (chosenBox != null) break;
            }

            if (chosenBox == null) {
                return GenerationResult.fail();
            }

            ensureFloor(walls, pStand);
            ensureFloor(walls, pBack);
            if (isCorner(walls, pStand)) carveDeCorner(walls, pStand, W, H);

            boxes.remove(chosenBox);
            boxes.add(pStand);

            standSquares.add(pStand);
            standSquares.add(pBack);

            player = pBack;
            steps++;
        }

        HashSet<P> targetSet = new HashSet<>(targets);
        for (P b : boxes) {
            if (!targetSet.contains(b) && isCorner(walls, b)) {
                carveDeCorner(walls, b, W, H);
            }
        }

        return GenerationResult.ok(boxes, standSquares, player);
    }

    private P nudgeBoxesOffTargetsOnce(boolean[][] walls, int W, int H,
                                       LinkedHashSet<P> boxes, List<P> targets, P playerStart) {
        HashSet<P> targetSet = new HashSet<>(targets);
        ArrayList<P> toFix = new ArrayList<>();
        for (P b : boxes) if (targetSet.contains(b)) toFix.add(b);

        P player = playerStart;
        for (P onTarget : toFix) {
            P next = forceSingleReversePull(walls, W, H, boxes, onTarget, player);
            if (next == null) return null; // failed to nudge while preserving reachability
            player = next;                 // after reverse-pull the player stands at pBack
        }
        return player;
    }

    private P forceSingleReversePull(boolean[][] walls, int W, int H,
                                     LinkedHashSet<P> boxes, P cur, P playerStart) {
        List<Dir> dirs = new ArrayList<>(Arrays.asList(Dir.values()));
        Collections.shuffle(dirs, rng);
        for (Dir d : dirs) {
            P pStand = new P(cur.x - d.dx, cur.y - d.dy);
            P pBack  = new P(pStand.x - d.dx, pStand.y - d.dy);
            if (!in(W, H, pStand) || !in(W, H, pBack)) continue;
            if (boxes.contains(pStand) || boxes.contains(pBack)) continue;

            // Only carve if we can actually reach from the *real* player
            boolean[][] reach = flood(walls, boxes, playerStart);
            if (!reach[pStand.y][pStand.x]) continue;

            ensureFloor(walls, pStand);
            ensureFloor(walls, pBack);
            if (wouldBecomeDeepPocket(walls, pStand)) continue;
            if (isCorner(walls, pStand)) carveDeCorner(walls, pStand, W, H);

            boxes.remove(cur);
            boxes.add(pStand);
            return pBack; // updated player position for subsequent steps
        }
        return null;
    }

    private boolean anyBoxOnTarget(Set<P> boxes, List<P> targets) {
        HashSet<P> t = new HashSet<>(targets);
        for (P b : boxes) if (t.contains(b)) return true;
        return false;
    }

    private void carveSparseWarehouse(boolean[][] g, double density) {
        int H = g.length, W = g[0].length;
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (rng.nextDouble() < density) g[y][x] = true;
        int holes = Math.max(2, (int) Math.round(density * 10));
        for (int k = 0; k < holes; k++) {
            int x = 1 + rng.nextInt(Math.max(1, W - 2));
            int y = 1 + rng.nextInt(Math.max(1, H - 2));
            g[y][x] = false;
        }
    }

    private void carveConnectorToOpenUp(boolean[][] g) {
        int H = g.length, W = g[0].length;
        int x = 2 + rng.nextInt(Math.max(1, W - 4));
        int y = 2 + rng.nextInt(Math.max(1, H - 4));
        g[y][x] = false;
        if (y > 1) g[y - 1][x] = false;
        if (y < H - 2) g[y + 1][x] = false;
        if (x > 1) g[y][x - 1] = false;
        if (x < W - 2) g[y][x + 1] = false;
    }

    private boolean[][] flood(boolean[][] walls, Set<P> boxes, P start) {
        int W = walls[0].length, H = walls.length;
        boolean[][] vis = new boolean[H][W];
        ArrayDeque<P> dq = new ArrayDeque<>();
        if (!in(W, H, start.x, start.y) || walls[start.y][start.x] || boxes.contains(start)) return vis;
        dq.add(start);
        vis[start.y][start.x] = true;
        while (!dq.isEmpty()) {
            P c = dq.removeFirst();
            for (Dir d : Dir.values()) {
                int nx = c.x + d.dx, ny = c.y + d.dy;
                P p = new P(nx, ny);
                if (!in(W, H, nx, ny) || vis[ny][nx] || walls[ny][nx] || boxes.contains(p)) continue;
                vis[ny][nx] = true;
                dq.addLast(p);
            }
        }
        return vis;
    }

    private void carveDeCorner(boolean[][] g, P p, int W, int H) {
        for (Dir d : Dir.values()) {
            P q = new P(p.x + d.dx, p.y + d.dy);
            if (!in(W, H, q.x, q.y)) continue;
            if (q.x == 0 || q.y == 0 || q.x == W - 1 || q.y == H - 1) continue;
            if (g[q.y][q.x]) {
                g[q.y][q.x] = false;
                break;
            }
        }
    }

    private World fallback(LevelKit kit, int playerX, int playerY) {
        boolean[][] walls = new boolean[5][7];
        for (int x = 0; x < 7; x++) {
            walls[0][x] = true;
            walls[4][x] = true;
        }
        for (int y = 0; y < 5; y++) {
            walls[y][0] = true;
            walls[y][6] = true;
        }
        WorldBuilder wb = WorldBuilder.ofSize(7, 5, kit).fromWalls(walls);
        wb.target(3, 2);
        wb.box(2, 2);
        wb.playerOn(playerX, playerY);
        return wb.build();
    }

    private enum Dir {
        U(0, -1), D(0, 1), L(-1, 0), R(1, 0);
        final int dx, dy;

        Dir(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }

    private record GenerationResult(boolean success, LinkedHashSet<P> boxes, HashSet<P> witnessStandSquares,
                                    P playerStart) {

        static GenerationResult ok(LinkedHashSet<P> b, HashSet<P> st, P playerStart) {
            return new GenerationResult(true, b, st, playerStart);
        }

        static GenerationResult fail() {
            return new GenerationResult(false, new LinkedHashSet<>(), new HashSet<>(), null);
        }
    }
}
