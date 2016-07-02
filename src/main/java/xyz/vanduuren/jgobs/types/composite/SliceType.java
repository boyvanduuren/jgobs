package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.types.GobType;

import java.lang.reflect.Array;

/**
 * Give a description of SliceType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class SliceType extends GobType<Array> {

    public final static int ID = 19;

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

    @Override
    public int getID() {
        return ID;
    }

}
