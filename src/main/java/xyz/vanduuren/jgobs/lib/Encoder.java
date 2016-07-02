package xyz.vanduuren.jgobs.lib;

import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.primitive.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Give a description of Encoder here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class Encoder {

    public static final Map<Class<?>, Class<? extends GobType>> supportedTypes;
    static {
        Map<Class<?>, Class<? extends GobType>> tempMap = new HashMap<>();
        // booleans
        tempMap.put(Boolean.class, GobBoolean.class);
        tempMap.put(Boolean.TYPE, GobBoolean.class);
        // signed integers
        tempMap.put(Short.class, GobSignedInteger.class);
        tempMap.put(Short.TYPE, GobSignedInteger.class);
        tempMap.put(Integer.class, GobSignedInteger.class);
        tempMap.put(Integer.TYPE, GobSignedInteger.class);
        tempMap.put(Long.class, GobSignedInteger.class);
        tempMap.put(Long.TYPE, GobSignedInteger.class);
        // todo: need to think of a way to handle unsigned ints
        // floating point
        tempMap.put(Float.class, GobFloatingPoint.class);
        tempMap.put(Float.TYPE, GobFloatingPoint.class);
        tempMap.put(Double.class, GobFloatingPoint.class);
        tempMap.put(Double.TYPE, GobFloatingPoint.class);
        // byte array
        tempMap.put(byte[].class, GobByteArray.class);
        // string
        tempMap.put(String.class, GobString.class);
        // todo: support complex?
        // todo: support interfaces?

        supportedTypes = Collections.unmodifiableMap(tempMap);
    }

}
