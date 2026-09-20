import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPairSolver {

    private static final int BRUTE_FORCE_CUTOFF = 3;

    private final Metrics metrics = new Metrics();

    public Metrics getMetrics() {
        return metrics;
    }

    private static class Result {
        final double dist;
        final Point[] sortedByY;

        Result(double dist, Point[] sortedByY) {
            this.dist = dist;
            this.sortedByY = sortedByY;
        }
    }

    public double closestPair(Point[] points) {
        metrics.reset();

        if (points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        Point[] px = points.clone();

        Arrays.sort(px, Comparator.comparingDouble(p -> p.x));

        return closestPair(px, 0, px.length - 1, 1).dist;
    }

    private Result closestPair(Point[] px, int lo, int hi, int depth) {
        metrics.recordCall(depth);

        int n = hi - lo + 1;

        if (n <= BRUTE_FORCE_CUTOFF) {
            Point[] slice = Arrays.copyOfRange(px, lo, hi + 1);

            Arrays.sort(slice, Comparator.comparingDouble(p -> p.y));

            return new Result(
                    bruteForce(px, lo, hi),
                    slice
            );
        }

        int mid = lo + (hi - lo) / 2;
        Point midPoint = px[mid];

        Result left = closestPair(px, lo, mid, depth + 1);
        Result right = closestPair(px, mid + 1, hi, depth + 1);

        double d = Math.min(left.dist, right.dist);

        Point[] merged = mergeByY(
                left.sortedByY,
                right.sortedByY
        );

        List<Point> strip = new ArrayList<>();

        for (Point p : merged) {
            if (Math.abs(p.x - midPoint.x) < d) {
                strip.add(p);
            }
        }

        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1;
                 j < strip.size()
                         && strip.get(j).y - strip.get(i).y < d;
                 j++) {

                metrics.comparisons++;

                double dist =
                        strip.get(i).distanceTo(strip.get(j));

                if (dist < d) {
                    d = dist;
                }
            }
        }

        return new Result(d, merged);
    }

    private Point[] mergeByY(Point[] a, Point[] b) {
        Point[] result = new Point[a.length + b.length];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < a.length && j < b.length) {
            metrics.comparisons++;

            if (a[i].y <= b[j].y) {
                result[k++] = a[i++];
            } else {
                result[k++] = b[j++];
            }
        }

        while (i < a.length) {
            result[k++] = a[i++];
        }

        while (j < b.length) {
            result[k++] = b[j++];
        }

        return result;
    }

    private double bruteForce(Point[] px, int lo, int hi) {
        double min = Double.POSITIVE_INFINITY;

        for (int i = lo; i <= hi; i++) {
            for (int j = i + 1; j <= hi; j++) {

                metrics.comparisons++;

                double d = px[i].distanceTo(px[j]);

                if (d < min) {
                    min = d;
                }
            }
        }

        return min;
    }

    public static double bruteForceReference(Point[] points) {
        double min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {

                double d = points[i].distanceTo(points[j]);

                if (d < min) {
                    min = d;
                }
            }
        }

        return min;
    }
}