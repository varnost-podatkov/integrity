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
 * `send(byte[])` in `byte[] receive(String)` agenti pošiljajo in prejemajo sporočila.
 * <p>
 * Enota poslanega podatka je polje bajtov (byte[]), zato je treba vsak podatek pred
 * pošiljanjem postrojiti.
 */
public class CommunicationExampleHMAC {
    public static void main(String[] args) throws Exception {
        // Simulacijsko okolje, v katerem bivajo agenti
        final Environment env = new Environment();

        env.add(new Agent("ana") {
            @Override
            public void task() throws Exception {
                final byte[] pt = "Zdravo Bor, tukaj Ana.".getBytes(StandardCharsets.UTF_8);
                send("bor", pt);
                print("Poslala sem sporočilo.");
            }
        });

        env.add(new Agent("bor") {
            @Override
            public void task() throws Exception {
                // Bor prejme sporočilo
                final byte[] pt = receive("ana");

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
