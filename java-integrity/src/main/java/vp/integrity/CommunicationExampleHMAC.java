package vp.integrity;

import fri.isp.Agent;
import fri.isp.Environment;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


/**
 * To je primer simulacijskega okolja, v katerem si agenti (Ana, Bor, Cene, Nandi idr.)
 * izmenjujejo sporočila.
 * <p>
 * Vsak agent se izvaja v svoji niti in neodvisno od ostalih. S pomočjo klicev funkcije
 * send(String) in receive(String) agenti pošiljajo in prejemajo sporočila.
 * <p>
 * Enota poslanega podatka je polje bajtov, zato je treba vsak podatek pred
 * pošiljanjem postrojiti.
 */
public class CommunicationExampleHMAC {
    public static void main(String[] args) throws Exception {
        // Simulacijsko okolje, v katerem bivajo agenti
        final Environment env = new Environment();

        // Globalno ustvarimo simetrični ključ, ki ga Ana in Bor uporabita za preverjanje MAC
        final SecretKey key = KeyGenerator.getInstance("HmacSHA256").generateKey();

        env.add(new Agent("ana") {
            @Override
            public void task() throws Exception {
                final byte[] pt = "Zdravo Bor, tukaj Ana.".getBytes(StandardCharsets.UTF_8);
                send("bor", pt);

                // Ana instanciira algoritem MAC in izračuna značko
                final Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(key);
                final byte[] tag = mac.doFinal(pt);
                // poleg sporočila pošlje še značko
                send("bor", tag);

                print("Poslala sem sporočilo in značko.");
            }
        });

        env.add(new Agent("bor") {
            @Override
            public void task() throws Exception {
                // Bor prejme sporočilo
                final byte[] pt = receive("ana");

                // Bor prejme še značko in sporočilo preveri
                final byte[] tag = receive("ana");

                final Mac mac = Mac.getInstance("HmacSHA256");
                mac.init(key);

                final byte[] recomputedTag = mac.doFinal(pt);

                if (!MessageDigest.isEqual(recomputedTag, tag)) {
                    print("Sporočilo ni veljavno, prekinjam program.");
                }

                // Bor sporočilo izpiše
                print("Sporočilo se glasi: '%s'", new String(pt, StandardCharsets.UTF_8));

            }
        });

        // Povežemo Ano in Bora
        env.connect("ana", "bor");
        // zaženemo simulacijsko okolje
        env.start();
    }
}
