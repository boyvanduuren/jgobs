package xyz.vanduuren.jgobs.types;

/**
 * Give a description of WireType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class WireType extends GobCompositeType<GobCompositeType> {

    public WireType(GobCompositeType gob) {
        super(gob);
    }

    @Override
    public GobCompositeType decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return unEncodedData.encode();
    }

}
