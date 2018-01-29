import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation assignment It is modeled as a grid board which material run into
 * from the first row, and run out from the last row 01.27.2018
 * 
 * @author liming
 *
 */
public class Percolation {
    private final int gridSize; // grid size for the physics problem.
    private final WeightedQuickUnionUF uf; // the WeightedQuickUnionUF class instance
    private final int start = 0; // a pseudo node connected to all cells in first row
    private final int end; // a pseudo node connected to all cells in last row
    private boolean[] cellState; // a boolean array to record the open/close state of all the cells
    private int numberOfOpenCell = 0; // value to record how many cell is open

    /**
     * constructor method to build a grid with all cell close.
     * 
     * @param n
     *            size of grid
     * 
     */
    public Percolation(int n) {
    // create n-by-n grid, with all sites blocked
    if (n < 1)
        throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");
    gridSize = n;
    end = gridSize * gridSize + 1;
    cellState = new boolean[end + 1];

    // initially, connect start cell with first row, and end cell with last row
    uf = new WeightedQuickUnionUF(end + 1);
    for (int j = 1; j <= gridSize; j++) {
        uf.union(start, j);
        uf.union(end, (gridSize - 1) * gridSize + j);
    }
    }

    /**
     * open a site based on its (row, col) index.
     * 
     * @param row
     *            row index
     * @param col
     *            column index
     */
    public void open(int row, int col) {
    // open site (row, col) if it is not open already
    if (row < 1 || row > gridSize || col < 1 || col > gridSize)
        throw new java.lang.IllegalArgumentException("size of grid should be in range [0, gridSize]!");

    int index = this.getIndex(row, col);
    if (!cellState[index]) {
        cellState[index] = true;
        numberOfOpenCell++;
        modifyUF(row, col);
    }
    }

    /**
     * with a new open cell (row, col), modify the Union-Find data model.
     * 
     * @param row
     *            row index
     * @param col
     *            column index
     */
    private void modifyUF(int row, int col) {
    int index = this.getIndex(row, col);

    // four corner cases
    if (row == 1 && col == 1) {
        // StdOut.println("up left corner case");
        this.checkOpenAndUnion(index, this.getIndex(2, 1));
    } else if (row == 1 && col == gridSize) {
        // StdOut.println("up right corner case");
        this.checkOpenAndUnion(index, this.getIndex(2, col));
    } else if (row == gridSize && col == 1) {
        // StdOut.println("down left corner case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, 1));
    } else if (row == gridSize && col == gridSize) {
        // StdOut.println("down right corner case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, col));
    }
    // four edge cases
    else if (row != 1 && row != gridSize && col == 1) {
        // StdOut.println("left edge case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row + 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row, col + 1));
    } else if (row != 1 && row != gridSize && col == gridSize) {
        // StdOut.println("right edge case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row + 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row, col - 1));
    } else if (row == 1 && col != 1 && col != gridSize) {
        // StdOut.println("up edge case");
        this.checkOpenAndUnion(index, this.getIndex(row + 1, col));
    } else if (row == gridSize && col != 1 && col != gridSize) {
        // StdOut.println("down edge case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, col));
    } else {
        // StdOut.println("normal case");
        this.checkOpenAndUnion(index, this.getIndex(row - 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row + 1, col));
        this.checkOpenAndUnion(index, this.getIndex(row, col - 1));
        this.checkOpenAndUnion(index, this.getIndex(row, col + 1));
    }
    }

    /**
     * check if a cell is open or not.
     * 
     * @param row
     *            row index
     * @param col
     *            col index
     * @return true if cell is open, false otherwise
     */
    public boolean isOpen(int row, int col) {
    // is site (row, col) open?
    if (row < 1 || row > gridSize || col < 1 || col > gridSize)
        throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");
    return cellState[this.getIndex(row, col)];

    }

    /**
     * check if a site is connected with top row.
     * 
     * @param row
     *            row index
     * @param col
     *            column index
     * @return
     */
    public boolean isFull(int row, int col) {
    // is site (row, col) full?
    if (row < 1 || row > gridSize || col < 1 || col > gridSize)
        throw new java.lang.IllegalArgumentException("size of grid should be larger than 0!");

    return isOpen(row, col) && uf.find(1) == uf.find(this.getIndex(row, col));
    }

    /**
     * return number of open cells.
     * 
     * @return
     */
    public int numberOfOpenSites() {
    // number of open sites
    return numberOfOpenCell;
    }

    /**
     * check if grid board percolates.
     * 
     * @return
     */
    public boolean percolates() {
    // does the system percolate?
    if (gridSize == 1) {
        return isOpen(1, 1);
    } else {
        return uf.find(start) == uf.find(end);
    }
    }

    /**
     * print out the state of grid.
     */
    private void printGrid() {
    StdOut.println("total length of uf: " + cellState.length);
    for (int r = 1; r <= gridSize; r++) {
        String[] str = new String[gridSize];
        for (int c = 1; c <= gridSize; c++) {
        if (this.isOpen(r, c)) {
            str[c - 1] = String.format("<(%s, %s)>", r, c);
        } else {
            str[c - 1] = String.format(" (%s, %s) ", r, c);
        }
        }
        StdOut.println(String.join(" ", str));
    }
    }

