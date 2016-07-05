package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;

import java.lang.reflect.Array;

/**
 * Give a description of ArrayType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class ArrayType extends GobCompositeType<Array> {

    public final static int ID = 17;

    public ArrayType(Encoder encoder, Array array) {
        super(encoder, array);
    }

    @Override
    public Array decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return null;
    }

}
