package physicalTests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import physic.*;

public class zzz13SilnikiBezExtends extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.001;
    //SilnikPrzesuwny sp13, sp23;
    RotaryMotor so1,so2;
    LinearMotor sp1,sp2;
    boolean Xpressed = false, Zpressed = false, Cpressed = false, Vpressed = false;
    boolean Apressed = false, Spressed = false, Dpressed = false, Fpressed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

        Ball sj1 = new Ball(-30, -20, 0, 3, 5);
        Ball ej1 = new Ball(-30, 0, 0, 3, 5);

        Joint j1 = new Joint(sj1, ej1, 2);
        group.getChildren().addAll(sj1, ej1, j1);

        Ball sj2 = new Ball(30, -20, 0, 3, 5);
        Ball ej2 = new Ball(30, 0, 0, 3, 5);

        Joint j2 = new Joint(sj2, ej2, 2);
        group.getChildren().addAll(sj2, ej2, j2);

        Ball bEngine1 = new Ball(-10, -10, 0, 3, 5);
        Ball bEngine2 = new Ball(10, -10, 0, 3, 5);

        Joint je = new Joint(bEngine1,bEngine2,2);
        Joint je1 = new Joint(ej1,bEngine1,2);
        Joint je2 = new Joint(ej2,bEngine2,2);

        group.getChildren().addAll(bEngine1,bEngine2,je,je1,je2);

        Chain c = new Chain(j1, j2, je,je1,je2);

        List<Ball> ballList1 = new ArrayList<>();
        List<Ball> ballList2 = new ArrayList<>();

        ballList1.clear();
        ballList2.clear();

        ballList1.add(sj1);
        ballList1.add(ej1);

        ballList2.add(bEngine1);
        ballList2.add(bEngine2);
        ballList2.add(sj2);
        ballList2.add(ej2);

        sp1 = new LinearMotor(je1,c,new ArrayList<>(ballList1),ej1,new ArrayList<>(ballList2),bEngine1,0.1);

        ballList1.clear();
        ballList2.clear();

        ballList1.add(sj2);
        ballList1.add(ej2);

        ballList2.add(bEngine1);
        ballList2.add(bEngine2);
        ballList2.add(sj1);
        ballList2.add(ej1);

        sp2 = new LinearMotor(je2,c,new ArrayList<>(ballList1),ej2,new ArrayList<>(ballList2),bEngine2,0.1);


        ballList1.clear();
        ballList2.clear();

        ballList1.add(sj1);
        ballList1.add(ej1);

        ballList2.add(bEngine2);
        ballList2.add(sj2);
        ballList2.add(ej2);

        so1 = new RotaryMotor(bEngine1,je1,je,c,new ArrayList<>(ballList1),ej1,new ArrayList<>(ballList2),bEngine2,0.01);

        ballList1.clear();
        ballList2.clear();

        ballList1.add(sj2);
        ballList1.add(ej2);

        ballList2.add(bEngine1);
        ballList2.add(sj1);
        ballList2.add(ej1);

        so2 = new RotaryMotor(bEngine2,je2,je,c,new ArrayList<>(ballList1),ej2,new ArrayList<>(ballList2),bEngine1,0.01);


        //c.V = new Point3D(-0.1, 0, 0);
        //c.W = new Point3D(0,0,0.01);
        //c3.V = new Point3D(0, 0, 0);
////////////////////////////////


         //c.W = new Point3D(0,-0.01,0);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //System.out.println(c.W);
                //Point3D[] punkty = Collisions.Chains2Joints.serveCollision(c2,c1);
//
//                c1.setBallsVCausedByJoint();
//                c2.setBallsVCausedByJoint();

                c.setBallsVCausedByChain();

                if (Zpressed) {
                    sp1.moveInside();
                }
                if (Xpressed) {
                    sp1.moveOutside();
                }
                if (Cpressed) {
                    sp2.moveInside();
                }
                if (Vpressed) {
                    sp2.moveOutside();
                }

                if (Apressed) {
                    so1.moveInside();
                }
                if (Spressed) {
                    so1.moveOutside();
                }
                if (Dpressed) {
                    so2.moveInside();
                }
                if (Fpressed) {
                    so2.moveOutside();
                }
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
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
