
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

public class Author extends Activity {

    Planet planet;

    public Author(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        super(group, groupBackground, chainList, keyboard, cameraManager, animation);

        planet = new Planet(group);
        planet.create();
        planet.setImage("/resources/images/author.bmp");
    }

    @Override
    public void cleanUpAfterActivity() {
        planet.delete();
    }

    @Override
    public void manage() {
        if (keyboard.isPressed(KeyCode.ENTER)) {
            LogicManager.loadActivity(new Menu(group, groupBackground, chainList, keyboard, cameraManager, animation));
        }
    }

}
