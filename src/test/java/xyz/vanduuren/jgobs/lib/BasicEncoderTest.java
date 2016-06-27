package xyz.vanduuren.jgobs.lib;

import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertArrayEquals;

/**
 * Various tests for the encoder
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-22
 */
public class BasicEncoderTest {

    private static byte[] stringToByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    @Test
    public void testBooleanEncoding() {
        assertArrayEquals(stringToByteArray("00"), BasicEncoder.encodeBoolean(false));
        assertArrayEquals(stringToByteArray("01"), BasicEncoder.encodeBoolean(true));
    }

    @Test
    public void testByteArrayEncoding() {
        Map<byte[], String> testValues = new HashMap<>();

        testValues.put(new byte[] { 1, 2, 3 }, "03010203");
        testValues.put(new byte[] { (byte)222, (byte)173, (byte)190, (byte)239 },
                "04deadbeef");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    retValue = BasicEncoder.encodeByteArray(testEntry.getKey());
                    assertArrayEquals(stringToByteArray(testEntry.getValue()), retValue);
                });
    }

    @Test
    public void testFloatingPointNumberEncoding() {
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
                    retValue = BasicEncoder.encodeFloatingPointNumber(testEntry.getKey());
                    assertArrayEquals(stringToByteArray(testEntry.getValue()), retValue);
                });
    }

    @Test
    public void testSignedIntegerEncoding() {
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
                    retValue = BasicEncoder.encodeSignedInteger(testEntry.getKey());
                    assertArrayEquals(stringToByteArray(testEntry.getValue()), retValue);
                });
    }

    @Test
    public void testStringEncoding() {
        Map<String, String> testValues = new HashMap<>();

        testValues.put("foo", "03666f6f");
        testValues.put("Hello, 世界", "0d48656c6c6f2c20e4b896e7958c");

        testValues.entrySet().stream()
                .forEach(testEntry -> {
                    byte[] retValue = new byte[0];
                    try {
                        retValue = BasicEncoder.encodeString(testEntry.getKey());
                    } catch (UnsupportedEncodingException e) {
                        fail(e.getMessage());
                    }
                    assertArrayEquals(stringToByteArray(testEntry.getValue()), retValue);
                });
    }

    @Test
    public void testUnsignedIntegerEncoding() {
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
                    retValue = BasicEncoder.encodeUnsignedInteger(testEntry.getKey());
                    assertArrayEquals(stringToByteArray(testEntry.getValue()), retValue);
                });
    }
}