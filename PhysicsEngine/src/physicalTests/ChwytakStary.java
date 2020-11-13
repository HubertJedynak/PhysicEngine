package physicalTests;

import javafx.geometry.Point3D;
import physic.*;

import java.util.LinkedList;
import java.util.List;
import physic.Ball;

public class ChwytakStary {
    Ball bChwytajaca; // chwytajaca rzeczy
    Ball bPodczepiona; // tu chwytak jest podczepiony
    Joint jChwytak;
    List<Chain> chainsList;
    Chain c; // chain z chwytakiem
    Chain grabbedChain; // chwycony chain

    List<Ball> oldBallList = new LinkedList<>();
    // UWAGA DODAC GRABBED JOINT I GRABBED BALL
//    Ball grabbedBall = null;
//    Joint grabbedJoint = null;

    public ChwytakStary(Chain c, Ball bChwytajaca, Ball bPodczepiona, Joint jChwytak, List<Chain> chainsList){
        this.bChwytajaca = bChwytajaca;
        this.bPodczepiona = bPodczepiona;
        this.jChwytak = jChwytak;
        this.chainsList = chainsList;
        this.c = c;
        bChwytajaca.isCollisionInteractive = false;
        jChwytak.isCollisionInteractive = false;
        jChwytak.isCollisionDetectable = false;
    }

    // UWAGA NA RAZIE CHYTA KAKRETNEGO JOINTA
    public void grab(Chain cgrabbed, Joint j){
        if(CollisionsOld.ChainsJointBallInJointEngines.detectCollision(j,bChwytajaca)){
            if(c.ballsList.contains(cgrabbed.ballsList.get(0))){
                return;
            }
            this.grabbedChain = cgrabbed;
            chainsList.remove(grabbedChain);

            oldBallList.addAll(c.ballsList);

            Point3D noweV = ( c.V.multiply(c.m) ).add( grabbedChain.V.multiply(grabbedChain.m) ).multiply( 1/(c.m+grabbedChain.m) );
            //System.out.println(noweV);
            // licze wypadkowy moment pedu
            // jak W = 0 to L = 0
            Point3D Lc;
            if(c.W.magnitude() != 0){
                double k = 0;
                for (Ball b : c.ballsList) {
                    Point3D r = new Point3D(
                            b.x.get() - c.centreOfGravity.getX(),
                            b.y.get() - c.centreOfGravity.getY(),
                            b.z.get() - c.centreOfGravity.getZ()
                    );
                    double modRProstopadle = (c.W.crossProduct(r)).magnitude() / c.W.magnitude();
                    k += modRProstopadle * modRProstopadle;
                }
                double Ic = c.m * k;
                Lc = c.W.multiply(Ic);
            }else{
                Lc = Point3D.ZERO;
            }

            Point3D LGrabbedChain;
            if (grabbedChain.W.magnitude() != 0){

                double k = 0;
                for (Ball b : grabbedChain.ballsList) {
                    Point3D r = new Point3D(
                            b.x.get() - grabbedChain.centreOfGravity.getX(),
                            b.y.get() - grabbedChain.centreOfGravity.getY(),
                            b.z.get() - grabbedChain.centreOfGravity.getZ()
                    );
                    double modRProstopadle = (grabbedChain.W.crossProduct(r)).magnitude() / grabbedChain.W.magnitude();
                    k += modRProstopadle * modRProstopadle;
                }
                double IgrabbedChain = grabbedChain.m * k;
                LGrabbedChain = grabbedChain.W.multiply(IgrabbedChain);
            } else{
                LGrabbedChain = Point3D.ZERO;
            }

            double noweM = c.m + grabbedChain.m;
            Point3D nowySrodekCiezkosci = (( c.centreOfGravity.multiply(c.m) ).add( grabbedChain.centreOfGravity.multiply(grabbedChain.m) )).multiply(1/noweM);

            // uwaga: nawet jak oba ciała się nie obracaja, to moga dać moment po polaczeniu
            // wektory miedzy srodkami ciezkosci
            Point3D noweC_C = c.centreOfGravity.subtract(nowySrodekCiezkosci);
            Point3D noweC_GrabbedChain = grabbedChain.centreOfGravity.subtract(nowySrodekCiezkosci);

            Point3D LcSpowodowaneZderzeniem = noweC_C.crossProduct( c.V.multiply(c.m) );
            Point3D LGrabbedChainSpowodowaneZderzeniem = noweC_GrabbedChain.crossProduct( grabbedChain.V.multiply(grabbedChain.m));

            Point3D noweL = ( Lc ).add( LGrabbedChain ).add( LcSpowodowaneZderzeniem ).add( LGrabbedChainSpowodowaneZderzeniem );

            // MODYFIKUJE SILNIKI
            // UWAGA: JESLI CHWYCONY JEST JOINT
            /////////////////////////////////
            for(LinearMotor i:grabbedChain.linearMotorList){
                if(i.jointList1.contains(j)){
                    System.out.println("tutaj1");
                    i.ballsList1.addAll(c.ballsList);
                }else{
                    System.out.println("tutaj2");
                    i.ballsList2.addAll(c.ballsList);
                }
            }

            for(RotaryMotor i:grabbedChain.rotaryMotorList){
                if(i.jointList1.contains(j)){
                    System.out.println("tutaj3");
                    i.ballsList1.addAll(c.ballsList);
                }else{
                    System.out.println("tutaj4");
                    i.ballsList2.addAll(c.ballsList);
                }
            }

            for(LinearMotor i:c.linearMotorList){
                if(i.ballsList1.contains(bChwytajaca)){
                    System.out.println("tutaj5");
                    i.ballsList1.addAll(grabbedChain.ballsList);
                }else{
                    System.out.println("tutaj6");
                    i.ballsList2.addAll(grabbedChain.ballsList);
                }
            }

            for(RotaryMotor i:c.rotaryMotorList){
                if(i.ballsList1.contains(bChwytajaca)){
                    System.out.println("tutaj7");
                    i.ballsList1.addAll(grabbedChain.ballsList);
                }else{
                    System.out.println("tutaj8");
                    i.ballsList2.addAll(grabbedChain.ballsList);
                }
            }

            /////////////////////////////////


            c.jointList.addAll(grabbedChain.jointList);
            c.ballsList.addAll(grabbedChain.ballsList);
            c.linearMotorList.addAll(grabbedChain.linearMotorList);
            c.rotaryMotorList.addAll(grabbedChain.rotaryMotorList);


            // LICZE NOWY SRODEK CIEZKOSCI
//            Point3D sumaRM = new Point3D(0, 0, 0);
//            double sumaM = 0;
//            for (Ball i : c.ballsList) {
//                sumaRM = sumaRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
//                sumaM += i.m;
//            }



            double k = 0;
            Point3D normL = noweL.normalize();
            for (Ball b : c.ballsList) {
                Point3D r = new Point3D(
                        b.x.get() - nowySrodekCiezkosci.getX(),
                        b.y.get() - nowySrodekCiezkosci.getY(),
                        b.z.get() - nowySrodekCiezkosci.getZ()
                );
                double modRProstopadle = (r.subtract( normL.multiply( normL.dotProduct(r) ) )).magnitude();
                k += modRProstopadle * modRProstopadle;
            }
            double noweI = c.m * k;

            Point3D noweW = noweL.multiply( 1/noweI );

            c.m = noweM;
            c.centreOfGravity = nowySrodekCiezkosci;
            c.W = noweW;
            c.V = noweV;
        }
    }



