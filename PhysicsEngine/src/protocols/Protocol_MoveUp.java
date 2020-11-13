package protocols;

import elements.Spaceship;

public class Protocol_MoveUp implements Protocol {

    Spaceship spaceship;

    public Protocol_MoveUp(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {
        spaceship.jetEngineMain.turnOn();
        spaceship.jetEngineLD.turnOn();
        spaceship.jetEngineRD.turnOn();
        spaceship.jetEngineFD.turnOn();
        spaceship.jetEngineBD.turnOn();

        spaceship.jetEngineMain.isActive = true;
        spaceship.jetEngineLD.isActive = true;
        spaceship.jetEngineRD.isActive = true;
        spaceship.jetEngineFD.isActive = true;
        spaceship.jetEngineBD.isActive = true;

    }

}
