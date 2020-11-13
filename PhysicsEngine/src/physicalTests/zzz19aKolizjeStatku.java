
package physicalTests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.*;

import java.util.LinkedList;
import java.util.List;

public class zzz19aKolizjeStatku extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.001;
    //SilnikPrzesuwny sp13, sp23;
    RotaryMotor so1, so2;
    LinearMotor sp1, sp2;
    JetEngine sr1a, sr2a, sr1b, sr2b;
    ChwytakStary ch;

    List<Chain> chainsList = new LinkedList<>();

    boolean Xpressed = false, Zpressed = false, Cpressed = false, Vpressed = false;
    boolean Apressed = false, Spressed = false, Dpressed = false, Fpressed = false;
    boolean Epressed = false, Rpressed = false;
    boolean TPressed = false, YPressed = false, Gpressed = false, Hpressed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();


//        Ball sj1 = new Ball(2+20, -10, 13, 2, 2.50);
//        Ball ej1 = new Ball(2+20, -10, -13, 2, 2.50);
//        Ball sj1 = new Ball(20, -10, 10, 3, 3.5);
//        Ball ej1 = new Ball(20, -10, -10, 3, 3.5);
//        Ball sj1 = new Ball(25, -10, 0, 3, 3.5);
//        Ball ej1 = new Ball(15, -10, 0, 3, 3.5);
//        Ball sj1 = new Ball(25, 15, 0, 3, 3.5);
//        Ball ej1 = new Ball(25, 5, 0, 3, 3.5);
        Ball sj1 = new Ball(20, 10, 10, 3, 3.5);
        Ball ej1 = new Ball(20, 10, -10, 3, 3.5);

        Joint j1 = new Joint(sj1, ej1, 3);

        Chain c1 = new Chain(j1);
        group.getChildren().addAll(sj1, ej1, j1);
        //c1.W = new Point3D(0.1, 0.1, 0.1);

//        Ball sj2 = new Ball(-2+13+20, 1, 0, 2, 5);
//        Ball ej2 = new Ball(-2-13+20, 1, 0, 2, 5);
        Ball ej2 = new Ball(-20, 50, 0, 2, 5);
        Ball sj2 = new Ball(-20, -10, 0, 10, 5);

        //        Ball sj2 = new Ball(-5-20, 10, -5, 2, 5);
//        Ball ej2 = new Ball(-5-20, -15, -5, 2, 5);

        Joint j2 = new Joint(sj2, ej2, 2);
        Chain c2 = new Chain(j2);
        group.getChildren().addAll(sj2, ej2, j2);
        //c1.W = new Point3D(0, 1, 0);
        c1.V = new Point3D(-1, 0, 0);

        chainsList.add(c1);
        chainsList.add(c2);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                for (Chain c : chainsList) {
                    c.setBallsVCausedByChain();
                }

                CollisionsOld.serveAllCollisions(chainsList);

            }
        };
        timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-400);
//        camera.setTranslateX(1000);
//        camera.setTranslateY(1000);


        scene = new Scene(group, 400, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GREEN);
        scene.setCamera(camera);

        mouseEvents();
        keyEvents();

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

    private void keyEvents() {
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.W)) {
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            } else if (event.getCode().equals(KeyCode.Q)) {
                camera.setTranslateZ(camera.getTranslateZ() + 10);
            } else if (event.getCode().equals(KeyCode.X)) {
                Xpressed = true;
            } else if (event.getCode().equals(KeyCode.Z)) {
                Zpressed = true;
            } else if (event.getCode().equals(KeyCode.C)) {
                Cpressed = true;
            } else if (event.getCode().equals(KeyCode.V)) {
                Vpressed = true;
            } else if (event.getCode().equals(KeyCode.A)) {
                Apressed = true;
            } else if (event.getCode().equals(KeyCode.S)) {
                Spressed = true;
            } else if (event.getCode().equals(KeyCode.D)) {
                Dpressed = true;
            } else if (event.getCode().equals(KeyCode.F)) {
                Fpressed = true;
            } else if (event.getCode().equals(KeyCode.E)) {
                Epressed = true;
            } else if (event.getCode().equals(KeyCode.R)) {
                Rpressed = true;
            } else if (event.getCode().equals(KeyCode.T)) {
                TPressed = true;
            } else if (event.getCode().equals(KeyCode.Y)) {
                YPressed = true;
            } else if (event.getCode().equals(KeyCode.G)) {
                Gpressed = true;
            } else if (event.getCode().equals(KeyCode.H)) {
                Hpressed = true;
            }


        });

        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.X)) {
                Xpressed = false;
            } else if (event.getCode().equals(KeyCode.Z)) {
                Zpressed = false;
            } else if (event.getCode().equals(KeyCode.C)) {
                Cpressed = false;
            } else if (event.getCode().equals(KeyCode.V)) {
                Vpressed = false;
            } else if (event.getCode().equals(KeyCode.A)) {
                Apressed = false;
            } else if (event.getCode().equals(KeyCode.S)) {
                Spressed = false;
            } else if (event.getCode().equals(KeyCode.D)) {
                Dpressed = false;
            } else if (event.getCode().equals(KeyCode.F)) {
                Fpressed = false;
            } else if (event.getCode().equals(KeyCode.E)) {
                Epressed = false;
            } else if (event.getCode().equals(KeyCode.R)) {
                Rpressed = false;
            } else if (event.getCode().equals(KeyCode.T)) {
                TPressed = false;
            } else if (event.getCode().equals(KeyCode.Y)) {
                YPressed = false;
            } else if (event.getCode().equals(KeyCode.G)) {
                Gpressed = false;
            } else if (event.getCode().equals(KeyCode.H)) {
                Hpressed = false;
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
