import java.util.Arrays;
import java.util.Random;

public class Tests {

    private static final Random RANDOM = new Random(42);

    public static void main(String[] args) {

        testSorting();
        testDeterministicSelect();
        testClosestPair();

        System.out.println("All tests passed.");
    }

    private static void testSorting() {

        int[][] tests = {
                {},
                {1},
                {5, 3, 1, 4, 2},
                {1, 1, 1, 1},
                {5, 4, 3, 2, 1},
                {1, 2, 3, 4, 5}
        };

        for (int[] test : tests) {

            int[] expected = test.clone();
            Arrays.sort(expected);

            int[] mergeArray = test.clone();
            new MergeSorter().sort(mergeArray);

            if (!Arrays.equals(mergeArray, expected)) {
                throw new AssertionError("MergeSort failed");
            }

            int[] quickArray = test.clone();
            new QuickSorter().sort(quickArray);

            if (!Arrays.equals(quickArray, expected)) {
                throw new AssertionError("QuickSort failed");
            }
        }

        for (int i = 0; i < 100; i++) {

            int n = RANDOM.nextInt(100) + 1;
            int[] original = generateArray(n, "random");

            int[] expected = original.clone();
            Arrays.sort(expected);

            int[] mergeArray = original.clone();
            new MergeSorter().sort(mergeArray);

            int[] quickArray = original.clone();
            new QuickSorter().sort(quickArray);

            if (!Arrays.equals(mergeArray, expected)) {
                throw new AssertionError("MergeSort random test failed");
            }

            if (!Arrays.equals(quickArray, expected)) {
                throw new AssertionError("QuickSort random test failed");
            }
        }
    }

    private static void testDeterministicSelect() {

        for (int i = 0; i < 100; i++) {

            int n = RANDOM.nextInt(100) + 1;
            int[] original = generateArray(n, "random");

            int k = RANDOM.nextInt(n);

            int[] expectedArray = original.clone();
            Arrays.sort(expectedArray);

            int expected = expectedArray[k];

            int[] actualArray = original.clone();

            DeterministicSelector selector =
                    new DeterministicSelector();

            int actual = selector.select(actualArray, k);

            if (actual != expected) {
                throw new AssertionError(
                        "Deterministic Select failed"
                );
            }
        }
    }

    private static void testClosestPair() {

        for (int i = 0; i < 20; i++) {

            int n = RANDOM.nextInt(100) + 2;

            Point[] points = generatePoints(n);

            ClosestPairSolver solver =
                    new ClosestPairSolver();

            double actual = solver.closestPair(points);

            double expected =
                    ClosestPairSolver.bruteForceReference(points);

            if (Math.abs(actual - expected) > 1e-9) {
                throw new AssertionError(
                        "Closest Pair failed"
                );
            }
        }

        Point[] duplicatePoints = {
                new Point(1, 1),
                new Point(1, 1),
                new Point(5, 5)
        };

        ClosestPairSolver solver =
                new ClosestPairSolver();

        double result = solver.closestPair(duplicatePoints);

        if (result != 0.0) {
            throw new AssertionError(
                    "Closest Pair duplicate test failed"
            );
        }
    }

    private static int[] generateArray(int n, String type) {

        int[] a = new int[n];

        if (type.equals("random")) {
            for (int i = 0; i < n; i++) {
                a[i] = RANDOM.nextInt(n * 10);
            }
        }

        return a;
    }

    private static Point[] generatePoints(int n) {

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            points[i] = new Point(
                    RANDOM.nextDouble() * n,
                    RANDOM.nextDouble() * n
            );
        }

        return points;
    }
}
