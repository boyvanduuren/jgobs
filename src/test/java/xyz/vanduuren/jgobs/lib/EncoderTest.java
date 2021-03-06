package xyz.vanduuren.jgobs.lib;

import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertArrayEquals;

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
        encoder = new Encoder(true, outputStream);
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


    // The resulting encoded struct was tested against a go program. Need to figure out how to test this better.

    public class Address {
        public String street;
        public int number;
        public String city;
    }

    public class PersonWithAddress {
        public String name;
        public Address address;

        public PersonWithAddress(String name, String street, int number, String city) {
            this.name = name;
            Address address = new Address();
            address.street = street;
            address.number = number;
            address.city = city;
            this.address = address;
        }
    }

    @Test
    public void encodeNestedStruct() throws Exception {
        PersonWithAddress personWithAddress = new PersonWithAddress("Jan Klaassen", "Kerksteeg", 12, "Leiden");
        encoder.encode(personWithAddress);
        String gobsEncodedPersonWithAddress ="34FF83030101074164647265737301FF840001030106537472656574010C00010"
                + "64E756D626572010400010443697479010C00000035FF8103010111506572736F6E576974684164647265737301"
                + "FF8200010201044E616D65010C0001074164647265737301FF8400000028FF82010C4A616E204B6C61617373656"
                + "E0101094B65726B7374656567011801064C656964656E0000";

        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPersonWithAddress), outputStream.toByteArray());
    }

    // Encode a type without auto registering newly encountered types. This should result in a person without
    // an address.
    @Test
    public void encodeNestedStructWithoutAutoRegister() throws Exception {
        encoder = new Encoder(false, outputStream);
        PersonWithAddress personWithAddress = new PersonWithAddress("Jan Klaassen", "Kerksteeg", 12, "Leiden");
        encoder.encode(personWithAddress);
        String gobsEncodedPersonWithAddress = "28ff8103010111506572736f6e576974684164647265737301ff8200010101044e616d65"
                + "010c00000011ff82010c4a616e204b6c61617373656e00";

        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedPersonWithAddress), outputStream.toByteArray());
    }


    // The result of this program was tested by deserializing the result in go, and verifying that:
    //
    //  dec.Decode(&node)
    //	curr := &node
    //  for curr != nil {
    //    i++
    //    fmt.Printf("n%d\n", i)
    //    curr = curr.Next
    //  }
    //
    // resulted in the output
    // n1
    // n2
    // n3

    public class Node {
        public Node next;
    }

    @Test
    public void encodeLinkedList() throws Exception {
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();

        n1.next = n2;
        n2.next = n3;

        encoder.encode(n1);

        String gobsEncodedLinkedList = "1CFF81030101044E6F646501FF8200010101044E65787401FF8200000006FF8201010000";

        assertArrayEquals(DatatypeConverter.parseHexBinary(gobsEncodedLinkedList), outputStream.toByteArray());
    }

}