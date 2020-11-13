package core;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    Group groupBackground = new Group();
    Group group;
    Scene scene;

    SimulationLoop simulationLoop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        groupBackground = new Group();
        group = new Group();

        groupBackground.getChildren().add(group);
        scene = new Scene(groupBackground, 400, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        simulationLoop = new SimulationLoop(scene, group, groupBackground);
        simulationLoop.startLoop();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
