package physicalTests;

import javafx.geometry.Point3D;
import physic.Ball;
import physic.Chain;
import physic.Joint;

import java.util.ArrayList;
import java.util.List;

public class SilnikPrzesuwnyStary extends Joint {

    double VEngine;
    Chain c;
    List<Ball> ballsList1,ballsList2;
    Ball b1,b2;
    public SilnikPrzesuwnyStary(Chain c, List<Ball> ballsList1, Ball b1, List<Ball> ballsList2, Ball b2, double radius, double VEngine) {
        super(b1, b2, radius);
        // UWAGA ROBIONE NA PAŁE: NIE SPRAWDZA CZY BALLLISTY I B NALEZA DO C
        this.VEngine=VEngine;
        this.ballsList1=ballsList1;
        this.ballsList2=ballsList2;
        this.b1=b1;
        this.b2=b2;
        this.c =c;
    }

    public void moveOutside(){

        // stary srodek ciezkosci
        Point3D oldCentreOfGravity = c.centreOfGravity;

        // uwaga : uwzglednic W=0
        // stary moment pedu L=I*W (potrzebne do zmiany predkosci katowej - zasada zachowaina momentu pedu)
        Point3D oldW = c.W;
        double oldI=0;
        if(oldW.magnitude() != 0 ){
            double k = 0;
            for(Ball b: c.ballsList){
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRProstopadle = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRProstopadle * modRProstopadle;
            }
            oldI = c.m * k;
        }

        // kierunek ruchu silnika
        Point3D n = new Point3D(
                b2.x.get()-b1.x.get(),
                b2.y.get()-b1.y.get(),
                b2.z.get()-b1.z.get()
                ).normalize();

        Point3D vec = n.multiply(-VEngine/2);
        for (Ball b : ballsList1) {
            b.addToX(vec.getX());
            b.addToY(vec.getY());
            b.addToZ(vec.getZ());
        }

        vec = n.multiply(VEngine/2);
        for (Ball b : ballsList2) {
            b.addToX(vec.getX());
            b.addToY(vec.getY());
            b.addToZ(vec.getZ());
        }
        List<Ball> lista=new ArrayList<>(ballsList1);
        lista.addAll(ballsList2);

        Point3D sumaRM = new Point3D(0, 0, 0);
        double sumaM = 0;
        for (Ball b : lista) {
            sumaRM = sumaRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
            sumaM += b.m;
        }
        Point3D newCentreOfGravity = sumaRM.multiply(1 / sumaM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );
        System.out.println("old "+oldCentreOfGravity);
        System.out.println("new "+newCentreOfGravity);
        //System.out.println("roznica "+oldCentreOfGravity.subtract(newCentreOfGravity));

        // nowa predkosc katwa W spowodowana zmianą ksztaltu chaina (zasada zachowania momentu pedu)
        // uwaga: jest to kontynuacja zasady zachowania momentu pędu, wiec musimy sprawdzic jeszcze raz czy oldW=0
        if(oldW.magnitude() != 0){
            double k = 0;
            for(Ball b: c.ballsList){
                Point3D r = new Point3D(
                        b.x.get() - oldCentreOfGravity.getX(),
                        b.y.get() - oldCentreOfGravity.getY(),
                        b.z.get() - oldCentreOfGravity.getZ()
                );
                double modRProstopadle = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                k += modRProstopadle * modRProstopadle;
            }
            double newI = c.m * k;

            // newW
            // uwaga: uwzglednic gdy newI = 0
            if (newI != 0){
                c.W = oldW.multiply(oldI/newI);
            }
        }
    }


    public void moveInside(){

        if(this.getHeight()>VEngine*2){

            //stary srodek ciezkosci
            Point3D oldCentreOfGravity = c.centreOfGravity;

            // uwaga : uwzglednic W=0
            // stary moment pedu L=I*W (potrzebne do zmiany predkosci katowej - zasada zachowaina momentu pedu)
            Point3D oldW = c.W;
            double oldI=0;
            if(oldW.magnitude() != 0 ){
                double k = 0;
                for(Ball b: c.ballsList){
                    Point3D r = new Point3D(
                            b.x.get() - oldCentreOfGravity.getX(),
                            b.y.get() - oldCentreOfGravity.getY(),
                            b.z.get() - oldCentreOfGravity.getZ()
                    );
                    double modRProstopadle = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                    k += modRProstopadle * modRProstopadle;
                }
                oldI = c.m * k;
            }


            // kierunek ruchu silnika
            Point3D n = new Point3D(
                    b2.x.get()-b1.x.get(),
                    b2.y.get()-b1.y.get(),
                    b2.z.get()-b1.z.get()
            ).normalize();

            Point3D vec = n.multiply(VEngine/2);
            for (Ball b : ballsList1) {
                b.addToX(vec.getX());
                b.addToY(vec.getY());
                b.addToZ(vec.getZ());
            }

            vec = n.multiply(-VEngine/2);
            for (Ball b : ballsList2) {
                b.addToX(vec.getX());
                b.addToY(vec.getY());
                b.addToZ(vec.getZ());
            }
            List<Ball> lista=new ArrayList<>(ballsList1);
            lista.addAll(ballsList2);

            Point3D sumaRM = new Point3D(0, 0, 0);
            double sumaM = 0;
            for (Ball b : lista) {
                sumaRM = sumaRM.add(new Point3D(b.x.get(), b.y.get(), b.z.get()).multiply(b.m));
                sumaM += b.m;
            }
            Point3D newCentreOfGravity = sumaRM.multiply(1 / sumaM);
            c.centreOfGravity = newCentreOfGravity;

            c.move(
                    oldCentreOfGravity.subtract(newCentreOfGravity)
            );

            System.out.println("roznica "+oldCentreOfGravity.subtract(newCentreOfGravity));

            // nowa predkosc katwa W spowodowana zmianą ksztaltu chaina (zasada zachowania momentu pedu)
            // uwaga: jest to kontynuacja zasady zachowania momentu pędu, wiec musimy sprawdzic jeszcze raz czy oldW=0
            if(oldW.magnitude() != 0){
                double k = 0;
                for(Ball b: c.ballsList){
                    Point3D r = new Point3D(
                            b.x.get() - oldCentreOfGravity.getX(),
                            b.y.get() - oldCentreOfGravity.getY(),
                            b.z.get() - oldCentreOfGravity.getZ()
                    );
                    double modRProstopadle = (oldW.crossProduct(r)).magnitude() / oldW.magnitude();
                    k += modRProstopadle * modRProstopadle;
                }
                double newI = c.m * k;

                // newW
                // uwaga: uwzglednic gdy newI = 0
                if (newI != 0){
                    c.W = oldW.multiply(oldI/newI);
                }
            }
        }

    }

}
