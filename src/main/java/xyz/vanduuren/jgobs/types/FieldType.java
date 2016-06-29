package xyz.vanduuren.jgobs.types;

import java.util.AbstractMap;

/**
 * Give a description of FieldType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class FieldType extends GobCompositeType<AbstractMap.SimpleEntry<String, Integer>> {

    public FieldType(AbstractMap.SimpleEntry<String, Integer> nameAndType) {
        super(nameAndType);
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
