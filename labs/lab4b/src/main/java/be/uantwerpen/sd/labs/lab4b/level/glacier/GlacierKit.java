package be.uantwerpen.sd.labs.lab4b.level.glacier;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.gen.GlacierWorldGenerator;
import be.uantwerpen.sd.labs.lab4b.gen.WorldGenerator;
import be.uantwerpen.sd.labs.lab4b.level.Level;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.level.PaletteBuilder;
import be.uantwerpen.sd.labs.lab4b.level.ThemedPaletteBuilder;
import be.uantwerpen.sd.labs.lab4b.logic.CoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.logic.glacier.GlacierCoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.glacier.GlacierMovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.domain.Box;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
import be.uantwerpen.sd.labs.lab4b.model.domain.glacier.*;
public final class GlacierKit extends LevelKit {

    @Override
    public RendererHints hints() {
        return () -> true;
    }

    private GlacierKit() {
    }

    public static LevelKit createLevelKit() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        static final LevelKit INSTANCE = new GlacierKit();
    }

    public MovementStrategy movement() {
        /*
            TODO: Return the correct MovementStrategy.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public WorldGenerator generator() {
        /*
            TODO: Return the correct WorldGenerator.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public Level level() {
        /*
            TODO: Return the correct Level.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public GroundTile floor() {
        /*
            TODO: Return the correct Floor.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public GroundTile wall() {
        /*
            TODO: Return the correct Wall.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public GroundTile target() {
        /*
            TODO: Return the correct Target.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public Box box() {
        /*
            TODO: Return the correct Box.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public Player player() {
        /*
            TODO: Return the correct Player.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public CoveragePolicy coverage() {
        /*
            TODO: Return the correct CoveragePolicy.
            TIP: Ensure that this abstract factory creates Glacier Objects.
        */
        return null;
    }

    public PaletteBuilder paletteBuilder() {
        /*
            TODO: Return the correct PaletteBuilder.
            TIP: The glacier theme can be obtained using 'AppConfig.get().themeGlacier'
        */
        return null;
    }
}
