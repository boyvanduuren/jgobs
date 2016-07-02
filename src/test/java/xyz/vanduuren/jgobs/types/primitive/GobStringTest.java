package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobStringTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobStringTest {
    @Test
    public void encode() throws Exception {
        Map<String, String> testValues = new HashMap<>();

        testValues.put("foo", "03666f6f");
        testValues.put("Hello, 世界", "0d48656c6c6f2c20e4b896e7958c");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    try {
                        retValue = new GobString(testEntry.getKey()).encode();
                    } catch (RuntimeException e) {
                        fail(e.getMessage());
                    }
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}