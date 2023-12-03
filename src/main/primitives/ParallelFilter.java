package primitives;

import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

import static primitives.BlockedFor.blockCount;


public class ParallelFilter extends RecursiveTask<ParallelFilter.FilterResult> {
    private final int block;
    private final int[] array;
    private final Predicate<Integer> predicate;

    public ParallelFilter(int block, int[] array, Predicate<Integer> predicate) {
        this.block = block;
        this.array = array;
        this.predicate = predicate;
    }


    @Override
    public FilterResult compute() {
        int n = array.length;

        int[] flags = new int[n];
        new ParallelFor(block, 0, n, (i) -> flags[i] = predicate.test(array[i]) ? 1 : 0).compute();

        int[] sums = new int[blockCount(block, 0, n)];
        new BlockedFor(block, 0, n, (block, start, end) -> {
            for (int i = start; i < end; i++) {
                sums[block] += flags[i];
            }
        }).compute();
        new ParallelScan(block, sums).compute();

        int size = sums[sums.length - 1];
        int[] result = new int[size];
        new BlockedFor(block, 0, n, (block, start, end) -> {
            int shift = block == 0 ? 0 : sums[block - 1];
            for (int i = start; i < end; i++) {
                if (flags[i] == 1) {
                    result[shift++] = array[i];
                }
            }
        }).compute();
        return new FilterResult(result);
    }

    public static class FilterResult {
        public final int[] result;

        public FilterResult(int[] result) {
            this.result = result;
        }
    }
}
