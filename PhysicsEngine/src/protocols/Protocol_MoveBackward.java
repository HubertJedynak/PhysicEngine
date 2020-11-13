package protocols;

import elements.Spaceship;

public class Protocol_MoveBackward implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveBackward(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineL3.turnOn();
        spaceship.jetEngineR1.turnOn();

        spaceship.jetEngineL3.isActive = true;
        spaceship.jetEngineR1.isActive = true;

    }

}
