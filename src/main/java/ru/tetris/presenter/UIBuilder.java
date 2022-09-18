package ru.tetris.presenter;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import ru.tetris.controller.LogicController;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public final class UIBuilder implements Presenter{

    private final DrawerFX drawer;
    private Scene scene;

    private LogicController logicController;


    public UIBuilder() {
        this.drawer = new DrawerFX();
    }


    public void setLogicController(LogicController logicController) {
        this.logicController = logicController;
    }

    @Override
    public void initialize(int widthCellsCount, int heightCellsCount, int minScaleCoefficient) {
        if (scene == null) {

            int minCanvasWidth = widthCellsCount * minScaleCoefficient;
            int minCanvasHeight = heightCellsCount * minScaleCoefficient;

            Pane controlsPanel = buildControlsPane();

            Canvas canvas = buildCanvas(minCanvasWidth, minCanvasHeight);
            Pane root = createRootPane(canvas, controlsPanel);

            this.scene = new Scene(root);
            this.drawer.setCanvas(canvas);
            this.drawer.setWidthCellsCount(widthCellsCount);
            this.drawer.setHeightCellsCount(heightCellsCount);

            final double hwRatio = (double) minCanvasHeight / (double) minCanvasWidth;

            this.scene.heightProperty().addListener((observableValue, number, newNumber) -> {
                canvas.setWidth((int) (newNumber.doubleValue() / hwRatio));
                canvas.setHeight(newNumber.intValue());
                drawer.drawBackground();
            });

            scene.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (KeyCode.LEFT == keyEvent.getCode()) {
                    logicController.moveLeft();
                    keyEvent.consume();
                } else if (KeyCode.RIGHT == keyEvent.getCode()) {
                    logicController.moveRight();
                    keyEvent.consume();
                }

            });
        }
    }

    public DrawerFX getDrawer() {
        return drawer;
    }


    public Scene getScene() {
        return scene;
    }

    private Pane createRootPane(Node drawingPane, Pane controlsPane) {
        HBox root = new HBox(10, drawingPane, controlsPane);
        root.setAlignment(Pos.TOP_CENTER);
        root.requestFocus();

        return root;
    }


    private Canvas buildCanvas(int minCanvasWidth, int minCanvasHeight) {
        Canvas canvas = new Canvas(minCanvasWidth,minCanvasHeight);
        canvas.minWidth(minCanvasWidth);
        canvas.minHeight(minCanvasHeight);
        return canvas;
    }

    private Pane buildControlsPane() {
        AnchorPane pane = new AnchorPane();

        Label label = new Label("Привет");

        Canvas miniCanvas = new Canvas(50,50);
        GraphicsContext gc = miniCanvas.getGraphicsContext2D();
        gc.setFill(Color.AQUA);
        gc.fillRect(0,0,50,50);
        gc.setFill(Color.RED);
        gc.fillOval(0,0,50,50);

        VBox controlsContainer = new VBox(10);

        AnchorPane.setTopAnchor(controlsContainer, 0.);
        AnchorPane.setLeftAnchor(controlsContainer, 10.);

        HBox topPanel = new HBox(10, label, miniCanvas);

        Button startButton = new Button("Старт");
        Button stopButton = new Button("Стоп");
        Button pauseButton = new Button("Пауза");

        startButton.setOnMouseClicked(mouseEvent -> {
//            logicController.start(60, 0.1);
//            logicController.start(10, 1);
            logicController.start();
        });

        stopButton.setOnMouseClicked(mouseEvent -> {
            logicController.stop();
            drawer.drawBackground();
        });

        pauseButton.setOnMouseClicked(mouseEvent -> {
            logicController.pause();
        });

        HBox buttons = new HBox(10, startButton, stopButton, pauseButton);

        controlsContainer.getChildren().addAll(topPanel, buttons);
        pane.getChildren().add(controlsContainer);

        return pane;

    }



}
