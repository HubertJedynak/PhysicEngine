package elements;

import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class Planet implements Element {

    Sphere canvas;
    PointLight pointLight;
    Group group;

    public Planet(Group group) {
        this.group = group;
        canvas = new Sphere(100);
        pointLight = new PointLight();
        pointLight.setTranslateZ(-400);

    }

    public void setImage(String path) {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream(path)));
        canvas.setMaterial(material);
    }

    @Override
    public void create() {
        group.getChildren().add(canvas);
    }

    @Override
    public void delete() {
        group.getChildren().remove(canvas);
        group.getChildren().remove(pointLight);
    }
}
