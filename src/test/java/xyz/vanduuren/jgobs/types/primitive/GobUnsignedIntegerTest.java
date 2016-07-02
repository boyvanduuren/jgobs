package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobUnsignedIntegerTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobUnsignedIntegerTest {
    @Test
    public void encode() throws Exception {
        Map<Long, String> testValues = new HashMap<>();

        testValues.put(256L, "fe0100");
        testValues.put(54738378L, "fc03433dca");
        testValues.put(8002336222354889572L, "f86f0e026562b45b64");
        testValues.put(634035792414663034L, "f808cc8c31a5c2617a");
        testValues.put(9L, "09");
        testValues.put(127L, "7f");
        testValues.put(128L, "ff80");
        testValues.put(0L, "00");
        testValues.put(0xffffffffffffffffL, "f8ffffffffffffffff");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    retValue = new GobUnsignedInteger(testEntry.getKey()).encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}