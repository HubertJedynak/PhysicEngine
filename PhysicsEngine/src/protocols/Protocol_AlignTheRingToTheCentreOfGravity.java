package protocols;

import elements.Spaceship;
import javafx.geometry.Point3D;

public class Protocol_AlignTheRingToTheCentreOfGravity implements Protocol {
    Spaceship spaceship;

    public Protocol_AlignTheRingToTheCentreOfGravity(Spaceship spaceship) {
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

        Point3D n = (vecBu.subtract(vecB0)).normalize();
        double projectionVecB0_CentreOfGravity_on_n = (spaceship.chain.centreOfGravity.subtract(vecB0)).dotProduct(n);

        if(projectionVecB0_CentreOfGravity_on_n > 0){
            spaceship.linearMotorRing.moveInside();
        }
        if(projectionVecB0_CentreOfGravity_on_n < 0){
            spaceship.linearMotorRing.moveOutside();
        }

    }
}
