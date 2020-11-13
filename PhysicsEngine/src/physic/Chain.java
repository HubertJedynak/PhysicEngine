package physic;

import javafx.geometry.Point3D;

import java.util.*;
import java.util.stream.Collectors;


public class Chain {

    public List<Joint> jointList;
    public List<Ball> ballsList;
    public List<LinearMotor> linearMotorList;
    public List<RotaryMotor> rotaryMotorList;


    public double m;
    public Point3D centreOfGravity;
    public Point3D W = new Point3D(0, 0, 0); //angular velocity
    public Point3D V = new Point3D(0, 0, 0); //linear velocity


    public Chain(Ball b) {
        BallsAndJointsLists ballsAndJointsLists = createBallsAndJointsLists(b);
        ballsList = ballsAndJointsLists.ballList;
        jointList = ballsAndJointsLists.jointList;

        this.linearMotorList = new ArrayList<>();
        this.rotaryMotorList = new ArrayList<>();

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball bi : ballsList) {
            sumRM = sumRM.add(new Point3D(bi.x.get(), bi.y.get(), bi.z.get()).multiply(bi.m));
            sumM += bi.m;
        }
        centreOfGravity = sumRM.multiply(1 / sumM);
        this.m = sumM;

    }

    public Chain(Joint... jointList) {
        this.jointList = new ArrayList<>(Arrays.asList(jointList));
        this.ballsList = new ArrayList<>();

        this.linearMotorList = new ArrayList<>();
        this.rotaryMotorList = new ArrayList<>();

        Set<Ball> set = new LinkedHashSet<>();

        for (Joint j : jointList) {
            set.add(j.b1);
            set.add(j.b2);
        }

        ballsList = set.stream().collect(Collectors.toList());

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball b : ballsList) {
            sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
            sumM += b.m;
        }
        centreOfGravity = sumRM.multiply(1 / sumM);
        this.m = sumM;
    }

    public Chain(List<Joint> jointList) {
        this.jointList = jointList;
        this.ballsList = new ArrayList<>();

        this.linearMotorList = new ArrayList<>();
        this.rotaryMotorList = new ArrayList<>();

        Set<Ball> set = new LinkedHashSet<>();

        for (Joint j : jointList) {
            set.add(j.b1);
            set.add(j.b2);
        }

        ballsList = set.stream().collect(Collectors.toList());

        // centre of gravity
        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball b : ballsList) {
            sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
            sumM += b.m;
        }
        centreOfGravity = sumRM.multiply(1 / sumM);
        this.m = sumM;
    }


    public void setBallsVCausedByChain() {

        double modW = W.magnitude();
        if (modW != 0) {
            Point3D versorW = W.multiply(1 / modW);
            for (Ball b : ballsList) {

                // vektor form centre of gravity do ball
                Point3D cRb = centreOfGravity.subtract(b.x.get(), b.y.get(), b.z.get()).multiply(-1);

                // calculation of value of perpendicular to W vector, which is the projection of cRb (vectorValueFromRonWandW)
                // BEFORE ADDING VELOCITY cVb
                // calculations necessary to distance correction after adding velocity

                double valueOfProjectionRonW = versorW.dotProduct(cRb);
                double modcRb = cRb.magnitude();
                double valueOfVectorBetweenRonWandR = Math.sqrt(modcRb * modcRb - valueOfProjectionRonW * valueOfProjectionRonW);//pitagoras

                // calculation of velocity caused by rotation
                Point3D cVb = W.crossProduct(cRb);

                b.addToX(cVb.getX());
                b.addToY(cVb.getY());
                b.addToZ(cVb.getZ());

                // calculation of value of perpendicular to W vector, which is the projection of cRb (vectorValueFromRonWandW)
                // AFTER ADDING VELOCITY cVb
                // calculations necessary to distance correction after adding velocity
                cRb = centreOfGravity.subtract(b.x.get(), b.y.get(), b.z.get()).multiply(-1);
                valueOfProjectionRonW = versorW.dotProduct(cRb);
                Point3D projectionRonW = versorW.multiply(valueOfProjectionRonW);
                Point3D vectorFromRtoRonW = projectionRonW.subtract(cRb);
                double valueOfCorrectionVector = vectorFromRtoRonW.magnitude() - valueOfVectorBetweenRonWandR;
                Point3D correctionVector = (vectorFromRtoRonW.normalize()).multiply(valueOfCorrectionVector);

                // prevention against changing distance caused by rotation

                b.addToX(correctionVector.getX());
                b.addToY(correctionVector.getY());
                b.addToZ(correctionVector.getZ());

            }
        }

        // actualisation for V
        for (Ball b : ballsList) {
            b.addToX(V.getX());
            b.addToY(V.getY());
            b.addToZ(V.getZ());
        }

        // CENTRE OF GRAVITY ACTUALISATION

        Point3D sumRM = new Point3D(0, 0, 0);
        double sumM = 0;
        for (Ball b : ballsList) {
            sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
            sumM += b.m;
        }
        centreOfGravity = sumRM.multiply(1 / sumM);

    }

    public void move(Point3D vec) {
        for (Ball b : ballsList) {
            b.addToX(vec.getX());
            b.addToY(vec.getY());
            b.addToZ(vec.getZ());
        }
        centreOfGravity = centreOfGravity.add(vec);
    }

    private static class BallsAndJointsLists {
        List<Ball> ballList;
        List<Joint> jointList;

        public BallsAndJointsLists(List<Ball> ballList, List<Joint> jointList) {
            this.ballList = ballList;
            this.jointList = jointList;
        }

    }

    private static BallsAndJointsLists createBallsAndJointsLists(Ball b) {
        List<Joint> jointList = new ArrayList<>();
        List<Ball> ballList = new ArrayList<>();

        // add b to list
        ballList.add(b);

        // add all joints of b
        for (Joint i : b.connectedJoints) {
            // add all joints
            jointList.add(i);
            // add balls
            if (b != i.b1) {
                ballList.add(i.b1);
            } else if (b != i.b2) {
                ballList.add(i.b2);
            }

        }

        // finish if dividedJointList is empty
        if (jointList.size() == 0) {
            return new BallsAndJointsLists(ballList, jointList);
        }

        // loop for first ball (index 1)
        for (int i = 1; i < ballList.size(); i++) {
            // for all joints of ball
            for (Joint ji : ballList.get(i).connectedJoints) {
                // does ji belong to dividedJointList
                if (!jointList.contains(ji)) {
                    jointList.add(ji);
                    //adding ball hypothetically
                    Ball hipotheticalBall = null;
                    if (ji.b1 != ballList.get(i)) {
                        hipotheticalBall = ji.b1;
                    } else if (ji.b2 != ballList.get(i)) {
                        hipotheticalBall = ji.b2;
                    }
                    if (hipotheticalBall != null) {
                        // add if there is not hipoteticBall inside the list
                        if (!ballList.contains(hipotheticalBall)) {
                            ballList.add(hipotheticalBall);
                        }
                    }
                }
            }
        }
        return new BallsAndJointsLists(ballList, jointList);
    }

}
