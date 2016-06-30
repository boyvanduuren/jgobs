package xyz.vanduuren.jgobs.types;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of FieldTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-30
 */
public class FieldTypeTest {
    @Test
    public void encode() throws Exception {
        Map<FieldType, String> testValues = new HashMap<>();

        testValues.put(new FieldType(new AbstractMap.SimpleEntry<>("X", 2)),
                "010158010400");
        testValues.put(new FieldType(new AbstractMap.SimpleEntry<>("Y", 2)),
                "010159010400");


        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = testEntry.getKey().encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}