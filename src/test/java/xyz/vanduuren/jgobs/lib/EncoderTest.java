package xyz.vanduuren.jgobs.lib;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Method;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

/**
 * Give a description of EncoderTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-22
 */
public class EncoderTest {

    private static Method encodeUnsignedInteger;

    private static byte[] stringToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    @BeforeClass
    public static void setupMethods() {
        try {
            encodeUnsignedInteger =
                    Encoder.class.getDeclaredMethod("encodeUnsignedInteger", Long.TYPE);
            encodeUnsignedInteger.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testUnsignedIntegerEncoding() throws Exception {
        if (encodeUnsignedInteger != null) {
            byte[] retValue;

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 256));
            assertArrayEquals(stringToByteArray("fe0100"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 54738378));
            assertArrayEquals(stringToByteArray("fc03433dca"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 8002336222354889572L));
            assertArrayEquals(stringToByteArray("f86f0e026562b45b64"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 634035792414663034L));
            assertArrayEquals(stringToByteArray("f808cc8c31a5c2617a"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 9));
            assertArrayEquals(stringToByteArray("09"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 127));
            assertArrayEquals(stringToByteArray("7f"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 128));
            assertArrayEquals(stringToByteArray("ff80"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 0));
            assertArrayEquals(stringToByteArray("00"), retValue);

            retValue = (byte[])(encodeUnsignedInteger.invoke(Encoder.class, 0xffffffffffffffffL));
            assertArrayEquals(stringToByteArray("f8ffffffffffffffff"), retValue);
        } else {
            fail("Error while getting Encoder.encodeUnsignedInteger using reflection.");
        }
    }

}