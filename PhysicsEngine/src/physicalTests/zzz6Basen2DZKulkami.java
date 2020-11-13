package physicalTests;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.Ball;
import physic.Joint;

public class zzz6Basen2DZKulkami extends Application {
    Group group;
    Camera camera;
    Scene scene;

    // do obracanie kamera
    double x0, y0, ax0, ay0;

    // do odbijania pilek
    double g = 0.01;

    @Override
    public void start(Stage primaryStage) throws Exception {
        group = new Group();

//        Ball b1 = new Ball(90, -50, 0, 5, 5);
//        Ball b2 = new Ball(-90, -50, 0, 5, 5);

        //        Ball sj = new Ball(130, -15, 0, 5, 5);
//        Ball ej = new Ball(-10, 0, 0, 5, 5);
//        Joint j = new Joint(sj, ej, 5);

//        group.getChildren().addAll(b, sj, ej, j);


        Ball k1 = new Ball(100,-30,0,5,5);
        Ball k2 = new Ball(0,0,0,5,5);
        Ball k3 = new Ball(-100,-30,0,5,5);

        Joint j1 = new Joint(k1,k2,5);
        Joint j2 = new Joint(k2,k3,5);

        group.getChildren().addAll(k1,k2,k3,j1,j2);

        Ball b[] = new Ball[60];
        for(int i=0;i<b.length/4;i++){
            b[i] = new Ball(-90+i*15, -50, 0, 5, 5);
            b[i+15] = new Ball(-90+i*13, -70, 0, 5, 5);
            b[i+30] = new Ball(-90+i*11, -90, 0, 5, 5);
            b[i+45] = new Ball(-90+i*13, -110, 0, 5, 5);

        }
        group.getChildren().addAll(b);



        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                for(int i=0;i<b.length;i++) {
                    b[i].vy += g;
                    b[i].addVtoXYZ();

                    CollisionsOld.BallJoint.serveCollisions(b[i], j1);
                    CollisionsOld.BallJoint.serveCollisions(b[i], j2);
                }

                for(int l=0;l<b.length;l++){
                    for(int m=l+1;m<b.length;m++){
                        CollisionsOld.Balls.serveCollisions(b[l],b[m]);
                    }
                }


            }
        };
        timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-500);

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
