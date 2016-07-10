package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.primitive.GobSignedInteger;
import xyz.vanduuren.jgobs.types.primitive.GobUnsignedInteger;

import java.lang.reflect.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.nullByteArray;
import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.oneByteArray;

/**
 * Give a description of StrucType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class StructType extends GobCompositeType<Class<?>> {

    public final static int ID = 20;
    List<Field> encodableFields = new ArrayList<>();
    private int classID = -1;

    public StructType(Encoder encoder, Class value) {
        super(encoder, value);
    }

    private String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private byte[] encodeField(Field field) throws NoSuchFieldException, IllegalAccessException,
            IllegalArgumentException {
        byte[] encodedField = null;
        // Capitalize the field name, because public fields in Go are capitalized
        final String fieldName = capitalize(field.getName());
        final int fieldID;
        if (Encoder.supportedTypes.containsKey(field.getType())) {
            fieldID = Encoder.supportedTypes.get(field.getType()).getField("ID").getInt(null);
        } else if (Encoder.registeredTypes.containsKey(field.getType())) {
            fieldID = Encoder.registeredTypes.get(field.getType());
        } else {
            throw new IllegalArgumentException("Field " + field.getName() + " with type " + field.getType().getName()
                    + " isn't registered with the encoder.");
        }
        encodedField = new FieldType(encoder, new AbstractMap.SimpleEntry<>(fieldName, fieldID)).encode();
        encodableFields.add(field);

        return encodedField;
    }

    @Override
    public Class decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        byte[] encodedStruct = oneByteArray;
        final String className = unEncodedData.getSimpleName();
        classID = encoder.registerType(unEncodedData);

        // Construct StructType.commonType with the class name and ID
        CommonType commonType = new CommonType(encoder, new AbstractMap.SimpleEntry<>(className, classID));
        // Add encoded commonType to the encoded struct and select the next field
        encodedStruct = ByteArrayUtilities.concat(encodedStruct, commonType.encode(), oneByteArray);

        // Start encoding the structType's fields
        Field[] classFields = unEncodedData.getDeclaredFields();
        byte[] encodedFields = null;
        // Used to keep track of how many fields we need to encode
        int fieldCount = 0;
        for (Field field: classFields) {
            // Only encode public fields
            if (Modifier.isPublic(field.getModifiers())) {
                try {
                    fieldCount++;
                    // Create or concat the encoded field to the encoded fields array
                    if (encodedFields == null) {
                        encodedFields = encodeField(field);
                    } else {
                        encodedFields = ByteArrayUtilities.concat(encodedFields, encodeField(field));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Couldn't access ID field of field");
                }
            }
        }
        // Concat StructType.commonType and StructType.fieldType(s)
        encodedStruct = ByteArrayUtilities.concat(
                encodedStruct, new GobUnsignedInteger(fieldCount).encode(), encodedFields);

        encodedData = encodedStruct;

        return ByteArrayUtilities.concat(encodedStruct, nullByteArray);
    }

    public byte[] encodeValue(Object obj) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if (!unEncodedData.isAssignableFrom(obj.getClass())) {
            throw new IllegalArgumentException("Cannot encode parameter: " + obj.getClass().getName()
                    + " is not assignable from " + unEncodedData.getName() + ".");
        }

        if (classID == -1) {
            this.encode();
        }

        byte[] encodedValue = new GobSignedInteger(classID).encode();

        for (Field f: encodableFields) {
            Object value = f.get(obj);
            Class<? extends GobType> gobType = Encoder.supportedTypes.get(value.getClass());
            Constructor<? extends GobType> constructor = gobType.getConstructor(f.getType());
            Method method = gobType.getMethod("encode");
            encodedValue = ByteArrayUtilities.concat(encodedValue, oneByteArray,
                    (byte[]) method.invoke(constructor.newInstance(value)));
        }

        encodedValue = ByteArrayUtilities.concat(encodedValue, nullByteArray);
        byte[] totalSize = new GobUnsignedInteger(encodedValue.length).encode();

        encodedValue = ByteArrayUtilities.concat(totalSize, encodedValue);

        return encodedValue;
    }

}
