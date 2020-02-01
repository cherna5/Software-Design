
/**HW3 - Factory design Decorator
 * Cesar Hernandez
 * cherna83
 *
 */

//=============================================================

public class FactoryDecorator
{
    public static void main(String[] args)
    {
        System.out.println("\nChoose between 5 burger decorators to build your burger:");
        System.out.println("\nBurger options include basicBurger, SalmonBurger, GrassfedBurger, BlackbeanBurger");
        System.out.println("with GrilledOnions, FriedEgg, Bacon, Avocado & RoastedPeppers\n");

        //order #1
        Burger order = new Avocado(new FriedEgg (new GrilledOnions(BurgerFactory.getBurger("IB"))));
        double cost = order.makeBurger();
        System.out.println("Total: $" + cost);
        System.out.println("\n");

        //order #2
        Burger order2 = new FriedEgg(new GrilledOnions(BurgerFactory.getBurger("blb")));
        double cost2 = order2.makeBurger();
        System.out.println("Total: $" + cost2 );
        System.out.println("\n");

        //order #3
        Burger order3 = new RoastedPeppers(new FriedEgg(new Bacon(new GrilledOnions(new Avocado(BurgerFactory.getBurger("sb"))))));
        double cost3 = order3.makeBurger();
        System.out.println("Total: $" + cost3);
        System.out.println("\n");


        //order #4
        Burger order4 = new Avocado(new FriedEgg(new RoastedPeppers(BurgerFactory.getBurger("gb"))));
        double cost4 = order4.makeBurger();
        System.out.println("Total: $" + cost4);
        System.out.print("\n\n");

    }
}

//=============================================================
//---------------------------------------------------------
//interface one operator
//Interface = Burger
interface Burger
{
    public double makeBurger();
}

//---------------------------------------------------------
//base class
//concrete component
class basicBurger implements Burger
{

    private double cost = 3.00;

    public double makeBurger()
    {
        System.out.println("Basic Burger: $3.00");
        return cost;
    }
}

//================----------------------------------------
class SalmonBurger implements Burger
{

    private double cost = 4.00;

    public double makeBurger()
    {
        System.out.println("Salmon Burger: $4.00");
        return cost;
    }
}

//================----------------------------------------
class GrassfedBurger implements Burger
{

    private double cost = 5.00;

    public double makeBurger()
    {
        System.out.println("GrassfedBurger: $5.00");
        return cost;
    }
}

//================----------------------------------------
class BlackbeanBurger implements Burger
{

    private double cost = 6.00;

    public double makeBurger()
    {
        System.out.println("Black bean Burger: $6.00");
        return cost;
    }
}

//================----------------------------------------
class BurgerFactory
{
    public static Burger getBurger(String type)
    {
        switch(type)
        {
            case "sb":
                return new SalmonBurger();
            case "gb":
                return new GrassfedBurger();
            case "blb":
                return new BlackbeanBurger();

                default:
                    System.out.println("unknown option returning basic Burger");
                    return new basicBurger();

        }
    }
}

//---------------------------------------------------------
//aggragates an interface
//abstract class
//decorator
abstract class BurgerDecorator implements Burger
{
    protected Burger specialBurger;

    public BurgerDecorator(Burger specialBurger)
    {

        this.specialBurger = specialBurger;
    }

    //single abstract method
    public double makeBurger()
    {
        return specialBurger.makeBurger();
    }

}

//---------------------------------------------------------
//decorator #1

class FriedEgg extends BurgerDecorator
{

    private double cost = 1.50;

    public FriedEgg(Burger specialBurger)
    {
        super(specialBurger);
    }
    public double makeBurger()
    {
        return specialBurger.makeBurger() + addFriedEgg();
    }

    public double addFriedEgg()
    {
        System.out.println(" + FriedEgg: $1.50");
        return cost;
    }
}

//---------------------------------------------------------
//decorator #2

class Avocado extends BurgerDecorator
{
    private double cost = .75;

    public Avocado(Burger specialBurger)
    {
        super(specialBurger);
    }

    public double makeBurger()
    {
        return specialBurger.makeBurger() + addAvocado();
    }

    public double addAvocado()
    {
        System.out.println(" + Avocado: $.75");
        return cost;
    }
}

//---------------------------------------------------------
//Concrete
//Decorator #3

class Bacon extends BurgerDecorator
{
    private double cost = 2.00;

    public Bacon(Burger specialBurger)
    {
        super(specialBurger);
    }

    public double makeBurger()
    {
        return specialBurger.makeBurger() + addBacon();
    }

    public double addBacon()
    {
        System.out.println(" + Bacon: $2.00");
        return cost;
    }
}

//---------------------------------------------------------
//decorator #4
class GrilledOnions extends BurgerDecorator
{
    private double cost = 1.00;

    public GrilledOnions(Burger specialBurger)
    {
        super(specialBurger);
    }

    public double makeBurger()
    {
        return specialBurger.makeBurger() + addGrilledOnions();
    }

    public double addGrilledOnions()
    {
        System.out.println(" + GrilledOnions: $1.00");
        return cost;
    }
}

//---------------------------------------------------------
//decorator #5

class RoastedPeppers extends BurgerDecorator
{
    private double cost = 5.00;

    public RoastedPeppers(Burger specialBurger)
    {
        super(specialBurger);
    }

    public double makeBurger()
    {
        return specialBurger.makeBurger() + addRoastedPeppers();
    }

    public double addRoastedPeppers()
    {
        System.out.println(" + RoastedPeppers: $5.00");
        return cost;

    }
}

//---------------------------------------------------------
//=======================================================================





