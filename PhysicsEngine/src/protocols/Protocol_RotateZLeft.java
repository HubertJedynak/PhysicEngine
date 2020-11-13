package protocols;

import elements.Spaceship;

public class Protocol_RotateZLeft implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateZLeft(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorF1.moveRightAlongTheAxis();
        spaceship.rotaryMotorF3.moveRightAlongTheAxis();
        spaceship.rotaryMotorB1.moveLeftAlongTheAxis();
        spaceship.rotaryMotorB3.moveLeftAlongTheAxis();

        spaceship.rotaryMotorF1.isActive = true;
        spaceship.rotaryMotorF3.isActive = true;
        spaceship.rotaryMotorB1.isActive = true;
        spaceship.rotaryMotorB3.isActive = true;
    }

}
