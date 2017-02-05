package xyz.vanduuren.jgobs.lib;

import java.util.Arrays;

/**
 * Give a description of ByteArrayUtilities here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public final class ByteArrayUtilities {

    public static final byte[] nullByteArray = new byte[]{0};
    public static final byte[] oneByteArray = new byte[]{1};
    // private constructor so this class cannot be instantiated
    rivate ByteArrayUtilities() {}

    /**
     * Concatenate multiple byte arrays
     * @param arrays The arrays to concatenate
     * @return A single array with all the arrays concatenated in the order of the args.
     */
    public final static byte[] concat(byte[]... arrays) {
        if (arrays.length < 2) {
            throw new IllegalArgumentException("ByteArrayUtilities.concat() requires more than "
                    + "one argument.");
        }

        byte[] result = new byte[]{};

        for (int i = 0; i < arrays.length; i++) {
            byte[] placeholder = Arrays.copyOf(result, result.length + arrays[i].length);
            System.arraycopy(arrays[i], 0, placeholder, result.length, arrays[i].length);
            result = placeholder;
        }

        return result;
    }

    /**
     * Does this byte array contain an element that isn't 0x00?
     * @param byteArray The byte array to check
     * @return Do we have a byte that doesn't equal 0x00?
     */
    public static boolean isNonNull(byte[] byteArray) {
        boolean nonNull = false;

        for (byte b: byteArray) {
            if ((int) b != 0) {
                nonNull = true;
                break;
            }
        }

        return nonNull;
    }

    /**
     * Convert a long to a byte array
     * @param value The value to be converted
     * @param amountOfBytes The amount of bytes we need to encode the long
     * @return The converted value as byte array
     */
    public static byte[] longToByteArray(long value, int amountOfBytes) {
        byte[] result = new byte[amountOfBytes];

        for (int i = 0; i < amountOfBytes; i++) {
            result[i] = (byte)(value >> (amountOfBytes-i-1)*8);
        }

        return result;
    }

}
