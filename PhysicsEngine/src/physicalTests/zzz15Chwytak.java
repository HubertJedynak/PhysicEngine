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

public class zzz15Chwytak extends Application {
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
    ChwytakStary ch;

    List<Chain> chainsList = new LinkedList<>();

    boolean Xpressed = false, Zpressed = false, Cpressed = false, Vpressed = false;
    boolean Apressed = false, Spressed = false, Dpressed = false, Fpressed = false;
    boolean Epressed = false, Rpressed = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

        Ball sj1 = new Ball(-30, -20, 0, 3, 5);
        Ball ej1 = new Ball(-30, -0, 0, 3, 5);

        Joint j1 = new Joint(sj1, ej1, 2);
        group.getChildren().addAll(sj1, ej1, j1);

        Ball sj2 = new Ball(30, -20, 0, 3, 500);
        Ball ej2 = new Ball(30, 0, 0, 3, 500);

        Joint j2 = new Joint(sj2, ej2, 2);
        group.getChildren().addAll(sj2, ej2, j2);

        Ball bEngine1 = new Ball(-10, -10, 0, 3, 5);
        Ball bEngine2 = new Ball(10, -10, 0, 3, 5);

        Joint je = new Joint(bEngine1, bEngine2, 2);
        Joint je1 = new Joint(ej1, bEngine1, 2);
        Joint je2 = new Joint(ej2, bEngine2, 2);

        group.getChildren().addAll(bEngine1, bEngine2, je, je1, je2);

        Ball bChwytak = new Ball(-50, -20, 0, 7, 5);
        Joint jChwytak = new Joint(sj1,bChwytak,2);
        group.getChildren().addAll(jChwytak,bChwytak);

        Chain c = new Chain(j1, j2, je, je1, je2,jChwytak);
        //Chain c = new Chain(j1, j2, je, je1, je2);

        chainsList.add(c);

        ch = new ChwytakStary(c,bChwytak,sj1,jChwytak,chainsList);

        List<Joint> jointList1 = new ArrayList<>();
        List<Joint> jointList2 = new ArrayList<>();

        jointList1.clear();
        jointList2.clear();

        jointList1.add(j1);
        jointList1.add(jChwytak);

        jointList2.add(je);
        jointList2.add(je2);
        jointList2.add(j2);

        sp1 = new LinearMotor(je1, ej1, bEngine1, new ArrayList<>(jointList1), new ArrayList<>(jointList2), c, 0.5);

        jointList1.clear();
        jointList2.clear();

        jointList1.add(j2);

        jointList2.add(je);
        jointList2.add(je1);
        jointList2.add(j1);
        jointList2.add(jChwytak);

        sp2 = new LinearMotor(je2, ej2, bEngine2, new ArrayList<>(jointList1), new ArrayList<>(jointList2), c, 0.1);

        jointList1.clear();
        jointList2.clear();

        jointList1.add(j1);
        jointList1.add(jChwytak);
        jointList1.add(je1);


        jointList2.add(je);
        jointList2.add(je2);
        jointList2.add(j2);

        so1 = new RotaryMotor(bEngine1, je1, je, ej1, bEngine2, new ArrayList<>(jointList1), new ArrayList<>(jointList2), c, 0.01);

        jointList1.clear();
        jointList2.clear();

        jointList1.add(j2);
        jointList1.add(je2);

        jointList2.add(je);
        jointList2.add(je1);
        jointList2.add(j1);
        jointList2.add(jChwytak);

        so2 = new RotaryMotor(bEngine2, je2, je, ej2, bEngine1, new ArrayList<>(jointList1), new ArrayList<>(jointList2), c, 0.01);

//        Ball sTest = new Ball(-6,0,20,2,5);
//        Ball eTest = new Ball(-6,0,-10,2,5);
        Ball sTest = new Ball(-30.1,-5,20,2,5);  // tu sie rombało
        Ball eTest = new Ball(-30.1,-5,-10,2,5); // tu sie rombało
//        Ball sTest = new Ball(-25,-7,20,2,5);
//        Ball eTest = new Ball(-25,-7,-10,2,5);
//        Ball sTest = new Ball(-50, -35, 10, 2, 50000);
//        Ball eTest = new Ball(-50, -35, -10, 2, 50000);
        Joint jTest = new Joint(sTest, eTest, 2);
        group.getChildren().addAll(sTest, eTest, jTest);

        Chain cTest = new Chain(jTest);
        chainsList.add(cTest);

        //c.V = new Point3D(0, -0.5, 0);
        //c.W = new Point3D(0,0,0.01);
        //c3.V = new Point3D(0, 0, 0);
////////////////////////////////


        //c.W = new Point3D(0,-0.01,0);

//        Sphere cg = new Sphere(10);
//        cg.setTranslateX(-70);
//        cg.setTranslateY(-15);
//        cg.setTranslateZ(0);
//        group.getChildren().add(cg);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                cg.setTranslateX(c.centreOfGravity.getX());
//                cg.setTranslateY(c.centreOfGravity.getY());
//                cg.setTranslateZ(c.centreOfGravity.getZ());


                //System.out.println(c.W);
                //Point3D[] punkty = Collisions.Chains2Joints.serveCollision(c2,c1);
//
                Point3D[] punkty = CollisionsOld.Chains2JointsEngines.serveCollision(c, cTest, j1, jTest);
//                Point3D[] punkty1 = Collisions.Chains2JointsEngines.serveCollision(c,cTest,je1,jTest);
//                c1.setBallsVCausedByJoint();
//                c2.setBallsVCausedByJoint();

//                c.setBallsVCausedByJoint();
//                cTest.setBallsVCausedByJoint();
                for(Chain c:chainsList){
                    c.setBallsVCausedByChain();
                }

                if (Epressed) {
                    ch.grab(cTest,jTest);
                }

                if (Rpressed) {
                    ch.release();
                }

                if (Zpressed) {
                    sp1.moveInside();
                }
                if (Xpressed) {
                    sp1.moveOutside();
                }

                if ((Xpressed && !Zpressed) || (!Xpressed && Zpressed)) {
                    sp1.isActive = true;
                } else {
                    sp1.isActive = false;
                }

//                if (Cpressed) {
//                    sp2.moveInside();
//                }
//                if (Vpressed) {
//                    sp2.moveOutside();
//                }
                //System.out.println("Spressed "+Spressed);
                //System.out.println("Apressed "+Apressed);

                if (Apressed) {
                    so1.moveInside();
                }
                if (Spressed) {
                    so1.moveOutside();
                }

                if ((Apressed && !Spressed) || (!Apressed && Spressed)) {
                    so1.isActive = true;
                } else {
                    so1.isActive = false;
                }

                //System.out.println("isActive w timerze "+so1.isActive);

//                if (Dpressed) {
//                    so2.moveInside();
//                }
//                if (Fpressed) {
//                    so2.moveOutside();
//                }
//
//                if( (Dpressed && !Fpressed) || (!Dpressed && Fpressed)){
//                    so2.isActive = true;
//                }else{
//                    so2.isActive = false;
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
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
