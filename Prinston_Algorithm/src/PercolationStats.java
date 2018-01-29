import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

/**
 * Assignment 1 This PercolationStats class runs Monte Carlo Simulation with a
 * specific grid size of site board, and number of trials. It uses Union-Find as
 * its underlining data structure and to compute connectivity. It uses weighted
 * Union-Find with Pave improvement algorithm. use command-line: java
 * PercolationStats 100 1000 to execute a board of grid size 100, and run 1000
 * simulation. 01.28.2018
 * 
 * @author liming
 *
 */
public class PercolationStats {
    private final double mean;
    private final double stddev; // standard deviation value
    private final double confidenceLo; // the lower side of confidence interval
    private final double confidenceHi; // the higher side of confidence interval

    /**
     * constructor. It runs the Monte Carlo simulation and compute statistics.
     * 
     * @param n
     *            grid size
     * @param trials
     *            number of simulations
     */
    public PercolationStats(int n, int trials) {
        if ( n < 1 || trials < 1)
            throw new java.lang.IllegalArgumentException("size of grid or trials number should be larger than 0!");
        
    // perform trials independent experiments on an n-by-n grid
        double[] threshold = new double[trials];
        for (int t = 1; t <= trials; t++) {
            double thres = runPercolation(n);
            threshold[t - 1] = thres;
        }
        mean = StdStats.mean(threshold);
        stddev = StdStats.stddev(threshold);
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(trials);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(trials);
    }

    /**
     * one simulation run to uniformly open a site until percolate, compute the
     * threshold of portion of open sites.
     * 
     * @param n
     *            grid size
     * @return
     */
    private double runPercolation(int n) {
    // run one percolation simulation
        Percolation P = new Percolation(n);
        while (true) {
            int row = StdRandom.uniform(n) + 1;
            int col = StdRandom.uniform(n) + 1;
            if (!P.isOpen(row, col)) {
                P.open(row, col);
            }
            if (row == n && P.isFull(row, col)) {
                break;
            }
        }
        double num = P.numberOfOpenSites();
        return num * 1.0 / n / n;
    }

    /**
     * return mean value.
     * 
     * @return
     */
    public double mean() {
    // sample mean of percolation threshold
	return mean;
    }

    /**
     * return standard deviation value.
     * 
     * @return
     */
    public double stddev() {
    // sample standard deviation of percolation threshold
	return stddev;
    }

    /**
     * return lower side of confidence interval.
     * 
     * @return
     */
    public double confidenceLo() {
    // low endpoint of 95% confidence interval
	return confidenceLo;
    }

    /**
     * return higher side of confidence interval.
     * 
     * @return
     */
    public double confidenceHi() {
    // high endpoint of 95% confidence interval
	return confidenceHi;
    }

    public static void main(String[] args) {
    // test client (described below)
        int N = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        StdOut.println("input of grid size: " + N);
        StdOut.println("input of trials number: " + trials);
        PercolationStats P = new PercolationStats(N, trials);
        StdOut.println("mean: " + P.mean());
        StdOut.println("stddev: " + P.stddev());
        StdOut.println("confidenceLo: " + P.confidenceLo());
        StdOut.println("confidenceHi: " + P.confidenceHi());
    }
}
