package elements;

import animations.Animation;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import physic.*;
import protocols.*;

import java.util.List;

public class Spaceship implements Element {

    Animation animation;

    public Chain chain;

    public Group group;
    public List<Chain> chainList;

    Chain trackedChain;

    public RotaryMotor rotaryMotorL1, rotaryMotorL3, rotaryMotorR1, rotaryMotorR3;
    public RotaryMotor rotaryMotorF1, rotaryMotorF3, rotaryMotorB1, rotaryMotorB3;
    public RotaryMotor rotaryMotorU1, rotaryMotorU2;
    public RotaryMotor rotaryMotorGripper;

    public LinearMotor linearMotorRing, linearMotorRotaryMotor, linearMotorGripper;

    public JetEngine jetEngineL1, jetEngineL3, jetEngineLU, jetEngineLD;
    public JetEngine jetEngineR1, jetEngineR3, jetEngineRU, jetEngineRD;
    public JetEngine jetEngineF1, jetEngineF3, jetEngineFU, jetEngineFD;
    public JetEngine jetEngineB1, jetEngineB3, jetEngineBU, jetEngineBD;

    public JetEngine jetEngineMain;

    public Gripper gripper;

    public Joint j0_Chamber, j0_blm2, jblm2_blm1, jblm2_blm3, j0_blj2, jblj2_blj1, jblj2_blj3, jblj2_blju, jblj2_bljd,
            j0_brm2, jbrm2_brm1, jbrm2_brm3, j0_brj2, jbrj2_brj1, jbrj2_brj3, jbrj2_brju, jbrj2_brjd, j0_bfm2,
            jbfm2_bfm1, jbfm2_bfm3, j0_bbm2, jbbm2_bbm1, jbbm2_bbm3, jChamber_u, ju_u1, ju_u2, jEnd_Gapbd, jGapbd_Gapbu, jEnd_Gapfd, jGapfd_Gapfu,
            jEnd_Gaprd, jGaprd_Gapru, jEnd_Gapld, jGapld_Gaplu, jChamber_RotaryMotor, jRotaryMotor_Gripper, jChamber_End,
            jEnd_MainJet, j0_bbj2, jbbj2_bbj1, jbbj2_bbj3, jbbj2_bbju, jbbj2_bbjd, j0_bfj2, jbfj2_bfj1, jbfj2_bfj3,
            jbfj2_bfju, jbfj2_bfjd;


    public Ball b0, bChamber, blm1, blm2, blm3,
            blj1, blj2, blj3, blju, bljd, brm1, brm2, brm3, brj1, brj2, brj3, brju, brjd, bfm1, bfm2, bfm3, bbm1, bbm2, bbm3, bu, bu1, bu2,
            bGapbd, bGapbu, bGapfd, bGapfu, bGaprd, bGapru, bGapld, bGaplu, bRotaryMotor, bGripper, bEnd, bMAinJet, bbj1,
            bbj2, bbj3, bbju, bbjd, bfj1, bfj2, bfj3, bfju, bfjd;


    public Protocol protocol_AlignTheRingToTheCentreOfGravity, protocol_MoveDown, protocol_MoveUp, protocol_MoveLeft,
            protocol_MoveRight, protocol_MoveForward, protocol_MoveBackward, protocol_InhibitTheRotation,
            protocol_SetTheSpaceshipTowardTheChain, protocol_EquateToVelocityOfTheChain, protocol_RotateXBackward,
            protocol_RotateXForward, protocol_RotateYLeft, protocol_RotateYRight, protocol_RotateZLeft,
            protocol_RotateZRight, protocol_RotateXBackwardAcceleration, protocol_RotateXForwardAcceleration,
            protocol_RotateYLeftAcceleration, protocol_RotateYRightAcceleration, protocol_RotateZLeftAcceleration,
            protocol_RotateZRightAcceleration;


