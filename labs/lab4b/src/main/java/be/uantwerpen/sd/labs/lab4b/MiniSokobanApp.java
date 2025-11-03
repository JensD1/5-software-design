package be.uantwerpen.sd.labs.lab4b;

import be.uantwerpen.sd.labs.lab4b.config.AppConfig;
import be.uantwerpen.sd.labs.lab4b.level.LevelKit;
import be.uantwerpen.sd.labs.lab4b.level.glacier.GlacierKit;
import be.uantwerpen.sd.labs.lab4b.level.warehouse.WarehouseKit;
import be.uantwerpen.sd.labs.lab4b.logic.DifficultyModel;
import be.uantwerpen.sd.labs.lab4b.model.World;
import be.uantwerpen.sd.labs.lab4b.ui.GridRenderer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayDeque;

public class MiniSokobanApp extends Application {
    private final AppConfig CFG = AppConfig.get();
    private final ArrayDeque<World> undo = new ArrayDeque<>();
    private LevelKit kit = WarehouseKit.createLevelKit();
    private int levelNumber = Math.max(1, CFG.startLevel);
    private World world;
    private Canvas canvas;
    private Stage stage;
    private GridRenderer renderer;
    private javafx.scene.control.Label lblLeft, lblRight;
    private VBox helpOverlay;
    private StackPane centerStack;
    private VBox completeOverlay;
    private boolean levelComplete = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // Renderer + world
        renderer = new GridRenderer(kit.palette(), kit.hints());
        buildNewLevel();

        // Canvas
        canvas = new Canvas(world.w * CFG.tilePx + CFG.paddingPx * 2, world.h * CFG.tilePx + CFG.paddingPx * 2);

        // HELP OVERLAY (hidden by default)
        helpOverlay = new VBox(8);
        helpOverlay.setPadding(new Insets(16));
        helpOverlay.setAlignment(Pos.CENTER_LEFT);
        helpOverlay.setMaxWidth(520);
        helpOverlay.setVisible(false);
        helpOverlay.setStyle("-fx-background-color: rgba(20,20,20,0.85);" + "-fx-background-radius: 12;" + "-fx-border-radius: 12;" + "-fx-border-color: rgba(255,255,255,0.25);" + "-fx-border-width: 1;");

        Label title = new Label("Sokoban — Help");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        String commonStyle = "-fx-text-fill: white; -fx-font-size: 14px;";
        helpOverlay.getChildren().addAll(title, new Label("• Move: ← ↑ → ↓"),      // style them all:
                new Label("• Undo: U"), new Label("• Restart: R"), new Label("• Next level: N or Space"), new Label("• Switch theme: 1 (warehouse), 2 (glacier)"), new Label("• Glacier levels: press Tab ( ⇥ ) to switch ice cube"), new Label("• Exit: Esc"), new Label(" "), new Label("Press H to close this menu."));
        helpOverlay.getChildren().stream().filter(n -> n instanceof Label && n != title).forEach(n -> n.setStyle(commonStyle));

        centerStack = new StackPane(canvas);
        StackPane.setAlignment(helpOverlay, Pos.CENTER);
        centerStack.getChildren().add(helpOverlay);

        // COMPLETION OVERLAY (hidden by default)
        completeOverlay = new VBox(10);
        completeOverlay.setPadding(new Insets(16));
        completeOverlay.setAlignment(Pos.CENTER);
        completeOverlay.setMaxWidth(420);
        completeOverlay.setVisible(false);
        completeOverlay.setStyle("-fx-background-color: rgba(20,20,20,0.65);" + "-fx-background-radius: 12;" + "-fx-border-radius: 12;" + "-fx-border-color: rgba(255,255,255,0.25);" + "-fx-border-width: 1;");
        Label doneTitle = new Label("Level " + levelNumber + " completed!");
        doneTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        Label doneBody = new Label("Press N or Space to go to the next level");
        doneBody.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        completeOverlay.getChildren().addAll(doneTitle, doneBody);

        // Put overlays on top of the canvas
        centerStack = new StackPane(canvas);
        StackPane.setAlignment(helpOverlay, Pos.CENTER_LEFT);
        StackPane.setAlignment(completeOverlay, Pos.CENTER);
        centerStack.getChildren().addAll(helpOverlay, completeOverlay);


