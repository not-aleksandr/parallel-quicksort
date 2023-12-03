package quicksort;

import primitives.ParallelFilter;
import primitives.ParallelFor;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.function.Predicate;

public class ParallelQuicksort extends RecursiveAction {
    private final int block;
    private final int[] array;
    private final int from;
    private final int to;


    public ParallelQuicksort(int block, int[] array) {
        this.block = block;
        this.array = array;
        this.from = 0;
        this.to = array.length - 1;

    }

    @Override
    public void compute() {
        if (to - from <= block) {
            SequentialQuicksort.quicksort(array, from, to);
        } else {
            int pivot = array[(from + to) / 2];
            var lessTask = sortTask(i -> i < pivot).fork();
            var eqTask = filterTask(i -> i == pivot).fork();
            var greaterTask = sortTask(i -> i > pivot).fork();
            var lessResult = lessTask.join().result;
            var eqResult = eqTask.join().result;
            var greaterResult = greaterTask.join().result;
            invokeAll(
                    move(0, lessResult).fork(),
                    move(lessResult.length, eqResult).fork(),
                    move(lessResult.length + eqResult.length, greaterResult).fork()
            );
        }
    }

    private ParallelFor move(int offset, int[] result) {
        return new ParallelFor(block, 0, result.length, i -> {
            array[i + offset] = result[i];
        });
    }

    private RecursiveTask<SortResult> sortTask(Predicate<Integer> predicate) {
        return new RecursiveTask<>() {
            @Override
            protected SortResult compute() {
                int[] values = filterTask(predicate).compute().result;
                new ParallelQuicksort(block, values).compute();
                return new SortResult(values);
            }
        };
    }

    private ParallelFilter filterTask(Predicate<Integer> predicate) {
        return new ParallelFilter(block, array, predicate);
    }

    public record SortResult(int[] result) {
    }
}