package physic;

import javafx.geometry.Point3D;

import java.util.List;

public class Collisions {

    public static CollisionDetectableElement findCollisionDetectableElement(Chain cWithGripper, Ball bGrabbing, List<Chain> chainList) {
        Ball b;
        Joint j;
        for (Chain c : chainList) {
            if (c != cWithGripper) {
                b = findFirstCollisionDetectableBall(cWithGripper, c, bGrabbing);
                if (b != null) {
                    return new CollisionDetectableElement(c, b);
                }
                j = findFirstCollisionDetectableJoint(cWithGripper, c, bGrabbing);
                if (j != null) {
                    return new CollisionDetectableElement(c, j);
                }
            }
        }
        return null;
    }

    public static Ball findFirstCollisionDetectableBall(Chain cWithGripper, Chain c, Ball bGripper) {
        for (Ball i : c.ballsList) {
            Point3D[] segmentBetweenCollidingComponents = Chains2Balls.detectCollision(cWithGripper, c, bGripper, i);
            if (segmentBetweenCollidingComponents != null) {
                return i;
            }
        }
        return null;
    }

    public static Joint findFirstCollisionDetectableJoint(Chain cWithGripper, Chain c, Ball bGripper) {
        for (Joint i : c.jointList) {
            Point3D[] segmentBetweenCollidingComponents = ChainsJointBall.detectCollision(cWithGripper, c, bGripper, i);
            if (segmentBetweenCollidingComponents != null) {
                return i;
            }
        }
        return null;
    }


    public static void serveCollisionBetween2Chains(Chain c1, Chain c2) {

        for (Ball b1 : c1.ballsList) {
            for (Ball b2 : c2.ballsList) {
                if(b1.isCollisionInteractive && b2.isCollisionInteractive){
                    Chains2Balls.serveCollision(c1, c2, b1, b2);
                }
            }
        }

        for (Ball b : c1.ballsList) {
            for (Joint j : c2.jointList) {
                if(b.isCollisionInteractive && j.isCollisionInteractive){
                    ChainsJointBall.serveCollision(c1, c2, b, j);
                }
            }
        }

        for (Ball b : c2.ballsList) {
            for (Joint j : c1.jointList) {
                if(b.isCollisionInteractive && j.isCollisionInteractive){
                    ChainsJointBall.serveCollision(c2, c1, b, j);
                }
            }
        }

        for (Joint j1 : c1.jointList) {
            for (Joint j2 : c2.jointList) {
                if(j1.isCollisionInteractive && j2.isCollisionInteractive){
                    Chains2Joints.serveCollision(c1, c2, j1, j2);
                }
            }
        }

    }

    public static void serveAllCollisions(List<Chain> chainList) {

        for (int x = 0; x < chainList.size() - 1; x++) {
            for (int y = x + 1; y < chainList.size(); y++) {
                Chain c1 = chainList.get(x);
                Chain c2 = chainList.get(y);
                serveCollisionBetween2Chains(c1, c2);
            }
        }


    }

    public static class Chains2Joints {

