package protocols;

import elements.Spaceship;

public class Protocol_RotateXBackward implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateXBackward(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorR1.moveRightAlongTheAxis();
        spaceship.rotaryMotorR3.moveRightAlongTheAxis();
        spaceship.rotaryMotorL1.moveLeftAlongTheAxis();
        spaceship.rotaryMotorL3.moveLeftAlongTheAxis();

        spaceship.rotaryMotorR1.isActive = true;
        spaceship.rotaryMotorR3.isActive = true;
        spaceship.rotaryMotorL1.isActive = true;
        spaceship.rotaryMotorL3.isActive = true;


    }

}
