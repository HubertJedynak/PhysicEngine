package protocols;

import elements.Spaceship;

public class Protocol_RotateZRight implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateZRight(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.rotaryMotorB1.moveRightAlongTheAxis();
        spaceship.rotaryMotorB3.moveRightAlongTheAxis();
        spaceship.rotaryMotorF1.moveLeftAlongTheAxis();
        spaceship.rotaryMotorF3.moveLeftAlongTheAxis();

        spaceship.rotaryMotorB1.isActive = true;
        spaceship.rotaryMotorB3.isActive = true;
        spaceship.rotaryMotorF1.isActive = true;
        spaceship.rotaryMotorF3.isActive = true;
    }

}
