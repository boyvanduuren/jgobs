package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.types.GobType;

import java.util.Map;

/**
 * Give a description of MapType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class MapType extends GobType<Map> {

    public static final int ID = 23;

    public MapType(Map map) {
        super(map);
    }

    @Override
    public Map decode() {
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
