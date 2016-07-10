package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobFloatingPoint here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobFloatingPoint extends GobType<Double> {

    public final static int ID = 4;

    public GobFloatingPoint(Double value) {
        super(value);
    }

    @Override
    public Double decode() {
        return null;
    }

    /**
     * Encode a floating point number to a byte array
     * @return A gob encoded byte array representing the value
     */
    @Override
    public byte[] encode() {
        byte[] encodedFloatingPointNumber;

        Long reversed = Long.reverseBytes(Double.doubleToRawLongBits(unencodedData));
        encodedFloatingPointNumber = new GobUnsignedInteger(reversed).encode();

        return encodedFloatingPointNumber;
    }
}
