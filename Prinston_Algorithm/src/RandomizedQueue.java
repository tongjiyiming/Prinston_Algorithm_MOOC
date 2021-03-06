import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

/**
 * assignment 2: RandomizedQueue class.
 * @author liming
 *
 * @param <Item>
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] q;       // queue elements
    private int N;          // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot
    
    /**
     * Initializes an empty queue.
     */
    public RandomizedQueue() {
        // construct an empty randomized queue
        q = new Object[2];
        N = 0;
        first = 0;
        last = 0;
    }
    
    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        // is the randomized queue empty?
        return N == 0;
    }
    
    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    public int size() {
        // return the number of items on the randomized queue
        return N;
    }
    
    // resize the underlying array
    private void resize(int max) {
        assert max >= N;
        Object[] temp = new Object[max];
        for (int i = 0; i < N; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last  = N;
    }
    
    /**
     * Adds the item to this queue.
     * @param item the item to add
     */
    public void enqueue(Item item) {
        // add the item
        if (item == null) throw new IllegalArgumentException("error: enqueue() take a null arguement!");
        
        if (N == q.length) resize(2*q.length);   // double size of array if necessary
        
        q[last++] = item;                        // add item
        
        if (last == q.length) last = 0;          // wrap-around
        N++;
    }
    
    /**
     * Removes and returns a item on this queue that was uniformly randomly chose.
     * @return the item on this queue that was least recently added
     * @throws java.util.NoSuchElementException if this queue is empty
     */
    public Item dequeue() {
        // remove and return a random item
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        
        int ind = StdRandom.uniform(N); // notice the wrap-around situation
        Item item = (Item) q[(ind + first) % q.length];
        q[(ind + first) % q.length] = q[first];
        q[first] = null;
        N--;
        first++;
        if (first == q.length) first = 0;           // wrap-around
        // shrink size of array if necessary
        if (N > 0 && N == q.length/4) resize(q.length/2); 
        return item;
    }
    
    /**
     * return a random selected element from queue and return its value.
     * @return value at a random selected element from queue
     */
    public Item sample() {
        // return a random item (but do not remove it)
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        
        int ind = StdRandom.uniform(N); // notice the wrap-around situation
        return (Item) q[(ind + first) % q.length];
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue at uniformly random order.
     * @return an iterator that iterates over the items in this queue at uniformly random order
     */
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new ArrayIterator();
    }
    
    private class ArrayIterator implements Iterator<Item>{
        private int[] order;
        private int i=0;
        
        public ArrayIterator() {
            order = new int[N];
            int j = 0;
            for (int i = first; i < first + N; i++) {
                order[j] = i % q.length;
                j++;
            }
            StdRandom.shuffle(order);
        }
        
        @Override
        public boolean hasNext() {
            return i < N;
        }

        public void remove()      { throw new UnsupportedOperationException(); }
        
        @Override
        public Item next() {
            if ( i < N) {
                Item item = (Item) q[ order[i] ];
                i++;
                return item;
            }
            else {
                throw new NoSuchElementException("There is no additional element left in iterator.");
            }
        }
        
    }
    // helper method to print sequence of element
    private void printQueue() {
        String str = "start< ";
        for (int i=0; i<q.length; i++) {
            if (q[i] == null) str = str + "null ";
            else str = str + ((Item) q[i]).toString() + " ";
        }
        str = str + " >end";
        StdOut.println(str);
        StdOut.println("is deque empty? " + isEmpty());
        StdOut.println("number at first pointer is " + q[first]);
        StdOut.println("number at last pointer is " + q[last]);
        StdOut.println("length of internal array: " + q.length);
        StdOut.println("length of queue: " + N + ", " + "first at: " + first
        + ", last at: " + last);
    }
    
    /**
     * test client.
     * @param args
     */
    public static void main(String[] args) {
        // unit testing (optional)
        RandomizedQueue<String> deq = new RandomizedQueue<String>();
        deq.printQueue();
        try {
            deq.enqueue(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("enqueue take a null arguement test passed!");
        };
        deq.enqueue("1"); deq.enqueue("2"); deq.enqueue("3"); deq.enqueue("4"); deq.enqueue("5"); deq.enqueue("6"); 
        deq.enqueue("7"); deq.enqueue("8");
        deq.printQueue();
        
        StdOut.println("dequeue: " + deq.dequeue());
        StdOut.println("dequeue: " + deq.dequeue());
        StdOut.println("dequeue: " + deq.dequeue());
        deq.printQueue();
        
        deq.enqueue("a"); deq.enqueue("b"); deq.enqueue("c");
        deq.printQueue();
        
        StdOut.println("dequeue: " + deq.dequeue());
        StdOut.println("dequeue: " + deq.dequeue());
        StdOut.println("dequeue: " + deq.dequeue());
        deq.printQueue();
        
        Iterator<String> iter = deq.iterator();
        while (iter.hasNext()) {
            StdOut.println("iterator return: " + iter.next());
        }
        try {
            iter.next();
        }
        catch (NoSuchElementException e) {
            StdOut.println("iterator reach the end test passed!");
        }
                
    }
}