package xyz.vanduuren.jgobs.types;

import java.util.Map;

/**
 * Give a description of MapType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class MapType extends GobCompositeType<Map> {

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

}
