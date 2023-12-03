package primitives;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BlockedForTest {

    @Test
    public void parallelBlockedFor() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new BlockedFor(3, 0, 10, (block, start, end) -> {
            for (int i = start; i < end; i++) {
                values[i]++;
            }
        }).compute();

        assertThat(values).isEqualTo(expected);
    }

    @Test
    public void parallelBlockedForBlockSizeOne() {
        var values = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        var expected = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        new BlockedFor(1, 0, 10, (block, start, end) -> {
            for (int i = start; i < end; i++) {
                values[i]++;
            }
        }).compute();


        assertThat(values).isEqualTo(expected);
    }

}