package physic;

import javafx.geometry.Point3D;

import java.util.Random;

public class Matrix33 {
    public Point3D c0, c1, c2;

    public Matrix33(double m00, double m01, double m02, double m10, double m11, double m12, double m20, double m21, double m22) {

        c0 = new Point3D(m00, m10, m20);
        c1 = new Point3D(m01, m11, m21);
        c2 = new Point3D(m02, m12, m22);

    }

    public Matrix33(double[][] m) {

        c0 = new Point3D(m[0][0], m[1][0], m[2][0]);
        c1 = new Point3D(m[0][1], m[1][1], m[2][1]);
        c2 = new Point3D(m[0][2], m[1][2], m[2][2]);

    }


    @Override
    public String toString() {
        return "\n" + c0.getX() + ", " + c1.getX() + ", " + c2.getX() + ",\n" +
                c0.getY() + ", " + c1.getY() + ", " + c2.getY() + ",\n" +
                c0.getZ() + ", " + c1.getZ() + ", " + c2.getZ() + ",\n";
    }

    public Point3D inverseMultiply(Point3D v) {
        double invDet = 1 / c0.dotProduct(c1.crossProduct(c2));
        return new Point3D(
                c1.crossProduct(c2).dotProduct(v),
                c2.crossProduct(c0).dotProduct(v),
                c0.crossProduct(c1).dotProduct(v)
        ).multiply(invDet);
    }

    public void removeSingularPoints() {
        Random random = new Random();
        c0 = c0.add(new Point3D(
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
        ));
        c1 = c1.add(new Point3D(
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
        ));
        c2 = c2.add(new Point3D(
                random.nextDouble(),
                random.nextDouble(),
                random.nextDouble()
        ));

    }

    public double get(int row, int column) {
        if (column == 0) {
            if (row == 0) {
                return c0.getX();
            } else if (row == 1) {
                return c0.getY();
            } else if (row == 2) {
                return c0.getZ();
            } else {
                return 0;
            }
        } else if (column == 1) {
            if (row == 0) {
                return c1.getX();
            } else if (row == 1) {
                return c1.getY();
            } else if (row == 2) {
                return c1.getZ();
            } else {
                return 0;
            }
        } else if (column == 2) {
            if (row == 0) {
                return c2.getX();
            } else if (row == 1) {
                return c2.getY();
            } else if (row == 2) {
                return c2.getZ();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void set(int row, int column, double value) {
        if (column == 0) {
            if (row == 0) {
                c0 = new Point3D(
                        value,
                        c0.getY(),
                        c0.getZ()
                );
            } else if (row == 1) {
                c0 = new Point3D(
                        c0.getX(),
                        value,
                        c0.getZ()
                );
            } else if (row == 2) {
                c0 = new Point3D(
                        c0.getX(),
                        c0.getY(),
                        value
                );
            }
        } else if (column == 1) {
            if (row == 0) {
                c1 = new Point3D(
                        value,
                        c1.getY(),
                        c1.getZ()
                );
            } else if (row == 1) {
                c1 = new Point3D(
                        c1.getX(),
                        value,
                        c1.getZ()
                );
            } else if (row == 2) {
                c1 = new Point3D(
                        c1.getX(),
                        c1.getY(),
                        value
                );
            }
        } else if (column == 2) {
            if (row == 0) {
                c2 = new Point3D(
                        value,
                        c2.getY(),
                        c2.getZ()
                );
            } else if (row == 1) {
                c2 = new Point3D(
                        c2.getX(),
                        value,
                        c2.getZ()
                );
            } else if (row == 2) {
                c2 = new Point3D(
                        c2.getX(),
                        c2.getY(),
                        value
                );
            }
        }
    }
}
