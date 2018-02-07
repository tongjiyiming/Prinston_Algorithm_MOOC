import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

/**
 * a client to reorder k numbers in a uniformly random way.
 * @author liming
 *
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> qe = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            qe.enqueue(StdIn.readString());
        }
        for (int i=0; i < k; i++) {
            StdOut.println(qe.dequeue());
        }
    }
}
