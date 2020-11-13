package physic;

import javafx.geometry.Point3D;

import java.util.LinkedList;
import java.util.List;

public class Gripper {
    public Ball bGrabbing; // grabbing solids
    public Ball bJointed; // the gripper is joined here
    public Joint jGripper;
    public List<Chain> chainList;
    public Chain c; // chain with gripper
    public Chain grabbedChain; // grabbed chain

    List<Ball> oldBallList = new LinkedList<>();

    public Gripper(Chain c, Ball bGrabbing, Ball bJointed, Joint jGripper, List<Chain> chainList) {
        this.bGrabbing = bGrabbing;
        this.bJointed = bJointed;
        this.jGripper = jGripper;
        this.chainList = chainList;
        this.c = c;
        bGrabbing.isCollisionInteractive = false;
        jGripper.isCollisionInteractive = false;
        jGripper.isCollisionDetectable = false;
    }

    public void grab() {
        // checking if is something grabbed
        if (grabbedChain != null) {
            return;
        }
        // checking if is something in range to grab
        Collisions.CollisionDetectableElement collisionDetectableElement = Collisions.findCollisionDetectableElement(c, bGrabbing, chainList);
        if (collisionDetectableElement == null) {
            return;
        }
        // checking if the grabbed element is ball or joint (and grabs if collisionDetectable == true)
        if (collisionDetectableElement.b != null) {
            if (collisionDetectableElement.b.isCollisionDetectable) {
                grabTheBall(collisionDetectableElement.c, collisionDetectableElement.b);
            }
        } else if (collisionDetectableElement.j != null) {
            if (collisionDetectableElement.j.isCollisionDetectable) {
                grabTheJoint(collisionDetectableElement.c, collisionDetectableElement.j);
            }
        }

    }

    public void grabTheBall(Chain cgrabbed, Ball bgrabbed) {
        this.grabbedChain = cgrabbed;
        chainList.remove(grabbedChain);

        oldBallList.addAll(c.ballsList);

        Point3D newV = (c.V.multiply(c.m)).add(grabbedChain.V.multiply(grabbedChain.m)).multiply(1 / (c.m + grabbedChain.m));

        // calculation of angular momentum
        // if W = 0 -> L = 0
        Point3D Lc;
        if (c.W.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - c.centreOfGravity.getX(),
                        b.y.get() - c.centreOfGravity.getY(),
                        b.z.get() - c.centreOfGravity.getZ()
                );
                double modRPerpendicular = (c.W.crossProduct(r)).magnitude() / c.W.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double Ic = c.m * k;
            Lc = c.W.multiply(Ic);
        } else {
            Lc = Point3D.ZERO;
        }

        Point3D LGrabbedChain;
        if (grabbedChain.W.magnitude() != 0) {

            double k = 0;
            for (Ball b : grabbedChain.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - grabbedChain.centreOfGravity.getX(),
                        b.y.get() - grabbedChain.centreOfGravity.getY(),
                        b.z.get() - grabbedChain.centreOfGravity.getZ()
                );
                double modRPerpendicular = (grabbedChain.W.crossProduct(r)).magnitude() / grabbedChain.W.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double IgrabbedChain = grabbedChain.m * k;
            LGrabbedChain = grabbedChain.W.multiply(IgrabbedChain);
        } else {
            LGrabbedChain = Point3D.ZERO;
        }

        double newM = c.m + grabbedChain.m;
        Point3D newCentreOfGravity = ((c.centreOfGravity.multiply(c.m)).add(grabbedChain.centreOfGravity.multiply(grabbedChain.m))).multiply(1 / newM);

        // warning: even if both of solids are not rotating, they can give angular momentum after joining
        // vector between centres of gravity
        Point3D newC_C = c.centreOfGravity.subtract(newCentreOfGravity);
        Point3D newC_GrabbedChain = grabbedChain.centreOfGravity.subtract(newCentreOfGravity);

        Point3D LcCausedByCollision = newC_C.crossProduct(c.V.multiply(c.m));
        Point3D LGrabbedChainCausedByCollision = newC_GrabbedChain.crossProduct(grabbedChain.V.multiply(grabbedChain.m));

        Point3D newL = (Lc).add(LGrabbedChain).add(LcCausedByCollision).add(LGrabbedChainCausedByCollision);

        // ENGINES MODIFICATION
        // warning: if joint is grabbed
        for (LinearMotor i : grabbedChain.linearMotorList) {
            if (i.ballsList1.contains(bgrabbed)) {
                i.ballsList1.addAll(c.ballsList);
            } else {
                i.ballsList2.addAll(c.ballsList);
            }
        }

