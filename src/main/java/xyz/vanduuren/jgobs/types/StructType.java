package xyz.vanduuren.jgobs.types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Give a description of StrucType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class StructType extends GobCompositeType<Object> {

    public StructType(Object object) {
        super(object);
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
                System.out.println(field.getName() + " is public");
            }
        }

        return new byte[]{};
    }

}
