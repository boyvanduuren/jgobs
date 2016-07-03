package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.GobType;
import xyz.vanduuren.jgobs.types.primitive.GobUnsignedInteger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;

import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.nullByteArray;
import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.oneByteArray;

/**
 * Give a description of StrucType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class StructType extends GobType<Class<?>> {

    public final static int ID = 20;

    public StructType(Class value) {
        super(value);
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
        encodedField = new FieldType(new AbstractMap.SimpleEntry<>(fieldName, fieldID)).encode();

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
        final int classID = Encoder.registerType(unEncodedData);

        // Construct StructType.commonType with the class name and ID
        CommonType commonType = new CommonType(new AbstractMap.SimpleEntry<>(className, classID));
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

        return ByteArrayUtilities.concat(encodedStruct, nullByteArray);
    }

}
