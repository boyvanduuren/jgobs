package xyz.vanduuren.jgobs.types.composite;

import org.junit.Before;
import org.junit.Test;
import xyz.vanduuren.jgobs.lib.Encoder;

import javax.xml.bind.DatatypeConverter;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertArrayEquals;

/**
 * Give a description of StructTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-01
 */
public class StructTypeTest {

    private Encoder encoder;
    private ByteArrayOutputStream outputStream;

    @Before
    public void clearEncoder() {
        outputStream = new ByteArrayOutputStream();
        encoder = new Encoder(true, outputStream);
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
        encoder.registerType(Point.class);
        StructType structType = new StructType(encoder, Point.class);

        String gobsEncodedStruct = "010105506f696e7401ff8200010201015801040001015901040000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                structType.encode());
    }

    @Test
    public void encodePointValue() throws Exception {
        encoder.registerType(Point.class);
        StructType structType = new StructType(encoder, Point.class);
        Point point = new Point();
        point.X = 22;
        point.Y = 33;

        String gobsEncodedPointValue = "07ff82012c014200";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPointValue), structType.encode(point));
    }

    @SuppressWarnings("unused")
    class Person {
        public String name;
        public int age;
        public String gender;
        public boolean married;
    }

    @Test
    public void encodePersonType() throws Exception {
        encoder.registerType(Person.class);
        StructType structType = new StructType(encoder, Person.class);

        String gobsEncodedStruct = "010106506572736f6e01ff8200010401044e616d65010c000103416765010400"
                + "010647656e646572010c0001074d61727269656401020000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct), structType.encode());
    }

    @Test
    public void encodePersonValue() throws Exception {
        encoder.registerType(Person.class);
        StructType structType = new StructType(encoder, Person.class);

        // person.married for the next object is 0x00 gobs encoded. This will result in a
        // skipped field at the end.
        Person person = new Person();
        person.name = "Boy van Duuren";
        person.age = 29;
        person.gender = "male";
        person.married = false;

        String gobsEncodedPersonValue = "1bff82010e426f792076616e2044757572656e013a01046d616c6500";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPersonValue), structType.encode(person));

        // The next Person object tests the skipping of fields with a non-skipped field at the end
        person = new Person();
        person.name = "Blaat Koe";
        person.age = 0;
        person.gender = "";
        person.married = true;

        gobsEncodedPersonValue = "10ff820109426c616174204b6f65030100";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPersonValue), structType.encode(person));

        // Skip the last three fields
        person.married = false;
        gobsEncodedPersonValue = "0eff820109426c616174204b6f6500";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPersonValue), structType.encode(person));
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
        encoder.registerType(Foo.class);
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
        encoder.registerType(Data.class);
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

        encoder.registerType(Data.class);
        encoder.registerType(Foo.class);

        String gobsEncodedStruct = "0101044461746101ff820001040103526177010a000107436865636b65640102"
                + "000106576569676874010800010653657269616c010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Data.class).encode());

        gobsEncodedStruct = "010103466f6f01ff840001020103426172010400010342617a010c0000";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedStruct),
                new StructType(encoder, Foo.class).encode());
    }

}