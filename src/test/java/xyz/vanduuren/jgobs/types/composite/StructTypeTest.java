package xyz.vanduuren.jgobs.types.composite;

import org.junit.Before;
import org.junit.Test;
import xyz.vanduuren.jgobs.lib.Encoder;

import javax.xml.bind.DatatypeConverter;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of StructTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-01
 */
public class StructTypeTest {

    private Encoder encoder;

    @Before
    public void clearEncoder() {
        encoder = new Encoder();
    }


    /*
    type Point struct {
        X, Y int
    }
     */

    @SuppressWarnings("unused")
    class Point {
        public int X;
        public int Y;
    }

    @Test
    public void encodePointType() throws Exception {
        StructType structType = new StructType(encoder, Point.class);

        String gobsEncodedStruct = "010105506f696e7401ff8200010201015801040001015901040000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                structType.encode());
    }

    @Test
    public void encodePointValue() throws Exception {
        StructType structType = new StructType(encoder, Point.class);
        Point point = new Point();
        point.X = 22;
        point.Y = 33;

        String gobsEncodedPointValue = "07ff82012c014200";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPointValue), structType.encodeValue(point));
    }

    @Test
    public void encodeFoo() throws Exception {
        /*
        type Foo struct {
            Bar int
            Baz string
        }
        */

        @SuppressWarnings("unused")
        class Foo {
            public int bar;
            public String baz;
        }
        String gobsEncodedStruct = "010103466f6f01ff820001020103426172010400010342617a010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Foo.class).encode());
    }

    @Test
    public void encodeData() throws Exception {
        /*
        type Data struct {
            Raw     []byte
            Checked bool
            Weight  float32
            Serial  string
            hidden  uint8
        }
        */

        @SuppressWarnings("unused")
        class Data {
            public byte[] raw;
            public boolean checked;
            public float weight;
            public String  serial;
            private byte hidden;
        }
        String gobsEncodedStruct = "0101044461746101ff820001040103526177010a000107436865636b65640102"
                +"000106576569676874010800010653657269616c010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Data.class).encode());
    }

    @Test
    public void encodeDataFoo() {
        /*
        type Data struct {
            Raw     []byte
            Checked bool
            Weight  float32
            Serial  string
            hidden  uint8
        }
        */

        @SuppressWarnings("unused")
        class Data {
            public byte[] raw;
            public boolean checked;
            public float weight;
            public String  serial;
            private byte hidden;
        }

        /*
        type Foo struct {
            Bar int
            Baz string
        }
        */

        @SuppressWarnings("unused")
        class Foo {
            public int bar;
            public String baz;
        }

        String gobsEncodedStruct = "0101044461746101ff820001040103526177010a000107436865636b65640102"
                + "000106576569676874010800010653657269616c010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Data.class).encode());

        gobsEncodedStruct = "010103466f6f01ff840001020103426172010400010342617a010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Foo.class).encode());
    }

}