package physicalTests;

import javafx.geometry.Point3D;
import physic.Ball;
import physic.Chain;
import physic.Joint;

import java.util.ArrayList;
import java.util.List;

// UWAGA: ROZWAZYC BEZ EXTENDS
public class SilnikObrotowyStary extends Ball {

    double WEngine;
    Chain c;
    List<Ball> ballsList1,ballsList2;
    Ball b1,b2;


    // UWAGA: KONSTRUKTOR NIE DODAJE JOINTOW LACZACYCH SILNIK Z CHAINAMI (TRZEBA DODAAWAC RECZNIE)
    // UWAGA: MASE PRZEGUBU PRZYJMUJE ZA ZEROWA
    public SilnikObrotowyStary(Chain c, Joint j1, Joint j2, List<Ball> ballsList1, Ball b1, List<Ball> ballsList2, Ball b2, Ball b, double WEngine) {
        super(b.x.get(),b.y.get(),b.z.get(),b.getRadius(),b.m);
        this.WEngine=WEngine;
        this.ballsList1=ballsList1;
        this.ballsList2=ballsList2;
        this.b1=b1;
        this.b2=b2;
        this.c =c;

        this.c.ballsList.add(this);
        this.c.jointList.add(j1);
        this.c.jointList.add(j2);

        // srodek ciezkosci
        Point3D sumaRM = new Point3D(0, 0, 0);
        double sumaM = 0;
        for (Ball i : c.ballsList) {
            sumaRM = sumaRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumaM += i.m;
        }
        this.c.centreOfGravity = sumaRM.multiply(1 / sumaM);
        this.c.m = sumaM;
    }

