import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * assignment 2: deque implementation.
 * @author liming
 *
 * @param <Item> an item contained by deque data structure
 */
public class Deque<Item> implements Iterable<Item> {
    private int N;         // number of elements on queue
    private Node first;    // beginning of queue
    private Node last;     // end of queue

    // helper linked list class
    private class Node {
        private Item item;
        private Node next;
        private Node last;
        
        public Node(Item i) {
            item = i;
            next = null;
            last = null;
        }
    }
    
    /**
     * Initializes an empty queue.
     */
    public Deque() {
        // construct an empty deque
        first = null;
        last  = null;
        N = 0;
        assert check();
    }
    
    /**
     * Is this queue empty?
     * @return true if this queue is empty; false otherwise
     */
    public boolean isEmpty() {
        // is the deque empty?
        return first == null;
    }
    
    /**
     * Returns the number of items in this queue.
     * @return the number of items in this queue
     */
    public int size() {
        // return the number of items on the deque
        return N;
    }

    /**
     * Adds the item to this queue on the first position.
     * @param item the item to add
     */
    public void addFirst(Item item) {
        // add the item to the end
        if (item == null) throw new IllegalArgumentException("error: addFirst() take a null arguement!");
        
        Node oldFirst = first;
        first = new Node(item);
        if (isEmpty()) first = last;
        else first.next = oldFirst; oldFirst.last = first;
        N++;
        assert check();
    }
    
    /**
     * Adds the item to this queue on the last position.
     * @param item the item to add
     */
    public void addLast(Item item) {
        // add the item to the front
        if (item == null) throw new IllegalArgumentException("error: addLast() take a null arguement!");
        
        Node oldlast = last;
        last = new Node(item);
        if (isEmpty()) first = last;
        else oldlast.next = last; last.last = oldlast;
        N++;
        assert check();
    }
    
    /**
     * remove the first item from deque, and return the value.
     * @return
     */
    public Item removeFirst() {
        // remove and return the item from the front
        if (first == null) throw new NoSuchElementException("error: removeFirst() since deque is empty!");
        
        Node n = first;
        if (first.next == null) {
            first = null; 
            last = null;
        }
        else {
            first = first.next;
            first.last = null;
        }
        assert check();
        return n.item;
    }
    
    /**
     * remove the last item from deque, and return the value.
     * @return
     */
    public Item removeLast() {
        // remove and return the item from the end
        if (last == null) throw new NoSuchElementException("error: removeFirst() since deque is empty!");
        
        Node n = last;
        if (first.next == null) {
            first = null; last = null;
        }
        else {
            last = last.last;
            last.next = null;
        }
        assert check();
        return n.item;
    }
    
    /**
     * Returns an iterator that iterates over the items in this queue from front to end.
     * @return an iterator that iterates over the items in this queue from front to end
     */
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new DequeIterator();
    }
    
    // an iterator, doesn't implement remove() since it's optional
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    // check internal invariants
    private boolean check() {
        if (N < 0) {
            return false;
        }
        else if (N == 0) {
            if (first != null) return false;
            if (last  != null) return false;
        }
        else if (N == 1) {
            if (first == null || last == null) return false;
            if (first != last)                 return false;
            if (first.next != null)            return false;
        }
        else {
            if (first == null || last == null) return false;
            if (first == last)      return false;
            if (first.next == null) return false;
            if (last.next  != null) return false;

            // check internal consistency of instance variable N
            int numberOfNodes = 0;
            for (Node x = first; x != null && numberOfNodes <= N; x = x.next) {
                numberOfNodes++;
            }
            if (numberOfNodes != N) return false;

            // check internal consistency of instance variable last
            Node lastNode = first;
            while (lastNode.next != null) {
                lastNode = lastNode.next;
            }
            if (last != lastNode) return false;
        }

        return true;
    }
    
    private void printDeque() {
        // print out the deque from first to last
        String s = "first< ";
        Node n = first;
        while ( n != null ) {
            s = s +  n.item.toString() + " ";
            n = n.next;
        }
        StdOut.println(s + ">last");
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
        Deque<Integer> deq = new Deque<Integer>();
        StdOut.println("is deque empty? " + deq.isEmpty());
        StdOut.println("size of deque? " + deq.size());
        try {
            deq.addLast(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("addLast take a null arguement test passed!");
        };
        deq.addLast(1); deq.addLast(2);deq.addLast(3);
        StdOut.println("is deque empty? " + deq.isEmpty());
        StdOut.println("size of deque? " + deq.size());
        deq.printDeque();
        try {
            deq.addFirst(null);
        }
        catch (IllegalArgumentException e) {
            StdOut.println("addFirst take a null arguement test passed!");
        };
        deq.addFirst(-1); deq.addFirst(-2); deq.addFirst(-3);
        StdOut.println("is deque empty? " + deq.isEmpty());
        StdOut.println("size of deque? " + deq.size());
        deq.printDeque();
        
//        StdOut.println(deq.removeFirst());
//        StdOut.println(deq.removeFirst());
//        StdOut.println(deq.removeFirst());
//        StdOut.println(deq.removeLast());
//        StdOut.println(deq.removeLast());
//        StdOut.println(deq.removeLast());
//        deq.printDeque();
        
        for (Integer i : deq) {
            StdOut.println(i);
        }
        deq.printDeque();
        
        
    }
}
