package com.mirrorchannelth.internship.net.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
*
* @author Objectoop
*/
public class MCrypt {

    private String iv = "objectoop1234567";
    private String SecretKey = "1234567objectoop";
    private IvParameterSpec ivspec;
    private SecretKeySpec keyspec;
    private Cipher cipher;

    public MCrypt() {
        ivspec = new IvParameterSpec(iv.getBytes());

        keyspec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] encrypted = null;

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }

    public byte[] decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) {
            throw new Exception("Empty string");
        }

        byte[] decrypted = null;

        try {
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            decrypted = cipher.doFinal(hexToBytes(code));
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }

    public static String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        }

        int len = data.length;
        String str = "";
        for (int i = 0; i < len; i++) {
            if ((data[i] & 0xFF) < 16) {
                str = str + "0" + Integer.toHexString(data[i] & 0xFF);
            } else {
                str = str + Integer.toHexString(data[i] & 0xFF);
            }
        }
        return str;
    }

    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }

    //##############################
    public static String encode(String str) {
        String encrypted = null;
        MCrypt mcrypt = new MCrypt();
        try {
            String uuid = UUID.randomUUID().toString();
            encrypted = MCrypt.bytesToHex(mcrypt.encrypt(uuid + "@@" + str + "@@" + (new Date()).getTime()));
        } catch (Exception e) {
            encrypted = "";
            System.out.println(e);
        }
        return encrypted;
    }

    public static String decode(String str) {

        MCrypt mcrypt = new MCrypt();

        String decrypted = null;
        try {
            String _tmp = new String(mcrypt.decrypt(str));
            String _ar[] = _tmp.split("@@");
            decrypted = _ar[1];
        } catch (Exception e) {
            decrypted = "";
            System.out.println(e);
        }
        
        return decrypted;

    }
    
    public static String hashStringToHex(String string) {
    	MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        md.update(string.getBytes());
        byte bytes[] = md.digest();
        return MCrypt.bytesToHex(bytes);
    }
}