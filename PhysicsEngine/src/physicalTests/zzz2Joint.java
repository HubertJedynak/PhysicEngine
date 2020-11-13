package physicalTests;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.*;

public class zzz2Joint extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.001;


    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

        Ball s1 = new Ball(0,0,0,5,5);
        Ball s2 = new Ball(-50,20,30,5,5);
        Ball s3 = new Ball(30,10,20,5,5);


        Joint joint = new Joint(s1,s2,1);
        Joint joint1 = new Joint(s1,s3,4);
        group.getChildren().addAll(s1,s2,s3,joint,joint1);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                s2.addToY(-0.1);
                s2.addToX(-0.1);
                s2.addToZ(-0.1);

                s1.addToY(0.1);
                s1.addToX(0.1);
                s1.addToZ(0.1);
            }
        };
        //timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-100);

        scene = new Scene(group, 400, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GREEN);
        scene.setCamera(camera);

        mouseEvents();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mouseEvents() {
        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);

        group.getTransforms().addAll(xRotate, yRotate);

        scene.setOnMousePressed(event -> {
            x0 = event.getSceneX();
            y0 = event.getSceneY();
            ax0 = xRotate.getAngle();
            ay0 = yRotate.getAngle();
        });

        scene.setOnMouseDragged(event -> {
            xRotate.setAngle(ax0 + event.getSceneY() - y0);
            yRotate.setAngle(ay0 - (event.getSceneX() - x0));
        });

        scene.setOnScroll(event -> {
            double scroll = event.getDeltaY();
            camera.setTranslateZ(camera.getTranslateZ() + scroll);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
