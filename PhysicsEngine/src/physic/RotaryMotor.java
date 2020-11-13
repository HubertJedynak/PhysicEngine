package physic;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RotaryMotor {

    public Ball bEngine;
    public double WEngine;
    public Chain c;
    public List<Ball> ballsList1, ballsList2;
    public List<Joint> jointList1, jointList2;
    public Ball b1, b2;
    public boolean isActive = false;
    public Point3D W1 = Point3D.ZERO, W2 = Point3D.ZERO;

    private static class DividedLists {
        List<Ball> ballList;
        List<Joint> jointList;

        public DividedLists(List<Ball> ballList, List<Joint> jointList) {
            this.ballList = ballList;
            this.jointList = jointList;
        }

    }

    private static DividedLists createDividedLists(Joint jEngine, Ball b) {
        List<Joint> dividedJointList = new ArrayList<>();
        List<Ball> dividedBallList = new ArrayList<>();

        // add b to list
        dividedBallList.add(b);

        Ball secondEngineBall;
        if (b != jEngine.b1) {
            secondEngineBall = jEngine.b1;
        } else if (b != jEngine.b2) {
            secondEngineBall = jEngine.b2;
        } else {
            return null;
        }

        // add all joints of b except jEngine
        for (Joint i : b.connectedJoints) {
            // except jEngine
            if (i != jEngine) {
                // add all joints
                dividedJointList.add(i);
                // add balls
                if (b != i.b1) {
                    dividedBallList.add(i.b1);
                } else if (b != i.b2) {
                    dividedBallList.add(i.b2);
                }
            }
        }

        // finish if dividedJointList is empty
        if (dividedJointList.size() == 0) {
            return new DividedLists(dividedBallList, dividedJointList);
        }

        // from first ball (index = 1)
        for (int i = 1; i < dividedBallList.size(); i++) {
            // for all joints of given ball
            for (Joint ji : dividedBallList.get(i).connectedJoints) {
                // does ji belong to dividedJointList
                if (!dividedJointList.contains(ji)) {
                    dividedJointList.add(ji);
                    // adding ball hypothetically
                    Ball hipotheticBall = null;
                    if (ji.b1 != dividedBallList.get(i)) {
                        hipotheticBall = ji.b1;
                    } else if (ji.b2 != dividedBallList.get(i)) {
                        hipotheticBall = ji.b2;
                    }
                    if (hipotheticBall != null) {
                        // is hypotheticalBall the same as secondBall of engine
                        if (hipotheticBall == secondEngineBall) {
                            // We cannot create engine because chain is closed
                            return null;
                        }

                        // add if in the list already is hypotheticall ball
                        if (!dividedBallList.contains(hipotheticBall)) {
                            dividedBallList.add(hipotheticBall);
                        }
                    }
                }
            }
        }

        return new DividedLists(dividedBallList, dividedJointList);
    }


    public RotaryMotor(Ball bEngine, Joint j1, Joint j2, Chain c, double WEngine) {

        this.bEngine = bEngine;
        this.WEngine = WEngine;

        this.c = c;
        this.c.rotaryMotorList.add(this);


        // checking, which is b1 and b2
        if (j1.b1 != bEngine) {
            this.b1 = j1.b1;
        } else if (j1.b2 != bEngine) {
            this.b1 = j1.b2;
        } else {
            System.err.println("joints are not correct");
        }

        if (j2.b1 != bEngine) {
            this.b2 = j2.b1;
        } else if (j2.b2 != bEngine) {
            this.b2 = j2.b2;
        } else {
            System.err.println("joints are not correct");
        }

        // creating jointlists and balllists
        DividedLists dividedLists = createDividedLists(j1, this.b1);
        this.jointList1 = dividedLists.jointList;
        this.ballsList1 = dividedLists.ballList;

        dividedLists = createDividedLists(j2, this.b2);
        this.jointList2 = dividedLists.jointList;
        this.ballsList2 = dividedLists.ballList;

        this.jointList1.add(j1);
        this.jointList2.add(j2);

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }
        this.c.centreOfGravity = sumRM.multiply(1 / sumM);
        this.c.m = sumM;

    }


    public RotaryMotor(Ball bEngine, Joint j1, Joint j2, Ball b1, Ball b2, List<Joint> jointList1, List<Joint> jointList2, Chain c, double WEngine) {

        this.bEngine = bEngine;
        this.WEngine = WEngine;
        this.jointList1 = jointList1;
        this.jointList2 = jointList2;
        this.b1 = b1;
        this.b2 = b2;
        this.c = c;
        this.c.rotaryMotorList.add(this);

        Set<Ball> set1 = new LinkedHashSet<>();

        for (Joint i : jointList1) {
            set1.add(i.b1);
            set1.add(i.b2);
        }
        set1.remove(this.bEngine);
        ballsList1 = set1.stream().collect(Collectors.toList());

        Set<Ball> set2 = new LinkedHashSet<>();

        for (Joint i : jointList2) {
            set2.add(i.b1);
            set2.add(i.b2);
        }
        set2.remove(this.bEngine);
        ballsList2 = set2.stream().collect(Collectors.toList());

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }
        this.c.centreOfGravity = sumRM.multiply(1 / sumM);
        this.c.m = sumM;

    }

    public RotaryMotor(Ball bEngine, Joint j1, Joint j2, Chain c, List<Ball> ballsList1, Ball b1, List<Ball> ballsList2, Ball b2, double WEngine) {

        this.bEngine = bEngine;
        this.WEngine = WEngine;
        this.ballsList1 = ballsList1;
        this.ballsList2 = ballsList2;
        this.b1 = b1;
        this.b2 = b2;
        this.c = c;

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }
        this.c.centreOfGravity = sumRM.multiply(1 / sumM);
        this.c.m = sumM;
    }

    public void moveOutside() {

        Point3D oldCentreOfGravity = c.centreOfGravity;

        // old angular momentum L=I*W (it's necessary to update angular velocity - conservation of angular momentum)
        Point3D oldW = c.W;
        double oldI = 0;
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            oldI = c.m * k;
        }

        // direction of engine movement
        Point3D bb1 = new Point3D(
                b1.x.get() - bEngine.x.get(),
                b1.y.get() - bEngine.y.get(),
                b1.z.get() - bEngine.z.get()
        );


        Point3D bb2 = new Point3D(
                b2.x.get() - bEngine.x.get(),
                b2.y.get() - bEngine.y.get(),
                b2.z.get() - bEngine.z.get()
        );

        Point3D n = (bb1.crossProduct(bb2)).normalize();

        //FOR ROTARY MOTOR
        //WARNING: SINGULAR POINTS FOR ANGLE = 0 AND ANGLE = 180
        Point3D vecWEngine = n.multiply(-WEngine);

        if (bb1.normalize().dotProduct(bb2.normalize()) < -0.999) {
            W1 = Point3D.ZERO;
            W2 = Point3D.ZERO;
            return;
        }

        // calculation for all perpendicular r and Ia for A (ballist 1)
        List<Point3D> rAPerpendicularList = new ArrayList<>();
        double Ia = 0;
        for (Ball i : ballsList1) {
            // vektor form this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rAPerpendicularList.add(rAPerpendicular);
            Ia += i.m * rAPerpendicular.magnitude() * rAPerpendicular.magnitude();
        }

        // calculation for all perpendicular r and Ib for B (ballist 2)
        List<Point3D> rBPerpendicularList = new ArrayList<>();
        double Ib = 0;
        for (Ball i : ballsList2) {
            // vektor from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rBPerpendicularList.add(rBPerpendicular);
            Ib += i.m * rBPerpendicular.magnitude() * rBPerpendicular.magnitude();
        }

        // calculation of Wa i Wb
        Point3D Wb = vecWEngine.multiply(-Ia / (Ia + Ib));
        Point3D Wa = vecWEngine.add(Wb);

        // calculation for all Va and adding
        for (int i = 0; i < ballsList1.size(); i++) {
            Point3D V = Wa.crossProduct(rAPerpendicularList.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // calculation for all Vb and adding
        for (int i = 0; i < ballsList2.size(); i++) {
            Point3D V = Wb.crossProduct(rBPerpendicularList.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // distance correction
        for (int i = 0; i < ballsList1.size(); i++) {
            // vektor from this to i
            Point3D rEngineI = new Point3D(
                    ballsList1.get(i).x.get() - bEngine.x.get(),
                    ballsList1.get(i).y.get() - bEngine.y.get(),
                    ballsList1.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRAPerpendicular.magnitude() - rAPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRAPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList1.get(i).addToX(correctionVector.getX());
            ballsList1.get(i).addToY(correctionVector.getY());
            ballsList1.get(i).addToZ(correctionVector.getZ());
        }

        for (int i = 0; i < ballsList2.size(); i++) {
            // vektor from this to i
            Point3D rEngineI = new Point3D(
                    ballsList2.get(i).x.get() - bEngine.x.get(),
                    ballsList2.get(i).y.get() - bEngine.y.get(),
                    ballsList2.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRBPerpendicular.magnitude() - rBPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRBPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList2.get(i).addToX(correctionVector.getX());
            ballsList2.get(i).addToY(correctionVector.getY());
            ballsList2.get(i).addToZ(correctionVector.getZ());
        }

        // calculation of new centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }

        Point3D newCentreOfGravity = sumRM.multiply(1 / sumM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

        // new angular velocity W is caused by shape of chain change (conservation of angular momentum)
        // warning: it's continuation of conservation of angular momentum, so We have to check again is oldW = 0
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double newI = c.m * k;

            // newW
            // warning: check if newI = 0
            if (newI != 0) {
                c.W = oldW.multiply(oldI / newI);
            }
        }
        // necessary to collision
        W1 = Wa;
        W2 = Wb;
    }


    public void moveInside() {

        Point3D oldCentreOfGravity = c.centreOfGravity;

        // old angular momentum L=I*W (it's necessary to update angular velocity - conservation of angular momentum)
        Point3D oldW = c.W;
        double oldI = 0;
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            oldI = c.m * k;
        }

        // direction of engine movement
        Point3D bb1 = new Point3D(
                b1.x.get() - bEngine.x.get(),
                b1.y.get() - bEngine.y.get(),
                b1.z.get() - bEngine.z.get()
        );


        Point3D bb2 = new Point3D(
                b2.x.get() - bEngine.x.get(),
                b2.y.get() - bEngine.y.get(),
                b2.z.get() - bEngine.z.get()
        );

        Point3D n = (bb1.crossProduct(bb2)).normalize();
        //FOR ROTARY MOTOR
        //WARNING: SINGULAR POINTS FOR ANGLE = 0 AND ANGLE = 180
        Point3D vecWEngine = n.multiply(WEngine);
        if (bb1.normalize().dotProduct(bb2.normalize()) > 0.999) {
            W1 = Point3D.ZERO;
            W2 = Point3D.ZERO;
            return;
        }

        // calculation for all perpendicular r and Ia for A (ballist 1)
        List<Point3D> rAPerpendicularList = new ArrayList<>();
        double Ia = 0;
        for (Ball i : ballsList1) {
            // vektor form this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rAPerpendicularList.add(rAPerpendicular);
            Ia += i.m * rAPerpendicular.magnitude() * rAPerpendicular.magnitude();
        }


        // calculation for all perpendicular r and Ib for B (ballist 2)
        List<Point3D> rBPerpendicullarList = new ArrayList<>();
        double Ib = 0;
        for (Ball i : ballsList2) {
            // vektor from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rBPerpendicullarList.add(rBPerpendicular);
            Ib += i.m * rBPerpendicular.magnitude() * rBPerpendicular.magnitude();
        }

        // calculation of Wa i Wb
        Point3D Wb = vecWEngine.multiply(-Ia / (Ia + Ib));
        Point3D Wa = vecWEngine.add(Wb);

        // calculation for all Va and adding
        for (int i = 0; i < ballsList1.size(); i++) {
            Point3D V = Wa.crossProduct(rAPerpendicularList.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // calculation for all Vb and adding
        for (int i = 0; i < ballsList2.size(); i++) {
            Point3D V = Wb.crossProduct(rBPerpendicullarList.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // distance correction
        for (int i = 0; i < ballsList1.size(); i++) {
            // vector from this to i

            Point3D rEngineI = new Point3D(
                    ballsList1.get(i).x.get() - bEngine.x.get(),
                    ballsList1.get(i).y.get() - bEngine.y.get(),
                    ballsList1.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRAPerpendicular.magnitude() - rAPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRAPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList1.get(i).addToX(correctionVector.getX());
            ballsList1.get(i).addToY(correctionVector.getY());
            ballsList1.get(i).addToZ(correctionVector.getZ());
        }

        for (int i = 0; i < ballsList2.size(); i++) {
            // vector from this to i

            Point3D rEngineI = new Point3D(
                    ballsList2.get(i).x.get() - bEngine.x.get(),
                    ballsList2.get(i).y.get() - bEngine.y.get(),
                    ballsList2.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRBPerpendicular.magnitude() - rBPerpendicullarList.get(i).magnitude();
            Point3D correctionVector = (newRBPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList2.get(i).addToX(correctionVector.getX());
            ballsList2.get(i).addToY(correctionVector.getY());
            ballsList2.get(i).addToZ(correctionVector.getZ());
        }

        // calculation of new centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }

        Point3D newCentreOfGravity = sumRM.multiply(1 / sumM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

        // new angular velocity W is caused by shape of chain change (conservation of angular momentum)
        // warning: it's continuation of conservation of angular momentum, so We have to check again is oldW = 0
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double newI = c.m * k;

            // newW
            // warning: check if newI = 0
            if (newI != 0) {
                c.W = oldW.multiply(oldI / newI);
            }
        }
        // necessary to collision
        W1 = Wa;
        W2 = Wb;
    }

    public void moveLeftAlongTheAxis() {

        Point3D oldCentreOfGravity = c.centreOfGravity;

        // old angular momentum L=I*W (it's necessary to update angular velocity - conservation of angular momentum)
        Point3D oldW = c.W;
        double oldI = 0;
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            oldI = c.m * k;
        }

        // direction of engine movement
        Point3D bb1 = new Point3D(
                b1.x.get() - bEngine.x.get(),
                b1.y.get() - bEngine.y.get(),
                b1.z.get() - bEngine.z.get()
        );


        Point3D n = bb1.normalize();

        //FOR ROTARY MOTOR
        //WARNING: SINGULAR POINTS FOR ANGLE = 0 AND ANGLE = 180
        Point3D vecWEngine = n.multiply(-WEngine);


        // calculation for all perpendicular r and Ia for A (ballist 1)
        List<Point3D> rAPerpendicularList = new ArrayList<>();
        double Ia = 0;
        for (Ball i : ballsList1) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rAPerpendicularList.add(rAPerpendicular);
            Ia += i.m * rAPerpendicular.magnitude() * rAPerpendicular.magnitude();
        }


        // calculation for all perpendicular r and Ib for B (ballist 2)
        List<Point3D> rBPerpendicularList = new ArrayList<>();
        double Ib = 0;
        for (Ball i : ballsList2) {
            // vektor from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rBPerpendicularList.add(rBPerpendicular);
            Ib += i.m * rBPerpendicular.magnitude() * rBPerpendicular.magnitude();
        }

        // calculation of Wa i Wb
        Point3D Wb = vecWEngine.multiply(-Ia / (Ia + Ib));
        Point3D Wa = vecWEngine.add(Wb);

        // calculation for all Va and adding
        for (int i = 0; i < ballsList1.size(); i++) {
            Point3D V = Wa.crossProduct(rAPerpendicularList.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // calculation for all Vb and adding
        for (int i = 0; i < ballsList2.size(); i++) {
            Point3D V = Wb.crossProduct(rBPerpendicularList.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // distance correction
        for (int i = 0; i < ballsList1.size(); i++) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    ballsList1.get(i).x.get() - bEngine.x.get(),
                    ballsList1.get(i).y.get() - bEngine.y.get(),
                    ballsList1.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRAPerpendicular.magnitude() - rAPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRAPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList1.get(i).addToX(correctionVector.getX());
            ballsList1.get(i).addToY(correctionVector.getY());
            ballsList1.get(i).addToZ(correctionVector.getZ());
        }

        for (int i = 0; i < ballsList2.size(); i++) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    ballsList2.get(i).x.get() - bEngine.x.get(),
                    ballsList2.get(i).y.get() - bEngine.y.get(),
                    ballsList2.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRBPerpendicular.magnitude() - rBPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRBPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList2.get(i).addToX(correctionVector.getX());
            ballsList2.get(i).addToY(correctionVector.getY());
            ballsList2.get(i).addToZ(correctionVector.getZ());
        }

        // calculation of new centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }

        Point3D newCentreOfGravity = sumRM.multiply(1 / sumM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

        // new angular velocity W is caused by shape of chain change (conservation of angular momentum)
        // warning: it's continuation of conservation of angular momentum, so We have to check again is oldW = 0
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double newI = c.m * k;

            // newW
            // warning: check if newI = 0
            if (newI != 0) {
                c.W = oldW.multiply(oldI / newI);
            }
        }
        // necessary to collision
        W1 = Wa;
        W2 = Wb;
    }

    public void moveRightAlongTheAxis() {

        Point3D oldCentreOfGravity = c.centreOfGravity;

        // old angular momentum L=I*W (it's necessary to update angular velocity - conservation of angular momentum)
        Point3D oldW = c.W;
        double oldI = 0;
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            oldI = c.m * k;
        }

        // direction of engine movement
        Point3D bb1 = new Point3D(
                b1.x.get() - bEngine.x.get(),
                b1.y.get() - bEngine.y.get(),
                b1.z.get() - bEngine.z.get()
        );

        Point3D n = bb1.normalize().multiply(-1);

        //FOR ROTARY MOTOR
        //WARNING: SINGULAR POINTS FOR ANGLE = 0 AND ANGLE = 180
        Point3D vecWEngine = n.multiply(-WEngine);

        // calculation for all perpendicular r and Ia for A (ballist 1)
        List<Point3D> rAPerpendicularList = new ArrayList<>();
        double Ia = 0;
        for (Ball i : ballsList1) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rAPerpendicularList.add(rAPerpendicular);
            Ia += i.m * rAPerpendicular.magnitude() * rAPerpendicular.magnitude();
        }


        // calculation for all perpendicular r and Ib for B (ballist 2)
        List<Point3D> rBPerpendicularList = new ArrayList<>();
        double Ib = 0;
        for (Ball i : ballsList2) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    i.x.get() - bEngine.x.get(),
                    i.y.get() - bEngine.y.get(),
                    i.z.get() - bEngine.z.get()
            );
            Point3D rBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            rBPerpendicularList.add(rBPerpendicular);
            Ib += i.m * rBPerpendicular.magnitude() * rBPerpendicular.magnitude();
        }

        // calculation of Wa i Wb
        Point3D Wb = vecWEngine.multiply(-Ia / (Ia + Ib));
        Point3D Wa = vecWEngine.add(Wb);

        // calculation for all Va and adding
        for (int i = 0; i < ballsList1.size(); i++) {
            Point3D V = Wa.crossProduct(rAPerpendicularList.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // calculation for all Vb and adding
        for (int i = 0; i < ballsList2.size(); i++) {
            Point3D V = Wb.crossProduct(rBPerpendicularList.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // distance correction
        for (int i = 0; i < ballsList1.size(); i++) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    ballsList1.get(i).x.get() - bEngine.x.get(),
                    ballsList1.get(i).y.get() - bEngine.y.get(),
                    ballsList1.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRAPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRAPerpendicular.magnitude() - rAPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRAPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList1.get(i).addToX(correctionVector.getX());
            ballsList1.get(i).addToY(correctionVector.getY());
            ballsList1.get(i).addToZ(correctionVector.getZ());
        }

        for (int i = 0; i < ballsList2.size(); i++) {
            // vector from this to i
            Point3D rEngineI = new Point3D(
                    ballsList2.get(i).x.get() - bEngine.x.get(),
                    ballsList2.get(i).y.get() - bEngine.y.get(),
                    ballsList2.get(i).z.get() - bEngine.z.get()
            );
            Point3D newRBPerpendicular = rEngineI.subtract(n.multiply(n.dotProduct(rEngineI)));
            double valueOfCorrectionVector = newRBPerpendicular.magnitude() - rBPerpendicularList.get(i).magnitude();
            Point3D correctionVector = (newRBPerpendicular.normalize()).multiply(-valueOfCorrectionVector);

            ballsList2.get(i).addToX(correctionVector.getX());
            ballsList2.get(i).addToY(correctionVector.getY());
            ballsList2.get(i).addToZ(correctionVector.getZ());
        }

        // calculation of new centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball i : c.ballsList) {
            sumRM = sumRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumM += i.m;
        }

        Point3D newCentreOfGravity = sumRM.multiply(1 / sumM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

        // new angular velocity W is caused by shape of chain change (conservation of angular momentum)
        // warning: it's continuation of conservation of angular momentum, so We have to check again is oldW = 0
        if (oldW.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRPerpendicular = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double newI = c.m * k;

            // newW
            // warning: check if newI = 0
            if (newI != 0) {
                c.W = oldW.multiply(oldI / newI);
            }
        }
        // necessary to collision
        W1 = Wa;
        W2 = Wb;
    }

    public void setWEngine(double WEngine) {
        this.WEngine = WEngine;
    }

    public double getWEngine() {
        return WEngine;
    }
}
