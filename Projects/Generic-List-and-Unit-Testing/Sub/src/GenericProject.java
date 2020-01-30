/*
    Name:        Cesar Hernandez
    NetID:       Cherna83
    UIC Email:   cherna83@uic.edu
    Description: An implementation of a stack and queue data structures. In this project there is an abstract class with
                 two data members that will be extended for other classes to inherit. Will have some methods to provide
                 the requirement tasks for the stack/queue.

 */



public class GenericProject {

    public static void main(String[] args) {
        System.out.println("Test Cases...\n\n");
        System.out.println("----TESTING MYQUEUE with an integer----");

        GenericQueue<Integer> myQueue = new GenericQueue<>(0);
        myQueue.Enqueue(1);
        myQueue.Enqueue(2);
        myQueue.Enqueue(3);
        myQueue.Enqueue(4);
        myQueue.Enqueue(5);
        System.out.println("This is the Length of myQueue: " + myQueue.getLength());
        myQueue.print();
        System.out.println("\n\nDelete first item: (FIFO)\n" + myQueue.Dequeue());
        System.out.println("\n\nDelete the item: \n" + myQueue.Dequeue());
        myQueue.print();

        System.out.println("\n\n----TESTING MYQUEUE1 with a STRING----");

        GenericQueue<String> myQueue1 = new GenericQueue<>("t");
        myQueue1.Enqueue("u");
        myQueue1.Enqueue("v");
        myQueue1.Enqueue("x");
        myQueue1.Enqueue("y");
        myQueue1.Enqueue("z");
        System.out.println("This is the Length of myQueue: " + myQueue1.getLength());
        myQueue1.print();
        System.out.println("\n\nDelete first item: (FIFO)\n" + myQueue1.Dequeue());
        System.out.println("\n\nDelete the item: \n" + myQueue1.Dequeue());
        myQueue1.print();


        GenericStack<Integer> myStack = new GenericStack<>(0);
        System.out.println("\n\n----TESTING MYStack with an integer-----\n");
        myStack.push(1);
        myStack.push(2);
        myStack.push(3);
        myStack.push(4);
        myStack.push(5); //last in first out!!!
        System.out.println("This is the Length of myStack: " + myStack.getLength());
        myStack.print();
        System.out.println("\n\nDelete last item (LIFO): \n" + myStack.pop());
        System.out.println("\n\nDelete last item (LIFO): \n" + myStack.pop());
        myStack.print();

        GenericStack<String> myStack1 = new GenericStack<>("a");
        System.out.println("\n\n----TESTING MYStack1 with a STRING-----\n");
        myStack1.push("b");
        myStack1.push("c");
        myStack1.push("d");
        myStack1.push("e");
        myStack1.push("f"); //last in first out!!!
        System.out.println("This is the Length of myStack: " + myStack1.getLength());
        myStack1.print();
        System.out.println("\n\nDelete last item (LIFO): \n" + myStack1.pop());
        System.out.println("\n\nDelete last item (LIFO): \n" + myStack1.pop());
        myStack1.print();

    }

}

    //======================================================
    abstract class GenericList<I>{

        //======================================================
        //defined generic inner node class
        //GenericList encapsulates this linked list
        class Node<T> {
            //two fields:
            T data;
            Node<T> next;

            Node(T data)
            {
                this.data = data;
                this.next = null;
            }
        }
        //======================================================

        //--------------------------------------------------

        //two data members
        Node<I> head;
        private int length;

        //--------------------------------------------------
        //getter
        public int getLength()
        {
            return this.length;
        }
        //setter
        public void setLength(int length)
        {
            this.length = length;
        }
        //--------------------------------------------------
        //--------------------------------------------------
        //method
        void print()
        {
            System.out.println("Items of the list are: \n");
            Node<I> temp = head;
            for (int i = 0; i < length; i++)
            {
                if (temp == null)
                {//checks if the list is empty
                    System.out.println("The list is empty!\n");
                }
                else
                {//prints the data value of the list
                    System.out.print(temp.data + " ");
                    temp = temp.next; //continue until null
                }
            }
        }

        //abstract modifier method
        //will add values to the list
        abstract void add(I data);

        //returns the first value of the list and deletes the node
        I delete()
        {
            Node<I> delete_tmp = head;
            if (head == null)
            {
                System.out.println("There is nothing to be deleted\n");
                return null;
            }
            else
            {
                head = head.next;   //Head points to the next node of the list
                this.length--;      //decreases the size of the list
                return delete_tmp.data;

            }
        }
        //------------------------------------------------------
        //======================================================
    }
    //======================================================

     class GenericQueue<I> extends GenericList<I>{
        //constructor with one parameter
        public GenericQueue(I new_head)
        {
            this.head = new Node<I>(new_head);  //initializing the stack
            this.setLength(1);                  //starting with 1 node
        }

        //implement the method add
        //Queue are FIFO
        void add(I data) {
            Node<I> tmp = head;
            while(tmp.next != null)
            {
                tmp = tmp.next;     //traversing values until last item is reached
            }

                tmp.next = new Node<I>(data);   //new head of the list
                this.setLength(this.getLength() + 1);


        }
        //---------------------------------------------------
            //methods
            public void Enqueue(I data)
            {
                add(data);
            }
            public I Dequeue()
            {
                return delete();
            }
    }

    //======================================================

    class GenericStack<I> extends GenericList<I>{
        //constructor taking one parameter
        public GenericStack(I new_head)
        {
            this.head = new Node<I>(new_head); //initializing the stack
            this.setLength(1);                 //starting with 1 node
        }

        //implement the method add
        //Stack are LIFO
        void add(I data)
        {
            Node<I> tmp = head;         //creating a temp position to store in our first push
            head = new Node<I>(data);   //this will point to the new item added
            head.data = data;           //traversing data values
            head.next = tmp;            //new head of the list
            this.setLength(this.getLength() + 1);      //size increased
        }

        //---------------------------------------------------
        //methods
        public void push(I data)
        {
            add(data);
        }
        public I pop()
        {
            return delete();
        }
    }

    //======================================================






