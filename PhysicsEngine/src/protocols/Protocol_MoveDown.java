
package protocols;

import elements.Spaceship;

public class Protocol_MoveDown implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveDown(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineLU.turnOn();
        spaceship.jetEngineRU.turnOn();
        spaceship.jetEngineFU.turnOn();
        spaceship.jetEngineBU.turnOn();

        spaceship.jetEngineLU.isActive = true;
        spaceship.jetEngineRU.isActive = true;
        spaceship.jetEngineFU.isActive = true;
        spaceship.jetEngineBU.isActive = true;

    }

}