    public Spaceship(double x, double y, double z, Group group, List<Chain> chainList, Animation animation) {
        this.animation = animation;
        this.group = group;
        this.chainList = chainList;

        this.protocol_MoveUp = new Protocol_MoveUp(this);
        this.protocol_MoveDown = new Protocol_MoveDown(this);
        this.protocol_MoveLeft = new Protocol_MoveLeft(this);
        this.protocol_MoveRight = new Protocol_MoveRight(this);
        this.protocol_MoveBackward = new Protocol_MoveBackward(this);
        this.protocol_MoveForward = new Protocol_MoveForward(this);

        this.protocol_RotateXBackward = new Protocol_RotateXBackward(this);
        this.protocol_RotateXForward = new Protocol_RotateXForward(this);
        this.protocol_RotateYLeft = new Protocol_RotateYLeft(this);
        this.protocol_RotateYRight = new Protocol_RotateYRight(this);
        this.protocol_RotateZLeft = new Protocol_RotateZLeft(this);
        this.protocol_RotateZRight = new Protocol_RotateZRight(this);

        this.protocol_RotateXBackwardAcceleration = new Protocol_RotateXBackwardAcceleration(this);
        this.protocol_RotateXForwardAcceleration = new Protocol_RotateXForwardAcceleration(this);
        this.protocol_RotateYLeftAcceleration = new Protocol_RotateYLeftAcceleration(this);
        this.protocol_RotateYRightAcceleration = new Protocol_RotateYRightAcceleration(this);
        this.protocol_RotateZLeftAcceleration = new Protocol_RotateZLeftAcceleration(this);
        this.protocol_RotateZRightAcceleration = new Protocol_RotateZRightAcceleration(this);

        this.protocol_EquateToVelocityOfTheChain = new Protocol_EquateToVelocityOfTheChain(this, null);
        this.protocol_InhibitTheRotation = new Protocol_InhibitTheRotation(this);

        this.protocol_AlignTheRingToTheCentreOfGravity = new Protocol_AlignTheRingToTheCentreOfGravity(this);

        this.protocol_SetTheSpaceshipTowardTheChain = new Protocol_SetTheSpaceshipTowardTheChain(this, null);

        b0 = new Ball(0 + x, 0 + y, 0 + z, 5, 5);
        bChamber = new Ball(0 + x, -20 + y, 0 + z, 15, 5);
        j0_Chamber = new Joint(b0, bChamber, 5);

        ///L
        blm1 = new Ball(-25 + x, 0 + y, -15 + z, 5, 50);
        blm2 = new Ball(-25 + x, 0 + y, 0 + z, 5, 5);
        blm3 = new Ball(-25 + x, 0 + y, 15 + z, 5, 50);
        j0_blm2 = new Joint(b0, blm2, 5);
        jblm2_blm1 = new Joint(blm2, blm1, 5);
        jblm2_blm3 = new Joint(blm2, blm3, 5);

        blj1 = new Ball(-40 + x, 0 + y, -10 + z, 2.5, 5);
        blj2 = new Ball(-40 + x, 0 + y, 0 + z, 2.5, 5);
        blj3 = new Ball(-40 + x, 0 + y, 10 + z, 2.5, 5);
        blju = new Ball(-40 + x, -10 + y, 0 + z, 2.5, 5);
        bljd = new Ball(-40 + x, 10 + y, 0 + z, 2.5, 5);

        j0_blj2 = new Joint(b0, blj2, 2.5);//
        jblj2_blj1 = new Joint(blj2, blj1, 2.5);
        jblj2_blj3 = new Joint(blj2, blj3, 2.5);
        jblj2_blju = new Joint(blj2, blju, 2.5);
        jblj2_bljd = new Joint(blj2, bljd, 2.5);

        ///R
        brm1 = new Ball(25 + x, 0 + y, 15 + z, 5, 50);
        brm2 = new Ball(25 + x, 0 + y, 0 + z, 5, 5);
        brm3 = new Ball(25 + x, 0 + y, -15 + z, 5, 50);
        j0_brm2 = new Joint(b0, brm2, 5);
        jbrm2_brm1 = new Joint(brm2, brm1, 5);
        jbrm2_brm3 = new Joint(brm2, brm3, 5);

        brj1 = new Ball(40 + x, 0 + y, 10 + z, 2.5, 5);
        brj2 = new Ball(40 + x, 0 + y, 0 + z, 2.5, 5);
        brj3 = new Ball(40 + x, 0 + y, -10 + z, 2.5, 5);
        brju = new Ball(40 + x, -10 + y, 0 + z, 2.5, 5);
        brjd = new Ball(40 + x, 10 + y, 0 + z, 2.5, 5);

        j0_brj2 = new Joint(b0, brj2, 2.5);//
        jbrj2_brj1 = new Joint(brj2, brj1, 2.5);
        jbrj2_brj3 = new Joint(brj2, brj3, 2.5);
        jbrj2_brju = new Joint(brj2, brju, 2.5);
        jbrj2_brjd = new Joint(brj2, brjd, 2.5);

        ///F
        bfm1 = new Ball(-15 + x, 0 + y, 25 + z, 5, 50);
        bfm2 = new Ball(0 + x, 0 + y, 25 + z, 5, 5);
        bfm3 = new Ball(15 + x, 0 + y, 25 + z, 5, 50);
        j0_bfm2 = new Joint(b0, bfm2, 5);
        jbfm2_bfm1 = new Joint(bfm2, bfm1, 5);
        jbfm2_bfm3 = new Joint(bfm2, bfm3, 5);

        bfj1 = new Ball(-10 + x, 0 + y, 40 + z, 2.5, 5);
        bfj2 = new Ball(0 + x, 0 + y, 40 + z, 2.5, 5);
        bfj3 = new Ball(10 + x, 0 + y, 40 + z, 2.5, 5);
        bfju = new Ball(0 + x, -10 + y, 40 + z, 2.5, 5);
        bfjd = new Ball(0 + x, 10 + y, 40 + z, 2.5, 5);

        j0_bfj2 = new Joint(b0, bfj2, 2.5);//
        jbfj2_bfj1 = new Joint(bfj2, bfj1, 2.5);
        jbfj2_bfj3 = new Joint(bfj2, bfj3, 2.5);
        jbfj2_bfju = new Joint(bfj2, bfju, 2.5);
        jbfj2_bfjd = new Joint(bfj2, bfjd, 2.5);

        ///B
        bbm1 = new Ball(15 + x, 0 + y, -25 + z, 5, 50);
        bbm2 = new Ball(0 + x, 0 + y, -25 + z, 5, 5);
        bbm3 = new Ball(-15 + x, 0 + y, -25 + z, 5, 50);
        j0_bbm2 = new Joint(b0, bbm2, 5);
        jbbm2_bbm1 = new Joint(bbm2, bbm1, 5);
        jbbm2_bbm3 = new Joint(bbm2, bbm3, 5);

        bbj1 = new Ball(10 + x, 0 + y, -40 + z, 2.5, 5);
        bbj2 = new Ball(0 + x, 0 + y, -40 + z, 2.5, 5);
        bbj3 = new Ball(-10 + x, 0 + y, -40 + z, 2.5, 5);
        bbju = new Ball(0 + x, -10 + y, -40 + z, 2.5, 5);
        bbjd = new Ball(0 + x, 10 + y, -40 + z, 2.5, 5);

        j0_bbj2 = new Joint(b0, bbj2, 2.5);//
        jbbj2_bbj1 = new Joint(bbj2, bbj1, 2.5);
        jbbj2_bbj3 = new Joint(bbj2, bbj3, 2.5);
        jbbj2_bbju = new Joint(bbj2, bbju, 2.5);
        jbbj2_bbjd = new Joint(bbj2, bbjd, 2.5);

        ///
        bRotaryMotor = new Ball(0 + x, -45 + y, 0 + z, 5, 5);
        bGripper = new Ball(15 + x, -60 + y, 0 + z, 15, 5);
        bEnd = new Ball(0 + x, 40 + y, 0 + z, 10, 70);
        bMAinJet = new Ball(0 + x, 60 + y, 0 + z, 5, 5);

        jChamber_RotaryMotor = new Joint(bChamber, bRotaryMotor, 5);
        jRotaryMotor_Gripper = new Joint(bRotaryMotor, bGripper, 2.5);
        jChamber_End = new Joint(bChamber, bEnd, 10);
        jEnd_MainJet = new Joint(bEnd, bMAinJet, 5);

        /// gaps
        bGapld = new Ball(-10 + x, 40 + y, 0 + z, 5, 0.5);
        bGaplu = new Ball(-10 + x, -20 + y, 0 + z, 5, 0.5);
        jEnd_Gapld = new Joint(bEnd, bGapld, 5);
        jGapld_Gaplu = new Joint(bGapld, bGaplu, 5);

        bGaprd = new Ball(10 + x, 40 + y, 0 + z, 5, 0.5);
        bGapru = new Ball(10 + x, -20 + y, 0 + z, 5, 0.5);
        jEnd_Gaprd = new Joint(bEnd, bGaprd, 5);
        jGaprd_Gapru = new Joint(bGaprd, bGapru, 5);


        bGapfd = new Ball(0 + x, 40 + y, 10 + z, 5, 0.5);
        bGapfu = new Ball(0 + x, -20 + y, 10 + z, 5, 0.5);
        jEnd_Gapfd = new Joint(bEnd, bGapfd, 5);
        jGapfd_Gapfu = new Joint(bGapfd, bGapfu, 5);

        bGapbd = new Ball(0 + x, 40 + y, -10 + z, 5, 0.5);
        bGapbu = new Ball(0 + x, -20 + y, -10 + z, 5, 0.5);
        jEnd_Gapbd = new Joint(bEnd, bGapbd, 5);
        jGapbd_Gapbu = new Joint(bGapbd, bGapbu, 5);

        /// u1 u2
        bu = new Ball(0 + x, -25 + y, 0 + z, 5, 5);
        bu1 = new Ball(-25 + x, -30 + y, 0 + z, 5, 50);
        bu2 = new Ball(25 + x, -30 + y, 0 + z, 5, 50);
        jChamber_u = new Joint(bChamber, bu, 5);
        ju_u1 = new Joint(bu, bu1, 5);
        ju_u2 = new Joint(bu, bu2, 5);

        chain = new Chain(b0);

        double WEngine = 0.1;

        rotaryMotorL1 = new RotaryMotor(blm2, j0_blm2, jblm2_blm1, chain, WEngine);
        rotaryMotorL3 = new RotaryMotor(blm2, j0_blm2, jblm2_blm3, chain, WEngine);
        rotaryMotorR1 = new RotaryMotor(brm2, j0_brm2, jbrm2_brm1, chain, WEngine);
        rotaryMotorR3 = new RotaryMotor(brm2, j0_brm2, jbrm2_brm3, chain, WEngine);

        rotaryMotorF1 = new RotaryMotor(bfm2, j0_bfm2, jbfm2_bfm1, chain, WEngine);
        rotaryMotorF3 = new RotaryMotor(bfm2, j0_bfm2, jbfm2_bfm3, chain, WEngine);
        rotaryMotorB1 = new RotaryMotor(bbm2, j0_bbm2, jbbm2_bbm1, chain, WEngine);
        rotaryMotorB3 = new RotaryMotor(bbm2, j0_bbm2, jbbm2_bbm3, chain, WEngine);

        rotaryMotorU1 = new RotaryMotor(bChamber, jChamber_u, ju_u1, chain, WEngine);
        rotaryMotorU2 = new RotaryMotor(bChamber, jChamber_u, ju_u2, chain, WEngine);

        rotaryMotorGripper = new RotaryMotor(bRotaryMotor, jChamber_RotaryMotor, jRotaryMotor_Gripper, chain, WEngine / 10);

        linearMotorGripper = new LinearMotor(jRotaryMotor_Gripper, chain, 0.1);
        linearMotorRing = new LinearMotor(j0_Chamber, chain, 0.1);
        linearMotorRotaryMotor = new LinearMotor(jChamber_RotaryMotor, chain, 0.8);

        double momentum = 15;
        jetEngineL1 = new JetEngine(chain, blj2, blj1, momentum);
        jetEngineL3 = new JetEngine(chain, blj2, blj3, momentum);
        jetEngineLU = new JetEngine(chain, blj2, blju, momentum);
        jetEngineLD = new JetEngine(chain, blj2, bljd, momentum);

        jetEngineR1 = new JetEngine(chain, brj2, brj1, momentum);
        jetEngineR3 = new JetEngine(chain, brj2, brj3, momentum);
        jetEngineRU = new JetEngine(chain, brj2, brju, momentum);
        jetEngineRD = new JetEngine(chain, brj2, brjd, momentum);

        jetEngineF1 = new JetEngine(chain, bfj2, bfj1, momentum);
        jetEngineF3 = new JetEngine(chain, bfj2, bfj3, momentum);
        jetEngineFU = new JetEngine(chain, bfj2, bfju, momentum);
        jetEngineFD = new JetEngine(chain, bfj2, bfjd, momentum);

        jetEngineB1 = new JetEngine(chain, bbj2, bbj1, momentum);
        jetEngineB3 = new JetEngine(chain, bbj2, bbj3, momentum);
        jetEngineBU = new JetEngine(chain, bbj2, bbju, momentum);
        jetEngineBD = new JetEngine(chain, bbj2, bbjd, momentum);

        jetEngineMain = new JetEngine(chain, bEnd, bMAinJet, 3 * momentum);

        gripper = new Gripper(chain, bGripper, bRotaryMotor, jRotaryMotor_Gripper, chainList);

    }

