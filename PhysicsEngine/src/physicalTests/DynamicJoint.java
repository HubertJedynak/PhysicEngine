package physicalTests;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import physic.Ball;

import static java.lang.Math.*;
import physic.*;


public class DynamicJoint extends Cylinder {

    Ball b1, b2;
    Rotate rotate;
    Point3D centreOfGravity;
    Point3D W = new Point3D(0,0,0); //predkosci katowa
    Point3D V= new Point3D(0,0,0); //predkosci liniowa
    double height;

    public DynamicJoint(Ball b1, Ball b2, double radius) {
        super(radius, 0);
        this.b1 = b1;
        this.b2 = b2;
        rotate = new Rotate();
        this.getTransforms().add(rotate);
        // wspolrzede srodka ciezkosci

        centreOfGravity = new Point3D(
                (b1.m * b1.x.get() + b2.m * b2.x.get()) / (b1.m + b2.m),
                (b1.m * b1.y.get() + b2.m * b2.y.get()) / (b1.m + b2.m),
                (b1.m * b1.z.get() + b2.m * b2.z.get()) / (b1.m + b2.m)
        );

        serveRotationCausedByBalls();
        this.height = super.getHeight();
        ChangeListener<Number> servingRotationCausedByBallsListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                serveRotationCausedByBalls();
            }
        };

        b1.x.addListener(servingRotationCausedByBallsListener);
        b1.y.addListener(servingRotationCausedByBallsListener);
        b1.z.addListener(servingRotationCausedByBallsListener);

        b2.x.addListener(servingRotationCausedByBallsListener);
        b2.y.addListener(servingRotationCausedByBallsListener);
        b2.z.addListener(servingRotationCausedByBallsListener);

    }

    private void serveRotationCausedByBalls() {
        synchronized (rotate) {
            this.setTranslateX((b1.x.get() + b2.x.get()) / 2);
            this.setTranslateY((b1.y.get() + b2.y.get()) / 2);
            this.setTranslateZ((b1.z.get() + b2.z.get()) / 2);

            double dx = b1.x.get() - b2.x.get();
            double dy = b1.y.get() - b2.y.get();
            double dz = b1.z.get() - b2.z.get();

            this.setHeight(sqrt(dx * dx + dy * dy + dz * dz));

            Point3D vrownolegly = new Point3D(dx, dy, dz).normalize();
            Point3D vprostopadly = new Point3D(-vrownolegly.getZ(), 0, vrownolegly.getX());
            double kat;
            if (dy < 0) {
                kat = asin(vprostopadly.magnitude()) * 180 / PI;
            } else {
                kat = -asin(vprostopadly.magnitude()) * 180 / PI;
            }
            rotate.setAxis(vprostopadly.normalize());
            rotate.setAngle(kat);
        }

    }

    public void setBallsVCausedByJoint(){

        //aktualizacja dla W

        Point3D cRb1 = centreOfGravity.subtract(b1.x.get(),b1.y.get(),b1.z.get());
        Point3D cRb2 = centreOfGravity.subtract(b2.x.get(),b2.y.get(),b2.z.get());

        //System.out.println(cRb1.getX());

        Point3D cVb1 = W.crossProduct(cRb1);
        Point3D cVb2 = W.crossProduct(cRb2);

        // potrzebny minus bo  wektory cR maja zwroty do srodka sieckosci a nie od
        b1.addToX(-cVb1.getX());
        b1.addToY(-cVb1.getY());
        b1.addToZ(-cVb1.getZ());

        b2.addToX(-cVb2.getX());
        b2.addToY(-cVb2.getY());
        b2.addToZ(-cVb2.getZ());

        //aktualizacja dla V

        b1.addToX(V.getX());
        b1.addToY(V.getY());
        b1.addToZ(V.getZ());

        b2.addToX(V.getX());
        b2.addToY(V.getY());
        b2.addToZ(V.getZ());

        // zapobiegniecie zmiany odleglosci spowodowanej obrotem

        //System.out.println("roznica wysokosci "+(this.getHeight() - height));

        double dh = this.getHeight() - height;

        b1.addToX(cRb1.getX()*dh/height);
        b1.addToY(cRb1.getY()*dh/height);
        b1.addToZ(cRb1.getZ()*dh/height);

        b2.addToX(cRb2.getX()*dh/height);
        b2.addToY(cRb2.getY()*dh/height);
        b2.addToZ(cRb2.getZ()*dh/height);

        // AKTUALIZACJA SRODKA CIEZKOSCI (MOZNA ZROBIC PROPERTY)

        centreOfGravity = new Point3D(
                (b1.m * b1.x.get() + b2.m * b2.x.get()) / (b1.m + b2.m),
                (b1.m * b1.y.get() + b2.m * b2.y.get()) / (b1.m + b2.m),
                (b1.m * b1.z.get() + b2.m * b2.z.get()) / (b1.m + b2.m)
        );



    }

}
