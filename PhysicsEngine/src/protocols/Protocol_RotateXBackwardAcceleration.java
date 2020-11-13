package protocols;

import elements.Spaceship;

public class Protocol_RotateXBackwardAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateXBackwardAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineFD.turnOn();
        spaceship.jetEngineBU.turnOn();

        spaceship.jetEngineFD.isActive = true;
        spaceship.jetEngineBU.isActive = true;


    }

}
