package xyz.vanduuren.jgobs.lib;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test the ByteArrayUtilities methods.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class ByteArrayUtilitiesTest {
    @Test
    public void concat() throws Exception {
        assertArrayEquals(ByteArrayUtilities.concat(new byte[]{1}, new byte[]{2}), new byte[]{1, 2});
        assertArrayEquals(ByteArrayUtilities.concat(new byte[]{3, 4}, new byte[]{2, 1}),
                new byte[]{3, 4, 2, 1});
    }

}