package vp.integrity;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HexFormat;

public class HMACExample {
    public static void main(String[] args) throws Exception {

        // Ustvarimo sporočilo in ga postrojimo z UTF8
        final String message = "Moje sporočilo.";
        final byte[] pt = message.getBytes(StandardCharsets.UTF_8);

        // Ustvarimo naključen ključ
        final Key key = KeyGenerator.getInstance("HmacSHA256").generateKey();

        // Ustvarimo algoritem MAC
        final Mac ana = Mac.getInstance("HmacSHA256");
        // In mu podamo ključ
        ana.init(key);

        // dodamo vsebimo
        ana.update(pt);

        // končamo procesiranje in preberemo značko
        final byte[] tag = ana.doFinal();

        // alternativno: zgornje lahko storimo v enem koraku
        // final byte[] tag = ana.doFinal(pt);

        // Šestnajstiški izpis izračunane značke
        final String tagHex = HexFormat.of().formatHex(tag);
        System.out.println("HMAC: " + tagHex);

        // Simuliramo, da Ana pošlje par (pt, tag) Boru

        // Bor prav tako ustvari enak algoritem MAC
        final Mac bor = Mac.getInstance("HmacSHA256");
        // Mu da isti ključ kot Ana
        bor.init(key);
        // In pri sebi izračuna značko iz prejetega sporočila in skrivnega ključa
        final byte[] recomputedTag = bor.doFinal(pt);

        // Sedaj preverimo, ali se izračunana in prejeta značka ujemata
        // Pri preverjanju moramo paziti na napade z merjenjem časa, zato primerjamo s funkcijo MessageDigest.isEqual
        // Poglejte v dokumentacijo in se prepričajte, da je čas izvajanja funkcije neodvisen od tega, ali sta podana
        // argumenta enaka, ali ne
        if (!MessageDigest.isEqual(tag, recomputedTag)) {
            System.out.println("Received invalid data, aborting!");
            System.exit(1);
        }

        // Če se ujemata, izpišemo sporočil (a še prej ga moremo dekodirati v UTF8 niz)
        System.out.printf("Data appears to be valid: '%s'%n", new String(pt, StandardCharsets.UTF_8));
    }
}
