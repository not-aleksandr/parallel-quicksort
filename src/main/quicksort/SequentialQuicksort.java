package quicksort;

public class SequentialQuicksort {

    public static void quicksort(int[] array) {
        quicksort(array, 0, array.length - 1);
    }

    public static void quicksort(int[] array, int from, int to) {
        if (from >= to) return;
        int pivot = array[(from + to) / 2];
        int partition = Partition.partition(array, from, to, pivot);
        quicksort(array, from, partition);
        quicksort(array, partition + 1, to);
    }
}
