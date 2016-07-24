package xyz.vanduuren.jgobs.types.composite;

import org.junit.Before;
import org.junit.Test;
import xyz.vanduuren.jgobs.lib.Encoder;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
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

    private Encoder encoder;
    private ByteArrayOutputStream outputStream;

    @Before
    public void clearEncoder() {
        outputStream = new ByteArrayOutputStream();
        encoder = new Encoder(true, outputStream);
    }

    @Test
    public void encode() throws Exception {
        Map<FieldType, String> testValues = new HashMap<>();

        testValues.put(new FieldType(encoder, new AbstractMap.SimpleEntry<>("X", 2)),
                "010158010400");
        testValues.put(new FieldType(encoder, new AbstractMap.SimpleEntry<>("Y", 2)),
                "010159010400");


        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = testEntry.getKey().encode();
                    assertArrayEquals(DatatypeConverter.parseHexBinary(testEntry.getValue()), retValue);
                });
    }

}