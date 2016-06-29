package xyz.vanduuren.jgobs.lib;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Encoder encodes a Java object to Go's gob encoding
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-22
 */
public class BasicEncoder {

    /**
     * Concatenate two byte arrays
     * @param a The first byte array
     * @param b The second byte array
     * @return The concatenated byte array
     */
    private static byte[] concatByteArrays(byte[] a, byte[] b) {
        byte[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     * Convert a long to a byte array
     * @param value The value to be converted
     * @param amountOfBytes The amount of bytes we need to encode the long
     * @return The converted value as byte array
     */
    private static byte[] longToByteArray(long value, int amountOfBytes) {
        byte[] result = new byte[amountOfBytes];

        for (int i = 0; i < amountOfBytes; i++) {
            result[i] = (byte)(value >> (amountOfBytes-i-1)*8);
        }

        return result;
    }

    /**
     * Encode a boolean
     * @param value The value to encode
     * @return A gob encoded byte array representing the value
     */
    public static byte[] encodeBoolean(boolean value) {
        if (value) {
            return encodeUnsignedInteger(1);
        } else {
            return encodeUnsignedInteger(0);
        }
    }

    /**
     * Encode a byte array
     * @param value The byte array prepended with the byte size as unsigned int
     * @return A gob encoded byte array representing the value
     */
    public static byte[] encodeByteArray(byte[] value) {
        return concatByteArrays(encodeUnsignedInteger(value.length), value);
    }

    /**
     * Encode a floating point number to a byte array
     * @param value The value (max 64 bits) to encode
     * @return A gob encoded byte array representing the value
     */
    public static byte[] encodeFloatingPointNumber(double value) {
        byte[] encodedFloatingPointNumber;

        Long reversed = Long.reverseBytes(Double.doubleToRawLongBits(value));
        encodedFloatingPointNumber = encodeUnsignedInteger(reversed);

        return encodedFloatingPointNumber;
    }

    /**
     * Encode a long value as a signed integer (max 64 bits)
     * @param value The value to encode
     * @return A gob encoded byte array representing the value
     */
    public static byte[] encodeSignedInteger(long value) {
        long encoded;
        // if value < 0 then complement, shift left and set LSB to 1
        if (value < 0) {
            encoded = (~value << 1) | 1;
        } else {
            encoded =  value << 1;
        }

        return encodeUnsignedInteger(encoded);
    }

    /**
     * Encode a string to gob
     * @param value The value to encode
     * @return A gob encoded byte array representing a string
     */
    public static byte[] encodeString(String value) throws UnsupportedEncodingException {
        // Get our string as UTF-8 encoded byte array
        byte[] valueUTF8Encoded = value.getBytes("UTF-8");
        byte[] encodedString;
        byte[] encodedStringSize;

        // Get the length of the UTF-8 encoded array, getting the length
        // of the String argument will count the runes, not the amount of bytes
        // needed to encode the string
        encodedStringSize = encodeUnsignedInteger(valueUTF8Encoded.length);
        // Encode the amount of bytes needed to encode the string, and the string itself
        encodedString = concatByteArrays(encodedStringSize, valueUTF8Encoded);

        return encodedString;
    }

    /**
     * Encode an unsigned integer (max 64 bits) to gob
     * @param value The value to encode
     * @return A gob encoded byte array representing the value
     */
    public static byte[] encodeUnsignedInteger(long value) {
        byte[] encodedUnsignedInteger;
        // If value < 128 return it as a byte value
        if (Long.compareUnsigned(value, 128) < 0) {
            encodedUnsignedInteger = new byte[]{(byte)value};
        } else {
            // Start encoding the value if it's >= 128
            // Calculate the amount of bytes
            byte amountOfBytes = (byte)((int)Math.ceil(Long.toBinaryString(value).length()/(double)8));
            // Create the encoded result, first byte is the amount of bytes, negated
            encodedUnsignedInteger = concatByteArrays(new byte[]{(byte)(amountOfBytes-1^255)},
                    longToByteArray(value, amountOfBytes));
        }

        return encodedUnsignedInteger;
    }

}