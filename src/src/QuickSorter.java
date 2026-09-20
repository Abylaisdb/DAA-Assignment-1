import java.util.Random;

public class QuickSorter {

    private static final int CUTOFF = 16;

    private final Metrics metrics = new Metrics();
    private final Random rnd = new Random();

    public Metrics getMetrics() {
        return metrics;
    }

    public void sort(int[] a) {
        metrics.reset();

        if (a.length < 2) {
            return;
        }

        sort(a, 0, a.length - 1, 1);
    }

    private void sort(int[] a, int lo, int hi, int depth) {
        metrics.recordCall(depth);

        while (hi - lo + 1 > CUTOFF) {

            int p = partition(a, lo, hi);

            int leftSize = p - lo;
            int rightSize = hi - p;

            if (leftSize < rightSize) {
                if (lo < p - 1) {
                    sort(a, lo, p - 1, depth + 1);
                }

                lo = p + 1;
            } else {
                if (p + 1 < hi) {
                    sort(a, p + 1, hi, depth + 1);
                }

                hi = p - 1;
            }
        }

        // Use insertion sort for small partitions.
        if (lo < hi) {
            insertionSort(a, lo, hi);
        }
    }

    private int partition(int[] a, int lo, int hi) {
        int pivotIndex = lo + rnd.nextInt(hi - lo + 1);
        swap(a, pivotIndex, hi);
        int pivot = a[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {

            metrics.comparisons++;

            if (a[j] < pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, hi);

        return i + 1;
    }

    private void insertionSort(int[] a, int lo, int hi) {

        for (int i = lo + 1; i <= hi; i++) {

            int key = a[i];
            int j = i - 1;

            while (j >= lo) {

                metrics.comparisons++;

                if (a[j] <= key) {
                    break;
                }

                a[j + 1] = a[j];
                metrics.writes++;

                j--;
            }

            a[j + 1] = key;
            metrics.writes++;
        }
    }

    private void swap(int[] a, int i, int j) {

        if (i == j) {
            return;
        }

        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;

        metrics.swaps++;
    }
}