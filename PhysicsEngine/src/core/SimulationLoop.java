package core;

import inputDevices.Keyboard;
import inputDevices.Mouse;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;

public class SimulationLoop extends AnimationTimer {

    Scene scene;
    Group group;
    Group groupBackground;

    boolean isLoopRunning = false;

    CameraManager cameraManager;
    LogicManager logicManager;
    Keyboard keyboard;
    Mouse mouse;

    public SimulationLoop(Scene scene, Group group, Group groupBackground) {
        super();
        this.scene = scene;
        this.group = group;
        this.groupBackground = groupBackground;

        cameraManager = new CameraManager(scene, group, groupBackground);

        keyboard = new Keyboard(scene);
        mouse = new Mouse(scene, cameraManager);

        logicManager = new LogicManager(cameraManager, keyboard, mouse, group, groupBackground);
    }

    @Override
    public void handle(long now) {
        logicManager.update();
    }

    public void startLoop() {
        if (isLoopRunning) {
            return;
        }

        isLoopRunning = true;
        this.start();
    }

    public void stopLoop() {
        isLoopRunning = false;
        System.exit(0);
    }

}
