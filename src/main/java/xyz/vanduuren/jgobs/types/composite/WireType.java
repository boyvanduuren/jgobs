package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of WireType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class WireType extends GobCompositeType<GobType> {

    private Encoder encoder;
    public final static int ID = 16;

    public WireType(Encoder encoder, GobType gob) {
        super(encoder, gob);
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
