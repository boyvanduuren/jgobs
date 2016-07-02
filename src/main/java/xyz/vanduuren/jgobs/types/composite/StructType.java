package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.GobType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Give a description of StrucType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class StructType extends GobType<Object> {

    public final static int ID = 20;

    private byte[] encodeField(Field field) {
        byte[] encodedField = null;
        if (Encoder.supportedTypes.containsKey(field.getType())) {
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
