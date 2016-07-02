package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobFloatingPointTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobFloatingPointTest {
    @Test
    public void encode() throws Exception {
        Map<Double, String> testValues = new HashMap<>();

        testValues.put(17.0, "fe3140");
        testValues.put(19.12345, "f8f2b0506b9a1f3340");
        testValues.put(9007199254740993.0, "fe4043");
        testValues.put(342985732.12365456, "f8d3a71f048c71b441");
        testValues.put(1/3.0, "f8555555555555d53f");
        testValues.put(Double.MAX_VALUE, "f8ffffffffffffef7f");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    retValue = new GobFloatingPoint(testEntry.getKey()).encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}