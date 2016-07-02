package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobSignedInteger here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobSignedInteger extends GobType<Long> {

    public static final int ID = 2;

    public GobSignedInteger(Long value) {
        super(value);
    }

    public GobSignedInteger(int value) {
        super(Long.valueOf(value));
    }

    @Override
    public Long decode() {
        return null;
    }

    /**
     * Encode a long value as a signed integer (max 64 bits)
     * @return A gob encoded byte array representing the value
     */
    @Override
    public byte[] encode() {
        long encoded;
        // if unEncodedData < 0 then complement, shift left and set LSB to 1
        if (unEncodedData < 0) {
            encoded = (~unEncodedData << 1) | 1;
        } else {
            encoded =  unEncodedData << 1;
        }

        return new GobUnsignedInteger(encoded).encode();
    }

    @Override
    public int getID() {
        return ID;
    }
}
