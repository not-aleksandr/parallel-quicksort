package quicksort;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.sort;
import static org.assertj.core.api.Assertions.assertThat;

class ParallelQuicksortInplaceTest {
    @Test
    public void parallelQuicksortInplace() {
        var array = new int[]{5, 6, 0, 9, 3, 1, 2, 4, 8, 7};
        var expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        new ParallelQuicksortInplace(3, array).compute();

        assertThat(array).isEqualTo(expected);
    }

    @Test
    public void parallelQuicksortInplaceEqualNumbers() {
        var array = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
        var expected = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1};

        new ParallelQuicksortInplace(3, array).compute();

        assertThat(array).isEqualTo(expected);
    }


    @Test
    public void parallelQuicksortEqualToStdSort() {
        var array = randomArray(10000);
        var expected = copyOf(array, array.length);
        sort(expected);

        new ParallelQuicksortInplace(3, array).compute();

        assertThat(array).isEqualTo(expected);
    }

    public static int[] randomArray(int size) {
        Random random = new Random(42);
        int maxAbsValue = size / 3;
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt() % maxAbsValue;
        }
        return array;
    }
}