package xyz.vanduuren.jgobs.types.primitive;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of GobSignedIntegerTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobSignedIntegerTest {
    @Test
    public void encode() throws Exception {
        Map<Long, String> testValues = new HashMap<>();

        testValues.put(12345L, "fe6072");
        testValues.put(-12345L, "fe6071");
        testValues.put(-129L, "fe0101");
        testValues.put(549287634987L, "fbffc8305056");
        testValues.put(-4593287429872L, "fa085aea87fddf");
        testValues.put(-3459823598723459780L, "f860078676d6d9d587");
        testValues.put(2345098723549807L, "f910a9b5b0c2c4de");
        testValues.put(-9223372036854775808L, "f8ffffffffffffffff");
        testValues.put(9223372036854775807L, "f8fffffffffffffffe");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    retValue = new GobSignedInteger(testEntry.getKey()).encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}