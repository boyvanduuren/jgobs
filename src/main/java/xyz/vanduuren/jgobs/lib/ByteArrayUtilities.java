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

    /**
     * Concatenate two byte arrays
     * @param a The first byte array
     * @param b The second byte array
     * @return The concatenated byte array
     */
    public final static byte[] concat(byte[] a, byte[] b) {
        byte[] result = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}
