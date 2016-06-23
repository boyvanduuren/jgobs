package xyz.vanduuren.jgobs.lib;

/**
 * Encoder encodes a Java object to Go's gob encoding
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-22
 */
public class Encoder {

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
            // First, we'll need the amount of bytes needed to represent this value
            byte amountOfBytes = (byte)((int)Math.ceil(Long.toBinaryString(value).length()/(double)8));
            // Create a minimal length byte array
            encodedUnsignedInteger = new byte[amountOfBytes+1];

            // Start with the size of the integer in bytes, negated
            encodedUnsignedInteger[0] = (byte)(amountOfBytes-1^255);
            for (int i = 0; i < amountOfBytes; i++) {
                // Add the integer values to our byte array, big-endian
                encodedUnsignedInteger[i+1] = (byte)(value >> (amountOfBytes-i-1)*8);
            }
        }

        return encodedUnsignedInteger;
    }

}
