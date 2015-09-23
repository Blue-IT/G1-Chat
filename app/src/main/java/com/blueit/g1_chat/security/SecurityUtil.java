package com.blueit.g1_chat.security;

import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
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
    // This is a static class
    private SecurityUtil() { }

    // Debug tag
    public static final String TAG = "SecurityUtil";

    public static void debug() {
        KeyPair key = SecurityUtil.generateRSAKey();
        String message = "Hello RSA Cryptography!";
        String encrypted = SecurityUtil.encryptRSA(key.getPublic(), message);
        String decrypted = SecurityUtil.decryptRSA(key.getPrivate(), encrypted);

        String TAG = SecurityUtil.TAG + ".debug()";
        Log.d(TAG, "  Message: '" + message + "'");
        Log.d(TAG, "Encrypted: '" + encrypted + "'");
        Log.d(TAG, "Decrypted: '" + decrypted + "'");
    }

    // 1024 bit key, fast and sufficiently secure
    public static final int RSA_KEY_BITLENGTH = 1024;

    /**
     * Encrypts the data with the key using AES algorithm.
     *
     * @param key The AES crypto-key.
     * @param data The clear data to encrypt.
     * @return The encrypted data.
     */
    public static byte[] encryptAES(byte[] key, byte[] data) {
        throw new UnsupportedOperationException("encryptAES() is not yet implemented!");
    }

    /**
     * Decrypts the data with the key using AES algorithm.
     *
     * @param key The AES crypto-key.
     * @param data The encrypted data to decrypt.
     * @return The decrypted data.
     */
    public static byte[] decryptAES(byte[] key, byte[] data) {
        throw new UnsupportedOperationException("decryptAES() is not yet implemented!");
    }

    /**
     * Encrypts the data with the key using RSA algorithm.
     *
     * @param key The public RSA crypto-key.
     * @param data The clear data to encrypt.
     * @return The encrypted data.
     */
    public static String encryptRSA(PublicKey key, String data) {
        return new String(encryptRSA(key, data.getBytes()));
    }
    public static byte[] encryptRSA(PublicKey key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Encrypt the data and encode it using Base64
            byte[] encrypted = cipher.doFinal(data);
            byte[] encoded = Base64.encode(encrypted, Base64.DEFAULT);
            return encoded;
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
    public static String decryptRSA(PrivateKey key, String data) {
        return new String(decryptRSA(key, data.getBytes()));
    }
    public static byte[] decryptRSA(PrivateKey key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            // Decode the data using Base64 and decrypt it
            byte[] decoded = Base64.decode(data, Base64.DEFAULT);
            byte[] decrypted = cipher.doFinal(decoded);
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
    public static KeyPair generateRSAKey() {
        try {
            // Create and initialize key generator
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(RSA_KEY_BITLENGTH);

            // Generate keys and extract the public and private key
            KeyPair key = keyGen.generateKeyPair();

            return key;
        }
        catch (NoSuchAlgorithmException ex) {
            // This should never happen!
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a new public key object.
     *
     * @param exponent The public key exponent.
     * @param modulus The public key modulus.
     * @return The public key object.
     */
    public static PublicKey createPublicKey(BigInteger exponent, BigInteger modulus) {
        try {
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            return fact.generatePublic(keySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a new private key object.
     *
     * @param exponent The private key exponent.
     * @param modulus The private key modulus.
     * @return The private key object.
     */
    public static PrivateKey createPrivateKey(BigInteger exponent, BigInteger modulus) {
        try {
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            return fact.generatePrivate(keySpec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
