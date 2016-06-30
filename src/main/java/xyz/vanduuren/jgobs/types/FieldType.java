package xyz.vanduuren.jgobs.types;

import xyz.vanduuren.jgobs.lib.BasicEncoder;
import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;

import java.io.UnsupportedEncodingException;
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

    public FieldType(AbstractMap.SimpleEntry<String, Integer> nameAndType) {
        super(nameAndType);
    }

    @Override
    public AbstractMap.SimpleEntry<String, Integer> decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        try {
            byte[] encodedCommonType;
            byte[] encodedName = BasicEncoder.encodeString(unEncodedData.getKey());
            byte[] encodedId = BasicEncoder.encodeSignedInteger(unEncodedData.getValue());

            encodedCommonType = ByteArrayUtilities.concat(oneByteArray, encodedName,
                    oneByteArray, encodedId, nullByteArray);

            return encodedCommonType;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while encoding " + this.getClass().getName()
                    + ", couldn't encode name using UTF-8.");
        }
    }

}
