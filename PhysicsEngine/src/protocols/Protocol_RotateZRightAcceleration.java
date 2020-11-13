package protocols;

import elements.Spaceship;

public class Protocol_RotateZRightAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateZRightAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineLD.turnOn();
        spaceship.jetEngineRU.turnOn();

        spaceship.jetEngineLD.isActive = true;
        spaceship.jetEngineRU.isActive = true;
    }

}
