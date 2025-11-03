package be.uantwerpen.sd.labs.lab4b.gen;

import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.model.WorldBuilder;
import be.uantwerpen.sd.labs.lab4b.model.domain.Empty;
import be.uantwerpen.sd.labs.lab4b.model.domain.Entity;

import java.util.*;

public final class GlacierWorldGenerator extends WorldGenerator {

    // 0=RIGHT, 1=LEFT, 2=DOWN, 3=UP
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final Random RNG = new Random();
    private LevelKit kit;

    @Override
    public World generate(int w, int h, int crates, double wallDensity, long seed, int minPulls, int maxPulls, LevelKit kit) {
        this.kit = kit;

        // Normalize once (these don't depend on attempt)
        RNG.setSeed(seed);
        w = Math.max(7, Math.min(w, SCREEN_CAP_W));
        h = Math.max(7, Math.min(h, SCREEN_CAP_H));
        crates = Math.max(1, Math.min(crates, Math.max(2, (w * h) / 20)));

        // Keep track of the last constructed world so we can always return something.
        World lastWorld = null;
        List<P> lastCubes = null;   // for placing the player if we need the fallback

        for (int attempt = 1; attempt <= 100; attempt++) {
            boolean[][] walls = new boolean[h][w];
            addPerimeter(walls);
            sprinkleBumpers(walls, wallDensity);

            // Choose backstopped targets; fallback to border-adjacent.
            List<P> targets = pickBackstoppedTargets(walls, crates);
            if (targets.size() != crates) targets = forceTargetsAlongBorder(walls, crates);

            WorldBuilder wb = WorldBuilder.ofSize(w, h, kit).fromWalls(walls);
            for (P t : targets) wb.target(t.x, t.y);

            List<P> cubes = new ArrayList<>(targets.size());
            for (P t : targets) {
                wb.box(t.x, t.y);
                cubes.add(new P(t.x, t.y));
            }

            World world = wb.build();

            // Keep most recent built world (for fallback return after loop if needed)
            lastWorld = world;
            lastCubes = cubes;

            // Scramble only with bidirectionally reversible moves.
            StepBudget sb = computeStepBudget(crates, minPulls, maxPulls, w, h);
            List<Move> scramble = scrambleUntilGoodReversible(world, cubes, targets, sb.minSteps, sb.maxSteps, Math.min(crates, 2 + crates / 2), Math.max(3, (minPulls + 1) / 2), 1);

            ensureOffTargetsWithReversibleMoves(world, cubes, targets, scramble);

            // If any cube still sits on a target and we still have attempts left, try again.
            if (anyOnTarget(cubes, targets) && attempt < 100) {
                continue;
            }

            // Success (either fully good, or it's attempt 100 and we accept it).
            P first = cubes.get(0);
            world.get(first.x, first.y).thing = kit.player();
            world.setPlayerPos(first.x, first.y);
            return world;
        }

        // Hard fallback: should be rare. Return the last built world (may have a cube on target).
        P first = lastCubes.get(0);
        lastWorld.get(first.x, first.y).thing = kit.player();
        lastWorld.setPlayerPos(first.x, first.y);
        System.out.println("Level with one or more cube solved generated...");
        return lastWorld;
    }

    private boolean anyOnTarget(List<P> cubes, List<P> targets) {
        Set<Long> tset = new HashSet<>();
        for (P t : targets) tset.add(key(t.x, t.y));
        for (P c : cubes) if (tset.contains(key(c.x, c.y))) return true;
        return false;
    }


    private StepBudget computeStepBudget(int crates, int minPulls, int maxPulls, int W, int H) {
        // Aim for ~2..10 moves per cube (clamped), with a small spread.
        int perCubeAim = Math.max(2, Math.min(10, (minPulls + maxPulls) / 2));
        int minSteps = Math.max(crates * 2, crates * Math.max(2, Math.min(8, perCubeAim - 1)));
        int maxSteps = Math.min(crates * 10, Math.max(minSteps + 4, crates * (perCubeAim + 2)));
        return new StepBudget(minSteps, maxSteps);
    }

