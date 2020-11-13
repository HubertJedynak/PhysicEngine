package protocols;

import elements.Spaceship;

public class Protocol_MoveForward implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveForward(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineL1.turnOn();
        spaceship.jetEngineR3.turnOn();

        spaceship.jetEngineL1.isActive = true;
        spaceship.jetEngineR3.isActive = true;

    }

}
