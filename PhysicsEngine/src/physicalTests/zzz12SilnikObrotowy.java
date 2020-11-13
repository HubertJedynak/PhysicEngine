
package physicalTests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.Ball;
import physic.Chain;
import physic.Joint;

import java.util.ArrayList;
import java.util.List;

public class zzz12SilnikObrotowy extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.001;
    //SilnikPrzesuwny sp13, sp23;
    SilnikObrotowyStary so;
    boolean Xpressed = false, Zpressed = false, Cpressed = false, Vpressed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

        Ball sj1 = new Ball(-20, -20, 0, 3, 5);
        Ball ej1 = new Ball(-20, 0, 0, 3, 5);

        Joint j1 = new Joint(sj1, ej1, 2);
        group.getChildren().addAll(sj1, ej1, j1);

        Ball sj2 = new Ball(20, -20, 0, 3, 5);
        Ball ej2 = new Ball(20, 0, 0, 3, 5);

        Joint j2 = new Joint(sj2, ej2, 2);
        group.getChildren().addAll(sj2, ej2, j2);

//        Ball sj3 = new Ball(0, -20, 0, 3, 5);
//        Ball ej3 = new Ball(0, 0, 0, 3, 5);
//
//        Joint j3 = new Joint(sj3, ej3, 2);
//        group.getChildren().addAll(sj3, ej3, j3);

        Chain c = new Chain(j1, j2);

        List<Ball> ballList1 = new ArrayList<>();
        ballList1.add(sj1);
        ballList1.add(ej1);

        List<Ball> ballList2 = new ArrayList<>();
        ballList2.add(sj2);
        ballList2.add(ej2);

//        List<Ball> ballList23 = new ArrayList<>();
//        ballList23.add(sj3);
//        ballList23.add(ej3);
//        ballList23.add(sj2);
//        ballList23.add(ej2);
//
//
//        List<Ball> ballList13 = new ArrayList<>();
//        ballList13.add(sj3);
//        ballList13.add(ej3);
//        ballList13.add(sj1);
//        ballList13.add(ej1);

//        sp = new SilnikPrzesuwny(c, ballList1, ej1, ballList2, ej2, 2, 0.1);
//        group.getChildren().add(sp);


        //c.V = new Point3D(-0.1, 0, 0);
        //c3.V = new Point3D(0, 0, 0);

//        sp13 = new SilnikPrzesuwny(c, ballList1, ej1, ballList23, ej3, 2, 0.1);
//        group.getChildren().add(sp13);
//
//        sp23 = new SilnikPrzesuwny(c, ballList2, ej2, ballList13, ej3, 2, 0.1);
//        group.getChildren().add(sp23);


        Ball b = new Ball(0, -10, 0, 3, 5);
        Joint jso1 = new Joint(b,ej1,2);
        Joint jso2 = new Joint(b,ej2,2);
        so = new SilnikObrotowyStary(c,jso1,jso2,ballList1,ej1,ballList2,ej2,b,0.1);
        group.getChildren().add(so);
        // UWAGA: TRZEBA NA RAZIE DODAWAC JOINTY OD SILNIKA OSOBNO
        group.getChildren().add(jso1);
        group.getChildren().add(jso2);


       // c.W = new Point3D(0,-0.01,0);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                System.out.println("kulka engina "+b.y.get());
//                System.out.println("kulka jointa "+jso1.b1.y.get());
//                System.out.println("sfera "+jso1.getTranslateX());
//                Point3D[] punkty = Collisions.Chains2Joints.serveCollision(c2,c1);
//
//                c1.setBallsVCausedByJoint();
//                c2.setBallsVCausedByJoint();

                c.setBallsVCausedByChain();

                if (Xpressed) {
                    so.moveInside();
                }
                if (Zpressed) {
                    so.moveOutside();
                }
//                if (Vpressed) {
//                    sp23.moveOutside();
//                }
//                if (Cpressed) {
//                    sp23.moveInside();
//                }
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
            if (event.getCode().equals(KeyCode.S)) {
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            } else if (event.getCode().equals(KeyCode.W)) {
                camera.setTranslateZ(camera.getTranslateZ() + 10);
            } else if (event.getCode().equals(KeyCode.X)) {
                Xpressed = true;
            } else if (event.getCode().equals(KeyCode.Z)) {
                Zpressed = true;
            } else if (event.getCode().equals(KeyCode.C)) {
                Cpressed = true;
            } else if (event.getCode().equals(KeyCode.V)) {
                Vpressed = true;
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
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
