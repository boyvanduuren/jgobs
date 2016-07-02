package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of WireType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class WireType extends GobType<GobType> {

    public final static int ID = 16;

    public WireType(GobType gob) {
        super(gob);
    }

    @Override
    public GobType decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return unEncodedData.encode();
    }

}
