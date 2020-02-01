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

public class GenericQueueTest {

    private GenericQueue<Integer> test;
    private Integer val;
    private  GenericQueue<String> tst;
    private String value;

    @BeforeEach
    public void init(){
        test = new GenericQueue<Integer>(10);
        test.Enqueue(20);
        test.Enqueue(30);
        val = 3;
    }

    @BeforeEach
    public void init2(){
        tst = new GenericQueue<String>(null);
    }

    //-------------------------------------------------------------------------------------

    @Test
    void num1(){
        //1. Test the constructor to ensure that the expected value was placed in the list
        //     will test using a double
        GenericQueue<Double> dTest = new GenericQueue<Double>(1.1);
        dTest.Enqueue(2.2);
        Double testVal = 1.1;
        //if we delete 2.2 then we should expect the value of 1.1 to be in the list
        assertEquals(testVal, dTest.Dequeue());
    }

    @Test
    void num2() {
        //2. In a list of three values, ensure that when you add a fourth it adds to the front
        //   for a stack and the back for a queue
        GenericQueue<String> test2 = new GenericQueue<String>("a");
        test2.Enqueue("b");
        test2.Enqueue("c");
        test2.Enqueue("d");
        //we pop "a" to ensure it was the last thing added because it will ensure FIFO
        //first in first out
        assertEquals(  "a", test2.Dequeue());
    }

    @Test
    void num3() {
        //3.In an Empty list, ensures that null is returned when you attempt to pop or dequeue
        //this method is done by using an integer and it will display a message
        GenericQueue<Integer> a = new GenericQueue<Integer>(0);
        a.Dequeue();
        assertEquals(null, a.Dequeue());
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
        GenericQueue<Integer> negTest = new GenericQueue<Integer>(-1);
        assertFalse(negTest.getLength() < 0, "we have a negative length value");
    }

    @Test
    void listIsNull(){
        assertNull(tst.Dequeue());
    }

    @Test
    void testPrint(){
        //testing test which outputs 10, 20, 30
        //tst outputs null
        OutputStream outputStream = new ByteArrayOutputStream();

        PrintStream printStream = new PrintStream(outputStream);

        test.print();

        System.setOut(printStream);
    }

    @Test
    public void dequeingAllButTheLastItem()
    {
        GenericQueue<Integer> q = new GenericQueue<Integer>(0);
        q.Enqueue(1);
        q.Enqueue(2);
        q.Dequeue();
        q.Dequeue();
        assertEquals(2, q.Dequeue());
    }

    @Test
    public void matchingDeque() {
        //another way to use an assertion to check that it is performing a deque

        Assertions.assertSame(10, test.Dequeue());
    }
}