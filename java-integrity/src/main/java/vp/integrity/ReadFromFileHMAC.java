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
        final Mac mac = Mac.getInstance("HmacSHA256");

        final byte[] keyBytes = Files.readAllBytes(Path.of("../data/msg.bin.key"));
        final Key key = new SecretKeySpec(keyBytes, "HmacSHA256");

        mac.init(key);

        final byte[] tagAndMessage = Files.readAllBytes(Path.of("../data/msg.bin"));
        final byte[] tag = Arrays.copyOfRange(tagAndMessage, 0, 32);
        final byte[] message = Arrays.copyOfRange(tagAndMessage, 32, tagAndMessage.length);

        mac.update(message);
        final byte[] recomputedTag = mac.doFinal();

        if (!MessageDigest.isEqual(tag, recomputedTag)) {
            System.out.println("Read invalid data, aborting!");
            System.exit(1);
        }

        System.out.printf("Data appears to be valid: '%s'%n", new String(message, StandardCharsets.UTF_8));
    }
}
