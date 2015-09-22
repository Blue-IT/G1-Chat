package com.blueit.g1_chat.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by Jonathan on 2015-09-22.
 */
public class SecurityUtil
{
    // 2048 bit key, slower but more secure than 1024 bit
    public static final int RSA_KEY_BITLENGTH = 2048;

    public static class RSAKey {
        private RSAPrivateKeySpec privateKey;
        private RSAPublicKeySpec publicKey;

        private RSAKey(RSAPrivateKeySpec privateKey, RSAPublicKeySpec publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }

    /**
     * Encrypts the data with the key using AES algorithm.
     *
     * @param key AES crypto-key.
     * @param data Clear data to encrypt.
     * @return The encrypted data.
     */
    public static byte[] encryptAES(byte[] key, byte[] data) {
        return null;
    }

    /**
     * Decrypts the data with the key using AES algorithm.
     *
     * @param key AES crypto-key.
     * @param data Encrypted data to decrypt.
     * @return The decrypted data.
     */
    public static byte[] decryptAES(byte[] key, byte[] data) {
        return null;
    }

    public static byte[] encryptRSA(SecurityKey key, byte[] clear) {
        return null;
    }

    public static byte[] decryptRSA(SecurityKey key, byte[] encrypted) {
        return null;
    }

    public static SecurityKey generateAESKey() {
        return new SecurityKey(new BigInteger("123"));
    }

    public static RSAKey generateRSAKey() {
        try {
            // Create and initialize key generator
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(RSA_KEY_BITLENGTH);

            KeyPair key = keyGen.generateKeyPair();
            KeyFactory keyFact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubKey = keyFact.getKeySpec(key.getPublic(), RSAPublicKeySpec.class);
            RSAPrivateKeySpec privKey = keyFact.getKeySpec(key.getPrivate(), RSAPrivateKeySpec.class);

            return new RSAKey(privKey, pubKey);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            // This should never happen!
            ex.printStackTrace();
            return null;
        }
    }
}
