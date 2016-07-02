package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.types.GobType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Give a description of StrucType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class StructType extends GobType<Object> {

    public final static int ID = 20;

    private final Map<Class<?>, Integer> supportedTypes = new HashMap<>();

    public StructType(Object object) {
        super(object);

        // booleans
        supportedTypes.put(Boolean.class, 1);
        supportedTypes.put(Boolean.TYPE, 1);

        // signed integers
        supportedTypes.put(Integer.class, 2);
        supportedTypes.put(Integer.TYPE, 2);

        // todo: need to think of a way to handle unsigned ints

        // floating point
        supportedTypes.put(Float.class, 4);
        supportedTypes.put(Float.TYPE, 4);
        supportedTypes.put(Double.class, 4);
        supportedTypes.put(Double.TYPE, 4);

        // byte array
        supportedTypes.put(byte[].class, 5);

        // string
        supportedTypes.put(String.class, 6);

        // todo: support complex?
        // todo: support interfaces?
    }

    private byte[] encodeField(Field field) {
        byte[] encodedField = null;
        if (supportedTypes.containsKey(field.getType())) {
            encodedField = new byte[]{};
        }
        return encodedField;
    }

    @Override
    public Object decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        int typeId;
        Field[] classFields = unEncodedData.getClass().getDeclaredFields();
        for (Field field: classFields) {
            if (Modifier.isPublic(field.getModifiers())) {
                encodeField(field);
            }
        }

        return new byte[]{};
    }

}
