package physic;

import javafx.geometry.Point3D;
import physicalTests.CollisionsOld;

public class JetEngine {
    public Ball bJointed;
    public Ball bJet;
    public Chain c;
    public double jr;
    public boolean isActive = false;

    public JetEngine(Chain c, Ball bJointed, Ball bJet, double momentum) {
        this.bJointed = bJointed;
        this.bJet = bJet;
        this.c = c;
        this.jr = momentum;
    }

    public void turnOnThrowingBallMethod() {

        // creating solid with non-zero inertia
        // 3 balls with radius = 1 and mass = 0.005
        double rBall = 1;
        double aBall = 1; // triangle height (created by balls) and half side square
        double mBall = 0.005;
        double Vchain = 5;
        Point3D vecBJoined = new Point3D(
                bJointed.x.get(),
                bJointed.y.get(),
                bJointed.z.get()
        );

        Point3D vecBEngine = new Point3D(
                bJet.x.get(),
                bJet.y.get(),
                bJet.z.get()
        );

        Point3D n = vecBEngine.subtract(vecBJoined).normalize();
        Point3D vecBFront = vecBEngine.add(n.multiply(bJet.getRadius() + rBall));

        Point3D np = n.crossProduct(new Point3D(0, 1, 0)).normalize(); //n perpendicular

        if (np.magnitude() == 0) {
            np = n.crossProduct(new Point3D(0, 0, 1)).normalize();
        }
        Point3D vecBBack1 = (vecBFront.add(n.multiply(aBall))).add(np.multiply(aBall));
        Point3D vecBBack2 = (vecBFront.add(n.multiply(aBall))).add(np.multiply(-aBall));

        Ball bFront = new Ball(
                vecBFront.getX(),
                vecBFront.getY(),
                vecBFront.getZ(),
                rBall,
                mBall
        );


        Ball bBack1 = new Ball(
                vecBBack1.getX(),
                vecBBack1.getY(),
                vecBBack1.getZ(),
                rBall,
                mBall
        );


        Ball bBack2 = new Ball(
                vecBBack2.getX(),
                vecBBack2.getY(),
                vecBBack2.getZ(),
                rBall,
                mBall
        );

        Joint j1 = new Joint(bFront, bBack1, rBall / 2);
        Joint j2 = new Joint(bFront, bBack2, rBall / 2);

        // creating chain colliding with engine
        Chain ci = new Chain(j1, j2);
        ci.V = n.multiply(-Vchain);

        CollisionsOld.Chains2BallsEngines.serveCollision(c, ci, bJet, bFront);

    }

    public void turnOn() {


        Point3D vecBJointed = new Point3D(
                bJointed.x.get(),
                bJointed.y.get(),
                bJointed.z.get()
        );

        Point3D vecBEngine = new Point3D(
                bJet.x.get(),
                bJet.y.get(),
                bJet.z.get()
        );

        Point3D n = vecBEngine.subtract(vecBJointed).normalize();

        Point3D collisionPoint = vecBEngine;
        Point3D r1 = collisionPoint.subtract(c.centreOfGravity);

        Matrix33 I1 = inertiaTensor(c, collisionPoint);

        c.V = c.V.add(n.multiply(-jr / c.m));

        // calculation of angular velocity W
        c.W = c.W.add(I1.inverseMultiply(r1.crossProduct(n)).multiply(-jr));


    }

    private static Matrix33 inertiaTensor(Chain c, Point3D collsionPoint) {

        double[][] I = new double[3][3];

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                double sum = 0;
                for (Ball b : c.ballsList) {
                    Point3D r = new Point3D(b.x.get(), b.y.get(), b.z.get()).subtract(collsionPoint);
                    double modR = r.magnitude();
                    double[] vecR = {r.getX(), r.getY(), r.getZ()};
                    sum += modR * modR * dirackImpulse(i, j) - vecR[i] * vecR[j];
                }
                I[i][j] = c.m * sum;
            }
        }
        return new Matrix33(I);
    }

    private static Matrix33 inertiaTensor1(Chain c) {

        double[][] I = new double[3][3];

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                double sum = 0;
                for (Ball b : c.ballsList) {
                    Point3D r = new Point3D(b.x.get(), b.y.get(), b.z.get());
                    double modR = r.magnitude();
                    double[] vecR = {r.getX(), r.getY(), r.getZ()};
                    sum += modR * modR * dirackImpulse(i, j) - vecR[i] * vecR[j];
                }
                I[i][j] = c.m * sum;
            }
        }
        return new Matrix33(I);
    }

    private static int dirackImpulse(int i, int j) {
        if (i != j) {
            return 0;
        } else {
            return 1;
        }
    }

    private static Point3D getVCausedByEngines(Chain c, Ball b, Point3D collisionPoint) {
        Point3D sumV = Point3D.ZERO;
        for (RotaryMotor i : c.rotaryMotorList) {

            if (i.isActive) {

                if (i.ballsList1.contains(b)) {
                    Point3D r = new Point3D(
                            collisionPoint.getX() - i.bEngine.x.get(),
                            collisionPoint.getY() - i.bEngine.y.get(),
                            collisionPoint.getZ() - i.bEngine.z.get()
                    );
                    Point3D V = i.W1.crossProduct(r);
                    sumV = sumV.add(V);
                } else if (i.ballsList2.contains(b)) {
                    Point3D r = new Point3D(
                            collisionPoint.getX() - i.bEngine.x.get(),
                            collisionPoint.getY() - i.bEngine.y.get(),
                            collisionPoint.getZ() - i.bEngine.z.get()
                    );
                    Point3D V = i.W2.crossProduct(r);
                    sumV = sumV.add(V);
                }
            }
        }

        for (LinearMotor i : c.linearMotorList) {
            if (i.isActive) {
                if (i.ballsList1.contains(b)) {
                    sumV = sumV.add(i.V1);
                } else if (i.ballsList2.contains(b)) {
                    sumV = sumV.add(i.V2);
                }
            }
        }
        return sumV;
    }


    public void setJr(double jr) {
        this.jr = jr;
    }

    public double getJr() {
        return jr;
    }
}
