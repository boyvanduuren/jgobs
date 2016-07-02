package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobBooleanTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobBooleanTest {
    @Test
    public void encode() throws Exception {
        assertArrayEquals(DatatypeConverter.parseHexBinary("00"), new GobBoolean(false).encode());
        assertArrayEquals(DatatypeConverter.parseHexBinary("01"), new GobBoolean(true).encode());
    }

}