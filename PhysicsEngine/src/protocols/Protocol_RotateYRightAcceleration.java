package protocols;

import elements.Spaceship;

public class Protocol_RotateYRightAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateYRightAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineB1.turnOn();
        spaceship.jetEngineF1.turnOn();
        spaceship.jetEngineL1.turnOn();
        spaceship.jetEngineR1.turnOn();

        spaceship.jetEngineB1.isActive = true;
        spaceship.jetEngineF1.isActive = true;
        spaceship.jetEngineL1.isActive = true;
        spaceship.jetEngineR1.isActive = true;
    }

}
