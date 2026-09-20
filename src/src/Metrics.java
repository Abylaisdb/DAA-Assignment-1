public class Metrics {
    public int maxDepth = 0;
    public long comparisons = 0;
    public long swaps = 0;
    public long recursiveCalls =0;
    public long writes =0;

    public void reset(){
        maxDepth =0;
        comparisons =0;
        swaps =0;
        recursiveCalls =0;
        writes =0;
    }

    public void recordCall(int depth){
        recursiveCalls++;
        if (depth>maxDepth){
            maxDepth =depth;
        }
    }


    @Override
    public String toString() {
        return "Metrics{" +
                "maxDepth=" + maxDepth +
                ", comparisons=" + comparisons +
                ", swaps=" + swaps +
                ", recursiveCalls=" + recursiveCalls +
                ", writes=" + writes +
                '}';
    }
}