    private List<Move> scrambleUntilGoodReversible(World world, List<P> cubes, List<P> targets, int minScramble, int maxScramble, int minDistinctCubes, int minDirChanges, int minTurnsPerCube) {
        for (int attempt = 0; attempt < 200; attempt++) {
            resetToSolved(world, cubes, targets);

            List<Move> seq = new ArrayList<>();
            Map<Integer, Integer> cubeTurns = new HashMap<>();
            Deque<Integer> recent = new ArrayDeque<>();
            int steps = RNG.nextInt(maxScramble - minScramble + 1) + minScramble;

            int lastDir = -1, dirChanges = 0;

            for (int s = 0; s < steps; s++) {
                int idx = pickCubeIndex(cubes.size(), recent);
                Move mv = randomLegalReversibleMove(world, cubes, idx, null);
                if (mv == null) { // try another cube
                    int alternate = tryFindAlternateCube(world, cubes, idx);
                    if (alternate >= 0) mv = randomLegalReversibleMove(world, cubes, alternate, null);
                }
                if (mv == null) {
                    s--;
                    continue;
                } // skip this step and retry

                applyMove(world, cubes, mv);
                seq.add(mv);

                recent.addLast(mv.cubeIndex);
                if (recent.size() > 3) recent.removeFirst();

                if (lastDir != -1 && lastDir != mv.dir) dirChanges++;
                lastDir = mv.dir;
                cubeTurns.put(mv.cubeIndex, cubeTurns.getOrDefault(mv.cubeIndex, 0) + 1);
            }

            if (distinctCubesUsed(seq) < minDistinctCubes) continue;
            if (dirChanges < minDirChanges) continue;
            if (!perCubeTurnsOK(cubeTurns, minTurnsPerCube)) continue;

            return seq;
        }

        // Conservative fallback: still reversible, just simpler.
        resetToSolved(world, cubes, targets);
        List<Move> fallback = new ArrayList<>();
        int steps = Math.max(minScramble, cubes.size() * 2);
        for (int i = 0; i < steps; i++) {
            int idx = RNG.nextInt(cubes.size());
            Move mv = randomLegalReversibleMove(world, cubes, idx, null);
            if (mv == null) {
                i--;
                continue;
            }
            applyMove(world, cubes, mv);
            fallback.add(mv);
        }
        return fallback;
    }

