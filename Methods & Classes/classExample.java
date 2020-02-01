

public class classExample {

    public static void main(String[] args){

        JungleCat.doNothing2();

        JungleCat regis = new JungleCat();

        JungleCat.MedReport r = regis.new MedReport();

        r.report();



        regis.setNineLives(1);

        System.out.println ("num of cats: " + regis.getLives());



        System.out.println ("how many lives left?: " + regis.numOfCats);

        JungleCat sima = new JungleCat("Sima", 2, 100, "Lion");


        System.out.println("num of cats for sima: " + sima.numOfCats);
        DomesticCat cozmo = new DomesticCat();

        regis.stats();
        regis.speak();

        sima.stats();
        sima.speak();

       // System.out.println(CatStats.catYears(sima.age));

        cozmo.stats();
        cozmo.speak();






    }
}
//---------------------------------------------------
abstract class Mammal{
     int age;
     int weight;
     String name;

    public abstract void stats();

    public abstract void speak();

}

//---------------------------------------------------
class JungleCat extends Mammal{

     String breed;
    private int nineLives;
    //keep track of how many
    static int numOfCats;



    JungleCat(){
        this.name = "Unknown";
        this.breed = "Unknown";
        this.nineLives = 9;
        ++numOfCats;
        this.doNothing();
    }

    JungleCat(String name, int age, int weight){
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.breed = "unknown";
        this.nineLives = 9;
        ++numOfCats;
    }

    JungleCat(String name, int age, int weight, String breed){
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.breed = breed;
        this.nineLives = 9;
        ++numOfCats;
    }


    public void stats(){
        System.out.println("Name:"+name+" Age:"+age+" Weight:"+weight+" Breed:"+breed);
    }

    public void speak(){
        System.out.println("Rooooaaar!!!");
    }

    //getter
    public int getLives(){

        return nineLives;
    }

    //setter
    public void setNineLives(int nineLives){
        this.nineLives = nineLives;
    }

    private void doNothing(){
        System.out.println("I do nothinnkg ");
    }


    static void doNothing2(){
        System.out.println("I do nothinnk2222222 ");
    }

    class MedReport{
        int age = 90;
        public void report(){
            System.out.println(age);
            System.out.println(this.age);
            System.out.println(JungleCat.this.age);
            System.out.println("the med report for the jungle cat  ");
        }
    }

}
//-------------------------------
final class DomesticCat extends JungleCat{
    public void speak(){
        System.out.println("Wake up, I am hungry");
    }

}



//---------------------------------------------------
class CatStats{

    public static int catYears(int y){
        return (y*7);
    }
}
