package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.primitive.GobSignedInteger;
import xyz.vanduuren.jgobs.types.primitive.GobString;

import java.util.AbstractMap;

import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.nullByteArray;
import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.oneByteArray;

/**
 * Give a description of FieldType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class FieldType extends GobCompositeType<AbstractMap.SimpleEntry<String, Integer>> {

    public final static int ID = 21;

    public FieldType(Encoder encoder, AbstractMap.SimpleEntry<String, Integer> nameAndType) {
        super(encoder, nameAndType);
    }

    @Override
    public AbstractMap.SimpleEntry<String, Integer> decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        byte[] encodedCommonType;
        byte[] encodedName = new GobString(unencodedData.getKey()).encode();
        byte[] encodedId = new GobSignedInteger(unencodedData.getValue()).encode();

        encodedCommonType = ByteArrayUtilities.concat(oneByteArray, encodedName,
                oneByteArray, encodedId, nullByteArray);

        return encodedCommonType;
    }

}