    public void setTrackedChain(Chain trackedChain) {
        this.trackedChain = trackedChain;
        this.protocol_EquateToVelocityOfTheChain = new Protocol_EquateToVelocityOfTheChain(this, trackedChain);
        this.protocol_SetTheSpaceshipTowardTheChain = new Protocol_SetTheSpaceshipTowardTheChain(this, trackedChain);
    }

    public Chain getTrackedChain() {
        return trackedChain;
    }

    @Override
    public void create() {
        chainList.add(chain);

        group.getChildren().addAll(chain.ballsList);
        group.getChildren().addAll(chain.jointList);

        setAnimations();
    }

    @Override
    public void delete() {
        chainList.remove(chain);

        group.getChildren().removeAll(chain.ballsList);
        group.getChildren().removeAll(chain.jointList);

    }

    public void setAnimations() {
        protocol_RotateXBackward.turnOn();
        protocol_RotateXForward.turnOn();

        jEnd_Gapld.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jGapld_Gaplu.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jEnd_Gaprd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jGaprd_Gapru.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jEnd_Gapfd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jGapfd_Gapfu.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jEnd_Gapbd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        jGapbd_Gapbu.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));

        bGapbd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        bGapfd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        bGapld.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));
        bGaprd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 1)));

        blj1.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        blj3.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        blju.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bljd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));

        brj1.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        brj3.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        brju.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        brjd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));

        bfj1.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bfj3.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bfju.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bfjd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));

        bbj1.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bbj3.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bbju.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bbjd.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));

        bMAinJet.setMaterial(new PhongMaterial(new Color(0.1, 0.1, 0.1, 0)));
        bGripper.setRotationCausedByBallsPosition(bGripper, bRotaryMotor);
        bGripper.setAnimatedTexture(animation.aGripper);

        bChamber.setAnimatedTexture(animation.aChamber);
        bChamber.setRotationCausedByBallsPosition(bChamber, bEnd);


        bRotaryMotor.setAnimatedTexture(animation.aRotaryMotor);
        bRotaryMotor.setRotationCausedByBallsPosition(b0, bbm2);

        jRotaryMotor_Gripper.setMaterial(new PhongMaterial(Color.WHITE));
        jChamber_RotaryMotor.setMaterial(new PhongMaterial(Color.WHITE));

        jChamber_End.setAnimatedTexture(animation.aGrey);
        ju_u1.setAnimatedTexture(animation.aGrey);
        ju_u2.setAnimatedTexture(animation.aGrey);

        jbfm2_bfm1.setAnimatedTexture(animation.aGreen);
        jbfm2_bfm3.setAnimatedTexture(animation.aGreen);

        jbbm2_bbm1.setAnimatedTexture(animation.aRed);
        jbbm2_bbm3.setAnimatedTexture(animation.aRed);

        jblm2_blm1.setAnimatedTexture(animation.aBlue);
        jblm2_blm3.setAnimatedTexture(animation.aBlue);

        jbrm2_brm1.setAnimatedTexture(animation.aBlue);
        jbrm2_brm3.setAnimatedTexture(animation.aBlue);


        PhongMaterial grey = new PhongMaterial();
        grey.setDiffuseMap(new Image("/resources/material/grey.png"));
        PhongMaterial red = new PhongMaterial();
        red.setDiffuseMap(new Image("/resources/material/red.png"));
        PhongMaterial blue = new PhongMaterial();
        blue.setDiffuseMap(new Image("/resources/material/blue.png"));


        blm1.setMaterial(blue);
        blm3.setMaterial(blue);
        brm1.setMaterial(blue);
        brm3.setMaterial(blue);

        bbm1.setMaterial(red);
        bbm3.setMaterial(red);
        bfm1.setMaterial(grey);
        bfm3.setMaterial(grey);

        bEnd.setMaterial(grey);

    }

    public void setAllEnginesFalse() {

        jetEngineL1.isActive = false;
        jetEngineL3.isActive = false;
        jetEngineLU.isActive = false;
        jetEngineLD.isActive = false;

        jetEngineR1.isActive = false;
        jetEngineR3.isActive = false;
        jetEngineRU.isActive = false;
        jetEngineRD.isActive = false;

        jetEngineF1.isActive = false;
        jetEngineF3.isActive = false;
        jetEngineFU.isActive = false;
        jetEngineFD.isActive = false;

        jetEngineB1.isActive = false;
        jetEngineB3.isActive = false;
        jetEngineBU.isActive = false;
        jetEngineBD.isActive = false;

        jetEngineMain.isActive = false;

        rotaryMotorL1.isActive = false;
        rotaryMotorL3.isActive = false;
        rotaryMotorR1.isActive = false;
        rotaryMotorR3.isActive = false;

        rotaryMotorF1.isActive = false;
        rotaryMotorF3.isActive = false;
        rotaryMotorB1.isActive = false;
        rotaryMotorB3.isActive = false;

        rotaryMotorU1.isActive = false;
        rotaryMotorU2.isActive = false;

        rotaryMotorGripper.isActive = false;

        linearMotorGripper.isActive = false;
        linearMotorRing.isActive = false;
        linearMotorRotaryMotor.isActive = false;

    }

    public void serveAllJetsAnimation() {

        try {

            if (jetEngineL1.isActive) {
                jblj2_blj1.setAnimatedTexture(animation.aJetOn);
            } else {
                jblj2_blj1.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineL3.isActive) {
                jblj2_blj3.setAnimatedTexture(animation.aJetOn);
            } else {
                jblj2_blj3.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineLU.isActive) {
                jblj2_blju.setAnimatedTexture(animation.aJetOn);
            } else {
                jblj2_blju.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineLD.isActive) {
                jblj2_bljd.setAnimatedTexture(animation.aJetOn);
            } else {
                jblj2_bljd.setAnimatedTexture(animation.aJet);
            }

            if (jetEngineR1.isActive) {
                jbrj2_brj1.setAnimatedTexture(animation.aJetOn);
            } else {
                jbrj2_brj1.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineR3.isActive) {
                jbrj2_brj3.setAnimatedTexture(animation.aJetOn);
            } else {
                jbrj2_brj3.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineRU.isActive) {
                jbrj2_brju.setAnimatedTexture(animation.aJetOn);
            } else {
                jbrj2_brju.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineRD.isActive) {
                jbrj2_brjd.setAnimatedTexture(animation.aJetOn);
            } else {
                jbrj2_brjd.setAnimatedTexture(animation.aJet);
            }

            if (jetEngineF1.isActive) {
                jbfj2_bfj1.setAnimatedTexture(animation.aJetOn);
            } else {
                jbfj2_bfj1.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineF3.isActive) {
                jbfj2_bfj3.setAnimatedTexture(animation.aJetOn);
            } else {
                jbfj2_bfj3.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineFU.isActive) {
                jbfj2_bfju.setAnimatedTexture(animation.aJetOn);
            } else {
                jbfj2_bfju.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineFD.isActive) {
                jbfj2_bfjd.setAnimatedTexture(animation.aJetOn);
            } else {
                jbfj2_bfjd.setAnimatedTexture(animation.aJet);
            }

            if (jetEngineB1.isActive) {
                jbbj2_bbj1.setAnimatedTexture(animation.aJetOn);
            } else {
                jbbj2_bbj1.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineB3.isActive) {
                jbbj2_bbj3.setAnimatedTexture(animation.aJetOn);
            } else {
                jbbj2_bbj3.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineBU.isActive) {
                jbbj2_bbju.setAnimatedTexture(animation.aJetOn);
            } else {
                jbbj2_bbju.setAnimatedTexture(animation.aJet);
            }
            if (jetEngineBD.isActive) {
                jbbj2_bbjd.setAnimatedTexture(animation.aJetOn);
            } else {
                jbbj2_bbjd.setAnimatedTexture(animation.aJet);
            }

            if (jetEngineMain.isActive) {
                jEnd_MainJet.setAnimatedTexture(animation.aMainJetOn);
            } else {
                jEnd_MainJet.setAnimatedTexture(animation.aMainJet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
