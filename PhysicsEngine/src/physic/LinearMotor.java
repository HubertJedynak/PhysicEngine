package physic;

import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class LinearMotor {

    public Joint jEngine;
    public double VEngine;
    public Chain c;
    public List<Ball> ballsList1, ballsList2;
    public List<Joint> jointList1, jointList2;
    public Ball b1, b2;
    public boolean isActive = false;
    public Point3D V1 = Point3D.ZERO, V2 = Point3D.ZERO;

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
                    Ball hypotheticalBall = null;
                    if (ji.b1 != dividedBallList.get(i)) {
                        hypotheticalBall = ji.b1;
                    } else if (ji.b2 != dividedBallList.get(i)) {
                        hypotheticalBall = ji.b2;
                    }
                    if (hypotheticalBall != null) {
                        // is hypotheticalBall the same as secondBall of engine
                        if (hypotheticalBall == secondEngineBall) {
                            // We cannot create engine because chain is closed
                            return null;
                        }

                        // add if in the list already is hypotheticall ball
                        if (!dividedBallList.contains(hypotheticalBall)) {
                            dividedBallList.add(hypotheticalBall);
                        }
                    }
                }
            }
        }

        return new DividedLists(dividedBallList, dividedJointList);
    }

    public LinearMotor(Joint jEngine, Chain c, double VEngine) {
        this.jEngine = jEngine;

        this.VEngine = VEngine;
        this.b1 = jEngine.b1;
        this.b2 = jEngine.b2;
        this.c = c;
        this.c.linearMotorList.add(this);


        // creating jointlists and balllists
        DividedLists dividedLists = createDividedLists(jEngine, b1);
        this.jointList1 = dividedLists.jointList;
        this.ballsList1 = dividedLists.ballList;


        dividedLists = createDividedLists(jEngine, b2);
        this.jointList2 = dividedLists.jointList;
        this.ballsList2 = dividedLists.ballList;

    }

    public LinearMotor(Joint jEngine, Ball b1, Ball b2, List<Joint> jointList1, List<Joint> jointList2, Chain c, double VEngine) {
        this.jEngine = jEngine;

        this.VEngine = VEngine;
        this.jointList1 = jointList1;
        this.jointList2 = jointList2;
        this.b1 = b1;
        this.b2 = b2;
        this.c = c;
        this.c.linearMotorList.add(this);

        Set<Ball> set1 = new LinkedHashSet<>();

        for (Joint i : jointList1) {
            set1.add(i.b1);
            set1.add(i.b2);
        }
        ballsList1 = set1.stream().collect(Collectors.toList());

        Set<Ball> set2 = new LinkedHashSet<>();

        for (Joint i : jointList2) {
            set2.add(i.b1);
            set2.add(i.b2);
        }
        ballsList2 = set2.stream().collect(Collectors.toList());

    }

    public LinearMotor(Joint jEngine, Chain c, List<Ball> ballsList1, Ball b1, List<Ball> ballsList2, Ball b2, double VEngine) {
        this.jEngine = jEngine;

        this.VEngine = VEngine;
        this.ballsList1 = ballsList1;
        this.ballsList2 = ballsList2;
        this.b1 = b1;
        this.b2 = b2;
        this.c = c;
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
        Point3D n = new Point3D(
                b2.x.get() - b1.x.get(),
                b2.y.get() - b1.y.get(),
                b2.z.get() - b1.z.get()
        ).normalize();

        Point3D vec = n.multiply(-VEngine / 2);
        for (Ball b : ballsList1) {
            b.addToX(vec.getX());
            b.addToY(vec.getY());
            b.addToZ(vec.getZ());
        }

        vec = n.multiply(VEngine / 2);
        for (Ball b : ballsList2) {
            b.addToX(vec.getX());
            b.addToY(vec.getY());
            b.addToZ(vec.getZ());
        }
        List<Ball> list = new ArrayList<>(ballsList1);
        list.addAll(ballsList2);

        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball b : list) {
            sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
            sumM += b.m;
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
            // warning: chcek if newI = 0
            if (newI != 0) {
                c.W = oldW.multiply(oldI / newI);
            }
        }

        // calculation of V1 i V2
        double m1 = 0;
        for (Ball b : ballsList1) {
            m1 += b.m;
        }

        double m2 = 0;
        for (Ball b : ballsList2) {
            m2 += b.m;
        }

        vec = n.multiply(VEngine);
        V1 = vec.multiply(-m2 / (m1 + m2));
        V2 = vec.add(V1);
    }

    public void moveInside() {

        if (jEngine.getHeight() > VEngine * 2) {

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
            Point3D n = new Point3D(
                    b2.x.get() - b1.x.get(),
                    b2.y.get() - b1.y.get(),
                    b2.z.get() - b1.z.get()
            ).normalize();

            Point3D vec = n.multiply(VEngine / 2);
            for (Ball b : ballsList1) {
                b.addToX(vec.getX());
                b.addToY(vec.getY());
                b.addToZ(vec.getZ());
            }

            vec = n.multiply(-VEngine / 2);
            for (Ball b : ballsList2) {
                b.addToX(vec.getX());
                b.addToY(vec.getY());
                b.addToZ(vec.getZ());
            }
            List<Ball> list = new ArrayList<>(ballsList1);
            list.addAll(ballsList2);

            Point3D sumRM = new Point3D(0, 0, 0);
            double sumM = 0;
            for (Ball b : list) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
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
                // warning: chcek if newI = 0
                if (newI != 0) {
                    c.W = oldW.multiply(oldI / newI);
                }
            }

            // calculation of V1 i V2
            double m1 = 0;
            for (Ball b : ballsList1) {
                m1 += b.m;
            }

            double m2 = 0;
            for (Ball b : ballsList2) {
                m2 += b.m;
            }

            vec = n.multiply(-VEngine);
            V1 = vec.multiply(-m2 / (m1 + m2));
            V2 = vec.add(V1);
        }

    }

    public double getVEngine() {
        return VEngine;
    }

    public void setVEngine(double VEngine) {
        this.VEngine = VEngine;
    }
}
