import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdOut;

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
     * value to record how many cell is open
     */
    private int numberOfOpenCell=0;
    
    /**
     * constructor method to build a grid with all cell close
     * @param n size of grid
     */
    public Percolation(int n) {
	// create n-by-n grid, with all sites blocked
	if (n < 1) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");
	gridSize = n;
	end = gridSize*gridSize + 1;
	cellState = new boolean[end+1];
	
	// initially, connect start cell with first row, and end cell with last row
	UF = new WeightedQuickUnionUF(end+1);
	for (int j = 1; j <= gridSize; j++) {
		UF.union(start, j);
		UF.union(end, (gridSize-1)*gridSize+j);
	}
    }
    
    public void open(int row, int col) {
	// open site (row, col) if it is not open already
    		if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new java.lang.IllegalArgumentException(
    				"size of grid should be in range [0, gridSize]!");
    		
    		int index = this.getIndex(row, col);
    		if ( !cellState[index] ) {
    			cellState[index] = true;
    			numberOfOpenCell ++;
    			modifyUF(row, col);
    		}
    }
    
    /**
     * with a new open cell (row, col), modify the Union-Find data model
     * @param row
     * @param col
     */
    private void modifyUF(int row, int col) {
    		int index = this.getIndex(row, col);
    		
    		// four corner cases
		if (row == 1 && col == 1) {
//			StdOut.println("up left corner case");
			this.checkOpenAndUnion(index, this.getIndex(2, 1));
		}
		else if ( row == 1 && col == gridSize) {
//			StdOut.println("up right corner case");
			this.checkOpenAndUnion(index, this.getIndex(2, col));
		}
		else if ( row == gridSize && col == 1 ) {
//			StdOut.println("down left corner case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, 1));
		}
		else if ( row == gridSize && col == gridSize ) {
//			StdOut.println("down right corner case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, col));
		}
		// four edge cases
		else if ( row != 1 && row != gridSize && col == 1) {
//			StdOut.println("left edge case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, col));
			this.checkOpenAndUnion(index, this.getIndex(row+1, col));
			this.checkOpenAndUnion(index, this.getIndex(row, col+1));
		}
		else if ( row != 1 && row != gridSize && col == gridSize ) {
//			StdOut.println("right edge case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, col));
			this.checkOpenAndUnion(index, this.getIndex(row+1, col));
			this.checkOpenAndUnion(index, this.getIndex(row, col-1));
		}
		else if ( row == 1 && col != 1 && col != gridSize ) {
//			StdOut.println("up edge case");
			this.checkOpenAndUnion(index, this.getIndex(row+1, col));
		}
		else if ( row == gridSize && col != 1 && col != gridSize ) {
//			StdOut.println("down edge case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, col));
		}
		else {
//			StdOut.println("normal case");
			this.checkOpenAndUnion(index, this.getIndex(row-1, col));
			this.checkOpenAndUnion(index, this.getIndex(row+1, col));
			this.checkOpenAndUnion(index, this.getIndex(row, col-1));
			this.checkOpenAndUnion(index, this.getIndex(row, col+1));
		}
	}

    /**
     * check if a cell is open or not
     * @param row
     * @param col
     * @return true if cell is open, false otherwise
     */
	public boolean isOpen(int row, int col) {
	// is site (row, col) open?
		if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");		
    		return cellState[this.getIndex(row, col)];
	
    }
	
    public boolean isFull(int row, int col) {
	// is site (row, col) full?
		if (row < 1 || row > gridSize || col < 1 || col > gridSize) throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");

    		return UF.find(1) == UF.find(this.getIndex(row, col));
    }
    
    /**
     * require API; return number of open cells
     * @return
     */
    public int numberOfOpenSites() {
	// number of open sites
    		return numberOfOpenCell;
    }
    
    public boolean percolates() {
	// does the system percolate?
    		return UF.find(start) == UF.find(end);
    }

    private void printGrid() {
    		StdOut.println("total length of UF: " + cellState.length);
    		for (int r = 1; r <= gridSize; r++) {
    			String[] str = new String[gridSize];
    			for ( int c = 1; c <= gridSize; c++) {
    				if ( this.isOpen(r, c) ) {
    					str[c-1] = String.format("<(%s, %s)>", r, c);
    				}
    				else {
    					str[c-1] = String.format(" (%s, %s) ", r, c);
    				}
    			}
    			StdOut.println(String.join(" ", str));
    		}
    }
    
    private boolean isConnected(int row1, int col1, int row2, int col2) {
    		int ind1 = getIndex(row1, col1);
    		int ind2 = getIndex(row2, col2);
    		return UF.connected(ind1, ind2);
    }
    
    private int getIndex(int row, int col) {
    		return gridSize * (row - 1) + col;
    }
    
    private void checkOpenAndUnion(int s, int neighbor) {
		if ( cellState[neighbor] ) {
			UF.union(s, neighbor);
		}
    }
    
    public static void main(String[] args) {
	// test client (optional)
    	Percolation P = new Percolation(4);
    	StdOut.println("grid size:" + P.gridSize);
    	StdOut.println(P.UF.find(2));
    	StdOut.println(P.UF.find(8));
    	StdOut.println(P.UF.find(14));
    	StdOut.println("(0, 3) connected? " + P.UF.connected(0, 3));
    	StdOut.println("(1, 1) (1, 2) connected? " + P.isConnected(1, 1, 1, 2));
    	StdOut.println("(17, 3) connected? " + P.UF.connected(17, 3));
    	StdOut.println("(17, 15) connected? " + P.UF.connected(17, 15));
    	P.printGrid();
    	// test corner cases
    	StdOut.println("open site (1, 2), (1, 1)>>>>>>");
    	P.open(1, 2);
    	StdOut.println(P.UF.find(16));
    	P.open(1, 1);
    	StdOut.println(P.UF.find(16));
    	P.printGrid();
    	StdOut.println("is (1, 2) connected to (1, 1)?" + P.isConnected(1, 2, 1, 1));
    	StdOut.println("open site (2, 1)>>>>>>");
    	P.open(2, 1);
    	StdOut.println(P.UF.find(16));
    	P.printGrid();
    	StdOut.println("is (1, 1) (2, 1) connected? " + P.isConnected(1, 1, 2, 1));
    	StdOut.println("is (1, 2) (2, 1) connected? " + P.isConnected(1, 2, 2, 1));
    	StdOut.println("open site (4, 1)>>>>>>");
    P.open(4, 1);
    StdOut.println(P.UF.find(16));
    P.printGrid();
    StdOut.println("is (4, 1) (3, 1) connected? " + P.isConnected(4, 1, 3, 1));
    P.open(4,  2);
    StdOut.println(P.UF.find(16));
    P.printGrid();
    StdOut.println("is (3, 1) (4, 2) connected? " + P.isConnected(3, 1, 4, 2));
    StdOut.println("Open site (2, 4)>>>>>>");
    P.open(2,  4);
    StdOut.println(P.UF.find(16));
    P.printGrid();
    StdOut.println("is (1, 4) (2, 4) connected? " + P.isConnected(1, 4, 2, 4));
    P.open(1, 4);
    StdOut.println(P.UF.find(16));
    P.printGrid();
    StdOut.println("is (1, 4) (2, 4) connected? " + P.isConnected(1, 4, 2, 4));
    P.open(3, 4);
    StdOut.println(P.UF.find(16));
    P.printGrid();
    StdOut.println("is (3, 4) (4, 4) connected? " + P.isConnected(3, 4, 4, 4));
    P.open(4, 3);
    StdOut.println("is (4, 3) (3, 4) connnected? " + P.isConnected(4, 3, 3, 4));
    P.printGrid();
    StdOut.println("number of open cell: " + P.numberOfOpenSites());
    StdOut.println("is (4, 4) full? " + P.isFull(4, 4));
    StdOut.println("is (3, 4) full? " + P.isFull(3, 4));
    StdOut.println("is (2, 1) full? " + P.isFull(2, 1));
    StdOut.println("is (4, 1) full? " + P.isFull(4, 1));
    StdOut.println("is percolate? " + P.percolates());
    P.open(3, 2);
    P.printGrid();
    StdOut.println("is percolate? " + P.percolates());
    P.open(3, 3);
    P.printGrid();
    StdOut.println("is percolate? " + P.percolates());
    }
 }
