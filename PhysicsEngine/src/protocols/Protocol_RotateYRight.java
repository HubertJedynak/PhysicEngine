package protocols;

import elements.Spaceship;

public class Protocol_RotateYRight implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateYRight(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorU1.moveRightAlongTheAxis();
        spaceship.rotaryMotorU2.moveRightAlongTheAxis();

        spaceship.rotaryMotorU1.isActive = true;
        spaceship.rotaryMotorU2.isActive = true;

    }

}
