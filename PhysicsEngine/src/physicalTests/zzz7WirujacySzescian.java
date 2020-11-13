package physicalTests;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.*;

public class zzz7WirujacySzescian extends Application {
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

        Ball b[] = new Ball[8];

        b[0] = new Ball(0, 0, 0, 5, 5);
        b[1] = new Ball(20, 0, 0, 5, 5);
        b[2] = new Ball(20, 20, 0, 5, 5);
        b[3] = new Ball(0, 20, 0, 5, 5);
        b[4] = new Ball(0, 0, 20, 5, 5);
        b[5] = new Ball(20, 0, 20, 5, 5);
        b[6] = new Ball(20, 20, 20, 5, 5);
        b[7] = new Ball(0, 20, 20, 5, 5);

        Joint[][] tempJ = new Joint[8][8];
        for (int y = 0; y < b.length; y++) {
            for (int x = y + 1; x < b.length; x++) {
                tempJ[x][y] = new Joint(b[x], b[y], 2);
            }
        }
        Joint[] j = new Joint[28];
        int i = 0;
        for (int y = 0; y < b.length; y++) {
            for (int x = 0; x < b.length; x++) {
                if (tempJ[x][y] != null) {
                    j[i] = tempJ[x][y];
                    i++;
                    //System.out.println(i);
                }
            }
        }

        group.getChildren().addAll(b);
        group.getChildren().addAll(j);

        Chain c = new Chain(j);

        //c.V = new Point3D(-0.05, -0.05, 0.05);
        //double f = 1./2/2/2/2/2/2/2/2/2/2/(2+1);
        double f = 5.1;
        System.out.println("f = "+f);
        c.W = new Point3D(f,f,f);

//        Ball sjc = new Ball(-10, 10, 0, 5, 5);
//        Ball ejc = new Ball(-30, 10, 0, 5, 5);
//        Joint jc = new Joint(sjc, ejc, 5);
//        Chain cj = new Chain(jc);
//        group.getChildren().addAll(sjc,ejc,jc);
//        cj.W = new Point3D(0.1,0.01,0);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                c.setBallsVCausedByChain();
                //cj.setBallsVCausedByChain();
//                System.out.println("");
            }
        };
        timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-400);

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
