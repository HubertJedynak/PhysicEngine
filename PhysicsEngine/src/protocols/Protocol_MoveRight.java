package protocols;

import elements.Spaceship;

public class Protocol_MoveRight implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveRight(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineF1.turnOn();
        spaceship.jetEngineB3.turnOn();

        spaceship.jetEngineF1.isActive = true;
        spaceship.jetEngineB3.isActive = true;

    }

}
