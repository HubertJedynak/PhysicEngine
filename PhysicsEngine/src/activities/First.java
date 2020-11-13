package activities;

import animations.Animation;
import core.CameraManager;
import core.LogicManager;
import inputDevices.Keyboard;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import physic.Chain;

import java.util.List;
import java.util.Random;

public class First extends Activity {

    public First(Group group, Group groupBackground, List<Chain> chainList, Keyboard keyboard, CameraManager cameraManager, Animation animation) {
        super(group, groupBackground, chainList, keyboard, cameraManager, animation);

        loadBackground();

    }

    @Override
    public void cleanUpAfterActivity() {
    }

    @Override
    public void manage() {
        LogicManager.loadActivity(new Menu(group, groupBackground, chainList, keyboard, cameraManager, animation));
    }


    public void loadBackground() {
        Sphere sun = new Sphere(100);
        sun.setTranslateX(-1000);
        sun.setTranslateZ(-2000);
        PhongMaterial materialSun = new PhongMaterial();
        materialSun.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/images/sun.bmp")));
        sun.setMaterial(materialSun);

        PointLight pointLight = new PointLight();
        pointLight.setTranslateX(-1000);
        pointLight.setTranslateZ(-2000);

        Sphere jupiter = new Sphere(50);
        jupiter.setTranslateX(-2000);
        jupiter.setTranslateY(-100);
        jupiter.setTranslateZ(2000);
        PhongMaterial materialJupiter = new PhongMaterial();
        materialJupiter.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/jupiter.bmp")));
        jupiter.setMaterial(materialJupiter);

        Sphere earth = new Sphere(150);
        earth.setTranslateX(400);
        earth.setTranslateY(300);
        earth.setTranslateZ(1000);
        PhongMaterial materialEarth = new PhongMaterial();
        materialEarth.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/earth.bmp")));
        earth.setMaterial(materialEarth);

        Sphere moon = new Sphere(200);
        moon.setTranslateX(1000);
        moon.setTranslateY(100);
        moon.setTranslateZ(-1000);
        PhongMaterial materialMoon = new PhongMaterial();
        materialMoon.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/moon.bmp")));
        moon.setMaterial(materialMoon);

        Sphere mars = new Sphere(70);
        mars.setTranslateX(-2500);
        mars.setTranslateY(-200);
        mars.setTranslateZ(500);
        PhongMaterial materialMars = new PhongMaterial();
        materialMars.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/mars.bmp")));
        mars.setMaterial(materialMars);


        Sphere venus = new Sphere(30);
        venus.setTranslateX(-2500);
        venus.setTranslateY(0);
        venus.setTranslateZ(-2000);
        PhongMaterial materialVenus = new PhongMaterial();
        materialVenus.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/venus.bmp")));
        venus.setMaterial(materialVenus);


        groupBackground.getChildren().addAll(sun, pointLight, jupiter, earth, moon, mars, venus);

        setStars(400);

    }

    public void setStars(int n) {

        PhongMaterial ms2 = new PhongMaterial(Color.SANDYBROWN);
        ms2.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/images/s3.bmp")));
        ms2.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/s3.bmp")));

        PhongMaterial ms1 = new PhongMaterial(Color.STEELBLUE);
        ms1.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/images/s2.bmp")));
        ms1.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/s2.bmp")));

        PhongMaterial ms0 = new PhongMaterial(Color.STEELBLUE);
        ms0.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/resources/images/s1.bmp")));
        ms0.setDiffuseMap(new Image(getClass().getResourceAsStream("/resources/images/s1.bmp")));


        Random random = new Random();
        Point3D vec;
        for (int i = 0; i < n; i++) {
            int isGalaxyStarNumber = random.nextInt(5);
            if (isGalaxyStarNumber == 0) {
                vec = new Point3D(
                        random.nextDouble() - 0.5,
                        random.nextDouble() - 0.5,
                        random.nextDouble() - 0.5
                ).normalize();
            } else {
                vec = new Point3D(
                        30 * (random.nextDouble() - 0.5),
                        random.nextDouble() - 0.5,
                        30 * (random.nextDouble() - 0.5)
                ).normalize();
            }

            double mod = 3000 + random.nextDouble() * 100;
            vec = vec.multiply(mod);
            Sphere star = new Sphere(random.nextInt(5));
            star.setTranslateX(vec.getX());
            star.setTranslateY(vec.getY());
            star.setTranslateZ(vec.getZ());
            int materialNumber = random.nextInt(3);
            if (materialNumber == 0) {
                star.setMaterial(ms0);
            } else if (materialNumber == 1) {
                star.setMaterial(ms1);
            } else if (materialNumber == 2) {
                star.setMaterial(ms2);
            }
            groupBackground.getChildren().add(star);
        }

    }


}
