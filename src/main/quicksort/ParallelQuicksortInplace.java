package quicksort;

import java.util.concurrent.RecursiveAction;

public class ParallelQuicksortInplace extends RecursiveAction {
    private final int block;
    private final int[] array;
    private final int from;
    private final int to;

    public ParallelQuicksortInplace(int block, int[] array) {
        this.block = block;
        this.array = array;
        this.from = 0;
        this.to = array.length - 1;
    }

    public ParallelQuicksortInplace(int block, int[] array, int from, int to) {
        this.block = block;
        this.array = array;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void compute() {
        if (to - from <= block) {
            SequentialQuicksort.quicksort(array, from, to);
        } else {
            int pivot = array[(from + to) / 2];
            int partition = Partition.partition(array, from, to, pivot);
            invokeAll(
                    new ParallelQuicksortInplace(block, array, from, partition),
                    new ParallelQuicksortInplace(block, array, partition + 1, to)
            );
        }
    }
}
