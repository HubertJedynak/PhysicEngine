
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class zzz18KolizjeChainowBasen extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.01;
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

        double r = 5;
        double M = 1000000;
        double m = 5;
        double n = 10;

        List<Ball> aqBalls = new ArrayList<>();
        List<Joint> aqJoints = new ArrayList<>();

        Ball b1 = new Ball(-70, -20, 0, r, M);
        Ball b2 = new Ball(0, 10, 0, r, M);
        Ball b3 = new Ball(70, -20, 0, r, M);
        aqBalls.add(b1);
        aqBalls.add(b2);
        aqBalls.add(b3);

        Joint j12 = new Joint(b1, b2, r);
        Joint j23 = new Joint(b2, b3, r);
        aqJoints.add(j12);
        aqJoints.add(j23);

        for (int i = 1; i < n; i++) {
            b1 = new Ball(-70, -20, 2 * i * r, r, M);
            b2 = new Ball(0, 10, 2 * i * r, r, M);
            b3 = new Ball(70, -20, 2 * i * r, r, M);
            aqBalls.add(b1);
            aqBalls.add(b2);
            aqBalls.add(b3);

            j12 = new Joint(b1, b2, r);
            j23 = new Joint(b2, b3, r);
            Joint jLaczacy = new Joint(aqBalls.get(1), b2, r);
            aqJoints.add(j12);
            aqJoints.add(j23);
            aqJoints.add(jLaczacy);

        }

        Chain cAquarium = new Chain(aqJoints);
        chainsList.add(cAquarium);

        group.getChildren().addAll(aqBalls);
        group.getChildren().addAll(aqJoints);

        List<Chain> smallChains = new ArrayList<>();
        for (int i = 1; i < n / 2; i++) {
            for (int ix = 1; ix < 7; ix++) {
                Ball sj1 = new Ball(-15 * ix, -50, 4 * i * r - r, r, m);
                Ball ej1 = new Ball(-15 * ix, -50 - r, 4 * i * r - r, r, m);
                Joint j1 = new Joint(sj1, ej1, 2);
                Chain c1 = new Chain(j1);
                smallChains.add(c1);
                chainsList.add(c1);
                group.getChildren().addAll(sj1, ej1, j1);

                Ball sj2 = new Ball(15 * ix, -50, 4 * i * r - r, r, m);
                Ball ej2 = new Ball(15 * ix, -50 - 4*r, 4 * i * r - r, r, m);
                Joint j2 = new Joint(sj2, ej2, 2);
                Chain c2 = new Chain(j2);
                smallChains.add(c2);
                chainsList.add(c2);
                group.getChildren().addAll(sj2, ej2, j2);
            }

        }

        System.out.println("chainlist size "+chainsList.size());
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                for(Chain c: smallChains){
                    c.V = c.V.add(new Point3D(0,g,0));
                }

                for (Chain c : chainsList) {
                    c.setBallsVCausedByChain();
                }

                Collisions2stary.serveAllCollisions(chainsList);
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