     // UWAGA: DOROBIC TUTAJ USTALANIE OSI OBROTU
    public void moveInside(){

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

        Point3D bb1 = new Point3D(
                b1.x.get()-this.x.get(),
                b1.y.get()-this.y.get(),
                b1.z.get()-this.z.get()
                );


        Point3D bb2 = new Point3D(
                b2.x.get()-this.x.get(),
                b2.y.get()-this.y.get(),
                b2.z.get()-this.z.get()
        );

        Point3D n = (bb1.crossProduct(bb2)).normalize();
        //DLA OBROTOWEGO
        //UWAGA: UWZGLEDNIC DLA KATOW 0 I 180
        //////////////////////////////////////////
        Point3D vecWEngine = n.multiply(WEngine);
        //System.out.println(bb1.normalize().crossProduct(bb2.normalize()).magnitude());
        System.out.println(bb1.normalize().dotProduct(bb2.normalize()));
        if(bb1.normalize().dotProduct(bb2.normalize())>0.999){
            System.out.println("kaka ");
            //vecWEngine = vecWEngine.multiply(-1);
            return;
        }
        // licze wszystkie r prostopadle oraz Ia dla A (balllist 1)
        List<Point3D> rAProstopadle = new ArrayList<>();
        double Ia = 0;
        for(Ball i:ballsList1){
            // wektor od this do i
            Point3D rThisI = new Point3D(
                    i.x.get() - this.x.get(),
                    i.y.get() - this.y.get(),
                    i.z.get() - this.z.get()
                    );
            Point3D rAProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            rAProstopadle.add(rAProstopadly);
            Ia += i.m * rAProstopadly.magnitude() * rAProstopadly.magnitude();
        }


        // licze wszystkie r prostopadle oraz Ib dla B (balllist 2)
        List<Point3D> rBProstopadle = new ArrayList<>();
        double Ib = 0;
        for(Ball i:ballsList2){
            // wektor od this do i
            Point3D rThisI = new Point3D(
                    i.x.get() - this.x.get(),
                    i.y.get() - this.y.get(),
                    i.z.get() - this.z.get()
            );
            Point3D rBProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            rBProstopadle.add(rBProstopadly);
            Ib += i.m * rBProstopadly.magnitude() * rBProstopadly.magnitude();
        }

        // licze Wa i Wb
        Point3D Wb = vecWEngine.multiply( -Ia/(Ia+Ib) );
        Point3D Wa = vecWEngine.add(Wb);

        // licze wszystkie Va i dodaje
        for (int i = 0;i<ballsList1.size();i++){
            Point3D V = Wa.crossProduct(rAProstopadle.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // licze wszystkie Vb i dodaje
        for (int i = 0;i<ballsList2.size();i++){
            Point3D V = Wb.crossProduct(rBProstopadle.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // poprawka odleglosci
        //--------------------------------
        for(int i=0;i<ballsList1.size();i++){
            // wektor od this do i

            Point3D rThisI = new Point3D(
                    ballsList1.get(i).x.get() - this.x.get(),
                    ballsList1.get(i).y.get() - this.y.get(),
                    ballsList1.get(i).z.get() - this.z.get()
            );
            Point3D noweRAProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            double dlugoscWektoraPoprawiajacego = noweRAProstopadly.magnitude() - rAProstopadle.get(i).magnitude();
            Point3D wektorPoprawiajacy = (noweRAProstopadly.normalize()).multiply(-dlugoscWektoraPoprawiajacego);

            ballsList1.get(i).addToX(wektorPoprawiajacy.getX());
            ballsList1.get(i).addToY(wektorPoprawiajacy.getY());
            ballsList1.get(i).addToZ(wektorPoprawiajacy.getZ());
        }

        for(int i=0;i<ballsList2.size();i++){
            // wektor od this do i

            Point3D rThisI = new Point3D(
                    ballsList2.get(i).x.get() - this.x.get(),
                    ballsList2.get(i).y.get() - this.y.get(),
                    ballsList2.get(i).z.get() - this.z.get()
            );
            Point3D noweRBProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            double dlugoscWektoraPoprawiajacego = noweRBProstopadly.magnitude() - rBProstopadle.get(i).magnitude();
            Point3D wektorPoprawiajacy = (noweRBProstopadly.normalize()).multiply(-dlugoscWektoraPoprawiajacego);

            ballsList2.get(i).addToX(wektorPoprawiajacy.getX());
            ballsList2.get(i).addToY(wektorPoprawiajacy.getY());
            ballsList2.get(i).addToZ(wektorPoprawiajacy.getZ());
        }
        //--------------------------------
        ////////////////////////////////////////

        // LICZE NOWY SRODEK CIEZKOSCI
        Point3D sumaRM = new Point3D(0, 0, 0);
        double sumaM = 0;
        for (Ball i : c.ballsList) {
            sumaRM = sumaRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumaM += i.m;
        }

        Point3D newCentreOfGravity = sumaRM.multiply(1 / sumaM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

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

        Point3D bb1 = new Point3D(
                b1.x.get()-this.x.get(),
                b1.y.get()-this.y.get(),
                b1.z.get()-this.z.get()
        );


        Point3D bb2 = new Point3D(
                b2.x.get()-this.x.get(),
                b2.y.get()-this.y.get(),
                b2.z.get()-this.z.get()
        );

        Point3D n = (bb1.crossProduct(bb2)).normalize();
        //DLA OBROTOWEGO
        //UWAGA: UWZGLEDNIC DLA KATOW 0 I 180
        //////////////////////////////////////////
        Point3D vecWEngine = n.multiply(-WEngine);
        //System.out.println(bb1.normalize().crossProduct(bb2.normalize()).magnitude());
        System.out.println(bb1.normalize().dotProduct(bb2.normalize()));
        if(bb1.normalize().dotProduct(bb2.normalize())<-0.999){
            System.out.println("kaka ");
            //vecWEngine = vecWEngine.multiply(-1);
            return;
        }
        // licze wszystkie r prostopadle oraz Ia dla A (balllist 1)
        List<Point3D> rAProstopadle = new ArrayList<>();
        double Ia = 0;
        for(Ball i:ballsList1){
            // wektor od this do i
            Point3D rThisI = new Point3D(
                    i.x.get() - this.x.get(),
                    i.y.get() - this.y.get(),
                    i.z.get() - this.z.get()
            );
            Point3D rAProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            rAProstopadle.add(rAProstopadly);
            Ia += i.m * rAProstopadly.magnitude() * rAProstopadly.magnitude();
        }


        // licze wszystkie r prostopadle oraz Ib dla B (balllist 2)
        List<Point3D> rBProstopadle = new ArrayList<>();
        double Ib = 0;
        for(Ball i:ballsList2){
            // wektor od this do i
            Point3D rThisI = new Point3D(
                    i.x.get() - this.x.get(),
                    i.y.get() - this.y.get(),
                    i.z.get() - this.z.get()
            );
            Point3D rBProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            rBProstopadle.add(rBProstopadly);
            Ib += i.m * rBProstopadly.magnitude() * rBProstopadly.magnitude();
        }

        // licze Wa i Wb
        Point3D Wb = vecWEngine.multiply( -Ia/(Ia+Ib) );
        Point3D Wa = vecWEngine.add(Wb);

        // licze wszystkie Va i dodaje
        for (int i = 0;i<ballsList1.size();i++){
            Point3D V = Wa.crossProduct(rAProstopadle.get(i));

            ballsList1.get(i).addToX(V.getX());
            ballsList1.get(i).addToY(V.getY());
            ballsList1.get(i).addToZ(V.getZ());

        }

        // licze wszystkie Vb i dodaje
        for (int i = 0;i<ballsList2.size();i++){
            Point3D V = Wb.crossProduct(rBProstopadle.get(i));

            ballsList2.get(i).addToX(V.getX());
            ballsList2.get(i).addToY(V.getY());
            ballsList2.get(i).addToZ(V.getZ());
        }

        // poprawka odleglosci
        //--------------------------------
        for(int i=0;i<ballsList1.size();i++){
            // wektor od this do i

            Point3D rThisI = new Point3D(
                    ballsList1.get(i).x.get() - this.x.get(),
                    ballsList1.get(i).y.get() - this.y.get(),
                    ballsList1.get(i).z.get() - this.z.get()
            );
            Point3D noweRAProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            double dlugoscWektoraPoprawiajacego = noweRAProstopadly.magnitude() - rAProstopadle.get(i).magnitude();
            Point3D wektorPoprawiajacy = (noweRAProstopadly.normalize()).multiply(-dlugoscWektoraPoprawiajacego);

            ballsList1.get(i).addToX(wektorPoprawiajacy.getX());
            ballsList1.get(i).addToY(wektorPoprawiajacy.getY());
            ballsList1.get(i).addToZ(wektorPoprawiajacy.getZ());
        }

        for(int i=0;i<ballsList2.size();i++){
            // wektor od this do i

            Point3D rThisI = new Point3D(
                    ballsList2.get(i).x.get() - this.x.get(),
                    ballsList2.get(i).y.get() - this.y.get(),
                    ballsList2.get(i).z.get() - this.z.get()
            );
            Point3D noweRBProstopadly = rThisI.subtract( n.multiply(n.dotProduct(rThisI)) );
            double dlugoscWektoraPoprawiajacego = noweRBProstopadly.magnitude() - rBProstopadle.get(i).magnitude();
            Point3D wektorPoprawiajacy = (noweRBProstopadly.normalize()).multiply(-dlugoscWektoraPoprawiajacego);

            ballsList2.get(i).addToX(wektorPoprawiajacy.getX());
            ballsList2.get(i).addToY(wektorPoprawiajacy.getY());
            ballsList2.get(i).addToZ(wektorPoprawiajacy.getZ());
        }
        //--------------------------------
        ////////////////////////////////////////

        // LICZE NOWY SRODEK CIEZKOSCI
        Point3D sumaRM = new Point3D(0, 0, 0);
        double sumaM = 0;
        for (Ball i : c.ballsList) {
            sumaRM = sumaRM.add(new Point3D(i.x.get(), i.y.get(), i.z.get()).multiply(i.m));
            sumaM += i.m;
        }

        Point3D newCentreOfGravity = sumaRM.multiply(1 / sumaM);
        c.centreOfGravity = newCentreOfGravity;

        c.move(
                oldCentreOfGravity.subtract(newCentreOfGravity)
        );

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
