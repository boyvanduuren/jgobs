package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobByteArrayTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobByteArrayTest {
    @Test
    public void encode() throws Exception {
        Map<byte[], String> testValues = new HashMap<>();

        testValues.put(new byte[] { 1, 2, 3 }, "03010203");
        testValues.put(new byte[] { (byte)222, (byte)173, (byte)190, (byte)239 },
                "04deadbeef");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    retValue = new GobByteArray(testEntry.getKey()).encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}