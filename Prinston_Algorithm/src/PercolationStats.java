import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

public class PercolationStats {
	private double mean;
	private double stddev;
	private double confidenceLo;
	private double confidenceHi;
	
    public PercolationStats(int n, int trials) {
	    // perform trials independent experiments on an n-by-n grid
    		double[] threshold = new double[trials];
	    for (int t = 1; t <= trials; t++) {
	    		double thres = runPercolation(n);
	 	    threshold[t-1] = thres;
	    }
	    mean = StdStats.mean(threshold);
	    stddev = StdStats.stddev(threshold);
	    confidenceLo = mean - 1.96 * stddev / Math.sqrt(trials);
	    confidenceHi = mean + 1.96 * stddev / Math.sqrt(trials);
    }

    private double runPercolation(int n) {
		// run one percolation simulation
    		Percolation P = new Percolation(n);
    		while ( !P.percolates() ) {
    			int row = StdRandom.uniform(n) + 1;
    			int col = StdRandom.uniform(n) + 1;
    			if ( !P.isOpen(row, col) ) {
    				P.open(row, col);
    			}
    			if ( P.percolates() ) break;
    		}
    		double num = P.numberOfOpenSites();
		return num * 1.0 / n / n;
	}
    
	public double mean() {
		// sample mean of percolation threshold
		return mean;
	}
	public double stddev() {
		// sample standard deviation of percolation threshold
		return stddev;
	}
	public double confidenceLo() {
		// low  endpoint of 95% confidence interval
		return confidenceLo;
	}
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
