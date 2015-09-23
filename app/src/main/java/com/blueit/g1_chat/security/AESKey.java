package com.blueit.g1_chat.security;

import com.tozny.crypto.android.AesCbcWithIntegrity;

import java.security.InvalidKeyException;

import static com.tozny.crypto.android.AesCbcWithIntegrity.SecretKeys;

/**
 * Created by Jonathan on 2015-09-23.
 */
public class AESKey {
    SecretKeys secretKeys;

    AESKey(SecretKeys secretKeys) {
        this.secretKeys = secretKeys;
    }

    public AESKey(String keyString) throws InvalidKeyException {
        this.secretKeys = AesCbcWithIntegrity.keys(keyString);
    }

    public String toString() {
        return AesCbcWithIntegrity.keyString(secretKeys);
    }
}
