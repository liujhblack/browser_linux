package com.liujhblack.util;

import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujunhui on 2019/7/9.
 */


public class EncryptUtil {

    private EncryptUtil() {
        //单例
    }

    //src 明文 res密文 key密钥  publicKey公钥 privateKey私钥

    /**
     * Base64加密
     */
    public static String Base64Encode(String src) {
        return Base64.getEncoder().encodeToString(src.getBytes());
    }

    /**
     * Base64解密
     */
    public static String Base64Decode(String res) {
        return new String(Base64.getDecoder().decode(res));
    }

    /**
     * MD5加密
     */
    public static String MD5Encode(String src){
        byte[] bytes = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(src.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HexUtils.toHexString(bytes);
    }

    /**
     * SHA加密
     */
    public static String SHAEncode(String src){
        byte[] bytes = new byte[0];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            bytes = md.digest(src.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return HexUtils.toHexString(bytes);
    }

    /**
     * DES加密 key-密钥，长度必须为8的倍数
     */
    public static String DESEncode(String src, String key){
        byte[] bytes = new byte[0];
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            bytes = cipher.doFinal(src.getBytes());
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return HexUtils.toHexString(bytes);
    }

    /**
     * DES解密 key-密钥，长度必须为8的倍数
     */
    public static String DESDecode(String res, String key){
        byte[] bytes = new byte[0];
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = secretKeyFactory.generateSecret(desKeySpec);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            bytes = cipher.doFinal(HexUtils.fromHexString(res));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    /**
     * AES加密 key-密钥，长度必须为16
     */
    public static String AESEncode(String src, String key){
        byte[] bytes = new byte[0];
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            bytes = cipher.doFinal(src.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return HexUtils.toHexString(bytes);
    }

    /**
     * AES解密 key-密钥，长度必须为16
     */
    public static String AESDecode(String res, String key){
        byte[] bytes = new byte[0];
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            bytes = cipher.doFinal(HexUtils.fromHexString(res));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }

    /**
     * PBE加密  基于口令的加密 有问题
     *//*
    public static String PBEEncode(String src, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //初始化盐
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);//盐 必须为8
        //口令
        PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec spec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        byte[] bytes = cipher.doFinal(src.getBytes());
        return HexUtils.toHexString(bytes);
    }

    *//**
     * PBE解密  基于口令的加密
     *//*
    public static String PBEDecode(String res, String key) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecureRandom random = new SecureRandom();
        byte[] salt = random.generateSeed(8);//盐 必须为8
        PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec spec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] bytes = cipher.doFinal(HexUtils.fromHexString(res));
        return new String(bytes);
    }*/

   /**
     * RSA加密--初始化密钥
     */
     public static Map getKeyPair(){
         Map map= null;
         try {
             KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
             keyPairGenerator.initialize(512);
             KeyPair keyPair = keyPairGenerator.generateKeyPair();
             RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
             RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
             map = new HashMap();
             map.put("rsaPrivateKey",Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
             map.put("rsaPublicKey",Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
         } catch (NoSuchAlgorithmException e) {
             e.printStackTrace();
         }
         return map;
     }

    /**
     * RSA加密--公钥加密，私钥解密-加密
     */
    public static String rsaPublicKeyEncode(String src,String publicKey){
        byte[] bytes = new byte[0];
        try {
            byte[] key = Base64.getDecoder().decode(publicKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE,rsaPublicKey);
            bytes = cipher.doFinal(src.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * RSA加密--公钥加密，私钥解密-解密
     */
    public static String rsaPrivateKeyDecode(String res,String privateKey){
        byte[] bytes = new byte[0];
        try {
            byte[] decode = Base64.getDecoder().decode(privateKey);
            byte[] decode1 = Base64.getDecoder().decode(res);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decode);
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE,rsaPrivateKey);
            bytes = cipher.doFinal(decode1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return new String(bytes);
    }



}