    /**
     * check if two sites are connected.
     * 
     * @param row1
     *            row index of first site
     * @param col1
     *            column index of second site
     * @param row2
     *            row index of first site
     * @param col2
     *            column index of second site
     * 
     * @return
     */
    private boolean isConnected(int row1, int col1, int row2, int col2) {
    int ind1 = getIndex(row1, col1);
    int ind2 = getIndex(row2, col2);
    return uf.connected(ind1, ind2);
    }

    /**
     * an utility function to compute the Union-Find index from (row, col).
     * 
     * @param row
     *            row index
     * @param col
     *            col index
     * 
     * @return
     */
    private int getIndex(int row, int col) {
    return gridSize * (row - 1) + col;
    }

    /**
     * check if its neighbor is open, if yes, union with neighbor.
     * 
     * @param s
     *            checked site itself
     * @param neighbor
     *            neighbor of checked site
     */
    private void checkOpenAndUnion(int s, int neighbor) {
    if (cellState[neighbor]) {
        uf.union(s, neighbor);
    }
    }

    public static void main(String[] args) {
    // test client (optional)
    Percolation perc = new Percolation(4);
    StdOut.println("grid size:" + perc.gridSize);
    StdOut.println(perc.uf.find(2));
    StdOut.println(perc.uf.find(8));
    StdOut.println(perc.uf.find(14));
    StdOut.println("(0, 3) connected? " + perc.uf.connected(0, 3));
    StdOut.println("(1, 1) (1, 2) connected? " + perc.isConnected(1, 1, 1, 2));
    StdOut.println("(17, 3) connected? " + perc.uf.connected(17, 3));
    StdOut.println("(17, 15) connected? " + perc.uf.connected(17, 15));
    perc.printGrid();
    // test corner cases
    StdOut.println("open site (1, 2), (1, 1)>>>>>>");
    perc.open(1, 2);
    StdOut.println(perc.uf.find(16));
    perc.open(1, 1);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (1, 2) connected to (1, 1)?" + perc.isConnected(1, 2, 1, 1));
    StdOut.println("open site (2, 1)>>>>>>");
    perc.open(2, 1);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (1, 1) (2, 1) connected? " + perc.isConnected(1, 1, 2, 1));
    StdOut.println("is (1, 2) (2, 1) connected? " + perc.isConnected(1, 2, 2, 1));
    StdOut.println("open site (4, 1)>>>>>>");
    perc.open(4, 1);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (4, 1) (3, 1) connected? " + perc.isConnected(4, 1, 3, 1));
    perc.open(4, 2);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (3, 1) (4, 2) connected? " + perc.isConnected(3, 1, 4, 2));
    StdOut.println("Open site (2, 4)>>>>>>");
    perc.open(2, 4);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (1, 4) (2, 4) connected? " + perc.isConnected(1, 4, 2, 4));
    perc.open(1, 4);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (1, 4) (2, 4) connected? " + perc.isConnected(1, 4, 2, 4));
    perc.open(3, 4);
    StdOut.println(perc.uf.find(16));
    perc.printGrid();
    StdOut.println("is (3, 4) (4, 4) connected? " + perc.isConnected(3, 4, 4, 4));
    perc.open(4, 3);
    StdOut.println("is (4, 3) (3, 4) connnected? " + perc.isConnected(4, 3, 3, 4));
    perc.printGrid();
    StdOut.println("number of open cell: " + perc.numberOfOpenSites());
    StdOut.println("is (4, 4) full? " + perc.isFull(4, 4));
    StdOut.println("is (3, 4) full? " + perc.isFull(3, 4));
    StdOut.println("is (2, 1) full? " + perc.isFull(2, 1));
    StdOut.println("is (4, 1) full? " + perc.isFull(4, 1));
    StdOut.println("is percolate? " + perc.percolates());
    perc.open(3, 2);
    perc.printGrid();
    StdOut.println("is percolate? " + perc.percolates());
    perc.open(3, 3);
    perc.printGrid();
    StdOut.println("is percolate? " + perc.percolates());
    // corner case 1
    perc = new Percolation(1);
    StdOut.println("grid size:" + perc.gridSize);
    StdOut.println(perc.uf.find(1));
    StdOut.println(perc.percolates());
    perc.open(1, 1);
    perc.printGrid();
    StdOut.println("is percolate? " + perc.percolates());
    // corner case 2
    perc = new Percolation(2);
    StdOut.println("grid size:" + perc.gridSize);
    StdOut.println(perc.isOpen(1, 1));
    StdOut.println(perc.isFull(1, 1));
    perc.printGrid();
    StdOut.println("(1, 1) (2, 2) connected? " + perc.isConnected(1, 1, 2, 2));
    StdOut.println(perc.percolates());
    perc.open(1, 1);
    perc.printGrid();
    StdOut.println("(1, 1) open? " + perc.isOpen(1, 1));
    StdOut.println("(1, 1) full? " + perc.isFull(1, 1));
    }
}
