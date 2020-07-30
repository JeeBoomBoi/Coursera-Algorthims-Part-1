import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // constant
    private static final double CONFIDENCE_95 = 1.96;

    // number of trials
    private final int trials;

    // list to contain the results
    private final double[] list;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1) {
            throw new IllegalArgumentException("Grid size should be greater than 1");
        }
        if (trials < 1) {
            throw new IllegalArgumentException("Number of trials should be greater than 1");
        }

        this.trials = trials;
        list = new double[this.trials];

        for (int i = 0; i < this.trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                percolation.open(row, col);
            }
            list[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(list);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(list);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(this.trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(this.trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridLength = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(gridLength, trials);

        StdOut.println("mean = " + stats.mean());
        StdOut.println("stddev = " + stats.stddev());
        StdOut.println(
                "95% confidence interval = " + "[" + stats.confidenceLo() + ", " + stats
                        .confidenceHi() + "]");
    }

}
