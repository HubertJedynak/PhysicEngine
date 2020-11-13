package protocols;

import elements.Spaceship;

public class Protocol_RotateXForwardAcceleration implements Protocol {

    Spaceship spaceship;

    public Protocol_RotateXForwardAcceleration(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineBD.turnOn();
        spaceship.jetEngineFU.turnOn();

        spaceship.jetEngineBD.isActive = true;
        spaceship.jetEngineFU.isActive = true;
    }

}
