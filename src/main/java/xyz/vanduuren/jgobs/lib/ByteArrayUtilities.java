package xyz.vanduuren.jgobs.lib;

import java.util.Arrays;

/**
 * Give a description of ByteArrayUtilities here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-29
 */
public final class ByteArrayUtilities {

    // private constructor so this class cannot be instantiated
    private ByteArrayUtilities() {}

    public static final byte[] nullByteArray = new byte[]{0};
    public static final byte[] oneByteArray = new byte[]{1};

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

}
