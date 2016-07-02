package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobBoolean here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobBoolean extends GobType<Boolean> {

    public static final int ID = 1;

    public GobBoolean(boolean value) {
        super(value);
    }

    @Override
    public Boolean decode() {
        return null;
    }

    /**
     * Encode a boolean
     * @return A gob encoded byte array representing the value
     */
    @Override
    public byte[] encode() {
        int value;
        if (unEncodedData) {
            value = 1;
        } else {
            value = 0;
        }

        return new GobUnsignedInteger(value).encode();
    }

}
