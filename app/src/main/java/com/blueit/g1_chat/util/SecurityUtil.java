package com.blueit.g1_chat.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by Jonathan on 2015-09-22.
 *
 * References and guide:
 * http://www.javamex.com/tutorials/cryptography/rsa_encryption.shtml
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

        public PublicKey getPublicKey() {
            try {
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PublicKey key = fact.generatePublic(publicKey);
                return key;
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                ex.printStackTrace();
                return null;
            }
        }
        private PrivateKey getPrivateKey() {
            try {
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PrivateKey key = fact.generatePrivate(privateKey);
                return key;
            }
            catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Encrypts the data with the key using AES algorithm.
     *
     * @param key The AES crypto-key.
     * @param data The clear data to encrypt.
     * @return The encrypted data.
     */
    public static byte[] encryptAES(byte[] key, byte[] data) {
        return null;
    }

    /**
     * Decrypts the data with the key using AES algorithm.
     *
     * @param key The AES crypto-key.
     * @param data The encrypted data to decrypt.
     * @return The decrypted data.
     */
    public static byte[] decryptAES(byte[] key, byte[] data) {
        return null;
    }

    /**
     * Encrypts the data with the key using RSA algorithm.
     *
     * @param key The public RSA crypto-key.
     * @param data The clear data to encrypt.
     * @return The encrypted data.
     */
    public static byte[] encryptRSA(RSAKey key, byte[] data) {
        PublicKey pubKey = key.getPublicKey();
        return encryptRSA(pubKey, data);
    }
    public static byte[] encryptRSA(PublicKey key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encrypted = cipher.doFinal(data);
            return encrypted;
        }
        catch (NoSuchAlgorithmException
                | InvalidKeyException
                | NoSuchPaddingException
                | BadPaddingException
                | IllegalBlockSizeException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypts the data with the key using RSA algorithm.
     *
     * @param key The private RSA crypto-key.
     * @param data The encrypted data to decrypt.
     * @return The decrypted data.
     */
    public static byte[] decryptRSA(RSAKey key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key.getPrivateKey());

            byte[] decrypted = cipher.doFinal(data);
            return decrypted;
        }
        catch (NoSuchAlgorithmException
                | InvalidKeyException
                | NoSuchPaddingException
                | BadPaddingException
                | IllegalBlockSizeException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a new RSA key pair.
     *
     * @return The generated RSA keys.
     */
    public static RSAKey generateRSAKey() {
        try {
            // Create and initialize key generator
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(RSA_KEY_BITLENGTH);

            // Generate keys and extract the public and private key
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

