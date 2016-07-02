package xyz.vanduuren.jgobs.types.primitive;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.types.GobType;

import java.io.UnsupportedEncodingException;

/**
 * Give a description of GobString here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class GobString extends GobType<String> {

    public final static int ID = 6;

    public GobString(String value) {
        super(value);
    }

    @Override
    public String decode() {
        return null;
    }

    /**
     * Encode a string to gob
     * @return A gob encoded byte array representing a string
     */
    @Override
    public byte[] encode() {
        // Get our string as UTF-8 encoded byte array
        try {
            byte[] unEncodedDataUTF8Encoded = unEncodedData.getBytes("UTF-8");
            byte[] encodedString;
            byte[] encodedStringSize;

            // Get the length of the UTF-8 encoded array, getting the length
            // of the GobString argument will count the runes, not the amount of bytes
            // needed to encode the string
            encodedStringSize = new GobUnsignedInteger(unEncodedDataUTF8Encoded.length).encode();
            // Encode the amount of bytes needed to encode the string, and the string itself
            encodedString = ByteArrayUtilities.concat(encodedStringSize, unEncodedDataUTF8Encoded);

            return encodedString;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is unsupported.");
        }
    }
}
