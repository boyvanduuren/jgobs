package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobUnsignedInteger here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobUnsignedInteger extends GobType<Long> {

    public static final int ID = 3;

    public GobUnsignedInteger(Long value) {
        super(value);
    }

    public GobUnsignedInteger(int value) {
        super(Long.valueOf(value));
    }

    @Override
    public Long decode() {
        return null;
    }

    /**
     * Encode an unsigned integer (max 64 bits) to gob
     * @return A gob encoded byte array representing the value
     */
    @Override
    public byte[] encode() {
        byte[] encodedUnsignedInteger;
        // If value < 128 return it as a byte value
        if (Long.compareUnsigned(unEncodedData, 128) < 0) {
            encodedUnsignedInteger = new byte[]{(byte)unEncodedData.longValue()};
        } else {
            // Start encoding the value if it's >= 128
            // Calculate the amount of bytes
            byte amountOfBytes = (byte)((int)Math.ceil(Long.toBinaryString(unEncodedData).length()/(double)8));
            // Create the encoded result, first byte is the amount of bytes, negated
            encodedUnsignedInteger = ByteArrayUtilities.concat(
                    new byte[]{(byte)(amountOfBytes-1^255)},
                    ByteArrayUtilities.longToByteArray(unEncodedData, amountOfBytes));
        }

        return encodedUnsignedInteger;
    }
}
