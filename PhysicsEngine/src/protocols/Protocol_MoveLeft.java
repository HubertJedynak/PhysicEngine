package protocols;

import elements.Spaceship;

public class Protocol_MoveLeft implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveLeft(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineF3.turnOn();
        spaceship.jetEngineB1.turnOn();

        spaceship.jetEngineF3.isActive = true;
        spaceship.jetEngineB1.isActive = true;

    }

}
