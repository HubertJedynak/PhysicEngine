
package activities;

import animations.Animation;
import core.CameraManager;
import core.LogicManager;
import elements.Planet;
import inputDevices.Keyboard;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import physic.Chain;

import java.util.List;

public class Menu extends Activity {

    Planet planet;

    public Menu(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        super(group, groupBackground, chainList, keyboard, cameraManager,animation);

        planet = new Planet(group);
        planet.create();
        planet.setImage("/resources/images/menu.bmp");
    }

    @Override
    public void cleanUpAfterActivity() {
        planet.delete();
    }

    @Override
    public void manage() {
        if (keyboard.isPressed(KeyCode.DIGIT1)) {
            LogicManager.loadActivity(new MainSimulation(group, groupBackground, chainList, keyboard, cameraManager,animation));
        }
        if (keyboard.isPressed(KeyCode.DIGIT2)) {
            LogicManager.loadActivity(new Author(group, groupBackground, chainList, keyboard, cameraManager,animation));
        }
        if (keyboard.isPressed(KeyCode.DIGIT3)) {
            LogicManager.loadActivity(new KeyCodes(group, groupBackground, chainList, keyboard, cameraManager,animation));
        }

    }

}
