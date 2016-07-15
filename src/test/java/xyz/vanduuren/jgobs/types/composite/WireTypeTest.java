package xyz.vanduuren.jgobs.types.composite;

import org.junit.Before;
import org.junit.Test;
import xyz.vanduuren.jgobs.lib.Encoder;

import javax.xml.bind.DatatypeConverter;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of WireTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-05
 */
public class WireTypeTest {

    private Encoder encoder;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        encoder = new Encoder(outputStream);
    }

    @Test
    public void encode() throws Exception {
        /*
        type Point struct {
            X, Y int
        }
         */

        @SuppressWarnings("unused")
        class Point {
            public int X;
            public int Y;
        }

        String gobsEncodedWireType = "1fff8103010105506f696e7401ff820001020101580104000101590104000000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedWireType),
                new WireType(encoder, Point.class).encode());
    }

}