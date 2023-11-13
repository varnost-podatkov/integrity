package vp.integrity;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ReadFromFileHMAC {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        // Z diska preberemo bajte za ključ
        final byte[] keyBytes = Files.readAllBytes(Path.of("../data/msg.bin.key"));
        // iz podanih bajtov ustvarimo objekt Key, ki ga bomo uporabili za izračun MAC
        final Key key = new SecretKeySpec(keyBytes, "HmacSHA256");

        // Instanciiramo algoritem MAC
        final Mac mac = Mac.getInstance("HmacSHA256");
        // mu podamo ključ
        mac.init(key);

        // Preberemo konkatenirano značko in sporočilo
        final byte[] tagAndMessage = Files.readAllBytes(Path.of("../data/msg.bin"));
        // Ju razdružimo: prvij 32 bajtov je značka
        final byte[] tag = Arrays.copyOfRange(tagAndMessage, 0, 32);
        // Preostali bajti predstavljajo sporočilo
        final byte[] message = Arrays.copyOfRange(tagAndMessage, 32, tagAndMessage.length);

        // sporočilo naložimo v algoritem MAC
        mac.update(message);
        // in pri sebi izračunamo značko
        final byte[] recomputedTag = mac.doFinal();

        // Preverimo, ali se naš izračun ujema z značko, ki smo jo prejeli z diska
        if (!MessageDigest.isEqual(tag, recomputedTag)) {
            System.out.println("Read invalid data, aborting!");
            System.exit(1);
        }

        // Le če se značka ujema, nadaljujemo z izvajanjem
        System.out.printf("Data appears to be valid: '%s'%n", new String(message, StandardCharsets.UTF_8));
    }
}
