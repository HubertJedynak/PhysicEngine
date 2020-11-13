package inputDevices;

import core.CameraManager;
import javafx.scene.Scene;

public class Mouse {
    Scene scene;
    CameraManager cameraManager;

    public Mouse(Scene scene, CameraManager cameraManager) {
        this.scene = scene;
        this.cameraManager = cameraManager;
        mouseEvents();
    }

    public void mouseEvents() {
        cameraManager.cameraEvents();
    }

}