    public void release(){
        if(grabbedChain != null){

            chainsList.add(grabbedChain);

            Point3D starySrodekCiezkosci = c.centreOfGravity;
            Point3D stareV = c.V;
            Point3D stareW = c.W;



            c.jointList.removeAll(grabbedChain.jointList);
            c.ballsList.removeAll(grabbedChain.ballsList);
            c.linearMotorList.removeAll(grabbedChain.linearMotorList);
            c.rotaryMotorList.removeAll(grabbedChain.rotaryMotorList);

            // MODYFIKUJE SILNIKI
            ///////////////////////////////////////////
            for(LinearMotor i:grabbedChain.linearMotorList){
                i.ballsList1.removeAll(c.ballsList);
                i.ballsList2.removeAll(c.ballsList);

            }
            for(RotaryMotor i:grabbedChain.rotaryMotorList){
                i.ballsList1.removeAll(c.ballsList);
                i.ballsList2.removeAll(c.ballsList);
            }

            for(LinearMotor i:c.linearMotorList){
                i.ballsList1.removeAll(grabbedChain.ballsList);
                i.ballsList2.removeAll(grabbedChain.ballsList);
            }
            for(RotaryMotor i:c.rotaryMotorList){
                i.ballsList1.removeAll(grabbedChain.ballsList);
                i.ballsList2.removeAll(grabbedChain.ballsList);
            }

            //////////////////////////////////////////


            // srodek ciezkosci
            Point3D sumaRM = new Point3D(0, 0, 0);
            double sumaM = 0;
            for (Ball b : c.ballsList) {
                sumaRM = sumaRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumaM += b.m;
            }
            c.centreOfGravity = sumaRM.multiply(1 / sumaM);
            c.m = sumaM;

            sumaRM = new Point3D(0, 0, 0);
            sumaM = 0;
            for (Ball b : grabbedChain.ballsList) {
                sumaRM = sumaRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumaM += b.m;
            }
            grabbedChain.centreOfGravity = sumaRM.multiply(1 / sumaM);
            grabbedChain.m = sumaM;

            grabbedChain.W = stareW;
            c.W = stareW;

            grabbedChain.V = stareV.add( stareW.crossProduct( grabbedChain.centreOfGravity.subtract(starySrodekCiezkosci) ) );
            c.V = stareV.add( stareW.crossProduct( c.centreOfGravity.subtract(starySrodekCiezkosci) ) );

            oldBallList.clear();
            grabbedChain = null;

        }

    }

}
