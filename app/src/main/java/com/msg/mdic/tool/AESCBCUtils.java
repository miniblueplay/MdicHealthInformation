package com.msg.mdic.tool;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESCBCUtils {
    private static final String TAG = "AESCBCUtils";

    // CBC(Cipher Block Chaining, 加密快鏈)模式，PKCS5Padding補碼方式
    // AES是加密方式 CBC是作業模式 PKCS5Padding是填充模式
    /**
     * 加解密演算法/作業模式/填充方式
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    // AES 加密
    private static final String AES = "AES";

    /**
     * AES 加密
     *
     * @param strKey            加密密鑰
     * @param strClearText      待加密內容
     * @param mstrIvParameter   密鑰偏移量
     * @return 回傳Base64轉碼后的加密資料
     */
    public static String encrypt_AES(String strKey, String strClearText, String mstrIvParameter){

        try {
            byte[] raw = strKey.getBytes();
            // 創建AES密鑰
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            // 創建密碼器
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            // 創建偏移量
            IvParameterSpec iv = new IvParameterSpec(mstrIvParameter.getBytes());
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            // 執行加密操作
            byte[] cipherText = cipher.doFinal(strClearText.getBytes());
            //Log.d(TAG, "encrypt result(not BASE64): " + cipherText.toString());
            String strBase64Content = Base64.encodeToString(cipherText, Base64.DEFAULT); // encode it by BASE64 again
            //Log.d(TAG, "encrypt result(BASE64): " + strBase64Content);
            strBase64Content = strBase64Content.replaceAll(System.getProperty("line.separator"), "");

            return strBase64Content;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AES 解密
     *
     * @param strKey            解密密鑰
     * @param strCipherText      待解密內容
     * @param mstrIvParameter   偏移量
     * @return 回傳Base64轉碼后的加密資料
     */
    public static String decrypt(String strKey, String strCipherText, String mstrIvParameter) throws Exception {

        try {
            byte[] raw = strKey.getBytes("ASCII");
            // 創建AES秘鑰
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            // 創建密碼器
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            // 創建偏移量
            IvParameterSpec iv = new IvParameterSpec(mstrIvParameter.getBytes());
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 執行解密操作
            byte[] cipherText = Base64.decode(strCipherText, Base64.DEFAULT); // decode by BASE64 first
            Log.d(TAG, "BASE64 decode result(): " + cipherText.toString());
            byte[] clearText = cipher.doFinal(cipherText);
            String strClearText = new String(clearText);
            Log.d(TAG, "decrypt result: " + strClearText);

            return strClearText;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
