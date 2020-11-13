package elements;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import physic.Ball;
import physic.Chain;
import physic.Joint;

import java.util.List;

public class Meteor implements Element {

    public Chain chain;

    public Group group;
    public List<Chain> chainList;

    Ball b0, b1, b2;
    Joint j01, j02, j12;

    public Meteor(double x, double y, double z, double m, Group group, List<Chain> chainList) {

        this.group = group;
        this.chainList = chainList;

        double kr = Math.sqrt(m / 100);

        b0 = new Ball(0 * kr + x, 0 * kr + y, 0 * kr + z, 20 * kr, m / 3);
        b1 = new Ball(0 * kr + x, -20 * kr + y, 0 * kr + z, 15 * kr, m / 3);
        j01 = new Joint(b0, b1, 1);

        b2 = new Ball(10 * kr + x, 0 * kr + y, 0 * kr + z, 20 * kr, m / 3);
        j02 = new Joint(b0, b2, 1);
        j12 = new Joint(b1, b2, 1);

        chain = new Chain(b0);

        PhongMaterial materialMeteor = new PhongMaterial();
        materialMeteor.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/meteor.bmp")));
        b0.setAnimatedTexture(1000, materialMeteor);
        b1.setAnimatedTexture(1000, materialMeteor);
        b2.setAnimatedTexture(1000, materialMeteor);

    }

    @Override
    public void create() {
        chainList.add(chain);

        group.getChildren().addAll(chain.ballsList);
        group.getChildren().addAll(chain.jointList);

    }

    @Override
    public void delete() {
        chainList.remove(chain);

        group.getChildren().removeAll(chain.ballsList);
        group.getChildren().removeAll(chain.jointList);
    }

}
