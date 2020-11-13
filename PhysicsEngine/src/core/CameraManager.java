package core;

import inputDevices.Keyboard;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import physic.Ball;
import physic.Chain;

public class CameraManager {

    Camera camera;
    Scene scene;

    Rotate xRotate;
    Rotate yRotate;

    Rotate invxRotate;
    Rotate invyRotate;

    public Translate translate;
    public Translate translateObjectTracker;

    Ball trackerBall;
    Chain trackerChain;

    boolean cameraMovement = false;

    Group group;
    Group groupBackground;

    // to rotate camera
    double x0, y0, alfaX0, alfaY0;

    public CameraManager(Scene scene, Group group, Group groupBackground) {
        this.scene = scene;
        this.group = group;
        this.groupBackground = groupBackground;

        camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(5000);
        camera.setTranslateZ(-400);
        scene.setCamera(camera);

        cameraEvents();
    }

    public void cameraEvents() {

        xRotate = new Rotate(0, Rotate.X_AXIS);
        yRotate = new Rotate(0, Rotate.Y_AXIS);

        invxRotate = new Rotate(0, Rotate.X_AXIS);
        invyRotate = new Rotate(0, Rotate.Y_AXIS);

        invxRotate.angleProperty().bind(xRotate.angleProperty().multiply(-1));
        invyRotate.angleProperty().bind(yRotate.angleProperty().multiply(-1));

        translate = new Translate(0, 0, 0);
        translateObjectTracker = new Translate(0, 0, 0);

        group.getTransforms().addAll(invyRotate, invxRotate, translate, xRotate, yRotate, translateObjectTracker);
        groupBackground.getTransforms().addAll(xRotate, yRotate);

        scene.setOnMousePressed(event -> {
            if (cameraMovement) {
                x0 = event.getSceneX();
                y0 = event.getSceneY();
                alfaX0 = xRotate.getAngle();
                alfaY0 = yRotate.getAngle();

            }
        });

        scene.setOnMouseDragged(event -> {
            if (cameraMovement) {

                xRotate.setAngle(alfaX0 + event.getSceneY() - y0);
                yRotate.setAngle(alfaY0 - (event.getSceneX() - x0));

            }
        });

        scene.setOnScroll(event -> {
            if (cameraMovement) {
                double scroll = event.getDeltaY();
                camera.setTranslateZ(camera.getTranslateZ() + scroll);

            }
        });
    }

    public void manageCamera(Keyboard keyboard) {
        if (cameraMovement) {
            if (keyboard.isPressed(KeyCode.SHIFT) && keyboard.isPressed(KeyCode.UP)) {
                translate.setY(translate.getY() + 3);
            }

            if (keyboard.isPressed(KeyCode.SHIFT) && keyboard.isPressed(KeyCode.DOWN)) {
                translate.setY(translate.getY() - 3);
            }

            if (keyboard.isPressed(KeyCode.SHIFT) && keyboard.isPressed(KeyCode.LEFT)) {
                translate.setX(translate.getX() + 3);
            }

            if (keyboard.isPressed(KeyCode.SHIFT) && keyboard.isPressed(KeyCode.RIGHT)) {
                translate.setX(translate.getX() - 3);
            }

            if (keyboard.isPressed(KeyCode.CONTROL) && keyboard.isPressed(KeyCode.UP)) {
                camera.setTranslateZ(camera.getTranslateZ() + 10);
            }

            if (keyboard.isPressed(KeyCode.CONTROL) && keyboard.isPressed(KeyCode.DOWN)) {
                camera.setTranslateZ(camera.getTranslateZ() - 10);
            }

            trackPoint();

        }


    }

    public void trackChain(Chain chain) {
        trackerChain = chain;
        trackerBall = null;

        translate.setX(0);
        translate.setY(0);
        translate.setZ(0);
    }

    public void trackPoint() {
        if (cameraMovement) {
            Point3D trackerPoint = new Point3D(0, 0, 0);// uwaga zmienic polozenie wyzej

            if (trackerBall != null && trackerChain == null) {
                trackerPoint = new Point3D(
                        trackerBall.x.get(),
                        trackerBall.y.get(),
                        trackerBall.z.get()
                );
            } else if (trackerBall == null && trackerChain != null) {
                trackerPoint = new Point3D(
                        trackerChain.centreOfGravity.getX(),
                        trackerChain.centreOfGravity.getY(),
                        trackerChain.centreOfGravity.getZ()
                );
            }


            translateObjectTracker.setX(-trackerPoint.getX());
            translateObjectTracker.setY(-trackerPoint.getY());
            translateObjectTracker.setZ(-trackerPoint.getZ());

        }
    }

    public void setCameraMovement(boolean value) {
        cameraMovement = value;
    }
}
