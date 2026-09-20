
public class DeterministicSelector {

    private static final int GROUP_SIZE = 5;

    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    public int select(int[] a, int k) {
        metrics.reset();
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("k out of bounds");
        }
        return select(a, 0, a.length - 1, k, 1);
    }

    private int select(int[] a, int lo, int hi, int k, int depth) {
        metrics.recordCall(depth);

        while (true) {
            if (lo == hi) {
                return a[lo];
            }

            int n = hi - lo + 1;
            int pivotValue;

            if (n <= GROUP_SIZE) {
                insertionSort(a, lo, hi);
                return a[k];
            }

            int numGroups = (n + GROUP_SIZE - 1) / GROUP_SIZE;
            for (int i = 0; i < numGroups; i++) {
                int groupLo = lo + i * GROUP_SIZE;
                int groupHi = Math.min(groupLo + GROUP_SIZE - 1, hi);
                insertionSort(a, groupLo, groupHi);
                int medianIndex = groupLo + (groupHi - groupLo) / 2;
                swap(a, lo + i, medianIndex);
            }

            int mediansLo = lo;
            int mediansHi = lo + numGroups - 1;
            int medianOfMediansRank = mediansLo + (mediansHi - mediansLo) / 2;

            pivotValue = select(a, mediansLo, mediansHi, medianOfMediansRank, depth + 1);

            int[] bounds = partition3way(a, lo, hi, pivotValue);
            int ltEnd = bounds[0];
            int gtStart = bounds[1];
            if (k < ltEnd) {
                hi = ltEnd - 1;
            } else if (k <= gtStart) {
                return pivotValue;
            } else {
                lo = gtStart + 1;
            }
        }
    }

    private int[] partition3way(int[] a, int lo, int hi, int pivotValue) {
        int lt = lo, i = lo, gt = hi;
        while (i <= gt) {
            metrics.comparisons++;
            if (a[i] < pivotValue) {
                swap(a, lt, i);
                lt++;
                i++;
            } else if (a[i] > pivotValue) {
                swap(a, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
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
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
        metrics.swaps++;
    }
}
