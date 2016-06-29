package xyz.vanduuren.jgobs.types;

import java.util.AbstractMap;

/**
 * Give a description of CommonType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class CommonType extends GobCompositeType<AbstractMap.SimpleEntry<String, Integer>> {

    private String name;
    private int id;

    public CommonType(AbstractMap.SimpleEntry<String, Integer> nameAndId) {
        super(nameAndId);
    }

    @Override
    public AbstractMap.SimpleEntry<String, Integer> decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

}
