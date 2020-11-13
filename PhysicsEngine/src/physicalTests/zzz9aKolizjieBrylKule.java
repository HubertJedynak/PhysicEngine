package physicalTests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.Ball;
import physic.Chain;
import physic.Collisions;
import physic.Joint;

public class zzz9aKolizjieBrylKule extends Application {
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

//        Ball sj1 = new Ball(2+20, -10, 13, 2, 2.50);
//        Ball ej1 = new Ball(2+20, -10, -13, 2, 2.50);
        Ball sj1 = new Ball(20, -10, 0, 3, 3.5);
        Ball ej1 = new Ball(10, -10, -0, 3, 3.5);

//        Ball sj1a = new Ball(25, -12, 0, 3, 3.5);
//        Ball sj1b = new Ball(25, -8, 0, 3, 3.5);
//        Ball ej1 = new Ball(15, -10, 0, 3, 3.5);


//        Joint j1a = new Joint(sj1a, ej1, 2);
//        Joint j1b = new Joint(sj1b, ej1, 2);

        Joint j1 = new Joint(sj1, ej1, 2);
        Chain c1 = new Chain(j1);
        group.getChildren().addAll(sj1, ej1, j1);


//        Chain c1 = new Chain(j1a, j1b);
//        group.getChildren().addAll(sj1a, sj1b, ej1, j1a, j1b);
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
        //c2.W = new Point3D(0, 0, 0.01);
        c1.V = new Point3D(-0.1, 0, 0);
        double Ekprzed = c1.m * c1.V.magnitude() * c1.V.magnitude();
        System.out.println("Ek przed " + Ekprzed);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                System.out.println(c1.W);
//                System.out.println(c2.W);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //Point3D[] punkty = Collisions.Chains2Joints.serveCollision(c1, c2);
                // uwaga: c1 jest od kulki (sprawdzana kulka startowa jointa) c2 od jointa
//                Point3D[] punkty = Collisions.ChainsJointBallInJoint.serveCollision(c2,c1);
                //Point3D[] punkty = Collisions.ChainsJointBallInJoint.serveCollision(c1, c2, ej1, j2);
//                Point3D[] punkty = CollisionsStary.Chains2BallsEngines.serveCollision(c1, c2, ej1, sj2);
                Point3D[] punkty = Collisions.Chains2Balls.serveCollision(c1,c2,ej1,sj2);


                c1.setBallsVCausedByChain();
                c2.setBallsVCausedByChain();

                //camera.setTranslateX(c1.centreOfGravity.getX());
                //camera.setTranslateY(c1.centreOfGravity.getY());
//
                if (punkty != null) {
//                    group.getChildren().addAll(
////                            new Ball(punkty[0].getX(),punkty[0].getY(),punkty[0].getZ(),j1.getRadius(),1),
////                            new Ball(punkty[1].getX(),punkty[1].getY(),punkty[1].getZ(),j2.getRadius(),1)
//                    );
                    System.out.println("kolizja");
//                    System.out.println("V1 "+c1.V.magnitude());
//                    System.out.println("V2 "+c2.V.magnitude());
//                    System.out.println("W2 "+c2.W.magnitude());

                    double Ekpo = c1.m * c1.V.magnitude() * c1.V.magnitude() +
                            c2.m * c2.V.magnitude() * c2.V.magnitude() +
                            c2.m * (j2.getHeight() / 2) * (j2.getHeight() / 2) * c2.W.magnitude() * c2.W.magnitude();

                    System.out.println("Ek po " +
                            (
                                    Ekpo
                            ));
                    System.out.println("stosunek energii " + (Ekpo / Ekprzed));

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
