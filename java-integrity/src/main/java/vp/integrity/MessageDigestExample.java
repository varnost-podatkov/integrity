package vp.integrity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HexFormat;

public class MessageDigestExample {

    public static void main(String[] args) throws NoSuchAlgorithmException {

        final String message = "Moje sporočilo";

        final byte[] pt = message.getBytes(StandardCharsets.UTF_8);

        // Izberemo ustrezno zgoščevalno funkcijo. Seznam veljavnih funkcij najdemo v dokumentaciji
        // https://docs.oracle.com/en/java/javase/21/docs/specs/security/standard-names.html
        final MessageDigest algorithm = MessageDigest.getInstance("SHA-256");

        // dodamo čistopis
        algorithm.update(pt);
        // zaključimo zgoščevanje
        final byte[] hashed = algorithm.digest();

        // pretvorba iz byte[] v String
        System.out.printf("Sporočilo: %s%n", new String(pt, StandardCharsets.UTF_8));

        // pretvorba v šestajstiški niz
        System.out.printf("Zgostitev (šestnajstiško): %s%n", HexFormat.of().formatHex(hashed));

        // izpis kot polje bajtov (v javi se bajti predstavijo s predznakom)
        System.out.printf("Zgostitev (polje bajtov): %s%n", Arrays.toString(hashed));
    }
}
