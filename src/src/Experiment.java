import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class Experiment {

    private static final Random RANDOM = new Random(42);

    public static void main(String[] args) throws Exception {

        File resultsDir = new File("results");

        if (!resultsDir.exists()) {
            resultsDir.mkdirs();
        }

        FileWriter writer = new FileWriter("results/results.csv");

        writer.write("algorithm,inputType,n,timeNs,maxDepth,comparisons,swaps,recursiveCalls\n");

        int[] sizes = {100, 1000, 10000, 100000};

        String[] types = {
                "random",
                "sorted",
                "reverse",
                "duplicate-heavy"
        };

        for (int n : sizes) {
            for (String type : types) {

                int[] data = generateArray(n, type);

                runMergeSort(data, type, writer);
                runQuickSort(data, type, writer);
                runSelect(data, type, writer);
                runClosestPair(n, type, writer);
            }
        }

        writer.close();

        System.out.println("Experiments finished successfully.");
        System.out.println("Results saved to results/results.csv");
    }

    private static void runMergeSort(
            int[] original,
            String type,
            FileWriter writer
    ) throws Exception {

        int[] data = original.clone();
        MergeSorter sorter = new MergeSorter();

        long start = System.nanoTime();
        sorter.sort(data);
        long end = System.nanoTime();

        writeResult(
                writer,
                "MergeSort",
                type,
                data.length,
                end - start,
                sorter.getMetrics()
        );
    }

    private static void runQuickSort(
            int[] original,
            String type,
            FileWriter writer
    ) throws Exception {

        int[] data = original.clone();
        QuickSorter sorter = new QuickSorter();

        long start = System.nanoTime();
        sorter.sort(data);
        long end = System.nanoTime();

        writeResult(
                writer,
                "QuickSort",
                type,
                data.length,
                end - start,
                sorter.getMetrics()
        );
    }

    private static void runSelect(
            int[] original,
            String type,
            FileWriter writer
    ) throws Exception {

        int[] data = original.clone();
        int k = data.length / 2;

        DeterministicSelector selector = new DeterministicSelector();

        long start = System.nanoTime();
        selector.select(data, k);
        long end = System.nanoTime();

        writeResult(
                writer,
                "DeterministicSelect",
                type,
                data.length,
                end - start,
                selector.getMetrics()
        );
    }

    private static void runClosestPair(
            int n,
            String type,
            FileWriter writer
    ) throws Exception {

        Point[] points = generatePoints(n, type);
        ClosestPairSolver solver = new ClosestPairSolver();

        long start = System.nanoTime();
        solver.closestPair(points);
        long end = System.nanoTime();

        writeResult(
                writer,
                "ClosestPair",
                type,
                n,
                end - start,
                solver.getMetrics()
        );
    }

    private static void writeResult(
            FileWriter writer,
            String algorithm,
            String type,
            int n,
            long timeNs,
            Metrics metrics
    ) throws Exception {

        writer.write(
                algorithm + "," +
                        type + "," +
                        n + "," +
                        timeNs + "," +
                        metrics.maxDepth + "," +
                        metrics.comparisons + "," +
                        metrics.swaps + "," +
                        metrics.recursiveCalls + "\n"
        );
    }

    private static int[] generateArray(int n, String type) {

        int[] a = new int[n];

        switch (type) {
            case "random" -> {
                for (int i = 0; i < n; i++) a[i] = RANDOM.nextInt(n * 10);
            }
            case "sorted" -> {
                for (int i = 0; i < n; i++) a[i] = i;
            }
            case "reverse" -> {
                for (int i = 0; i < n; i++) a[i] = n - i;
            }
            case "duplicate-heavy" -> {
                for (int i = 0; i < n; i++) a[i] = RANDOM.nextInt(10);
            }
        }

        return a;
    }

    private static Point[] generatePoints(int n, String type) {

        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            double x;
            double y;

            switch (type) {
                case "duplicate-heavy" -> {
                    x = RANDOM.nextInt(100);
                    y = RANDOM.nextInt(100);
                }
                case "sorted" -> {
                    x = i;
                    y = i;
                }
                case "reverse" -> {
                    x = n - i;
                    y = n - i;
                }
                default -> {
                    x = RANDOM.nextDouble() * n;
                    y = RANDOM.nextDouble() * n;
                }
            }

            points[i] = new Point(x, y);
        }

        return points;
    }
}