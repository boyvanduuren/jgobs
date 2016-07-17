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
    private int classID = -1;
    private List<Field> encodableFields = new ArrayList<>();

    public StructType(Encoder encoder, Class value) {
        super(encoder, value);
    }

    private String capitalize(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Encode the field of a structType
     * @param field The field to encode
     * @return A byte array representing the encoded field
     */
    private byte[] encodeField(Field field) {
        byte[] encodedField;
        // Capitalize the field name, because public fields in Go are capitalized
        final String fieldName = capitalize(field.getName());
        final int fieldID;
        if (Encoder.supportedTypes.containsKey(field.getType())) {
            try {
                fieldID = Encoder.supportedTypes.get(field.getType()).getField("ID").getInt(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while retrieving encoding information.");
            }
        } else if (encoder.registeredTypeIDs.containsKey(field.getType())) {
            fieldID = encoder.registeredTypeIDs.get(field.getType());
        } else {
            // Encode the new type
            fieldID = encoder.registerType(field.getType());
        }
        encodedField = new FieldType(encoder, new AbstractMap.SimpleEntry<>(fieldName, fieldID)).encode();
        encodableFields.add(field);

        return encodedField;
    }

    private void encodeType() {
        byte[] encodedStruct = oneByteArray;
        final String className = unencodedData.getSimpleName();
        classID = encoder.registerType(unencodedData);

        // Construct StructType.commonType with the class name and ID
        CommonType commonType = new CommonType(encoder, new AbstractMap.SimpleEntry<>(className, classID));
        // Add encoded commonType to the encoded struct and select the next field
        encodedStruct = ByteArrayUtilities.concat(encodedStruct, commonType.encode(), oneByteArray);

        // Start encoding the structType's fields
        Field[] classFields = unencodedData.getDeclaredFields();
        byte[] encodedFields = null;
        // Used to keep track of how many fields we need to encode
        int fieldCount = 0;
        for (Field field: classFields) {
            // Only encode public fields
            if (Modifier.isPublic(field.getModifiers())) {
                fieldCount++;
                // Create or concat the encoded field to the encoded fields array
                if (encodedFields == null) {
                    encodedFields = encodeField(field);
                } else {
                    encodedFields = ByteArrayUtilities.concat(encodedFields, encodeField(field));
                }
            }
        }
        // Concat StructType.commonType and StructType.fieldType(s)
        if (encodedFields != null) {
            encodedStruct = ByteArrayUtilities.concat(
                    encodedStruct, new GobUnsignedInteger(fieldCount).encode(), encodedFields);
        } else {
            throw new IllegalArgumentException("Couldn't encode StructType \"" + unencodedData.getSimpleName()
                    + "\" because it has no public fields.");
        }

        encodedData = ByteArrayUtilities.concat(encodedStruct, nullByteArray);
    }

    @Override
    public Class decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        if (classID == -1) {
            encodeType();
        }

        return encodedData;
    }

    public byte[] encode(Object obj) {
        return encode(obj, false);
    }

    public byte[] encode(Object obj, boolean nested) {
        if (!unencodedData.isAssignableFrom(obj.getClass())) {
            throw new IllegalArgumentException("Cannot encode parameter: " + obj.getClass().getName()
                    + " is not assignable from " + unencodedData.getName() + ".");
        }

        // Encode the class of the object
        this.encode();

        byte[] encodedValue = null;
        if (!nested) {
             encodedValue = new GobSignedInteger(classID).encode();
        }
        int skippedAmountOfFields = 0;

        for (Field f: encodableFields) {
            Object value = null;
            try {
                value = f.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Error while retrieving field from object of type "
                        + obj.getClass().getName() + ".");
            }

            // Skip encoding this field if it's empty
            if (value == null) {
                skippedAmountOfFields++;
                continue;
            }

            // Get the gobType, which might be a default supported type, or a registered type
            Class<? extends GobType> gobType = Encoder.supportedTypes.get(value.getClass());
            if (gobType == null) {
                gobType = encoder.getWireTypeByType(value.getClass()).encapsulatedType.getClass();
            }

            try {
                // We'll need to get an instance of the GobType representing the type of the field
                // and call encode() on it.
                Constructor<? extends GobType> constructor;
                Method method;
                byte[] tempValue;
                // GobCompositeTypes should already be registered, so we can just fetch them from the encoder
                if (GobCompositeType.class.isAssignableFrom(gobType)) {
                    method = gobType.getMethod("encode", Object.class, Boolean.TYPE);
                    // constructor = gobType.getConstructor(Encoder.class, f.getType().getClass());
                    GobCompositeType gobCompositeType = encoder.getWireTypeByType(f.getType()).encapsulatedType;
                    tempValue = (byte[]) method.invoke(gobCompositeType, value, true);
                }
                // Else we'll need to construct one reflectively
                else {
                    method = gobType.getMethod("encode");
                    constructor = gobType.getConstructor(f.getType());
                    tempValue = (byte[]) method.invoke(constructor.newInstance(value));
                }

                // We need to check if an encoded value results in a null array
                // if it does, we skip it.
                if (ByteArrayUtilities.isNonNull(tempValue)) {
                    byte[] fieldIncrement = new GobUnsignedInteger(skippedAmountOfFields + 1).encode();
                    if (encodedValue == null) {
                        encodedValue = ByteArrayUtilities.concat(fieldIncrement, tempValue);
                    } else {
                        encodedValue = ByteArrayUtilities.concat(encodedValue, fieldIncrement,
                                tempValue);
                    }
                    skippedAmountOfFields = 0;
                } else {
                    // If we're not encoding the last field we need to increase the skipped fields counter
                    if (encodableFields.indexOf(f) < encodableFields.size()-1) {
                        skippedAmountOfFields++;
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't retrieve encoding method.");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't invoke encoding method.");
            } catch (InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                throw new RuntimeException("Couldn't instantiate simple GobType.");
            }
        }

        // Encode the struct. We have three possibilities:
        //
        // 1) Either this is an empty object, in which case we return a null byte
        // 2) We have a field which refers to an empty (not null!) object, in which case we need to
        //    select that field, and then encode a null byte (happens in for instance a linked list, where
        //    we have class Node { Node next; } and n1.next = n2, and n2.next = null, where both n1
        //    and n2 are of type Node
        // 3) The most common case, all our fields encoded to some byte array and we just stick a null byte on
        //    it to signify the end of the struct
        if (skippedAmountOfFields == encodableFields.size()) {
            // case 1
            encodedValue = nullByteArray;
        } else {
            encodedValue = encodedValue == null
                    // case 2
                    ? ByteArrayUtilities.concat(oneByteArray, nullByteArray)
                    // case 3
                    : ByteArrayUtilities.concat(encodedValue, nullByteArray);
        }

        // If we're not nested. If we are nested, the total size will be encoded in the outer StructType
        if (!nested) {
            byte[] totalSize = new GobUnsignedInteger(encodedValue.length).encode();
            encodedValue = ByteArrayUtilities.concat(totalSize, encodedValue);
        }

        return encodedValue;
    }

}
