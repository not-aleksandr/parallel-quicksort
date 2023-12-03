package primitives;


import java.util.concurrent.RecursiveAction;

import static java.lang.Math.min;

public class BlockedFor extends RecursiveAction {
    private final int block;
    private final int from;
    private final int to;
    private final TernaryConsumer<Integer, Integer, Integer> action;

    public BlockedFor(int block, int from, int to, TernaryConsumer<Integer, Integer, Integer> action) {
        this.block = block;
        this.from = from;
        this.to = to;
        this.action = action;
    }

    @Override
    protected void compute() {
        new ParallelFor(block, 0, blockCount(block, from, to), i -> {
            int start = from + i * block;
            int end = min(start + block, to);
            action.accept(i, start, end);
        }).compute();
    }

    public static int blockCount(int block, int from, int to) {
        return (to - from + block - 1) / block;
    }


    @FunctionalInterface
    public interface TernaryConsumer<T, U, E> {
        void accept(T a, U b, E c);
    }
}