        for (RotaryMotor i : grabbedChain.rotaryMotorList) {
            if (i.jointList1.contains(bgrabbed)) {
                i.ballsList1.addAll(c.ballsList);
            } else {
                i.ballsList2.addAll(c.ballsList);
            }
        }

        for (LinearMotor i : c.linearMotorList) {
            if (i.ballsList1.contains(bGrabbing)) {
                i.ballsList1.addAll(grabbedChain.ballsList);
            } else {
                i.ballsList2.addAll(grabbedChain.ballsList);
            }
        }

        for (RotaryMotor i : c.rotaryMotorList) {
            if (i.ballsList1.contains(bGrabbing)) {
                i.ballsList1.addAll(grabbedChain.ballsList);
            } else {
                i.ballsList2.addAll(grabbedChain.ballsList);
            }
        }

        c.jointList.addAll(grabbedChain.jointList);
        c.ballsList.addAll(grabbedChain.ballsList);
        c.linearMotorList.addAll(grabbedChain.linearMotorList);
        c.rotaryMotorList.addAll(grabbedChain.rotaryMotorList);


        double k = 0;
        Point3D normL = newL.normalize();
        for (Ball b : c.ballsList) {
            Point3D r = new Point3D(
                    b.x.get() - newCentreOfGravity.getX(),
                    b.y.get() - newCentreOfGravity.getY(),
                    b.z.get() - newCentreOfGravity.getZ()
            );
            double modRPerpendicular = (r.subtract(normL.multiply(normL.dotProduct(r)))).magnitude();
            k += modRPerpendicular * modRPerpendicular;
        }
        double newI = c.m * k;

        Point3D newW = newL.multiply(1 / newI);

