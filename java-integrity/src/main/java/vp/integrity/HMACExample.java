package vp.integrity;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HMACExample {
    public static void main(String[] args) throws Exception {

        final String message = "Moje sporočilo.";
        final byte[] pt = message.getBytes(StandardCharsets.UTF_8);

        final Key key = KeyGenerator.getInstance("HmacSHA256").generateKey();

        final Mac ana = Mac.getInstance("HmacSHA256");
        ana.init(key);

        ana.update(pt);
        final byte[] tag = ana.doFinal();

        // alternativno v enem koraku
        // final byte[] tag = ana.doFinal(pt);

        final String tagHex = HexFormat.of().formatHex(tag);
        System.out.println("HMAC: " + tagHex);

        // Ana pošlje (pt, tag) Boru

        final Mac bor = Mac.getInstance("HmacSHA256");
        bor.init(key); // isti ključ kot Ana
        final byte[] recomputedTag = bor.doFinal(pt);

        // Bor preveri značko in sporočilo: ključ domevamo pozna
        // Pri preverjanju moramo paziti na napade z merjenjem časa, zato primerjamo s funkcijo MessageDigest.isEqual
        if (!MessageDigest.isEqual(tag, recomputedTag)) {
            System.out.println("Received invalid data, aborting!");
            System.exit(1);
        }

        System.out.printf("Data appears to be valid: '%s'%n", new String(pt, StandardCharsets.UTF_8));
    }
}
