package xyz.vanduuren.jgobs.types;

import java.lang.reflect.Array;

/**
 * Give a description of ArrayType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class ArrayType extends GobCompositeType<Array> {

    public ArrayType(Array array) {
        super(array);
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