        public static Point3D[] detectCollision(Chain c1, Chain c2, Joint j1, Joint j2) {

            Point3D vecj1 = new Point3D(
                    j1.b2.x.get() - j1.b1.x.get(),
                    j1.b2.y.get() - j1.b1.y.get(),
                    j1.b2.z.get() - j1.b1.z.get()
            );

            Point3D nvecj1 = vecj1.normalize();

            Point3D vecj2 = new Point3D(
                    j2.b2.x.get() - j2.b1.x.get(),
                    j2.b2.y.get() - j2.b1.y.get(),
                    j2.b2.z.get() - j2.b1.z.get()
            );

            Point3D nvecj2 = vecj2.normalize();

            Point3D vecsj1sj2 = new Point3D(
                    j2.b1.x.get() - j1.b1.x.get(),
                    j2.b1.y.get() - j1.b1.y.get(),
                    j2.b1.z.get() - j1.b1.z.get()
            );

            // p versor - the shortest wector form j1 to j2
            // calculation of wector p direction (p versor)

            Point3D p = nvecj1.crossProduct(nvecj2).normalize();

            // are parallel (if cross product = 0 -> calculate joint-ball distance and we can finish the function)
            if (p.magnitude() == 0) {
                return null;
            }

            double projectionOfVecsj1sj2onP = vecsj1sj2.dotProduct(p);

            // p wector calculation
            p = p.multiply(projectionOfVecsj1sj2onP);

            //  move j1 by p vector to j2 (j1 and j2 should intersect each other)
            double nx1 = j1.b1.x.get() + p.getX();
            double ny1 = j1.b1.y.get() + p.getY();
            double nz1 = j1.b1.z.get() + p.getZ();

            // wector between nwe j1.b1 and old j2.b1
            Point3D vecnsj2 = new Point3D(
                    j2.b1.x.get() - nx1,
                    j2.b1.y.get() - ny1,
                    j2.b1.z.get() - nz1
            );

            // calculation of beta coefficient (value of vector between j2.b1 and intersection point on j2)
            double beta = 0;
            if (p.getY() != 0) {
                //if py != 0
                //1=x, 2=z
                beta = nvecj1.crossProduct(vecnsj2).getY() / nvecj2.crossProduct(nvecj1).getY();
            } else if (p.getX() != 0) {
                //if px != 0
                //1=y, 2=z
                beta = nvecj1.crossProduct(vecnsj2).getX() / nvecj2.crossProduct(nvecj1).getX();
            } else {
                //default 1=x 2=y, pz != 0
                beta = nvecj1.crossProduct(vecnsj2).getZ() / nvecj2.crossProduct(nvecj1).getZ();
            }

            // searched point on j2
            Point3D F = new Point3D(
                    j2.b1.x.get() + nvecj2.getX() * beta,
                    j2.b1.y.get() + nvecj2.getY() * beta,
                    j2.b1.z.get() + nvecj2.getZ() * beta
            );

            // searched point on j1
            Point3D E = F.subtract(p);

            //  CHECKING, IF FOUNDED POINTS BELONG TO APPROPRIATE SEGMENTS
            Point3D vecsj1E = E.subtract(j1.b1.x.get(), j1.b1.y.get(), j1.b1.z.get());
            if (nvecj1.dotProduct(vecsj1E) < 0 || nvecj1.dotProduct(vecsj1E) > vecj1.magnitude()) {
                return null;
            }

            Point3D vecsj2F = F.subtract(j2.b1.x.get(), j2.b1.y.get(), j2.b1.z.get());
            if (nvecj2.dotProduct(vecsj2F) < 0 || nvecj2.dotProduct(vecsj2F) > vecj2.magnitude()) {
                return null;
            }

            if (E.distance(F) > j1.getRadius() + j2.getRadius()) {
                return null;
            }

            return new Point3D[]{E, F};

        }

