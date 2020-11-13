package protocols;

import elements.Spaceship;
import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import physic.Ball;
import physic.Chain;

public class Protocol_EquateToVelocityOfTheChain implements Protocol {

    Spaceship spaceship;
    Chain trackedChain;


    public Protocol_EquateToVelocityOfTheChain(Spaceship spaceship, Chain trackedChain) {
        this.spaceship = spaceship;
        this.trackedChain = trackedChain;
    }

    @Override
    public void turnOn() {

        // versors calculation

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

        Point3D vecBrm2 = new Point3D(
                spaceship.brm2.x.get(),
                spaceship.brm2.y.get(),
                spaceship.brm2.z.get()
        );

        Point3D vecBfm2 = new Point3D(
                spaceship.bfm2.x.get(),
                spaceship.bfm2.y.get(),
                spaceship.bfm2.z.get()
        );

        Point3D nX = (vecBrm2.subtract(vecB0)).normalize();

        Point3D ny = (vecB0.subtract(vecBu)).normalize();

        Point3D nZ = (vecBfm2.subtract(vecB0)).normalize();

        Point3D vecV = Point3D.ZERO;
        if (trackedChain != null) {
            vecV = trackedChain.V.subtract(spaceship.chain.V);
        }

        double vecX = nX.dotProduct(vecV);
        double vecY = ny.dotProduct(vecV);
        double vecZ = nZ.dotProduct(vecV);

        x(vecX);
        y(vecY);
        z(vecZ);

    }

    private void x(double value) {

        double absValue = Math.abs(value);
        double oldJr = spaceship.jetEngineF1.getJr();

        if (value > 0) {
            spaceship.jetEngineF1.setJr(2 * oldJr * absValue);
            spaceship.jetEngineB3.setJr(2 * oldJr * absValue);

            spaceship.jetEngineF1.turnOn();
            spaceship.jetEngineB3.turnOn();

            spaceship.jetEngineF1.isActive = true;
            spaceship.jetEngineB3.isActive = true;

            spaceship.jetEngineF1.setJr(oldJr);
            spaceship.jetEngineB3.setJr(oldJr);

        }

        if (value < 0) {
            spaceship.jetEngineF3.setJr(2 * oldJr * absValue);
            spaceship.jetEngineB1.setJr(2 * oldJr * absValue);

            spaceship.jetEngineF3.turnOn();
            spaceship.jetEngineB1.turnOn();

            spaceship.jetEngineF3.isActive = true;
            spaceship.jetEngineB1.isActive = true;

            spaceship.jetEngineF3.setJr(oldJr);
            spaceship.jetEngineB1.setJr(oldJr);

        }

    }

    private void y(double value) {

        double absValue = Math.abs(value);
        double oldJr = spaceship.jetEngineLU.getJr();

        if (value > 0) {
            spaceship.jetEngineLU.setJr(oldJr * absValue);
            spaceship.jetEngineRU.setJr(oldJr * absValue);
            spaceship.jetEngineFU.setJr(oldJr * absValue);
            spaceship.jetEngineBU.setJr(oldJr * absValue);

            spaceship.jetEngineLU.turnOn();
            spaceship.jetEngineRU.turnOn();
            spaceship.jetEngineFU.turnOn();
            spaceship.jetEngineBU.turnOn();

            spaceship.jetEngineLU.isActive = true;
            spaceship.jetEngineRU.isActive = true;
            spaceship.jetEngineFU.isActive = true;
            spaceship.jetEngineBU.isActive = true;

            spaceship.jetEngineLU.setJr(oldJr);
            spaceship.jetEngineRU.setJr(oldJr);
            spaceship.jetEngineFU.setJr(oldJr);
            spaceship.jetEngineBU.setJr(oldJr);
        }

        if (value < 0) {
            spaceship.jetEngineLD.setJr(oldJr * absValue);
            spaceship.jetEngineRD.setJr(oldJr * absValue);
            spaceship.jetEngineFD.setJr(oldJr * absValue);
            spaceship.jetEngineBD.setJr(oldJr * absValue);

            spaceship.jetEngineLD.turnOn();
            spaceship.jetEngineRD.turnOn();
            spaceship.jetEngineFD.turnOn();
            spaceship.jetEngineBD.turnOn();

            spaceship.jetEngineLD.isActive = true;
            spaceship.jetEngineRD.isActive = true;
            spaceship.jetEngineFD.isActive = true;
            spaceship.jetEngineBD.isActive = true;

            spaceship.jetEngineLD.setJr(oldJr);
            spaceship.jetEngineRD.setJr(oldJr);
            spaceship.jetEngineFD.setJr(oldJr);
            spaceship.jetEngineBD.setJr(oldJr);

        }

    }

    private void z(double value) {

        double absValue = Math.abs(value);
        double oldJr = spaceship.jetEngineL1.getJr();


        if (value > 0) {
            spaceship.jetEngineL1.setJr(2 * oldJr * absValue);
            spaceship.jetEngineR3.setJr(2 * oldJr * absValue);

            spaceship.jetEngineL1.turnOn();
            spaceship.jetEngineR3.turnOn();


            spaceship.jetEngineL1.isActive = true;
            spaceship.jetEngineR3.isActive = true;

            spaceship.jetEngineL1.setJr(oldJr);
            spaceship.jetEngineR3.setJr(oldJr);

        }

        if (value < 0) {
            spaceship.jetEngineR1.setJr(2 * oldJr * absValue);
            spaceship.jetEngineL3.setJr(2 * oldJr * absValue);

            spaceship.jetEngineR1.turnOn();
            spaceship.jetEngineL3.turnOn();


            spaceship.jetEngineR1.isActive = true;
            spaceship.jetEngineL3.isActive = true;


            spaceship.jetEngineR1.setJr(oldJr);
            spaceship.jetEngineL3.setJr(oldJr);

        }

    }

}
