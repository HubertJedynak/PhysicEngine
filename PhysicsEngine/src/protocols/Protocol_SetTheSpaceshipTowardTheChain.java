package protocols;

import elements.Spaceship;
import javafx.geometry.Point3D;
import javafx.scene.shape.Sphere;
import physic.Ball;
import physic.Chain;

public class Protocol_SetTheSpaceshipTowardTheChain implements Protocol {

    Spaceship spaceship;
    Chain trackedChain;


    public Protocol_SetTheSpaceshipTowardTheChain(Spaceship spaceship, Chain trackedChain) {
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

        Point3D nZ = (vecBfm2.subtract(vecB0)).normalize();

        Point3D vecPointed = null;
        if (trackedChain != null) {
            vecPointed = trackedChain.centreOfGravity.subtract(spaceship.chain.centreOfGravity);
        }

        double vecX = nX.dotProduct(vecPointed);
        double vecZ = nZ.dotProduct(vecPointed);

        x(vecX);
        z(vecZ);

    }

    private void x(double value) {
        if (value > 0) {
            spaceship.rotaryMotorB1.moveRightAlongTheAxis();
            spaceship.rotaryMotorB3.moveRightAlongTheAxis();
            spaceship.rotaryMotorF1.moveLeftAlongTheAxis();
            spaceship.rotaryMotorF3.moveLeftAlongTheAxis();

            spaceship.rotaryMotorB1.isActive = true;
            spaceship.rotaryMotorB3.isActive = true;
            spaceship.rotaryMotorF1.isActive = true;
            spaceship.rotaryMotorF3.isActive = true;


        }

        if (value < 0) {
            spaceship.rotaryMotorF1.moveRightAlongTheAxis();
            spaceship.rotaryMotorF3.moveRightAlongTheAxis();
            spaceship.rotaryMotorB1.moveLeftAlongTheAxis();
            spaceship.rotaryMotorB3.moveLeftAlongTheAxis();

            spaceship.rotaryMotorF1.isActive = true;
            spaceship.rotaryMotorF3.isActive = true;
            spaceship.rotaryMotorB1.isActive = true;
            spaceship.rotaryMotorB3.isActive = true;

        }

    }

    private void z(double value) {
        if (value > 0) {

            spaceship.rotaryMotorR1.moveRightAlongTheAxis();
            spaceship.rotaryMotorR3.moveRightAlongTheAxis();
            spaceship.rotaryMotorL1.moveLeftAlongTheAxis();
            spaceship.rotaryMotorL3.moveLeftAlongTheAxis();

            spaceship.rotaryMotorR1.isActive = true;
            spaceship.rotaryMotorR3.isActive = true;
            spaceship.rotaryMotorL1.isActive = true;
            spaceship.rotaryMotorL3.isActive = true;
        }
        if (value < 0) {

            spaceship.rotaryMotorL1.moveRightAlongTheAxis();
            spaceship.rotaryMotorL3.moveRightAlongTheAxis();
            spaceship.rotaryMotorR1.moveLeftAlongTheAxis();
            spaceship.rotaryMotorR3.moveLeftAlongTheAxis();

            spaceship.rotaryMotorL1.isActive = true;
            spaceship.rotaryMotorL3.isActive = true;
            spaceship.rotaryMotorR1.isActive = true;
            spaceship.rotaryMotorR3.isActive = true;

        }

    }


}
