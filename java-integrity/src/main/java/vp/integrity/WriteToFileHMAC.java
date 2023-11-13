package vp.integrity;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class WriteToFileHMAC {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        final Key key = KeyGenerator.getInstance("HmacSHA256").generateKey();

        final Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);

        final String message = "Sporočilo, ki bo zapisano na disk in zavarovano pred spremembami.";
        final byte[] pt = message.getBytes(StandardCharsets.UTF_8);
        mac.update(pt);
        final byte[] tag = mac.doFinal();

        final byte[] data = Arrays.copyOf(tag, tag.length + pt.length);
        System.arraycopy(pt, 0, data, tag.length, pt.length);

        Files.write(Path.of("../data/msg.bin"), data);
        System.out.println("Data written to disk!");

        Files.write(Path.of("../data/msg.bin.key"), key.getEncoded());
        System.out.println("Key written to disk!");
    }
}
