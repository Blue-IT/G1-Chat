package com.blueit.g1_chat.security;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Jonathan on 2015-09-23.
 */
public class RSAKey {
    KeyPair keys;

    RSAKey(KeyPair keys) {
        this.keys = keys;
    }

    public PublicKey getPublic() {
        return keys.getPublic();
    }

    public PrivateKey getPrivate() {
        return keys.getPrivate();
    }
}
