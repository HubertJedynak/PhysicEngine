package inputDevices;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {

    Scene scene;
    Map<KeyCode, Boolean> keysPressed;

    public Keyboard(Scene scene) {

        this.scene = scene;
        keyboardEvents();

    }

    private void keyboardEvents() {

        keysPressed = new HashMap<>();

        for (KeyCode kci : KeyCode.values()) {
            keysPressed.put(kci, false);
        }

        scene.setOnKeyPressed(event -> {
            synchronized (keysPressed) {
                keysPressed.put(event.getCode(), true);
            }
        });

        scene.setOnKeyReleased(event -> {
            synchronized (keysPressed) {
                keysPressed.put(event.getCode(), false);
            }
        });

    }

    public boolean isPressed(KeyCode kc) {
        return keysPressed.getOrDefault(kc, false);
    }
}
