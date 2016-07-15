package xyz.vanduuren.jgobs.lib;

import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.composite.WireType;
import xyz.vanduuren.jgobs.types.primitive.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Give a description of Encoder here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-02
 */
public class Encoder {

    public static final LinkedHashMap<Class<?>, Integer> registeredTypes = new LinkedHashMap<>();
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

    private int firstFreeID = 65;
    private OutputStream outputStream;

    public Encoder(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Encode an object as a gob.
     * Only simple (primitive fields only) struct types are supported for now.
     * @param object The object to encode
     * @return A gob encoded byte array representing the object
     */
    public void encode(Object object) throws IOException {
        byte[] result = null;
        WireType wireType;
        boolean newType = false;

        if (!registeredTypes.containsKey(object.getClass())) {
            newType = true;
        }

        wireType = new WireType(this, object.getClass());
        try {
            if (newType) {
                result = ByteArrayUtilities.concat(wireType.encode(), wireType.encapsulatedType.encodeValue(object));
            } else {
                result = wireType.encapsulatedType.encodeValue(object);
            }
        } catch (IllegalAccessException | NoSuchMethodException
                | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Ran into an error while encoding " + object.getClass().getSimpleName());
        }

        outputStream.write(result);
    }

    /**
     * Register a class with the encoder.
     * If we have already registered this class, just return its ID.
     * @param classToRegister The class to register
     * @return The ID of the class
     */
    public int registerType(Class<?> classToRegister) {
        if (!registeredTypes.containsKey(classToRegister)) {
            registeredTypes.put(classToRegister, firstFreeID);
            return firstFreeID++;
        } else {
            return registeredTypes.get(classToRegister);
        }
    }

}
