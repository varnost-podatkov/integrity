package vp.integrity;

import fri.isp.Agent;
import fri.isp.Environment;

import java.security.SecureRandom;


public class CommunicationExample {
    public static void main(String[] args) throws Exception {
        final Environment env = new Environment();

        env.add(new Agent("alice") {
            @Override
            public void task() throws Exception {
                final byte[] data = new byte[200 * 1024 * 1024];
                SecureRandom.getInstanceStrong().nextBytes(data);
            }
        });

        env.add(new Agent("public-space") {
            @Override
            public void task() throws Exception {

            }
        });

        env.add(new Agent("bob") {
            @Override
            public void task() throws Exception {

            }
        });

        env.connect("alice", "bob");
        env.connect("alice", "public-space");
        env.connect("public-space", "bob");
        env.start();
    }
}
