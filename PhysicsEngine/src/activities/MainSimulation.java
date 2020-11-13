package activities;

import animations.Animation;
import core.CameraManager;
import elements.Meteor;
import elements.Spaceship;
import inputDevices.Keyboard;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import physic.*;

import java.util.List;

public class MainSimulation extends Activity {

    Spaceship spaceship;
    Meteor smallMeteor, hugeMeteor;

    public MainSimulation(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        super(group, groupBackground, chainList, keyboard, cameraManager, animation);

        cameraManager.setCameraMovement(true);

        smallMeteor = new Meteor(500, -100, 0, 200, group, chainList);
        smallMeteor.create();

        hugeMeteor = new Meteor(-100, -100, -100, 1000, group, chainList);
        hugeMeteor.create();

        spaceship = new Spaceship(0, 0, 0, group, chainList, animation);
        spaceship.create();

        spaceship.setTrackedChain(smallMeteor.chain);

    }

    @Override
    public void cleanUpAfterActivity() {
        hugeMeteor.delete();
        smallMeteor.delete();
        spaceship.delete();
        cameraManager.setCameraMovement(false);
    }

    @Override
    public void manage() {

        spaceship.setAllEnginesFalse();

        if (keyboard.isPressed(KeyCode.Q)) {
            cameraManager.trackChain(spaceship.chain);
        }

        if (keyboard.isPressed(KeyCode.W)) {
            cameraManager.trackChain(smallMeteor.chain);
            spaceship.setTrackedChain(smallMeteor.chain);
        }

        if (keyboard.isPressed(KeyCode.E)) {
            cameraManager.trackChain(hugeMeteor.chain);
            spaceship.setTrackedChain(hugeMeteor.chain);
        }

        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateXBackward.turnOn();
        }
        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.DOWN) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateXForward.turnOn();
        }
        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.LEFT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateZLeft.turnOn();
        }
        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.RIGHT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateZRight.turnOn();
        }
        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.LEFT) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateYLeft.turnOn();
        }
        if (keyboard.isPressed(KeyCode.N) && keyboard.isPressed(KeyCode.RIGHT) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateYRight.turnOn();
        }

        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateXForwardAcceleration.turnOn();
        }
        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.DOWN) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateXBackwardAcceleration.turnOn();
        }
        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.LEFT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateZLeftAcceleration.turnOn();
        }
        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.RIGHT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateZRightAcceleration.turnOn();
        }
        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.LEFT) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateYLeftAcceleration.turnOn();
        }
        if (keyboard.isPressed(KeyCode.L) && keyboard.isPressed(KeyCode.RIGHT) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_RotateYRightAcceleration.turnOn();
        }

        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveForward.turnOn();
        }
        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.DOWN) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveBackward.turnOn();
        }
        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.LEFT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveLeft.turnOn();
        }
        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.RIGHT) && !keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveRight.turnOn();
        }
        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.UP) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveUp.turnOn();
        }
        if (keyboard.isPressed(KeyCode.M) && keyboard.isPressed(KeyCode.DOWN) && keyboard.isPressed(KeyCode.B)) {
            spaceship.protocol_MoveDown.turnOn();
        }

        if (keyboard.isPressed(KeyCode.V) && keyboard.isPressed(KeyCode.M)) {
            spaceship.protocol_EquateToVelocityOfTheChain.turnOn();
        }
        if (keyboard.isPressed(KeyCode.V) && keyboard.isPressed(KeyCode.N)) {
            spaceship.protocol_SetTheSpaceshipTowardTheChain.turnOn();

        }

        if (keyboard.isPressed(KeyCode.O)) {
            spaceship.protocol_AlignTheRingToTheCentreOfGravity.turnOn();
        }
        if (keyboard.isPressed(KeyCode.P)) {
            spaceship.protocol_InhibitTheRotation.turnOn();
        }

        if (keyboard.isPressed(KeyCode.C)) {
            if (spaceship.gripper.grabbedChain == null) {
                spaceship.bGripper.setAnimatedTexture(animation.aGripper);
            }
            spaceship.gripper.release();
        }
        if (keyboard.isPressed(KeyCode.X)) {
            if (spaceship.gripper.grabbedChain != null) {
                spaceship.bGripper.setAnimatedTexture(animation.aGripperOn);

            }
            spaceship.gripper.grab();
        }

        if (keyboard.isPressed(KeyCode.Z) && keyboard.isPressed(KeyCode.UP)) {
            spaceship.linearMotorRotaryMotor.moveOutside();
        }
        if (keyboard.isPressed(KeyCode.Z) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.linearMotorRotaryMotor.moveInside();
        }
        if (keyboard.isPressed(KeyCode.Z) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.DOWN) ||
                keyboard.isPressed(KeyCode.Z) && !keyboard.isPressed(KeyCode.UP) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.linearMotorRotaryMotor.isActive = true;
        } else {
            spaceship.linearMotorRotaryMotor.isActive = false;
        }

        if (keyboard.isPressed(KeyCode.A) && keyboard.isPressed(KeyCode.UP)) {
            spaceship.linearMotorGripper.moveOutside();
        }
        if (keyboard.isPressed(KeyCode.A) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.linearMotorGripper.moveInside();
        }
        if (keyboard.isPressed(KeyCode.A) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.DOWN) ||
                keyboard.isPressed(KeyCode.A) && !keyboard.isPressed(KeyCode.UP) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.linearMotorGripper.isActive = true;
        } else {
            spaceship.linearMotorGripper.isActive = false;
        }

        if (keyboard.isPressed(KeyCode.S) && keyboard.isPressed(KeyCode.UP)) {
            spaceship.rotaryMotorGripper.moveOutside();
        }
        if (keyboard.isPressed(KeyCode.S) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.rotaryMotorGripper.moveInside();
        }
        if (keyboard.isPressed(KeyCode.S) && keyboard.isPressed(KeyCode.UP) && !keyboard.isPressed(KeyCode.DOWN) ||
                keyboard.isPressed(KeyCode.S) && !keyboard.isPressed(KeyCode.UP) && keyboard.isPressed(KeyCode.DOWN)) {
            spaceship.rotaryMotorGripper.isActive = true;
        } else {
            spaceship.rotaryMotorGripper.isActive = false;
        }

        int i = 0;
        for (RotaryMotor r : spaceship.chain.rotaryMotorList) {
            if (r.isActive) {
                i++;
            }
        }

        spaceship.serveAllJetsAnimation();

    }

}
