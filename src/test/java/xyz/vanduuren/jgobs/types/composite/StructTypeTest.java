package xyz.vanduuren.jgobs.types.composite;

import org.junit.Test;
import xyz.vanduuren.jgobs.types.composite.StructType;

import javax.xml.bind.DatatypeConverter;

/**
 * Give a description of StructTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-01
 */
public class StructTypeTest {
    @Test
    public void encode() throws Exception {
        class foo {
            public int i;
            public Integer io;
            public boolean b;
            public Boolean bo;
            public float f;
            public Float fo;
            public double d;
            public Double dO;
            public byte[] ba;
            public String s;
        }

        System.out.println(DatatypeConverter.printHexBinary(
                new StructType(new foo()).encode()));
    }

}