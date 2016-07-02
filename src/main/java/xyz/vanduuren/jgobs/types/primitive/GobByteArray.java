package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobByteArray here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobByteArray extends GobType<byte[]> {

    public final static int ID = 5;

    public GobByteArray(byte[] value) {
        this.unEncodedData = value;
    }

    @Override
    public byte[] decode() {
        return new byte[0];
    }

    /**
     * Encode a byte array
     * @return A gob encoded byte array representing the value
     */
    @Override
    public byte[] encode() {
        return ByteArrayUtilities.concat(new GobUnsignedInteger(unEncodedData.length).encode(), unEncodedData);
    }
}
