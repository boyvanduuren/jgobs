package xyz.vanduuren.jgobs.types.composite;

import org.junit.Test;
import xyz.vanduuren.jgobs.types.composite.StructType;
import xyz.vanduuren.jgobs.types.composite.WireType;

/**
 * Give a description of WireTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-06-27
 */
public class WireTypeTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructor() throws Exception {
        // A WireType can never be constructed with another WireType as argument
        WireType wireType = new WireType(new WireType(new StructType(new Object())));
    }
    @Test
    public void encode() throws Exception {
        class Foo {
            private int a;
            public int b;
            public StructType structType;
        }
        WireType wireType = new WireType(new StructType(new Foo()));
        wireType.encode();
    }

    @Test
    public void decode() throws Exception {

    }

}