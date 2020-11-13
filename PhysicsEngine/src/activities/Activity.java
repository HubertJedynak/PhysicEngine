package activities;

import animations.Animation;
import core.CameraManager;
import inputDevices.Keyboard;
import javafx.scene.Group;
import physic.Chain;

import java.util.List;

public abstract class Activity {

    Group group;
    Group groupBackground;
    List<Chain> chainList;
    Keyboard keyboard;
    CameraManager cameraManager;
    Animation animation;

    public Activity(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        this.animation = animation;
        this.group = group;
        this.groupBackground = groupBackground;
        this.chainList = chainList;
        this.keyboard = keyboard;
        this.cameraManager = cameraManager;
    }

    public abstract void cleanUpAfterActivity();

    public abstract void manage();

}
