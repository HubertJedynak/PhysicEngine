package physic;

import graphics.AnimatedTexture;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

import static java.lang.Math.*;


public class Joint extends Cylinder {

    public Ball b1, b2;
    public Rotate rotate;
    public boolean isCollisionDetectable = true;
    public boolean isCollisionInteractive = true;

    AnimatedTexture animatedTexture;
    public boolean isAnimated = false;

    public Joint(Ball b1, Ball b2, double radius, boolean cos) {
        super(radius, 0);
        this.b1 = b1;
        this.b2 = b2;
        rotate = new Rotate();
        this.getTransforms().add(rotate);

        serveRotationCausedByBalls();

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

        b1.connectedJoints.add(this);
        b2.connectedJoints.add(this);

    }

    public Joint(Ball b1, Ball b2, double radius) {
        super(radius, 0);
        this.b1 = b1;
        this.b2 = b2;
        rotate = new Rotate();
        this.getTransforms().add(rotate);

        serveRotationCausedByBalls();

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

        b1.connectedJoints.add(this);
        b2.connectedJoints.add(this);

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
