package primitives;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelScanTest {

    @Test
    public void parallelScan() {
        var values = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        var expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new ParallelScan(3, values).compute();

        assertThat(values).isEqualTo(expected);
    }

    @Test
    public void parallelScanBlockSizeOne() {
        var values = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        var expected = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new ParallelScan(1, values).compute();

        assertThat(values).isEqualTo(expected);
    }

}