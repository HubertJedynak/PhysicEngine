
package elements;

import animations.Animation;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import physic.Ball;
import physic.Chain;
import physic.Joint;

import java.util.List;

public class Frame implements Element {

    Box canvas;
    Group group;
    Chain cFrame;
    List<Chain> chainList;
    Animation animation;

    public Frame(Group group, List<Chain> chainList, Animation animation) {
        this.animation = animation;
        this.group = group;
        this.chainList = chainList;
        canvas = new Box(300, 200, 50);

        PhongMaterial grey = new PhongMaterial();
        grey.setDiffuseMap(new Image("/resources/material/grey.png"));

        Ball b1 = new Ball(-150, -100, -25, 5, 5);
        b1.setMaterial(grey);
        Ball b2 = new Ball(150, -100, -25, 5, 5);
        b2.setMaterial(grey);
        Ball b3 = new Ball(150, 100, -25, 5, 5);
        b3.setMaterial(grey);
        Ball b4 = new Ball(-150, 100, -25, 5, 5);
        b4.setMaterial(grey);

        Joint j1 = new Joint(b1, b2, 3);
        j1.setAnimatedTexture(this.animation.aGreen);
        Joint j2 = new Joint(b2, b3, 3);
        j2.setAnimatedTexture(this.animation.aGreen);
        Joint j3 = new Joint(b3, b4, 3);
        j3.setAnimatedTexture(this.animation.aGreen);
        Joint j4 = new Joint(b4, b1, 3);
        j4.setAnimatedTexture(this.animation.aGreen);

        cFrame = new Chain(b1);
    }

    public void setImage(String path) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream(path)));
        canvas.setMaterial(material);
    }

    @Override
    public void create() {
        group.getChildren().add(canvas);
        chainList.add(cFrame);

        group.getChildren().addAll(cFrame.ballsList);
        group.getChildren().addAll(cFrame.jointList);

    }

    @Override
    public void delete() {
        group.getChildren().remove(canvas);

        chainList.remove(cFrame);

        group.getChildren().removeAll(cFrame.ballsList);
        group.getChildren().removeAll(cFrame.jointList);
    }

}
