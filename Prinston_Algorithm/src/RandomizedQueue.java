import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Object[] q;       // queue elements
    private int N;          // number of elements on queue
    private int first;      // index of first element of queue
    private int last;       // index of next available slot
    
    /**
     * Initializes an empty queue.
     */
    @SuppressWarnings("unchecked")
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
        @SuppressWarnings("unchecked")
        Item item = (Item) q[first];
        q[first] = null;                            // to avoid loitering
        N--;
        first++;
        if (first == q.length) first = 0;           // wrap-around
        // shrink size of array if necessary
        if (N > 0 && N == q.length/4) resize(q.length/2); 
        return item;
    }
    
    public Item sample() {
        // return a random item (but do not remove it)
        if (isEmpty()) throw new NoSuchElementException("error: sample() since deque is empty!");
        
        int index = StdRandom.uniform(N);
        return null;
    }
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return null;
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
        StdOut.println("first pointer is at " + q[first]);
        StdOut.println("last pointer is at " + q[last]);
        StdOut.println("length of queue: " + N + ", " + String.valueOf(last-first));
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
        RandomizedQueue<Integer> deq = new RandomizedQueue<Integer>();
        StdOut.println("is deque empty? " + deq.isEmpty());
        StdOut.println("size of deque? " + deq.size());
        try {
            deq.enqueue(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("enqueue take a null arguement test passed!");
        };
        deq.enqueue(1); deq.enqueue(2);deq.enqueue(3);
//        StdOut.println((Integer[]) deq.q);
        deq.printQueue();
    }
}