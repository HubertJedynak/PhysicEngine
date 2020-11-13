package physicalTests;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import physic.Ball;
import physic.Joint;

public class zzz5KolizjeJointow extends Application {
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

        Ball sj1 = new Ball(20, -0, -24, 2, 5);
        Ball ej1 = new Ball(0, -25, 22, 2, 5);
//        Ball sj1 = new Ball(10, 10, 10, 5, 5);
//        Ball ej1 = new Ball(-10, -10, 10, 5, 5);

        Joint j1 = new Joint(ej1, sj1, 1);
        group.getChildren().addAll(j1,sj1,ej1);

        Ball sj2 = new Ball(30, -15, 20, 2, 5);
        Ball ej2 = new Ball(-20, 5, 10, 2, 5);
//        Ball sj2 = new Ball(10+30, 10+30, 0, 5, 5);
//        Ball ej2 = new Ball(-10+30, -10+30, 0, 5, 5);
        Joint j2 = new Joint(ej2, sj2, 1);

        group.getChildren().addAll(j2,sj2,ej2);

        Point3D przeciecie[] = CollisionsOld.Joints.serveCollisions(j2,j1);
        System.out.println("przeciecie" + przeciecie);

        Point3D p = przeciecie[0].subtract(przeciecie[1]);

        Ball sj3 = new Ball(sj1.x.get() + p.getX(), sj1.y.get() + p.getY(), sj1.z.get() + p.getZ(), 1, 5);
        Ball ej3 = new Ball(ej1.x.get() + p.getX(), ej1.y.get() + p.getY(), ej1.z.get() + p.getZ(), 1, 5);

        Joint j3 = new Joint(sj3,ej3,0.5);
        group.getChildren().addAll(j3,sj3,ej3);



        //long t = System.nanoTime();
        //for(int i=0;i<100000;i++){
        //Collisions.Joints.serveCollisions(j2,j1);
        //}
        //t = System.nanoTime() - t ;
        //System.out.println("czas "+ t * 1e-6);
        group.getChildren().add(new Ball(
                przeciecie[0].getX(),
                przeciecie[0].getY(),
                przeciecie[0].getZ(),2,5
        ));

        group.getChildren().add(new Ball(
                przeciecie[1].getX(),
                przeciecie[1].getY(),
                przeciecie[1].getZ(),2,5
        ));

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                sj2.vy += g;
                ej2.vy += g;

                sj2.addVtoXYZ();
                ej2.addVtoXYZ();

//                Collisions.BallJoint.serveCollisions(b, j1);
            }
        };
//        timer.start();

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(1000);
        camera.setTranslateZ(-100);

        scene = new Scene(group, 400, 400, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.GREEN);
        scene.setCamera(camera);

        mouseEvents();

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
