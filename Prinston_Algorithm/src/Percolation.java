import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation assignment
 * It is modeled as a grid board which material run into from the first row, and run out from the last row
 * @since 01.27.2018
 * @author liming
 *
 */
public class Percolation {
    /**
     * grid size for the physics problem
     */
    public int gridSize;
    /**
     * the WeightedQuickUnionUF class instance to model and compute the connectivity
     */
    private WeightedQuickUnionUF UF;
    /**
     * a pseudo node connected to all cells in first row
     */
    private int start=0;
    /**
     * a pseudo node connected to all cells in last row
     */
    private int end;
    /**
     * a boolean array to record the open/close state of all the cells
     */
    private boolean[] cellState;
    
    /**
     * constructor method to build a grid with all cell close
     * @param n size of grid
     */
    public Percolation(int n) {
	// create n-by-n grid, with all sites blocked
	if (n <= 0) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");
	gridSize = n;
	end = gridSize*gridSize + 1;
	cellState = new boolean[end+1];
	cellState[0] = true;
	cellState[end] = true;
	
	// initially, connect start cell with first row, and end cell with last row
	UF = new WeightedQuickUnionUF(end+1);
	for (int j = 1; j <= gridSize; j++) {
		UF.union(start, j);
		UF.union(end, (gridSize-1)*gridSize+j);
	}
    }
    
    public void open(int row, int col) {
	// open site (row, col) if it is not open already
    		if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");
    		
    		int index = (row-1)*gridSize + col;
    		if ( !cellState[index] ) {
    			cellState[index] = true;
    			modifyUF(row, col);
    		}
    }
    
    /**
     * with a new open cell (row, col), modify the Union-Find data model
     * @param row
     * @param col
     */
    private void modifyUF(int row, int col) {
    		int index = gridSize*(row-1) + col;
    		Object up = null;
    		Object down = null;
    		Object left = null;
    		Object right = null;
    		
    		// four corner cases
		if (row == 1 && col == 1) {
			if ( cellState[2] ) {
				right = 2;
			}
			if ( cellState[gridSize+1] ) {
				down = gridSize+1;
			}
		}
		else if ( row == 1 && col == gridSize) {
			if ( cellState[gridSize-1] ) {
				left = gridSize-1;
			}
			if ( cellState[gridSize*2] ) {
				down = gridSize*2;
			}
		}
		else if ( row == gridSize && col == 1 ) {
			if ( cellState[gridSize*(gridSize-2)+1] ) {
				up = gridSize*(gridSize-2)+1;
			}
			if ( cellState[gridSize*(gridSize-1)+2] ) {
				right = gridSize*(gridSize-1)+2;
			}
		}
		else if ( row == gridSize && col == gridSize ) {
			if ( cellState[gridSize*(gridSize-1)] ) {
				up = gridSize*(gridSize-1);
			}
			if ( cellState[gridSize*gridSize-1]) {
				left = gridSize*gridSize-1;
			}
		}
		// four edge cases
		else if ( row != 1 && row != gridSize && col == 1) {
			up = gridSize*(row-2)+col;
			down = gridSize*row + col;
			right = gridSize*(row-1)+2;
		}
		else if ( row != 1 && row != gridSize && col == gridSize ) {
			up = gridSize*(row-2)+col;
			down = gridSize*row + col;
			left = gridSize*(row-1) + col - 1;
		}
		else if ( row == 1 && col != 1 && col != gridSize ) {
			down = gridSize*row + col;
			left = gridSize*(row-1) + col - 1;
			right = gridSize*(row-1) + col + 1;
		}
		else if ( row == gridSize && col != 1 && col != gridSize ) {
			up = gridSize*(row-2) + col;
			left = gridSize*(row-1) + col - 1;
			right = gridSize*(row-1) + col + 1;
		}
		else {
			up = gridSize*(row-2) + col;
			down = gridSize*row + col;
			left = gridSize*(row-1) + col - 1;
			right = gridSize*(row-1) + col + 1;
		}
		
		// perform union
		if ( up != null ) {
			UF.union(index, (int) up);
		}
		if ( down != null ) {
			UF.union(index, (int) down);
		}
		if ( left != null ) {
			UF.union(index, (int) left);
		}
		if ( right != null ) {
			UF.union(index, (int) right);
		}
	}

	public boolean isOpen(int row, int col) {
	// is site (row, col) open?
		if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");

    		return true;
	
    }
    public boolean isFull(int row, int col) {
	// is site (row, col) full?
		if (row <= 0 || row > gridSize || col <= 0 || col > gridSize) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");

    		return true;
    }
    public int numberOfOpenSites() {
	// number of open sites
    		return 42;
    }
    public boolean percolates() {
	// does the system percolate?
    		return true;
    }

    public static void main(String[] args) {
	// test client (optional)
    	Percolation P = new Percolation(3);
    	System.out.println("grid size:" + P.gridSize);
    	System.out.println(P.UF.find(2));
    	System.out.println(P.UF.find(8));
    	System.out.println("(0, 3) connected? " + P.UF.connected(0, 3));
    	System.out.println("(10, 3) connected? " + P.UF.connected(10, 3));
    	System.out.println("(10, 9) connected? " + P.UF.connected(10, 9));
    }
 }
