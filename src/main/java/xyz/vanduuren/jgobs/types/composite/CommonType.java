package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.primitive.GobSignedInteger;
import xyz.vanduuren.jgobs.types.primitive.GobString;

import java.util.AbstractMap;

import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.nullByteArray;
import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.oneByteArray;

/**
 * Give a description of CommonType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public class CommonType extends GobType<AbstractMap.SimpleEntry<String, Integer>> {

    public final static int ID = 18;

    public CommonType(AbstractMap.SimpleEntry<String, Integer> nameAndId) {
        super(nameAndId);
    }

    @Override
    public AbstractMap.SimpleEntry<String, Integer> decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        byte[] encodedCommonType;
        byte[] encodedName = new GobString(unEncodedData.getKey()).encode();
        byte[] encodedId = new GobSignedInteger(unEncodedData.getValue()).encode();

        encodedCommonType = ByteArrayUtilities.concat(oneByteArray, encodedName,
                oneByteArray, encodedId, nullByteArray);

        return encodedCommonType;
    }

    @Override
    public int getID() {
        return ID;
    }

}