        // bottom status (compact)
        lblLeft = new Label();   // "Targets: X/Y"
        lblRight = new Label();   // "help: H"
        HBox status = new HBox(12, lblLeft, new Region(), lblRight);
        HBox.setHgrow(status.getChildren().get(1), Priority.ALWAYS);
        status.setPadding(new Insets(6, 12, 6, 12));
        status.setStyle("-fx-background-color: " + CFG.statusBg + ";");

        // root
        BorderPane root = new BorderPane();
        root.setCenter(centerStack);
        root.setBottom(status);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Arrow keys always go to the game
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        canvas.setOnMouseClicked(e -> canvas.requestFocus());
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, e -> {
            switch (e.getCode()) {
                case UP -> {
                    onMove(0, -1);
                    e.consume();
                }
                case DOWN -> {
                    onMove(0, 1);
                    e.consume();
                }
                case LEFT -> {
                    onMove(-1, 0);
                    e.consume();
                }
                case RIGHT -> {
                    onMove(1, 0);
                    e.consume();
                }
                case U -> {
                    if (!undo.isEmpty()) {
                        world = undo.pop();
                        draw();
                    }
                    e.consume();
                }
                case R -> {
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case N, SPACE -> {
                    if (levelComplete) {
                        nextLevel();
                        levelComplete = false;
                        completeOverlay.setVisible(false);
                        e.consume();
                        break;
                    }
                    nextLevel();
                    e.consume();
                }
                case ESCAPE, Q -> {
                    stage.close();
                    e.consume();
                }
                case DIGIT1 -> {
                    kit = WarehouseKit.createLevelKit();
                    renderer = new GridRenderer(kit.palette(), kit.hints());
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case DIGIT2 -> {
                    kit = GlacierKit.createLevelKit();
                    renderer = new GridRenderer(kit.palette(), kit.hints());
                    buildNewLevel();
                    resizeCanvasIfNeeded();
                    undo.clear();
                    draw();
                    e.consume();
                }
                case H -> {
                    helpOverlay.setVisible(!helpOverlay.isVisible());
                    e.consume();
                }
                case TAB -> {
                    if (kit.movement().nextSelectable(world)) draw();
                    e.consume();
                }
                default -> {
                }
            }
        });

        draw();
    }

    private void nextLevel() {
        if (levelNumber < CFG.maxLevel) levelNumber++;
        buildNewLevel();
        resizeCanvasIfNeeded();
        undo.clear();
        levelComplete = false;
        if (completeOverlay != null) completeOverlay.setVisible(false);
        draw();
    }

    private void buildNewLevel() {
        var p = DifficultyModel.paramsForLevel(levelNumber, kit.level());

        world = kit.generator().generate(p.w(), p.h(), p.crates(), p.wallDensity(), p.seed(), p.minPulls(), p.maxPulls(), kit);
        if (completeOverlay != null) completeOverlay.setVisible(false);
        levelComplete = false;

        setWindowTitle();
    }

    private void onMove(int dx, int dy) {
        World snapshot = world.copy();
        if (kit.movement().move(world, dx, dy)) {
            undo.push(snapshot);
            draw();
            int covered = world.coveredTargets(kit.coverage());
            int total = world.totalTargets();
            if (covered == total) {
                levelComplete = true;
                ((Label) completeOverlay.getChildren().get(0)).setText("Level " + levelNumber + " completed!");
                completeOverlay.setVisible(true);
            }
        }
    }

    private void draw() {
        renderer.draw(canvas.getGraphicsContext2D(), world);
        if (lblLeft != null)
            lblLeft.setText("Targets: " + world.coveredTargets(kit.coverage()) + "/" + world.totalTargets());
        if (lblRight != null) lblRight.setText("help: H");
    }

    private void resizeCanvasIfNeeded() {
        int wantW = world.w * CFG.tilePx + CFG.paddingPx * 2;
        int wantH = world.h * CFG.tilePx + CFG.paddingPx * 2;
        if ((int) canvas.getWidth() != wantW || (int) canvas.getHeight() != wantH) {
            canvas.setWidth(wantW);
            canvas.setHeight(wantH);
            if (stage != null) stage.sizeToScene();
        }
    }

    private void setWindowTitle() {
        if (stage != null) stage.setTitle("Sokoban — " + kit.name() + "  L" + levelNumber);
    }
}
