package xyz.vanduuren.jgobs.lib;

import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.composite.WireType;
import xyz.vanduuren.jgobs.types.primitive.*;

import java.io.IOException;
import java.io.OutputStream;
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

    public final boolean autoRegister;
    public final LinkedHashMap<Class<?>, Integer> registeredTypeIDs = new LinkedHashMap<>();
    public final Map<Integer, WireType> registeredWireTypes = new HashMap<>();
    private int firstFreeID = 65;
    private OutputStream outputStream;

    /**
     * Construct a new encoder.
     * @param autoRegister Setting this boolean to true results in nested public fields of an unknown type to be
     *                     automatically registered with the encoder. Setting this to false means fields with an
     *                     unknown type will be skipped when encoding them.
     * @param outputStream The stream used to capture output.
     */
    public Encoder(boolean autoRegister, OutputStream outputStream) {
        this.autoRegister = autoRegister;
        this.outputStream = outputStream;

        // Add the ID's of the default supported types to opur registeredTypeIDs map
        supportedTypes.entrySet().stream()
                .map(entry -> entry.getValue())
                .distinct()
                .forEach(supportedType -> {
                    try {
                        registeredTypeIDs.put(supportedType, supportedType.getField("ID").getInt(null));
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Error while getting ID field of " + supportedType.getSimpleName()
                                + ".");
                    }
                });
    }

    /**
     * Encode an object as a gob.
     * Only simple (primitive fields only) struct types are supported for now.
     *
     * @param object The object to encode
     * @return A gob encoded byte array representing the object
     */
    public void encode(Object object) throws IOException {
        byte[] result = null;
        WireType wireType;
        boolean newType = false;

        registerType(object.getClass());

        wireType = getWireTypeByType(object.getClass());
        try {
            result = wireType.encapsulatedType.encode(object);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Ran into an error while encoding " + object.getClass().getSimpleName());
        }

        outputStream.write(result);
    }

    public int getTypeID(Class<?> clazz) {
        if (supportedTypes.get(clazz) != null) {
            clazz = supportedTypes.get(clazz);
        }

        return registeredTypeIDs.containsKey(clazz) ?
                registeredTypeIDs.get(clazz) :
                -1;
    }

    /**
     * When a particular type has been registered with the encoder we usually want to
     * interact with its WireType to encode/decode data, this method fetches the WireType.
     * @param type The type
     * @return The WireType
     */
    public WireType getWireTypeByType(Class<?> type) {
        WireType wireType = null;
        Integer registeredID = registeredTypeIDs.get(type);
        if (registeredID != null) {
            wireType = registeredWireTypes.get(registeredID);
        }

        return wireType;
    }

    /**
     * Register a class with the encoder.
     * If we have already registered this class, just return its ID.
     *
     * @param classToRegister The class to register
     * @return The ID of the class
     */
    public int registerType(Class<?> classToRegister) {
        if (!registeredTypeIDs.containsKey(classToRegister) && !supportedTypes.containsKey(classToRegister)) {
            int currentID = firstFreeID++;
            WireType wireType = new WireType(this, classToRegister);
            registeredTypeIDs.put(classToRegister, currentID);
            registeredWireTypes.put(currentID, wireType);
            wireType.encode();
            return currentID;
        } else {
            return getTypeID(classToRegister);
        }
    }

    /**
     * Write a byte array to the encoder's output stream.
     * @param output The byte array to write.
     */
    public void writeToOutputStream(byte[] output) {
        try {
            outputStream.write(output);
        } catch (IOException e) {
            throw new RuntimeException("Could not write to encoder's output stream.");
        }
    }

}
