package ru.tetris.main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.tetris.controller.LogicController;
import ru.tetris.controller.UIController;
import ru.tetris.logic.GameLogic;
import ru.tetris.presenter.UIBuilder;

public class Main extends Application {

    public static void main(String[] args)  {
        System.out.println("Hello?");
        Application.launch(args);
    }


    @Override
    public void start(Stage primaryStage){

        System.out.println("start");
        UIBuilder sceneBuilder = new UIBuilder();

        UIController uiController = new UIController(sceneBuilder, sceneBuilder.getDrawer());
        GameLogic gameLogic = new GameLogic(uiController,10,15, 100, 0.1);

        //напрямую
//        logicController.start(60, 0.1);
//        logicController.start(10, 1);
//        sceneBuilder.initialize(10,25, 10);

        LogicController logicController = new LogicController(gameLogic);
        sceneBuilder.setLogicController(logicController);

        Scene primaryScene = sceneBuilder.getScene();

        if (primaryScene == null) {
            throw new NullPointerException("PrimaryScene is empty, haven't initialize in Logic");
        }

        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        primaryStage.setScene(primaryScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });

    }
}
