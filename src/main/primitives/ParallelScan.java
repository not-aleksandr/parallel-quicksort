package primitives;

import java.util.concurrent.RecursiveAction;

import static primitives.BlockedFor.blockCount;

public class ParallelScan extends RecursiveAction {
    private final int block;
    private final int[] array;

    public ParallelScan(int block, int[] array) {
        this.block = block;
        this.array = array;
    }

    @Override
    protected void compute() {
        int n = array.length;
        if (n <= block || block == 1) {
            scanSeq(array, 0, n);
        } else {
            int[] blockSums = new int[blockCount(block, 0, n)];
            new BlockedFor(block, 0, n, (block, start, end) -> blockSums[block] = sumSeq(array, start, end)).compute();
            new ParallelScan(block, blockSums).compute();
            new BlockedFor(block, 0, n, (block, start, end) -> {
                int add = block == 0 ? 0 : blockSums[block - 1];
                array[start] += add;
                scanSeq(array, start, end);
            }).compute();
        }
    }

    public static void scanSeq(int[] array, int from, int to) {
        for (int i = from + 1; i < to; i++) {
            array[i] += array[i - 1];
        }
    }

    public static int sumSeq(int[] array, int from, int to) {
        int result = 0;
        for (int i = from; i < to; i++) {
            result += array[i];
        }
        return result;
    }
}
