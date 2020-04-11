package com.wangh7.wht.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

public class DesUtils {
    static AlgorithmParameterSpec iv = null;
    private static SecretKey key = null;
    private static String desIv = "giftcard";
    private static String desKey = "giftcard";

    public DesUtils() throws Exception {
        byte[] DESKey = desKey.getBytes();
        byte[] DESIv = desIv.getBytes();
        DESKeySpec desKeySpec = new DESKeySpec(DESKey);
        iv = new IvParameterSpec(DESIv);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("DES");
        key = secretKeyFactory.generateSecret(desKeySpec);
    }

    public String encode(String data) throws Exception {
        Cipher enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        enCipher.init(Cipher.ENCRYPT_MODE,key,iv);
        byte[] pasByte = enCipher.doFinal(data.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    public String decode(String data) throws Exception {
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE,key,iv);
        byte[] pasByte = deCipher.doFinal(Base64.getDecoder().decode(data));
        return new String(pasByte, "utf-8");
    }

//    public static void main(String[] args) throws Exception{
//        DesUtils desUtils = new DesUtils("apcdefgh");
//        String data = "1q2w3asdfasdfasdfasfasfe";
//        String encode = desUtils.encode(data);
//        String decode = desUtils.decode(encode);
//        System.out.println(encode+' '+decode);
//
//    }
}
