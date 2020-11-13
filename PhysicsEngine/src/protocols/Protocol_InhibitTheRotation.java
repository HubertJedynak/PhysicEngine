package protocols;

import elements.Spaceship;
import javafx.geometry.Point3D;

public class Protocol_InhibitTheRotation implements Protocol {

    Spaceship spaceship;

    public Protocol_InhibitTheRotation(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    @Override
    public void turnOn() {

        Point3D vecB0 = new Point3D(
                spaceship.b0.x.get(),
                spaceship.b0.y.get(),
                spaceship.b0.z.get()
        );

        Point3D vecBu = new Point3D(
                spaceship.bu.x.get(),
                spaceship.bu.y.get(),
                spaceship.bu.z.get()
        );

        Point3D nY = (vecB0.subtract(vecBu)).normalize();
        double vecY = nY.dotProduct(spaceship.chain.W);

        Point3D vecBrm2 = new Point3D(
                spaceship.brm2.x.get(),
                spaceship.brm2.y.get(),
                spaceship.brm2.z.get()
        );

        Point3D nX = (vecBrm2.subtract(vecB0)).normalize();
        double vecX = nX.dotProduct(spaceship.chain.W);

        Point3D vecBfm2 = new Point3D(
                spaceship.bfm2.x.get(),
                spaceship.bfm2.y.get(),
                spaceship.bfm2.z.get()
        );

        Point3D nZ = (vecBfm2.subtract(vecB0)).normalize();
        double vecZ = nZ.dotProduct(spaceship.chain.W);

        y(vecY);
        x(vecX);
        z(vecZ);
        if (spaceship.chain.W.magnitude() < 1e-3) {
            spaceship.chain.W = Point3D.ZERO;
        }
    }

    private void y(double value) {
        if (value < 0) {
            spaceship.jetEngineL1.turnOn();
            spaceship.jetEngineR1.turnOn();
            spaceship.jetEngineF1.turnOn();
            spaceship.jetEngineB1.turnOn();

            spaceship.jetEngineL1.isActive = true;
            spaceship.jetEngineR1.isActive = true;
            spaceship.jetEngineF1.isActive = true;
            spaceship.jetEngineB1.isActive = true;

        }

        if (value > 0) {
            spaceship.jetEngineL3.turnOn();
            spaceship.jetEngineR3.turnOn();
            spaceship.jetEngineF3.turnOn();
            spaceship.jetEngineB3.turnOn();

            spaceship.jetEngineL3.isActive = true;
            spaceship.jetEngineR3.isActive = true;
            spaceship.jetEngineF3.isActive = true;
            spaceship.jetEngineB3.isActive = true;

        }
    }

    private void x(double value) {
        if (value > 0) {
            spaceship.jetEngineFU.turnOn();
            spaceship.jetEngineBD.turnOn();

            spaceship.jetEngineFU.isActive = true;
            spaceship.jetEngineBD.isActive = true;


        }
        if (value < 0) {
            spaceship.jetEngineFD.turnOn();
            spaceship.jetEngineBU.turnOn();

            spaceship.jetEngineFD.isActive = true;
            spaceship.jetEngineBU.isActive = true;

        }

    }

    private void z(double value) {
        if (value > 0) {
            spaceship.jetEngineLU.turnOn();
            spaceship.jetEngineRD.turnOn();

            spaceship.jetEngineLU.isActive = true;
            spaceship.jetEngineRD.isActive = true;

        }
        if (value < 0) {
            spaceship.jetEngineLD.turnOn();
            spaceship.jetEngineRU.turnOn();

            spaceship.jetEngineLD.isActive = true;
            spaceship.jetEngineRU.isActive = true;

        }

    }


}
