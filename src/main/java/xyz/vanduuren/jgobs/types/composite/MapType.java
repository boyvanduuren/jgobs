package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;

import java.util.Map;

/**
 * Give a description of MapType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class MapType extends GobCompositeType<Map> {

    public static final int ID = 23;
    public final int keyID, elemID;

    public MapType(Encoder encoder, Map map) {
        super(encoder, map);

        Class<?> keyClass, elemClass = null;
        int keyID, elemID;

        if (map.size() > 0) {
            // Get the classes of the key and value of the map
            keyClass = map.keySet().iterator().next().getClass();
            elemClass = map.values().iterator().next().getClass();
            // Get their IDs
            keyID = encoder.getTypeID(keyClass);
            elemID = encoder.getTypeID(elemClass);

            // Check if the typeID's of both the key and element are known as registered types
            if (keyID != -1 && elemID != -1) {
                // If they are, set the fields to those values
                this.keyID = keyID;
                this.elemID = elemID;
            // Check if the typeID's are known as supported types
            } else if (encoder.supportedTypes.containsKey(keyClass) && encoder.supportedTypes.containsKey(elemClass)) {
                this.keyID = encoder.registeredTypeIDs.get(encoder.supportedTypes.get(keyClass));
                this.elemID = encoder.registeredTypeIDs.get(encoder.supportedTypes.get(elemClass));
            } else {
                // If not, check if we allow auto registration of new types
                if (encoder.autoRegister) {
                    // We do? Register them
                    this.keyID = encoder.registerType(keyClass);
                    this.elemID = encoder.registerType(elemClass);
                } else {
                    // We don't? Throw an exception
                    throw new IllegalArgumentException("Either the type of the key [" + keyClass.getSimpleName()
                            + "] or value [" + elemClass.getSimpleName() + "] of this map is unknown.");
                }
            }
        } else {
            // We need elements in our map, else we can't get type info
            throw new RuntimeException("Can't create MapType with an empty map.");
        }
    }

    @Override
    public Map decode() {
        return null;
    }

    @Override
    public byte[] encode() {
        return null;
    }

}
