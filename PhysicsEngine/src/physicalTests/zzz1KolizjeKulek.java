package physicalTests;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.Ball;

public class zzz1KolizjeKulek extends Application {
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

        Box box = new Box(150, 5, 130);

        group.getChildren().addAll(box);

        Ball b1 = new Ball(0,-40,0,5,5);
        Ball b2 = new Ball(5.1,-20,0,5,5);
        Ball b3 = new Ball(-5.1,-20,0,5,5);

        b1.vy = 0.1;
        group.getChildren().addAll(b1,b2,b3);

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-200);

        scene = new Scene(group, 400, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GREEN);
        scene.setCamera(camera);

        mouseEvents();
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // gravity

                //b1.vy += g;
        //        b2.vy += g;

                b1.addVtoXYZ();
                b2.addVtoXYZ();
                b3.addVtoXYZ();

                // c1 vs c2
                CollisionsOld.Balls.serveCollisions(b1,b2);
                CollisionsOld.Balls.serveCollisions(b1,b3);
                CollisionsOld.Balls.serveCollisions(b2,b3);

                // platforma vs c1

                if (b1.getBoundsInParent().intersects(box.getBoundsInParent())) {

                    b1.y.set(-2.5 - b1.r.get());

                    //b1.vx *= -1;
                    b1.vy *= -1;
                    //b1.vz *= -1;

                    b1.addVtoXYZ();
                }

                // platforma vs c2
                if (b2.getBoundsInParent().intersects(box.getBoundsInParent())) {

                    System.out.println("b2");
                    b2.y.set(-2.5 - b2.r.get());

                    //b2.vx *= -1;
                    b2.vy *= -1;
                    //b2.vz *= -1;

                    b2.addVtoXYZ();
                }


                if (b3.getBoundsInParent().intersects(box.getBoundsInParent())) {

                    System.out.println("b3");


                    b3.y.set(-2.5 - b3.r.get());

                    //b2.vx *= -1;
                    b3.vy *= -1;
                    //b2.vz *= -1;

                    b3.addVtoXYZ();
                }

            }
        };

        timer.start();
        primaryStage.setMaximized(true);
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
