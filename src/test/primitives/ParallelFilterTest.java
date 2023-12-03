package primitives;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelFilterTest {

    @Test
    public void parallelFilter() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[] {0, 2, 4, 6, 8};

        var result = new ParallelFilter(3, values, (i) -> i % 2 == 0).compute().result;

        assertThat(result).isEqualTo(expected);
    }


    @Test
    public void parallelFilterBlockSizeOne() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[] {0, 2, 4, 6, 8};

        var result = new ParallelFilter(1, values, (i) -> i % 2 == 0).compute().result;

        assertThat(result).isEqualTo(expected);
    }
}