        public static Point3D[] serveCollision(Chain c1, Chain c2, Joint j1, Joint j2) {

            Point3D oldW = c2.W;

            // COLLISION DETECTION BETWEEN 2 JOINTS

            // there is no collision if null
            Point3D[] segmentBetweenJoints = Chains2Joints.detectCollision(c1, c2, j1, j2);

            if (segmentBetweenJoints == null) {
                return null;
            }
    
            // parameters calculation (step 1 wikipedia)
            double segmentLengthBetweenJoints = segmentBetweenJoints[0].distance(segmentBetweenJoints[1]);
            // versor n is form j1 to j2
            Point3D versorN = (segmentBetweenJoints[1].subtract(segmentBetweenJoints[0])).normalize();

            double distanceCausedByIntersection = j1.getRadius() + j2.getRadius() - segmentLengthBetweenJoints;

            // searching collision point (solids separation included)
            Point3D collisionPoint = segmentBetweenJoints[0].add(versorN.multiply(j1.getRadius() - distanceCausedByIntersection / 2));

            //solids separation
            c1.move(versorN.multiply(-distanceCausedByIntersection / 2));
            segmentBetweenJoints[0] = segmentBetweenJoints[0].add(versorN.multiply(-distanceCausedByIntersection / 2));
            c2.move(versorN.multiply(distanceCausedByIntersection / 2));
            segmentBetweenJoints[1] = segmentBetweenJoints[1].add(versorN.multiply(distanceCausedByIntersection / 2));

            // calculation of  r1 i r2 (wikipedia)
            Point3D r1 = collisionPoint.subtract(c1.centreOfGravity);
            Point3D r2 = collisionPoint.subtract(c2.centreOfGravity);

            // calculation of Vr (wikipedia)
            Point3D vp1 = c1.V.add(c1.W.crossProduct(r1)).add(getVCausedByEngines(c1, j1, collisionPoint));
            Point3D vp2 = c2.V.add(c2.W.crossProduct(r2)).add(getVCausedByEngines(c2, j2, collisionPoint));
            Point3D vr = vp2.subtract(vp1);

            // calculation of I1 i I2
            Point3D sumRM = new Point3D(0, 0, 0);
            double sumM = 0;
            for (Ball b : c1.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            for (Ball b : c2.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            Point3D centreOfPairOfSolids = sumRM.multiply(1 / sumM);

            Matrix33 I1 = inertiaTensor(c1, collisionPoint);
            Matrix33 I2 = inertiaTensor(c2, collisionPoint);


            // calculation of jr impulse (wikipedia)
            double e = 0.5;
            double jr = (-(1 + e) * vr.dotProduct(versorN)) /
                    (1 / c1.m + 1 / c2.m +
                            ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                    I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                            )).dotProduct(versorN)
                    );


            // SINGULARITY POINTS DETECTION
            if (Double.isNaN(jr)) {
                // SINGULARITY POINTS REMOVING
                I1.removeSingularPoints();
                I2.removeSingularPoints();

                jr = (-(1 + e) * vr.dotProduct(versorN)) /
                        (1 / c1.m + 1 / c2.m +
                                ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                        I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                                )).dotProduct(versorN)
                        );

            }

            // VECTOR JR (STEP 2 WIKIPEDIA)
            // CALCULATION OF LINEAR VELOCITIES V
            c1.V = c1.V.add(versorN.multiply(-jr / c1.m));
            c2.V = c2.V.add(versorN.multiply(jr / c2.m));

            // CALCULATION OF ANGULAR VELOCITIES W
            c1.W = c1.W.add(I1.inverseMultiply(r1.crossProduct(versorN)).multiply(-jr));
            c2.W = c2.W.add(I2.inverseMultiply(r2.crossProduct(versorN)).multiply(jr));

            c1.setBallsVCausedByChain();
            c2.setBallsVCausedByChain();

