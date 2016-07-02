package xyz.vanduuren.jgobs.types.composite;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of StructTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-01
 */
public class StructTypeTest {
    @Test
    public void encode() throws Exception {
        // Simple test case from the gobs documentation
        class Point {
            public int X;
            public int Y;
        }
        assertArrayEquals(DatatypeConverter.parseHexBinary("010105506f696e7401ff8200010201015801040001015901040000"),
                new StructType(Point.class).encode());

    }

}