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
import java.util.List;

public class zzz10SilnikPrzesuwny extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.001;
    SilnikPrzesuwnyStary sp;
    boolean Xpressed=false, Zpressed=false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

        Ball sj1 = new Ball(-10, -20, 0, 3, 5);
        Ball ej1 = new Ball(-10, 0, 0, 3, 5);

        Joint j1 = new Joint(sj1, ej1, 2);
        group.getChildren().addAll(sj1, ej1, j1);

        Ball sj2 = new Ball(10, -20, 0, 3, 5);
        Ball ej2 = new Ball(10, 0, 0, 3, 5);

        Joint j2 = new Joint(sj2, ej2, 2);

        group.getChildren().addAll(sj2, ej2, j2);

        Chain c = new Chain(j1,j2);
        List<Ball> ballList1 = new ArrayList<>();
        ballList1.add(sj1);
        ballList1.add(ej1);

        List<Ball> ballList2 = new ArrayList<>();
        ballList2.add(sj2);
        ballList2.add(ej2);

        sp = new SilnikPrzesuwnyStary(c,ballList1,ej1,ballList2,ej2,2,1.1);
        group.getChildren().add(sp);


        Ball sj3 = new Ball(-150, -10, 10, 3, 10);
        Ball ej3 = new Ball(-150, -10, -10, 3, 10);

        Joint j3 = new Joint(sj3, ej3, 2);
        Chain c3 = new Chain(j3);
        group.getChildren().addAll(sj3, ej3, j3);

        c.V = new Point3D(-1,0,0);
       // c3.V = new Point3D(0,0,0);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                Point3D[] punkty = Collisions.Chains2Joints.serveCollision(c2,c1);
//
//                c1.setBallsVCausedByJoint();
//                c2.setBallsVCausedByJoint();

                Point3D[] punkty = CollisionsOld.Chains2Joints.serveCollision(c,c3);

                c.setBallsVCausedByChain();
                c3.setBallsVCausedByChain();

                if (punkty != null){
                    System.out.println("kolizja");
                    System.out.println("predkosc "+c.V);
                }

                if(Xpressed){
                    sp.moveOutside();
                }
                if(Zpressed){
                    sp.moveInside();
                }
            }
        };
        timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-900);
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

    private void keyEvents(){
        scene.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.S)){
                camera.setTranslateZ(camera.getTranslateZ()-10);
            }else if(event.getCode().equals(KeyCode.W)){
                camera.setTranslateZ(camera.getTranslateZ()+10);
            }else if(event.getCode().equals(KeyCode.X)){
                Xpressed=true;
            }else if(event.getCode().equals(KeyCode.Z)){
                Zpressed=true;
            }
        });

        scene.setOnKeyReleased(event -> {
            if(event.getCode().equals(KeyCode.X)){
                Xpressed=false;
            }else if(event.getCode().equals(KeyCode.Z)){
                Zpressed=false;
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
