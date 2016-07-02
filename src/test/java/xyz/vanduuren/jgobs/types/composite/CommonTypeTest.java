package xyz.vanduuren.jgobs.types.composite;

import org.junit.Test;
import xyz.vanduuren.jgobs.types.composite.CommonType;

import javax.xml.bind.DatatypeConverter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test the encoding and decoding for CommonType.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class CommonTypeTest {
    @Test
    public void encode() throws Exception {
        Map<CommonType, String> testValues = new HashMap<>();

        testValues.put(new CommonType(new AbstractMap.SimpleEntry<>("Point", 65)),
                "0105506f696e7401ff8200");
        testValues.put(new CommonType(new AbstractMap.SimpleEntry<>("Baz", 65)),
                "010342617a01ff8200");
        testValues.put(new CommonType(new AbstractMap.SimpleEntry<>("Foo", 66)),
                "0103466f6f01ff8400");


        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = testEntry.getKey().encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}