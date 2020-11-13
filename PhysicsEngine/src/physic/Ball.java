package physic;

import graphics.AnimatedTexture;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;

public class Ball extends Sphere {
    public double ax, ay, az, vx, vy, vz;
    public DoubleProperty x, y, z, r;
    public double m;
    public boolean isCollisionDetectable = true;
    public boolean isCollisionInteractive = true;

    public List<Joint> connectedJoints = new LinkedList<>();

    Ball bRotationAxisBegining, bRotationAxisEnd;
    public Rotate rotate;
    private boolean rotationTextureMode = false;

    AnimatedTexture animatedTexture;
    public boolean isAnimated = false;


    public Ball(double x, double y, double z, double r, double m) {
        super();

        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.z = new SimpleDoubleProperty(z);
        this.r = new SimpleDoubleProperty(r);

        translateXProperty().bindBidirectional(this.x);
        translateYProperty().bindBidirectional(this.y);
        translateZProperty().bindBidirectional(this.z);
        radiusProperty().bindBidirectional(this.r);

        this.m = m;
    }

    public void addVtoXYZ() {
        this.x.set(this.x.get() + vx);
        this.y.set(this.y.get() + vy);
        this.z.set(this.z.get() + vz);
    }

    public void addToX(double x) {
        this.x.set(this.x.get() + x);
    }

    public void addToY(double y) {
        this.y.set(this.y.get() + y);
    }

    public void addToZ(double z) {
        this.z.set(this.z.get() + z);
    }

    public void setRotationCausedByBallsPosition(Ball bRotationAxisBegining, Ball bRotationAxisEnd) {

        if (!rotationTextureMode) {

            rotationTextureMode = true;

            this.bRotationAxisBegining = bRotationAxisBegining;
            this.bRotationAxisEnd = bRotationAxisEnd;

            rotate = new Rotate();
            this.getTransforms().add(rotate);

            serveRotationCausedByBalls();

            ChangeListener<Number> servingRotationCausedByBallsListener = new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    serveRotationCausedByBalls();
                }
            };

            this.x.addListener(servingRotationCausedByBallsListener);
            this.y.addListener(servingRotationCausedByBallsListener);
            this.z.addListener(servingRotationCausedByBallsListener);

        }

    }

    private void serveRotationCausedByBalls() {
        synchronized (rotate) {

            double dx = bRotationAxisEnd.x.get() - bRotationAxisBegining.x.get();
            double dy = bRotationAxisEnd.y.get() - bRotationAxisBegining.y.get();
            double dz = bRotationAxisEnd.z.get() - bRotationAxisBegining.z.get();

            Point3D vParallel = new Point3D(dx, dy, dz).normalize();
            Point3D vPerpendicular = new Point3D(-vParallel.getZ(), 0, vParallel.getX());
            double angle;
            if (dy < 0) {
                angle = asin(vPerpendicular.magnitude()) * 180 / PI;
            } else {
                angle = 180 - asin(vPerpendicular.magnitude()) * 180 / PI;
            }

            rotate.setAxis(vPerpendicular.normalize());
            rotate.setAngle(angle);

        }
    }


    public void setAnimatedTexture(AnimatedTexture animatedTexture) {
        this.animatedTexture = animatedTexture;
        isAnimated = true;
    }

    public void setAnimatedTexture(int timeToChangeFrame, PhongMaterial... materials) {
        animatedTexture = new AnimatedTexture(timeToChangeFrame, materials);
        isAnimated = true;
    }

    public AnimatedTexture getAnimatedTexture() {
        return animatedTexture;
    }
}
