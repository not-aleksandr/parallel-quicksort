import quicksort.ParallelQuicksort;
import quicksort.ParallelQuicksortInplace;
import quicksort.SequentialQuicksort;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;

import static java.lang.Runtime.getRuntime;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.copyOf;

public class Benchmark {
    private static final int ARRAY_SIZE = 100_000_000;
    private static final ForkJoinPool POOL = new ForkJoinPool(getRuntime().availableProcessors());
    private static final int[] BLOCKS = new int[]{1_000, 10_000, 100_000, 1_000_000, 10_000_000};
    private static final int WARMUP_ITERATIONS = 3;
    private static final int MEASURE_ITERATIONS = 5;
    private static final int SEED = 42;

    public static void main(String[] arguments) {
        var availableProcessors = getRuntime().availableProcessors();
        var array = randomArray();
        logSeparator();
        log("CPU count", availableProcessors);
        logSeparator();

        log("Warmup iterations", WARMUP_ITERATIONS);
        run(SorterType.SEQUENTIAL_SORT, WARMUP_ITERATIONS, array);
        run(SorterType.PARALLEL_SORT, WARMUP_ITERATIONS, array);
        run(SorterType.PARALLEL_SORT_INPLACE, WARMUP_ITERATIONS, array);
        logSeparator();

        log("Measure iterations", MEASURE_ITERATIONS);
        run(SorterType.SEQUENTIAL_SORT, MEASURE_ITERATIONS, array);
        run(SorterType.PARALLEL_SORT, MEASURE_ITERATIONS, array);
        run(SorterType.PARALLEL_SORT_INPLACE, MEASURE_ITERATIONS, array);
        logSeparator();
    }

    public static void run(SorterType sorterType, int iterationCount, int[] array) {
        log("Sort with type", sorterType);
        var blocks = sorterType.requireBlock ? BLOCKS : new int[1];
        for (int block : blocks) {
            if (sorterType.requireBlock) {
                log("Block size", block);
            }
            var sorter = sorter(sorterType, block);
            double totalTime = 0;
            for (int i = 0; i < iterationCount; i++) {
                var testArray = copyOf(array, array.length);
                System.gc();
                var start = currentTimeMillis();
                sorter.accept(testArray);
                var end = currentTimeMillis();
                long time = end - start;
                log(format("Iteration %d", i + 1), format("%d millis", time));
                totalTime += time;
            }
            log("Avg time", format("%f millis", totalTime / iterationCount));
        }
    }

    private static void log(String header, Object value) {
        System.out.print(header);
        System.out.print(":     ");
        System.out.println(value.toString());
    }

    private static void logSeparator() {
        System.out.println("=".repeat(10));
    }

    private static Consumer<int[]> sorter(SorterType type, int block) {
        return switch (type) {
            case SEQUENTIAL_SORT -> SequentialQuicksort::quicksort;
            case PARALLEL_SORT -> (array) -> POOL.invoke(new ParallelQuicksort(block, array));
            case PARALLEL_SORT_INPLACE -> (array) -> POOL.invoke(new ParallelQuicksortInplace(block, array));
        };
    }

    private enum SorterType {
        SEQUENTIAL_SORT(false),
        PARALLEL_SORT(true),
        PARALLEL_SORT_INPLACE(true);

        final boolean requireBlock;
        SorterType(boolean requireBlock) {
            this.requireBlock = requireBlock;
        }
    }

    private static int[] randomArray() {
        Random random = new Random(SEED);
        int[] array = new int[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt() % (ARRAY_SIZE / 3);
        }
        return array;
    }
}