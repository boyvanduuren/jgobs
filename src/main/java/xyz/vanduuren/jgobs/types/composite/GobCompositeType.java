package xyz.vanduuren.jgobs.types.composite;

import xyz.vanduuren.jgobs.lib.Encoder;
import xyz.vanduuren.jgobs.types.GobType;

/**
 * Give a description of GobCompositeType here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-05
 */
public abstract class GobCompositeType<T> extends GobType<T> {

    protected final Encoder encoder;

    public GobCompositeType(Encoder encoder, T t) {
        super(t);
        this.encoder = encoder;
    }

}
