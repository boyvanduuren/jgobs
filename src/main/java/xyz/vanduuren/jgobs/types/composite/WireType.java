package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.ByteArrayUtilities;
import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.primitive.GobSignedInteger;
import xyz.vanduuren.jgobs.types.primitive.GobUnsignedInteger;

import static xyz.vanduuren.jgobs.lib.ByteArrayUtilities.nullByteArray;

/**
 * Give a description of WireType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class WireType extends GobCompositeType<Class<?>> {

    public final static int ID = 16;
    // encapsulatedType will need to become generic
    public StructType encapsulatedType;

    public WireType(Encoder encoder, Class<?> clazz) {
        super(encoder, clazz);
        encapsulatedType = new StructType(encoder, unencodedData);
    }

    @Override
    public Class<?> decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        // for now we just support structTypes
        final byte[] structTypeField = new byte[]{3};
        byte[] encodedWireType;
        // register the class and save the classID for later
        int classID = encoder.registerType(unencodedData);
        // encode the classID
        byte[] encodedClassID = new GobSignedInteger(-classID).encode();
        // encode the struct
        byte[] encodedStruct = encapsulatedType.encode();

        encodedWireType = ByteArrayUtilities.concat(encodedClassID, structTypeField,
                encodedStruct, nullByteArray);

        byte[] totalSize = new GobUnsignedInteger(encodedWireType.length).encode();

        // encode the total size + classID + encoded struct type
        encodedWireType = ByteArrayUtilities.concat(totalSize, encodedWireType);

        return encodedWireType;
    }

}
