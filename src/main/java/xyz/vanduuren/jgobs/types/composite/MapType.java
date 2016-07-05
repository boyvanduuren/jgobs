package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;

import java.util.Map;

/**
 * Give a description of MapType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class MapType extends GobCompositeType<Map> {

    public static final int ID = 23;

    public MapType(Encoder encoder, Map map) {
        super(encoder, map);
    }

    @Override
    public Map decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return null;
    }

}
