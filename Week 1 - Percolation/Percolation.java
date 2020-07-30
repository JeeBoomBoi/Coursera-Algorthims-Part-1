import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // The size of grid
    private final int gridLength;

    // An array to check if the site is open
    private final boolean[] sites;

    // number of open site
    private int openSites;

    // virtual top site
    private final int virtualTopIndex;

    // virtual bottom site
    private final int virtualBottomIndex;

    // Ds to calculate Percolation
    private final WeightedQuickUnionUF ufForPercolation;

    // Ds to calculate Fullness (to avoid backwash of percolation)
    private final WeightedQuickUnionUF ufForFullness;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException(
                    "Grid must have row and column of size greater than 1");
        }
        this.gridLength = n;
        int gridSize = this.gridLength * this.gridLength;
        int numberOfSites = gridSize + 2;
        sites = new boolean[numberOfSites];
        openSites = 0;
        virtualTopIndex = 0;
        virtualBottomIndex = this.gridLength * this.gridLength + 1;
        sites[virtualTopIndex] = true;
        sites[virtualBottomIndex] = true;
        ufForPercolation = new WeightedQuickUnionUF(numberOfSites);
        ufForFullness = new WeightedQuickUnionUF(numberOfSites);

        // initial all sites as closed;
        for (int col = 1; col <= this.gridLength; col++) {
            int rowTop = 1;
            int siteTopIndex = getIndex(rowTop, col);
            ufForPercolation.union(virtualTopIndex, siteTopIndex);
            ufForFullness.union(virtualTopIndex, siteTopIndex);

            int siteBottomIndex = getIndex(gridLength, col);
            ufForPercolation.union(virtualBottomIndex, siteBottomIndex);
        }
    }

    private int getIndex(int row, int col) {
        validateRowCol(row, col);
        return ((row - 1) * this.gridLength) + col;
    }

    private void validateRowCol(int row, int col) {
        if (row < 1 || row > gridLength) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        if (col < 1 || col > gridLength) {
            throw new IllegalArgumentException("Col index out of bounds");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateRowCol(row, col);
        int currSite = getIndex(row, col);
        if (sites[currSite]) {
            return;
        }
        sites[currSite] = true;
        // if left is open union with it
        if (col > 1 && isOpen(row, col - 1)) {
            int leftSite = getIndex(row, col - 1);
            ufForPercolation.union(currSite, leftSite);
            ufForFullness.union(currSite, leftSite);
        }
        // if right is open union with it
        if (col < gridLength && isOpen(row, col + 1)) {
            int rightSite = getIndex(row, col + 1);
            ufForPercolation.union(currSite, rightSite);
            ufForFullness.union(currSite, rightSite);
        }
        // if top is open union with it
        if (row > 1 && isOpen(row - 1, col)) {
            int topSite = getIndex(row - 1, col);
            ufForPercolation.union(currSite, topSite);
            ufForFullness.union(currSite, topSite);
        }

        // if bottom is open union with it
        if (row < gridLength && isOpen(row + 1, col)) {
            int bottomSite = getIndex(row + 1, col);
            ufForPercolation.union(currSite, bottomSite);
            ufForFullness.union(currSite, bottomSite);
        }
        openSites++;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateRowCol(row, col);
        int currSite = getIndex(row, col);
        return sites[currSite];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateRowCol(row, col);
        int currSite = getIndex(row, col);
        return (isOpen(row, col) && ufForFullness.find(currSite) == ufForFullness
                .find(virtualTopIndex));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (gridLength == 1) {
            int currSite = getIndex(1, 1);
            return sites[currSite];
        }
        return ufForPercolation.find(virtualBottomIndex) == ufForPercolation.find(virtualTopIndex);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(2);
        StdOut.println(percolation.isOpen(1, 1));
        percolation.open(1, 1);
        StdOut.println(percolation.isOpen(1, 1));
        StdOut.println(percolation.isFull(1, 1));

        percolation.open(1, 2);
        StdOut.println(percolation.isOpen(1, 2));
        StdOut.println(percolation.isFull(1, 2));
        StdOut.println(percolation.percolates());

        percolation.open(2, 1);
        StdOut.println(percolation.isFull(2, 1));
    }
}
