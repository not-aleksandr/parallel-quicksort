package quicksort;

public class Partition {

    public static int partition(int[] array, int from, int to, int pivot) {
        while (from <= to) {
            while (array[from] < pivot) from++;
            while (array[to] > pivot) to--;
            if (from >= to) break;
            int temp = array[from];
            array[from++] = array[to];
            array[to--] = temp;
        }
        return to;
    }
}
