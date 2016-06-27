package xyz.vanduuren.jgobs.types;

/**
 * The abstract class that the composite gob types like
 * WireType, ArrayType, SliceType, etc. extend.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public abstract class GobCompositeType<T> {

    protected byte[] encodedData;
    protected T unEncodedData;

    public GobCompositeType(T t) {
        this.unEncodedData = t;
    }

    public GobCompositeType(byte[] encodedData) {
        this.encodedData = encodedData;
    }

    public abstract T decode();

    public abstract byte[] encode();

}
