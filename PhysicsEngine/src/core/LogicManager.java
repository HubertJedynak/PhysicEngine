package core;

import activities.*;
import animations.Animation;
import graphics.AnimatedTexture;
import inputDevices.Keyboard;
import inputDevices.Mouse;
import javafx.scene.Group;
import physic.*;

import java.util.LinkedList;
import java.util.List;

public class LogicManager {

    Group group;
    Group groupBackground;
    CameraManager cameraManager;
    Keyboard keyboard;
    Mouse mouse;

    static Activity currentActivity;

    List<Chain> chainsList = new LinkedList<>();

    public LogicManager(CameraManager cameraManager, Keyboard keyboard, Mouse mouse, Group group, Group groupBackground) {
        this.cameraManager = cameraManager;
        this.keyboard = keyboard;
        this.mouse = mouse;
        this.group = group;
        this.groupBackground = groupBackground;

        Animation animation = new Animation();

        currentActivity = new First(group, groupBackground, chainsList, keyboard, cameraManager, animation);

    }

    public void update() {

        // manage the camera
        cameraManager.manageCamera(keyboard);

        // manage of collisions
        Collisions.serveAllCollisions(chainsList);

        // manage activity
        currentActivity.manage();

        // manage the velocity of Chains
        for (Chain c : chainsList) {
            c.setBallsVCausedByChain();
        }

        // manage animations
        for (Chain c : chainsList) {
            for (Ball b : c.ballsList) {
                AnimatedTexture.serveAnimationOfBall(b);
            }
            for (Joint j : c.jointList) {
                AnimatedTexture.serveAnimationOfJoint(j);
            }
        }

    }

    public static void loadActivity(Activity loadedActivity) {
        currentActivity.cleanUpAfterActivity();
        currentActivity = loadedActivity;
    }
}
