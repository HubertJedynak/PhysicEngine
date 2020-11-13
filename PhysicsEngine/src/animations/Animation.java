package animations;

import graphics.AnimatedTexture;
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;

public class Animation {

    public AnimatedTexture aGripper;
    public AnimatedTexture aGripperOn;

    public AnimatedTexture aJet;
    public AnimatedTexture aJetOn;

    public AnimatedTexture aMainJet;
    public AnimatedTexture aMainJetOn;

    public AnimatedTexture aChamber;
    public AnimatedTexture aRotaryMotor;

    public AnimatedTexture aGrey;
    public AnimatedTexture aGreen;
    public AnimatedTexture aRed;
    public AnimatedTexture aBlue;

    public Animation() {
        setaGripper();
        setaGripperOn();
        setaJet();
        setaJetOn();
        setaMainJet();
        setaMainJetOn();
        setaChamber();
        setaRotaryMotor();
        setaGrey();
        setaGreen();
        setaRed();
        setaBlue();
    }


    public void setaGrey() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/material/greyDiode.png"));
        m1.setSelfIlluminationMap(new Image("/resources/material/iBlue.png"));
        m2.setDiffuseMap(new Image("/resources/material/greyDiode.png"));
        m2.setSelfIlluminationMap(new Image("/resources/material/iBlink.png"));

        aGrey = new AnimatedTexture(100, m1, m2);
    }

    public void setaGreen() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/material/greenDiode.png"));
        m1.setSelfIlluminationMap(new Image("/resources/material/iGreen.png"));
        m2.setDiffuseMap(new Image("/resources/material/greenDiode.png"));
        m2.setSelfIlluminationMap(new Image("/resources/material/iBlink.png"));

        aGreen = new AnimatedTexture(100, m1, m2);
    }

    public void setaRed() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/material/redDiode.png"));
        m1.setSelfIlluminationMap(new Image("/resources/material/iRed.png"));
        m2.setDiffuseMap(new Image("/resources/material/redDiode.png"));
        m2.setSelfIlluminationMap(new Image("/resources/material/iBlink.png"));

        aRed = new AnimatedTexture(100, m1, m2);
    }

    public void setaBlue() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/material/blueDiode.png"));
        m1.setSelfIlluminationMap(new Image("/resources/material/iBlue.png"));
        m2.setDiffuseMap(new Image("/resources/material/blueDiode.png"));
        m2.setSelfIlluminationMap(new Image("/resources/material/iBlink.png"));

        aBlue = new AnimatedTexture(100, m1, m2);
    }

    public void setaRotaryMotor() {
        PhongMaterial m1 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/rotaryMotor/rotaryMotor.png"));

        aRotaryMotor = new AnimatedTexture(100, m1);
    }

    public void setaChamber() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/chamber/chamber.png"));
        m1.setSelfIlluminationMap(new Image("/resources/chamber/iChamber.png"));
        m2.setDiffuseMap(new Image("/resources/chamber/chamberBlink.png"));
        m2.setSelfIlluminationMap(new Image("/resources/chamber/iChamberBlink.png"));


        aChamber = new AnimatedTexture(100, m1, m2);

    }

    public void setaMainJet() {
        PhongMaterial m1 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/mainJet/jetMain.png"));

        aMainJet = new AnimatedTexture(1000, m1);

    }

    public void setaMainJetOn() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();
        PhongMaterial m3 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/mainJet/jetMainOn1.png"));
        m1.setSelfIlluminationMap(new Image("/resources/mainJet/jetMainOn1.png"));
        m2.setDiffuseMap(new Image("/resources/mainJet/jetMainOn2.png"));
        m2.setSelfIlluminationMap(new Image("/resources/mainJet/jetMainOn2.png"));
        m3.setDiffuseMap(new Image("/resources/mainJet/jetMainOn3.png"));
        m3.setSelfIlluminationMap(new Image("/resources/mainJet/jetMainOn3.png"));

        aMainJetOn = new AnimatedTexture(1, m1, m2, m3);

    }

    public void setaJetOn() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();
        PhongMaterial m3 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/jet/jetOn1.png"));
        m1.setSelfIlluminationMap(new Image("/resources/jet/jetOn1.png"));
        m2.setDiffuseMap(new Image("/resources/jet/jetOn2.png"));
        m2.setSelfIlluminationMap(new Image("/resources/jet/jetOn2.png"));
        m3.setDiffuseMap(new Image("/resources/jet/jetOn3.png"));
        m3.setSelfIlluminationMap(new Image("/resources/jet/jetOn3.png"));

        aJetOn = new AnimatedTexture(5, m1, m2, m3);

    }

    public void setaJet() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/jet/jet.png"));
        m1.setSelfIlluminationMap(new Image("/resources/jet/iJet.png"));
        m2.setDiffuseMap(new Image("/resources/jet/jetBlink.png"));
        m2.setSelfIlluminationMap(new Image("/resources/jet/iJetBlink.png"));
        aJet = new AnimatedTexture(16 * 100, m1, m2);

    }

    public void setaGripper() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/gripper/gripper.png"));
        m1.setSelfIlluminationMap(new Image("/resources/gripper/iGripper.png"));
        m2.setDiffuseMap(new Image("/resources/gripper/gripperBlink.png"));
        m2.setSelfIlluminationMap(new Image("/resources/gripper/iGripperBlink.png"));
        aGripper = new AnimatedTexture(100, m1, m2);
    }

    public void setaGripperOn() {
        PhongMaterial m1 = new PhongMaterial();
        PhongMaterial m2 = new PhongMaterial();

        m1.setDiffuseMap(new Image("/resources/gripper/gripperOn.png"));
        m1.setSelfIlluminationMap(new Image("/resources/gripper/iGripperOn.png"));
        m2.setDiffuseMap(new Image("/resources/gripper/gripperOnBlink.png"));
        m2.setSelfIlluminationMap(new Image("/resources/gripper/iGripperBlink.png"));
        aGripperOn = new AnimatedTexture(50, m1, m2);

    }
}
