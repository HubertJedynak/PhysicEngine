package protocols;

import elements.Spaceship;

public class Protocol_RotateYLeft implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateYLeft(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorU1.moveLeftAlongTheAxis();
        spaceship.rotaryMotorU2.moveLeftAlongTheAxis();

        spaceship.rotaryMotorU1.isActive = true;
        spaceship.rotaryMotorU2.isActive = true;
    }

}
