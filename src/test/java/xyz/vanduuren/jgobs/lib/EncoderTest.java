package xyz.vanduuren.jgobs.lib;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Give a description of WireTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-05
 */
public class EncoderTest {

    private Encoder encoder;

    @Before
    public void setUp() throws Exception {
        encoder = new Encoder();
    }
    @Test(expected=IllegalArgumentException.class)
    public void encodeStructWithoutPublicFields() throws Exception {
        class Foo {
            int a;
        };

        encoder.encode(new Foo());
    }

}