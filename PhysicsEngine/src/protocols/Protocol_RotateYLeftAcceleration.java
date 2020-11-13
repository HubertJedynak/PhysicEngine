package protocols;

import elements.Spaceship;

public class Protocol_RotateYLeftAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateYLeftAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineB3.turnOn();
        spaceship.jetEngineF3.turnOn();
        spaceship.jetEngineL3.turnOn();
        spaceship.jetEngineR3.turnOn();

        spaceship.jetEngineB3.isActive = true;
        spaceship.jetEngineF3.isActive = true;
        spaceship.jetEngineL3.isActive = true;
        spaceship.jetEngineR3.isActive = true;
    }

}
