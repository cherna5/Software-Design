/*
Name:       Cesar Hernandez
NetID:      cherna83
Assignment: HW #1
*/
import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class GenericStackTest {

    private GenericStack<Integer> test;
    private Integer val;
    private  GenericStack<String> tst;
    private String value;

    @BeforeEach
    public void init(){
        test = new GenericStack<Integer>(10);
        test.push(20);
        test.push(30);
        val = 3;
    }

    @BeforeEach
    public void init2(){
        tst = new GenericStack<String>(null);
    }

    //-------------------------------------------------------------------------------------

    @Test
    void num1(){
        //1. Test the constructor to ensure that the expected value was placed in the list
        //     will test using a double
        GenericStack<Double> dTest = new GenericStack<Double>(1.1);
        dTest.push(2.2);
        Double testVal = 2.2;
        //2.2 got popped that meant it was in the list
        assertEquals(testVal, dTest.pop());
    }

    @Test
    void num2() {
        //2. In a list of three values, ensure that when you add a fourth it adds to the front
        //   for a stack and the back for a queue
        GenericStack<String> test2 = new GenericStack<String>("a");
        test2.push("b");
        test2.push("c");
        test2.push("d");
        //we pop "d" to ensure it was the last thing added because it will ensure LIFO
        //last in first out
        assertEquals(  "d", test2.pop());
    }

    @Test
    void num3() {
        //3.In an Empty list, ensures that null is returned when you attempt to pop or dequeue
        //this method is done by using an integer and it will display a message
        GenericStack<Integer> a = new GenericStack<Integer>(0);
        a.pop();
        assertEquals(null, a.pop());
    }

    @Test
    void num4(){
        //4.Ensures that the length value will not be negative
        //testing with an integer
        //using annotation for this one
        assertTrue( test.getLength() > 0);
    }

    @Test
    void num5(){
        //5.Ensures that the length value properly increments when you create a new list
        //using annotation for this test from init class
        assertEquals(val, test.getLength());
    }
    @Test
    void incorrectLength(){
        //Checking that it should not be the same as the actual value given
        assertTrue(test.getLength() != 4, "Not the correct length of the list");
    }

    @Test
    void isNegativeNumber() {
        GenericStack<Integer> negTest = new GenericStack<Integer>(-1);
        assertFalse(negTest.getLength() < 0, "we have a negative length value");
    }

    @Test
    void listIsNull(){
        assertNull(tst.pop());
    }

    @Test
    void testPrint(){
        //testing test which outputs 30, 20, 10
        //tst outputs null
        OutputStream outputStream = new ByteArrayOutputStream();

        PrintStream printStream = new PrintStream(outputStream);

        test.print();

        System.setOut(printStream);
    }

    @Test
    public void popAllButTheLastItem()
    {
        GenericStack<Integer> s = new GenericStack<Integer>(0);
        s.push(1);
        s.push(2);
        s.pop();
        s.pop();
        assertEquals(0, s.pop());
    }

    @Test
    public void matchingPush() {
        //another way to use an assertion to check that it is performing a pop

        Assertions.assertSame(30, test.pop());
    }
}