            return segmentBetweenJoints;

        }

        private static Matrix33 inertiaTensor(Chain c, Point3D collisionPoint) {

            double[][] I = new double[3][3];

            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    double suma = 0;
                    for (Ball b : c.ballsList) {
                        Point3D r = new Point3D(b.x.get(), b.y.get(), b.z.get()).subtract(collisionPoint);
                        double modR = r.magnitude();
                        double[] vecR = {r.getX(), r.getY(), r.getZ()};
                        suma += modR * modR * dirackImpulse(i, j) - vecR[i] * vecR[j];
                    }
                    I[i][j] = c.m * suma;
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

        private static Point3D getVCausedByEngines(Chain c, Joint j, Point3D collisionPoint) {
            Point3D sumV = Point3D.ZERO;
            for (RotaryMotor i : c.rotaryMotorList) {
                if (i.isActive) {
                    if (i.jointList1.contains(j)) {
                        Point3D r = new Point3D(
                                collisionPoint.getX() - i.bEngine.x.get(),
                                collisionPoint.getY() - i.bEngine.y.get(),
                                collisionPoint.getZ() - i.bEngine.z.get()
                        );
                        Point3D V = i.W1.crossProduct(r);
                        sumV = sumV.add(V);
                    } else if (i.jointList2.contains(j)) {
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
                    if (i.jointList1.contains(j)) {
                        sumV = sumV.add(i.V1);
                    } else if (i.jointList2.contains(j)) {
                        sumV = sumV.add(i.V2);
                    }
                }
            }
            return sumV;
        }

    }

    public static class ChainsJointBall {

        public static Point3D[] detectCollision(Chain cb, Chain cj, Ball b, Joint j) {

            // normalised vector between joints ends
            double dxj = (j.b2.x.get() - j.b1.x.get()) / j.getHeight();
            double dyj = (j.b2.y.get() - j.b1.y.get()) / j.getHeight();
            double dzj = (j.b2.z.get() - j.b1.z.get()) / j.getHeight();
            //vector form b1 (joint beginning) to ball b
            double dxb = b.x.get() - j.b1.x.get();
            double dyb = b.y.get() - j.b1.y.get();
            double dzb = b.z.get() - j.b1.z.get();
            // value of projection vector b1b on segment (vector) b1b2
            double l = dxj * dxb + dyj * dyb + dzj * dzb;
            if (l < 0 || l > j.getHeight()) {
                return null;
            }
            // vector "i" ball ( ball "i" appears only by one iteration and collide with ball "b")
            double dxi = dxj * l;
            double dyi = dyj * l;
            double dzi = dzj * l;
            //wspolrzedne kuli "i"
            // ball i location
            double ix = j.b1.x.get() + dxi;
            double iy = j.b1.y.get() + dyi;
            double iz = j.b1.z.get() + dzi;

            Point3D vecBI = new Point3D(
                    ix - b.x.get(),
                    iy - b.y.get(),
                    iz - b.z.get()
            );

            if (vecBI.magnitude() > j.getRadius() + b.getRadius()) {
                return null;
            } else {
                return new Point3D[]{new Point3D(b.x.get(), b.y.get(), b.z.get()), new Point3D(ix, iy, iz)};
            }

        }

        public static Point3D[] serveCollision(Chain cb, Chain cj, Ball b, Joint j) {

            // there is no collision if null
            Point3D[] segmentBetweenJointAndBall = ChainsJointBall.detectCollision(cb, cj, b, j);
            if (segmentBetweenJointAndBall == null) {
                return null;
            }

            // parameters calculation (step 1 wikipedia)
            double dlugoscOdcinkaMiedzyJointami = segmentBetweenJointAndBall[0].distance(segmentBetweenJointAndBall[1]);
            // versor n is form j1 to j2
            Point3D versorN = (segmentBetweenJointAndBall[1].subtract(segmentBetweenJointAndBall[0])).normalize();
            //System.out.println(versorN);
            double distanceCausedByIntersection = b.getRadius() + j.getRadius() - dlugoscOdcinkaMiedzyJointami;

            // searching collision point (solids separation included)
            Point3D punktKolizji = segmentBetweenJointAndBall[0].add(versorN.multiply(b.getRadius() - distanceCausedByIntersection / 2));
            //solids separation
            cb.move(versorN.multiply(-distanceCausedByIntersection / 2));
            segmentBetweenJointAndBall[0] = segmentBetweenJointAndBall[0].add(versorN.multiply(-distanceCausedByIntersection / 2));
            cj.move(versorN.multiply(distanceCausedByIntersection / 2));
            segmentBetweenJointAndBall[1] = segmentBetweenJointAndBall[1].add(versorN.multiply(distanceCausedByIntersection / 2));

            // calculation of  r1 i r2 (wikipedia)
            Point3D r1 = punktKolizji.subtract(cb.centreOfGravity);
            Point3D r2 = punktKolizji.subtract(cj.centreOfGravity);

            // calculation of Vr (wikipedia)
            Point3D vp1 = cb.V.add(cb.W.crossProduct(r1)).add(getVCausedByEngines(cb, b, punktKolizji));
            Point3D vp2 = cj.V.add(cj.W.crossProduct(r2)).add(getVCausedByEngines(cj, j, punktKolizji));
            Point3D vr = vp2.subtract(vp1);

            // calculation of I1 i I2
            Matrix33 I1 = inertiaTensor(cb, punktKolizji);
            Matrix33 I2 = inertiaTensor(cj, punktKolizji);

            // calculation of jr impulse (wikipedia)
            double e = 0.5;
            double jr = (-(1 + e) * vr.dotProduct(versorN)) /
                    (1 / cb.m + 1 / cj.m +
                            ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                    I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                            )).dotProduct(versorN)
                    );


            // SINGULARITY POINTS DETECTION
            if (Double.isNaN(jr)) {
                // SINGULARITY POINTS REMOVING
                I1.removeSingularPoints();
                I2.removeSingularPoints();

                jr = (-(1 + e) * vr.dotProduct(versorN)) /
                        (1 / cb.m + 1 / cj.m +
                                ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                        I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                                )).dotProduct(versorN)
                        );

            }

            // WECTOR JR (STEP 2 WIKIPEDIA)
            // CALCULATION OF LINEAR VELOCITIES V
            cb.V = cb.V.add(versorN.multiply(-jr / cb.m));
            cj.V = cj.V.add(versorN.multiply(jr / cj.m));

            // CALCULATION OF ANGULAR VELOCITIES W
            cb.W = cb.W.add(I1.inverseMultiply(r1.crossProduct(versorN)).multiply(-jr));
            cj.W = cj.W.add(I2.inverseMultiply(r2.crossProduct(versorN)).multiply(jr));

            cb.setBallsVCausedByChain();
            cj.setBallsVCausedByChain();

            return segmentBetweenJointAndBall;

        }

        private static Matrix33 inertiaTensor(Chain c, Point3D collisionPoint) {

            double[][] I = new double[3][3];

            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    double suma = 0;
                    for (Ball b : c.ballsList) {
                        Point3D r = new Point3D(b.x.get(), b.y.get(), b.z.get()).subtract(collisionPoint);
                        double modR = r.magnitude();
                        double[] vecR = {r.getX(), r.getY(), r.getZ()};
                        suma += modR * modR * dirackImpulse(i, j) - vecR[i] * vecR[j];
                    }
                    I[i][j] = c.m * suma;
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

        private static Point3D getVCausedByEngines(Chain c, Joint j, Point3D collisionPoint) {
            Point3D sumV = Point3D.ZERO;
            for (RotaryMotor i : c.rotaryMotorList) {

                if (i.isActive) {

                    if (i.jointList1.contains(j)) {
                        Point3D r = new Point3D(
                                collisionPoint.getX() - i.bEngine.x.get(),
                                collisionPoint.getY() - i.bEngine.y.get(),
                                collisionPoint.getZ() - i.bEngine.z.get()
                        );
                        Point3D V = i.W1.crossProduct(r);
                        sumV = sumV.add(V);
                    } else if (i.jointList2.contains(j)) {
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
                    if (i.jointList1.contains(j)) {
                        sumV = sumV.add(i.V1);
                    } else if (i.jointList2.contains(j)) {
                        sumV = sumV.add(i.V2);
                    }
                }
            }
            return sumV;
        }


    }

    public static class Chains2Balls {

        public static Point3D[] detectCollision(Chain c1, Chain c2, Ball b1, Ball b2) {
            Point3D vecb1 = new Point3D(
                    b1.x.get(),
                    b1.y.get(),
                    b1.z.get()
            );

            Point3D vecb2 = new Point3D(
                    b2.x.get(),
                    b2.y.get(),
                    b2.z.get()
            );


            if (vecb1.distance(vecb2) > b1.getRadius() + b2.getRadius()) {
                return null;
            } else {
                return new Point3D[]{vecb1, vecb2};
            }
        }

        public static Point3D[] serveCollision(Chain c1, Chain c2, Ball b1, Ball b2) {

            Point3D[] segmentBetweenBalls = Chains2Balls.detectCollision(c1, c2, b1, b2);

            if (segmentBetweenBalls == null) {
                return null;
            }


            // parameters calculation (step 1 wikipedia)
            double segmentLengthBetweenJoints = segmentBetweenBalls[0].distance(segmentBetweenBalls[1]);
            // versor n is form j1 to j2
            Point3D versorN = (segmentBetweenBalls[1].subtract(segmentBetweenBalls[0])).normalize();
            double distanceCausedByIntersection = b1.getRadius() + b2.getRadius() - segmentLengthBetweenJoints;

            // searching collision point (solids separation included)
            Point3D collisionPoint = segmentBetweenBalls[0].add(versorN.multiply(b1.getRadius() - distanceCausedByIntersection / 2));

            //solids separation
            c1.move(versorN.multiply(-distanceCausedByIntersection / 2));
            segmentBetweenBalls[0] = segmentBetweenBalls[0].add(versorN.multiply(-distanceCausedByIntersection / 2));
            c2.move(versorN.multiply(distanceCausedByIntersection / 2));
            segmentBetweenBalls[1] = segmentBetweenBalls[1].add(versorN.multiply(distanceCausedByIntersection / 2));

            // calculation of  r1 i r2 (wikipedia)
            Point3D r1 = collisionPoint.subtract(c1.centreOfGravity);
            Point3D r2 = collisionPoint.subtract(c2.centreOfGravity);

            // calculation of Vr (wikipedia)
            Point3D vp1 = c1.V.add(c1.W.crossProduct(r1)).add(getVCausedByEngines(c1, b1, collisionPoint));
            Point3D vp2 = c2.V.add(c2.W.crossProduct(r2)).add(getVCausedByEngines(c2, b2, collisionPoint));
            Point3D vr = vp2.subtract(vp1);

            // calculation of I1 i I2
            Point3D sumRM = new Point3D(0, 0, 0);
            double sumM = 0;
            for (Ball b : c1.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            for (Ball b : c2.ballsList) {
                sumRM = sumRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumM += b.m;
            }
            Point3D centreOfPairOfSolids = sumRM.multiply(1 / sumM);

            Matrix33 I1 = inertiaTensor(c1, collisionPoint);
            Matrix33 I2 = inertiaTensor(c2, collisionPoint);


            // calculation of jr impulse (wikipedia)
            double e = 0.5;
            double jr = (-(1 + e) * vr.dotProduct(versorN)) /
                    (1 / c1.m + 1 / c2.m +
                            ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                    I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                            )).dotProduct(versorN)
                    );

            // SINGULARITY POINTS DETECTION
            if (Double.isNaN(jr)) {
                // SINGULARITY POINTS REMOVING
                I1.removeSingularPoints();
                I2.removeSingularPoints();

                jr = (-(1 + e) * vr.dotProduct(versorN)) /
                        (1 / c1.m + 1 / c2.m +
                                ((I1.inverseMultiply(r1.crossProduct(versorN)).crossProduct(r1)).add(
                                        I2.inverseMultiply(r2.crossProduct(versorN)).crossProduct(r2)
                                )).dotProduct(versorN)
                        );
            }

            // VECTOR JR (STEP 2 WIKIPEDIA)
            // CALCULATION OF LINEAR VELOCITIES V
            c1.V = c1.V.add(versorN.multiply(-jr / c1.m));
            c2.V = c2.V.add(versorN.multiply(jr / c2.m));

            // CALCULATION OF ANGULAR VELOCITIES W
            c1.W = c1.W.add(I1.inverseMultiply(r1.crossProduct(versorN)).multiply(-jr));
            c2.W = c2.W.add(I2.inverseMultiply(r2.crossProduct(versorN)).multiply(jr));

            c1.setBallsVCausedByChain();
            c2.setBallsVCausedByChain();

            return segmentBetweenBalls;

        }

        private static Matrix33 inertiaTensor(Chain c, Point3D collisionPoint) {

            double[][] I = new double[3][3];

            for (int j = 0; j < 3; j++) {
                for (int i = 0; i < 3; i++) {
                    double sum = 0;
                    for (Ball b : c.ballsList) {
                        Point3D r = new Point3D(b.x.get(), b.y.get(), b.z.get()).subtract(collisionPoint);
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

    }

    public static class CollisionDetectableElement {
        Chain c;
        Ball b;
        Joint j;

        public CollisionDetectableElement(Chain c, Ball b) {
            this.c = c;
            this.b = b;
            this.j = null;
        }

        public CollisionDetectableElement(Chain c, Joint j) {
            this.c = c;
            this.b = null;
            this.j = j;
        }

    }

}