    private int tryFindAlternateCube(World w, List<P> cubes, int avoidIdx) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < cubes.size(); i++) if (i != avoidIdx) order.add(i);
        Collections.shuffle(order, RNG);
        for (int i : order) if (hasAnyReversibleMove(w, cubes, i, null)) return i;
        return -1;
    }

    private boolean hasAnyReversibleMove(World w, List<P> cubes, int idx, Set<Long> forbidTargets) {
        int x = cubes.get(idx).x, y = cubes.get(idx).y;
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs, RNG);
        for (int di : dirs) {
            Move mv = tryBuildReversibleMove(w, cubes, idx, di, x, y, forbidTargets);
            if (mv != null) return true;
        }
        return false;
    }

    private Move randomLegalReversibleMove(World w, List<P> cubes, int idx, Set<Long> forbidTargets) {
        int x = cubes.get(idx).x, y = cubes.get(idx).y;
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs, RNG);
        for (int di : dirs) {
            Move mv = tryBuildReversibleMove(w, cubes, idx, di, x, y, forbidTargets);
            if (mv != null) return mv;
        }
        return null;
    }

    private Move tryBuildReversibleMove(World w, List<P> cubes, int idx, int di, int x, int y, Set<Long> forbidTargets) {
        // Must be able to move at all.
        P dest = slideStop(w, cubes, idx, di);
        if (dest == null || (dest.x == x && dest.y == y)) return null;

        // Reversibility requirement: start cell must have an immediate stopper in the *opposite* direction.
        int odi = opposite(di);
        if (!hasImmediateStopper(w, cubes, idx, x, y, odi)) return null;

        // keep scramble neat by not ending on a target when requested.
        if (forbidTargets != null && forbidTargets.contains(key(dest.x, dest.y))) return null;

        return new Move(idx, di, x, y, dest.x, dest.y);
    }

    private boolean hasImmediateStopper(World w, List<P> cubes, int movingIdx, int x, int y, int dirIndex) {
        int dx = DIRS[dirIndex][0], dy = DIRS[dirIndex][1];
        int sx = x + dx, sy = y + dy;
        if (!inBounds(w.getW(), w.getH(), sx, sy)) return true;
        if (isWall(w, sx, sy)) return true;
        for (int i = 0; i < cubes.size(); i++) {
            if (i == movingIdx) continue;
            P p = cubes.get(i);
            if (p.x == sx && p.y == sy) return true;
        }
        return false;
    }

    private int opposite(int d) {
        return (d == 0 ? 1 : d == 1 ? 0 : d == 2 ? 3 : 2);
    }

    private void ensureOffTargetsWithReversibleMoves(World w, List<P> cubes, List<P> targets, List<Move> seq) {
        Set<Long> targetSet = new HashSet<>();
        for (P t : targets) targetSet.add(key(t.x, t.y));

        // If any cube is still on a target after scrambling, nudge it with a reversible move that
        // ends off-target and respects the same "immediate opposite stopper" rule.
        for (int i = 0; i < cubes.size(); i++) {
            P c = cubes.get(i);
            if (targetSet.contains(key(c.x, c.y))) {
                Move mv = randomLegalReversibleMove(w, cubes, i, targetSet);
                if (mv == null) {
                    // Two-step reversible detour as a fallback.
                    Move m1 = randomLegalReversibleMove(w, cubes, i, null);
                    if (m1 != null) {
                        applyMove(w, cubes, m1);
                        seq.add(m1);
                        P after = cubes.get(i);
                        if (targetSet.contains(key(after.x, after.y))) {
                            Move m2 = randomLegalReversibleMove(w, cubes, i, targetSet);
                            if (m2 != null) {
                                applyMove(w, cubes, m2);
                                seq.add(m2);
                            }
                        }
                    }
                } else {
                    applyMove(w, cubes, mv);
                    seq.add(mv);
                }
            }
        }
    }

    private List<Move> invert(List<Move> seq) {
        List<Move> out = new ArrayList<>(seq.size());
        for (int i = seq.size() - 1; i >= 0; i--) {
            Move m = seq.get(i);
            out.add(new Move(m.cubeIndex, opposite(m.dir), m.toX, m.toY, m.fromX, m.fromY));
        }
        return out;
    }

    private void printSolutionStepsAndVerify(World w, List<P> scrambledCubes, List<P> targets, List<Move> solution) {
        List<P> sim = new ArrayList<>(scrambledCubes.size());
        for (P c : scrambledCubes) sim.add(new P(c.x, c.y));

        Set<Long> targetSet = new HashSet<>();
        for (P t : targets) targetSet.add(key(t.x, t.y));

        int n = sim.size();
        int selected = 0;

        System.out.println("=== GLACIER SOLUTION (press TAB to switch cube, then arrow key) ===");
        int stepNo = 1;

        for (Move m : solution) {
            int taps = ((m.cubeIndex - selected) % n + n) % n;
            if (taps > 0) {
                System.out.printf("%02d. TAB ×%d  (select cube #%d)%n", stepNo++, taps, m.cubeIndex + 1);
                selected = (selected + taps) % n;
            }

            System.out.printf("%02d. %s%n", stepNo++, dirName(m.dir));

            P dest = slideStop(w, sim, selected, m.dir);
            if (dest == null) {
                System.out.println("    (!) No legal slide. Aborting.");
                return;
            }
            if (dest.x != m.toX || dest.y != m.toY) {
                System.out.printf("    (!) Engine stop mismatch: expected (%d,%d) but got (%d,%d).%n", m.toX, m.toY, dest.x, dest.y);
                return;
            }
            sim.set(selected, dest);
        }

        boolean solved = true;
        for (P c : sim)
            if (!targetSet.contains(key(c.x, c.y))) {
                solved = false;
                break;
            }
        System.out.println(solved ? "=== Verified: SOLVED ===" : "=== Verification failed: NOT SOLVED ===");
        System.out.flush();
    }

    private String dirName(int dir) {
        return switch (dir) {
            case 0 -> "RIGHT";
            case 1 -> "LEFT";
            case 2 -> "DOWN";
            case 3 -> "UP";
            default -> "???";
        };
    }

    private void resetToSolved(World world, List<P> cubes, List<P> targets) {
        clearCubes(world);
        cubes.clear();
        for (P t : targets) {
            world.get(t.x, t.y).thing = this.kit.box();
            cubes.add(new P(t.x, t.y));
        }
    }

    private void clearCubes(World world) {
        for (int y = 0; y < world.getH(); y++)
            for (int x = 0; x < world.getW(); x++)
                if (!world.get(x, y).isEmpty() && world.get(x, y).thing.isBox())
                    world.get(x, y).thing = Empty.instance();
    }

    private int pickCubeIndex(int n, Deque<Integer> recent) {
        List<Integer> all = new ArrayList<>();
        for (int i = 0; i < n; i++) all.add(i);
        if (RNG.nextDouble() < 0.7 && recent.size() >= 2) {
            all.removeAll(recent);
            if (!all.isEmpty()) return all.get(RNG.nextInt(all.size()));
        }
        return RNG.nextInt(n);
    }

    private boolean perCubeTurnsOK(Map<Integer, Integer> turns, int min) {
        for (int v : turns.values()) if (v < min) return false;
        return !turns.isEmpty();
    }

    private int distinctCubesUsed(List<Move> seq) {
        boolean[] used = new boolean[256];
        int count = 0;
        for (Move m : seq)
            if (!used[m.cubeIndex]) {
                used[m.cubeIndex] = true;
                count++;
            }
        return count;
    }

    private void applyMove(World w, List<P> cubes, Move mv) {
        P from = new P(mv.fromX, mv.fromY);
        P to = new P(mv.toX, mv.toY);
        Entity box = w.get(from.x, from.y).thing;
        w.get(from.x, from.y).thing = Empty.instance();
        w.get(to.x, to.y).thing = box;
        cubes.set(mv.cubeIndex, to);
    }

    private P slideStop(World w, List<P> cubes, int movingIndex, int dirIndex) {
        int W = w.getW(), H = w.getH();
        int dx = DIRS[dirIndex][0], dy = DIRS[dirIndex][1];

        boolean[][] occ = new boolean[H][W];
        for (int j = 0; j < cubes.size(); j++)
            if (j != movingIndex) {
                P p = cubes.get(j);
                occ[p.y][p.x] = true;
            }

        P start = cubes.get(movingIndex);
        int x = start.x, y = start.y;

        int nx = x + dx, ny = y + dy;
        if (!inBounds(W, H, nx, ny) || isWall(w, nx, ny) || occ[ny][nx]) return new P(x, y);
        while (true) {
            int tx = nx + dx, ty = ny + dy;
            if (!inBounds(W, H, tx, ty) || isWall(w, tx, ty) || occ[ty][tx]) return new P(nx, ny);
            nx = tx;
            ny = ty;
        }
    }

    private boolean isWall(World w, int x, int y) {
        return w.get(x, y).ground.isSolid();
    }

    private boolean inBounds(int W, int H, int x, int y) {
        return x >= 0 && x < W && y >= 0 && y < H;
    }

    private long key(int x, int y) {
        return (((long) y) << 32) ^ (x & 0xffffffffL);
    }

    private void sprinkleBumpers(boolean[][] walls, double wallDensity) {
        int H = walls.length, W = walls[0].length;
        double p = Math.max(0.0, Math.min(0.10, wallDensity * 0.4));
        for (int y = 2; y < H - 2; y++)
            for (int x = 2; x < W - 2; x++)
                if (!walls[y][x] && RNG.nextDouble() < p) walls[y][x] = true;
    }

    private List<P> pickBackstoppedTargets(boolean[][] walls, int k) {
        int H = walls.length, W = walls[0].length;
        List<P> cand = new ArrayList<>();
        for (int y = 1; y < H - 1; y++)
            for (int x = 1; x < W - 1; x++)
                if (!walls[y][x] && (walls[y - 1][x] || walls[y + 1][x] || walls[y][x - 1] || walls[y][x + 1]))
                    cand.add(new P(x, y));
        Collections.shuffle(cand, RNG);
        List<P> out = new ArrayList<>();
        for (P p : cand) {
            boolean ok = true;
            for (P q : out)
                if (Math.abs(p.x - q.x) + Math.abs(p.y - q.y) < 2) {
                    ok = false;
                    break;
                }
            if (ok) out.add(p);
            if (out.size() == k) break;
        }
        return out;
    }

    private List<P> forceTargetsAlongBorder(boolean[][] walls, int k) {
        int H = walls.length, W = walls[0].length;
        List<P> out = new ArrayList<>();
        for (int y = 1; y < H - 1 && out.size() < k; y++)
            for (int x = 1; x < W - 1 && out.size() < k; x++)
                if (!walls[y][x] && (y == 1 || y == H - 2 || x == 1 || x == W - 2)) out.add(new P(x, y));
        if (out.size() < k) for (int y = 1; y < H - 1 && out.size() < k; y++)
            for (int x = 1; x < W - 1 && out.size() < k; x++)
                if (!walls[y][x] && (walls[y - 1][x] || walls[y + 1][x] || walls[y][x - 1] || walls[y][x + 1]))
                    out.add(new P(x, y));
        if (out.size() > k) out = out.subList(0, k);
        return out;
    }

    private record StepBudget(int minSteps, int maxSteps) {
    }

    private record Move(int cubeIndex, int dir, int fromX, int fromY, int toX, int toY) {
    }

    private record P(int x, int y) {
    }
}