package protocols;

import elements.Spaceship;

public class Protocol_RotateZLeftAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateZLeftAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineRD.turnOn();
        spaceship.jetEngineLU.turnOn();

        spaceship.jetEngineRD.isActive = true;
        spaceship.jetEngineLU.isActive = true;
    }

}
