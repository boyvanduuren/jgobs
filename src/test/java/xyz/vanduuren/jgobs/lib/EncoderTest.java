package xyz.vanduuren.jgobs.lib;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.*;

/**
 * Give a description of WireTypeTest here.
 *
 * @author Boy van Duuren <boy@vanduuren.xyz>
 * @since 2016-07-05
 */
public class EncoderTest {

    private Encoder encoder;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        encoder = new Encoder(outputStream);
    }

    @Test(expected=IllegalArgumentException.class)
    public void encodeStructWithoutPublicFields() throws Exception {
        class Foo {
            int a;
        };

        encoder.encode(new Foo());
    }

    public class Person {
        public String name;
        public int age;
        public String gender;
        public boolean married;
    }

    @Test
    public void encodePerson() throws Exception {
        Person person = new Person();
        person.name = "Foo Bar";
        person.age = 76;
        person.gender = "F";
        person.married = false;
        encoder.encode(person);

        String gobsEncodedPerson = "3cff8103010106506572736f6e01ff8200010401044e616d65010c000103"
                + "416765010400010647656e646572010c0001074d617272696564010200000012ff820107466f6f2042617201ff9801014600";
        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPerson), outputStream.toByteArray());
    }

}