
package graphics;

import javafx.scene.paint.PhongMaterial;
import physic.Ball;
import physic.Joint;

public class AnimatedTexture implements Cloneable {

    private PhongMaterial[] material;
    private int iteratorOfImages = 0;
    private int timeToChangeFrame;
    private int indexOfCurrentImage;

    public AnimatedTexture(int timeToChangeFrame, PhongMaterial... material) {
        this.material = material;
        this.timeToChangeFrame = timeToChangeFrame;
        if (material.length == 0) {
            indexOfCurrentImage = 0;
        }
    }

    public void setImageList(PhongMaterial... material) {
        this.material = material;
    }

    public static void serveAnimationOfBall(Ball b) {

        if (b.isAnimated) {
            AnimatedTexture at = b.getAnimatedTexture();
            at.iteratorOfImages++;
            if (at.iteratorOfImages > at.timeToChangeFrame) {
                at.indexOfCurrentImage++;
                if (at.indexOfCurrentImage > at.material.length - 1) {
                    at.indexOfCurrentImage = 0;
                }
                at.iteratorOfImages = 0;
            }
            b.setMaterial(at.material[at.indexOfCurrentImage]);
        }
    }

    public static void serveAnimationOfJoint(Joint j) {

        if (j.isAnimated) {
            AnimatedTexture at = j.getAnimatedTexture();
            at.iteratorOfImages++;
            if (at.iteratorOfImages > at.timeToChangeFrame) {
                at.indexOfCurrentImage++;
                if (at.indexOfCurrentImage > at.material.length - 1) {
                    at.indexOfCurrentImage = 0;
                }
                at.iteratorOfImages = 0;
            }
            j.setMaterial(at.material[at.indexOfCurrentImage]);
        }

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
