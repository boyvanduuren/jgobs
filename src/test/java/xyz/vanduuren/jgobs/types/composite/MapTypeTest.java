package xyz.vanduuren.jgobs.types.composite;

import org.junit.Before;
import org.junit.Test;
import xyz.vanduuren.jgobs.lib.Encoder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * Give a description of MapTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-26
 */
public class MapTypeTest {

    private Encoder encoder;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        encoder = new Encoder(true, outputStream);
    }

    @Test
    public void testConstructor() throws Exception {
        MapType mapType;

        // String has ID 6, Float has ID 4
        HashMap<String, Double> stringDoubleMap = new HashMap<>();
        stringDoubleMap.put("foo", 123.456);
        mapType = new MapType(encoder, stringDoubleMap);
        assertEquals(6, mapType.keyID);
        assertEquals(4, mapType.elemID);

        // Person will be assigned ID 65, Integer has ID 2
        class Person {
            public String name;
        }
        HashMap<Person, Integer> personIntMap = new HashMap<>();
        personIntMap.put(new Person(), 1);
        mapType = new MapType(encoder, personIntMap);
        assertEquals(65, mapType.keyID);
        assertEquals(2, mapType.elemID);
    }

}