        c.m = newM;
        c.centreOfGravity = newCentreOfGravity;
        c.W = newW;
        c.V = newV;


    }

    public void grabTheJoint(Chain cgrabbed, Joint jgrabbed) {
        this.grabbedChain = cgrabbed;
        chainList.remove(grabbedChain);

        oldBallList.addAll(c.ballsList);

        Point3D newV = (c.V.multiply(c.m)).add(grabbedChain.V.multiply(grabbedChain.m)).multiply(1 / (c.m + grabbedChain.m));
        // calculation of angular momentum
        // if W = 0 -> L = 0
        Point3D Lc;
        if (c.W.magnitude() != 0) {
            double k = 0;
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - c.centreOfGravity.getX(),
                        b.y.get() - c.centreOfGravity.getY(),
                        b.z.get() - c.centreOfGravity.getZ()
                );
                double modRPerpendicular = (c.W.crossProduct(r)).magnitude() / c.W.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double Ic = c.m * k;
            Lc = c.W.multiply(Ic);
        } else {
            Lc = Point3D.ZERO;
        }

        Point3D LGrabbedChain;
        if (grabbedChain.W.magnitude() != 0) {

            double k = 0;
            for (Ball b : grabbedChain.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - grabbedChain.centreOfGravity.getX(),
                        b.y.get() - grabbedChain.centreOfGravity.getY(),
                        b.z.get() - grabbedChain.centreOfGravity.getZ()
                );
                double modRPerpendicular = (grabbedChain.W.crossProduct(r)).magnitude() / grabbedChain.W.magnitude();
                k += modRPerpendicular * modRPerpendicular;
            }
            double IgrabbedChain = grabbedChain.m * k;
            LGrabbedChain = grabbedChain.W.multiply(IgrabbedChain);
        } else {
            LGrabbedChain = Point3D.ZERO;
        }

        double newM = c.m + grabbedChain.m;
        Point3D newCentreOfGravity = ((c.centreOfGravity.multiply(c.m)).add(grabbedChain.centreOfGravity.multiply(grabbedChain.m))).multiply(1 / newM);

        // warning: even if both of solids are not rotating, they can give angular momentum after joining
        // vector between centres of gravity
        Point3D newC_C = c.centreOfGravity.subtract(newCentreOfGravity);
        Point3D newC_GrabbedChain = grabbedChain.centreOfGravity.subtract(newCentreOfGravity);

        Point3D LcCausedByCollision = newC_C.crossProduct(c.V.multiply(c.m));
        Point3D LGrabbedChainCausedByCollision = newC_GrabbedChain.crossProduct(grabbedChain.V.multiply(grabbedChain.m));

        Point3D newL = (Lc).add(LGrabbedChain).add(LcCausedByCollision).add(LGrabbedChainCausedByCollision);

        // ENGINES MODIFICATION
        // warning: if joint is grabbed
        for (LinearMotor i : grabbedChain.linearMotorList) {
            if (i.jointList1.contains(jgrabbed)) {
                i.ballsList1.addAll(c.ballsList);
            } else {
                i.ballsList2.addAll(c.ballsList);
            }
        }

        for (RotaryMotor i : grabbedChain.rotaryMotorList) {
            if (i.jointList1.contains(jgrabbed)) {
                i.ballsList1.addAll(c.ballsList);
            } else {
                i.ballsList2.addAll(c.ballsList);
            }
        }

        for (LinearMotor i : c.linearMotorList) {
            if (i.ballsList1.contains(bGrabbing)) {
                i.ballsList1.addAll(grabbedChain.ballsList);
            } else {
                i.ballsList2.addAll(grabbedChain.ballsList);
            }
        }

        for (RotaryMotor i : c.rotaryMotorList) {
            if (i.ballsList1.contains(bGrabbing)) {
                i.ballsList1.addAll(grabbedChain.ballsList);
            } else {
                i.ballsList2.addAll(grabbedChain.ballsList);
            }
        }

        c.jointList.addAll(grabbedChain.jointList);
        c.ballsList.addAll(grabbedChain.ballsList);
        c.linearMotorList.addAll(grabbedChain.linearMotorList);
        c.rotaryMotorList.addAll(grabbedChain.rotaryMotorList);


        double k = 0;
        Point3D normL = newL.normalize();
        for (Ball b : c.ballsList) {
            Point3D r = new Point3D(
                    b.x.get() - newCentreOfGravity.getX(),
                    b.y.get() - newCentreOfGravity.getY(),
                    b.z.get() - newCentreOfGravity.getZ()
            );
            double modRPerpendicular = (r.subtract(normL.multiply(normL.dotProduct(r)))).magnitude();
            k += modRPerpendicular * modRPerpendicular;
        }
        double newI = c.m * k;

        Point3D newW = newL.multiply(1 / newI);

        c.m = newM;
        c.centreOfGravity = newCentreOfGravity;
        c.W = newW;
        c.V = newV;


    }

    public void release() {
        if (grabbedChain != null) {

            chainList.add(grabbedChain);

            Point3D oldCentreOfGravity = c.centreOfGravity;
            Point3D oldV = c.V;
            Point3D oldW = c.W;


            c.jointList.removeAll(grabbedChain.jointList);
            c.ballsList.removeAll(grabbedChain.ballsList);
            c.linearMotorList.removeAll(grabbedChain.linearMotorList);
            c.rotaryMotorList.removeAll(grabbedChain.rotaryMotorList);

            // ENGINES MODIFICATION
            for (LinearMotor i : grabbedChain.linearMotorList) {
                i.ballsList1.removeAll(c.ballsList);
                i.ballsList2.removeAll(c.ballsList);

            }
            for (RotaryMotor i : grabbedChain.rotaryMotorList) {
                i.ballsList1.removeAll(c.ballsList);
                i.ballsList2.removeAll(c.ballsList);
            }

            for (LinearMotor i : c.linearMotorList) {
                i.ballsList1.removeAll(grabbedChain.ballsList);
                i.ballsList2.removeAll(grabbedChain.ballsList);
            }
            for (RotaryMotor i : c.rotaryMotorList) {
                i.ballsList1.removeAll(grabbedChain.ballsList);
                i.ballsList2.removeAll(grabbedChain.ballsList);
            }


            // centre of gravity
            Point3D sumRM = new Point3D(0, 0, 0);
            double sumM = 0;
            for (Ball b : c.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            c.centreOfGravity = sumRM.multiply(1 / sumM);
            c.m = sumM;

            sumRM = new Point3D(0, 0, 0);
            sumM = 0;
            for (Ball b : grabbedChain.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            grabbedChain.centreOfGravity = sumRM.multiply(1 / sumM);
            grabbedChain.m = sumM;

            grabbedChain.W = oldW;
            c.W = oldW;

            grabbedChain.V = oldV.add(oldW.crossProduct(grabbedChain.centreOfGravity.subtract(oldCentreOfGravity)));
            c.V = oldV.add(oldW.crossProduct(c.centreOfGravity.subtract(oldCentreOfGravity)));

            oldBallList.clear();
            grabbedChain = null;

        }

    }

    public Chain getGrabbedChain() {
        return grabbedChain;
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

}
