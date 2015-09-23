package com.blueit.g1_chat.security;

import android.util.Base64;
import android.util.Log;

import static com.tozny.crypto.android.AesCbcWithIntegrity.*;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
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
        Log.d(TAG, "Beginning AES debug...");
        AESDebug();
        Log.d(TAG, "Beginning RSA debug...");
        RSADebug();
    }
    public static void AESDebug() {
        AESKey key = AESGenerateKey();
        String message = "Hello AES Cryptography!";
        String encrypted = AESEncrypt(key, message);
        String decrypted = AESDecrypt(key, encrypted);

        Log.d(TAG, "  Message: '" + message + "'");
        Log.d(TAG, "Encrypted: '" + encrypted + "'");
        Log.d(TAG, "Decrypted: '" + decrypted + "'");
    }
    public static void RSADebug() {
        RSAKey key = RSAGenerateKey();
        String message = "Hello RSA Cryptography!";
        String encrypted = RSAEncrypt(key.getPublic(), message);
        String decrypted = RSADecrypt(key.getPrivate(), encrypted);

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
    public static String AESEncrypt(AESKey key, String data) {
        try {
            CipherTextIvMac cipherText = encrypt(data, key.secretKeys);
            return cipherText.toString();
        }
        catch (GeneralSecurityException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static byte[] AESEncrypt(AESKey key, byte[] data) {
        try {
            CipherTextIvMac cipherText = encrypt(data, key.secretKeys);
            return cipherText.toString().getBytes();
        }
        catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypts the data with the key using AES algorithm.
     *
     * @param key The AES crypto-key.
     * @param data The encrypted data to decrypt.
     * @return The decrypted data.
     */
    public static String AESDecrypt(AESKey key, String data) {
        try {
            CipherTextIvMac cipherText = new CipherTextIvMac(data);
            return decryptString(cipherText, key.secretKeys);
        }
        catch (GeneralSecurityException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    public static byte[] AESDecrypt(AESKey key, byte[] data) {
        try {
            CipherTextIvMac cipherText = new CipherTextIvMac(new String(data));
            return decrypt(cipherText, key.secretKeys);
        }
        catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a new AES crypto-key.
     *
     * @return The new AES key.
     */
    public static AESKey AESGenerateKey() {
        try {
            SecretKeys keys = generateKey();
            return new AESKey(keys);
        }
        catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Encrypts the data with the key using RSA algorithm.
     *
     * @param key The public RSA crypto-key.
     * @param data The clear data to encrypt.
     * @return The encrypted data.
     */
    public static String RSAEncrypt(PublicKey key, String data) {
        return new String(RSAEncrypt(key, data.getBytes()));
    }
    public static byte[] RSAEncrypt(PublicKey key, byte[] data) {
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
    public static String RSADecrypt(PrivateKey key, String data) {
        return new String(RSADecrypt(key, data.getBytes()));
    }
    public static byte[] RSADecrypt(PrivateKey key, byte[] data) {
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
    public static RSAKey RSAGenerateKey() {
        try {
            // Create and initialize key generator
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(RSA_KEY_BITLENGTH);

            // Generate keys and extract the public and private key
            KeyPair keys = keyGen.generateKeyPair();

            return new RSAKey(keys);
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
    public static PublicKey RSACreatePublicKey(BigInteger exponent, BigInteger modulus) {
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
    public static PrivateKey RSACreatePrivateKey(BigInteger exponent, BigInteger modulus) {
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
