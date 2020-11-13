package protocols;

import elements.Spaceship;

public class Protocol_RotateXForward implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateXForward(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorL1.moveRightAlongTheAxis();
        spaceship.rotaryMotorL3.moveRightAlongTheAxis();
        spaceship.rotaryMotorR1.moveLeftAlongTheAxis();
        spaceship.rotaryMotorR3.moveLeftAlongTheAxis();

        spaceship.rotaryMotorL1.isActive = true;
        spaceship.rotaryMotorL3.isActive = true;
        spaceship.rotaryMotorR1.isActive = true;
        spaceship.rotaryMotorR3.isActive = true;

    }

}
