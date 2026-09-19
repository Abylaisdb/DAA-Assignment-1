public class MergeSort {

    private static final int CUTOFF = 16;

    private final Metrics metrics = new Metrics();
    private int[] aux;

    public Metrics getMetrics() {
        return metrics;
    }

    public void sort(int[] a) {
        metrics.reset();
        if (a.length < 2) {
            return;
        }
        aux = new int[a.length];
        sort(a, 0, a.length - 1, 1);
    }

    private void sort(int[] a, int lo, int hi, int depth) {
        metrics.recordCall(depth);

        if (hi - lo + 1 <= CUTOFF) {
            insertionSort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, lo, mid, depth + 1);
        sort(a, mid + 1, hi, depth + 1);
        merge(a, lo, mid, hi);
    }

    private void merge(int[] a, int lo, int mid, int hi) {
        System.arraycopy(a, lo, aux, lo, hi - lo + 1);

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > hi) {
                a[k] = aux[i++];
            } else {
                metrics.comparisons++;
                if (aux[i] <= aux[j]) {
                    a[k] = aux[i++];
                } else {
                    a[k] = aux[j++];
                }
            }
            metrics.swaps++;
        }
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
                metrics.swaps++;
                j--;
            }
            a[j + 1] = key;
        }
    }
}