package xyz.vanduuren.jgobs.types;

import java.lang.reflect.Array;

/**
 * Give a description of SliceType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class SliceType extends GobCompositeType<Array> {

    public SliceType(Array array) {
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
