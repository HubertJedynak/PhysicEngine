package activities;

import animations.Animation;
import core.CameraManager;
import core.LogicManager;
import elements.Frame;
import inputDevices.Keyboard;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import physic.Chain;

import java.util.List;

public class KeyCodes extends Activity {

    Frame frame;

    public KeyCodes(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        super(group, groupBackground, chainList, keyboard, cameraManager, animation);

        frame = new Frame(group, chainList, animation);
        frame.create();
        frame.setImage("/resources/images/keyboard.bmp");

    }

    @Override
    public void cleanUpAfterActivity() {
        frame.delete();
    }

    @Override
    public void manage() {
        if (keyboard.isPressed(KeyCode.ENTER)) {
            LogicManager.loadActivity(new Menu(group, groupBackground, chainList, keyboard, cameraManager, animation));
        }
    }

}
