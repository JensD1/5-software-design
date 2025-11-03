package be.uantwerpen.sd.labs.lab4b.level;

import be.uantwerpen.sd.labs.lab4b.gen.WorldGenerator;
import be.uantwerpen.sd.labs.lab4b.logic.CoveragePolicy;
import be.uantwerpen.sd.labs.lab4b.logic.MovementStrategy;
import be.uantwerpen.sd.labs.lab4b.model.domain.Box;
import be.uantwerpen.sd.labs.lab4b.model.domain.GroundTile;
import be.uantwerpen.sd.labs.lab4b.model.domain.Player;
/*
TODO: Implement the Abstract Factory Pattern here.
*/
public class LevelKit {

    public String name() {
        return getClass().getSimpleName().replace("Kit", "");
    }

    public interface RendererHints {
        boolean showSelectionOverlay();
    }

    public Palette palette() {
        return PaletteDirector.construct(paletteBuilder());
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public MovementStrategy movement() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public WorldGenerator generator() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public RendererHints hints() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public Level level() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public GroundTile floor() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public GroundTile wall() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public GroundTile target() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public Box box() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public Player player() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public CoveragePolicy coverage() {
        return null;
    }

    /*
		TODO: Something about this method declaration needs to be changed.
		TIP: What type of class should this be?
	*/
    public PaletteBuilder paletteBuilder() {
        return null;
    }

}
