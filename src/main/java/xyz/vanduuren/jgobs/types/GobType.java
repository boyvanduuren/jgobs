package xyz.vanduuren.jgobs.types;

import xyz.vanduuren.jgobs.types.composite.WireType;

/**
 * The abstract class that the composite gob types like
 * WireType, ArrayType, SliceType, etc. extend.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public abstract class GobType<T> {

    protected byte[] encodedData;
    protected T unEncodedData;

    // in case we're dealing with something like a byte[], use empty constructor
    public GobType() {}

    public GobType(T t) {
        if (t instanceof WireType) {
            throw new IllegalArgumentException("Cannot pass WireType as parameter.");
        }
        this.unEncodedData = t;
    }

    public GobType(byte[] encodedData) {
        this.encodedData = encodedData;
    }

    public abstract T decode();
    public abstract byte[] encode();
    public abstract int getID();

}
