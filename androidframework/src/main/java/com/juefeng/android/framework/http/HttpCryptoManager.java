package com.juefeng.android.framework.http;

import android.net.Uri;
import android.util.Base64;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.base.Constant;
import com.juefeng.android.framework.common.util.*;
import com.juefeng.android.framework.http.interfaces.IHttpCryptoManager;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/25
 * Time: 10:16
 * Description:http request crypto manager
 */
public abstract class HttpCryptoManager implements IHttpCryptoManager {

    private static Map<String, String> map = new HashMap<>();

    public static final String EncryptoKey = "crypto";
    public static final String EncryptoValue = "yes";

    public static final String DES = "DES";

    private static final int PASSWD_LENGTH = 8;

    private static EncryptoEntity encryptoEntity;

    @Override
    public Boolean isCrypto() {
        if (encryptoEntity != null) {
            return encryptoEntity.isEncrypto();
        }
        return false;
    }

    @Override
    public String deCrypto(String content, String time) {
        if (StringUtil.isEmpty(content)) {
            return "";
        }
        if (cryptoText() == null || cryptoText().isEmpty() || cryptoText().length() < PASSWD_LENGTH) {
            return null;
        }
        String passwd = getSecretKey(cryptoText(), time);
        if (passwd == null) {
            return content;
        }
        byte[] bs = Base64.decode(content.getBytes(Charset.forName("UTF-8")), Base64.DEFAULT);
        bs = decrypt(bs, passwd);
        if (bs != null) {
            return new String(bs, Charset.forName("UTF-8"));
        }
        return null;
    }

    @Override
    public String enCrypto(String content, String time) {
        if (StringUtil.isEmpty(content)) {
            return "";
        }
        if (cryptoText() == null || cryptoText().isEmpty() || cryptoText().length() < PASSWD_LENGTH) {
            return null;
        }
        String passwd = getSecretKey(cryptoText(), time);
        if (passwd == null) {
            return content;
        }
        byte[] bs = encrypt(content.getBytes(Charset.forName("UTF-8")), passwd);
        if (bs != null) {
            bs = Base64.encode(bs, Base64.DEFAULT);
            return new String(bs, Charset.forName("UTF-8"));
        }
        return null;
    }

    @Override
    public String cryptoText() {
        if (encryptoEntity != null) {
            return encryptoEntity.getEncryptoText();
        }
        return null;
    }

    @Override
    public Map<String, String> initHeader(String time) {
        map.put(EncryptoKey, EncryptoValue);
        map.put(Constant.CURRENT_TIME, time);
        return map;
    }

    @Override
    public void setEncrypto(EncryptoEntity encrypto) {
        try {
            encryptoEntity = encrypto;
        } catch (Exception e) {
            LogUtil.w("HttpCryptoManager init error!!!", e);
        }
    }

    /**
     * encrypt text
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    private static byte[] encrypt(byte[] datasource, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //Create a key factory and take it convert to DESKeySpec
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher do encrypt
            Cipher cipher = Cipher.getInstance(DES);
            //use password init Cipher
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //do encrypt and return data
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * decrypt text
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     */
    private static byte[] decrypt(byte[] src, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //Create a key factory and take it convert to DESKeySpec
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher do decrypt
            Cipher cipher = Cipher.getInstance(DES);
            //use password init Cipher
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            //do encrypt and return data
            return cipher.doFinal(src);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get secret key
     *
     * @param key
     * @param time
     * @return
     */
    private String getSecretKey(String key, String time) {
        try {
            if (StringUtil.isEmpty(key)) {
                return null;
            }
            if (StringUtil.isEmpty(time) || time.length() < PASSWD_LENGTH) {
                throw new Exception();
            }
            if (time.length() > PASSWD_LENGTH) {
                time = time.substring(time.length() - PASSWD_LENGTH);
            }
            char[] chars = key.toCharArray();
            StringBuilder passwd = new StringBuilder();
            for (int i = 0; i < time.length(); i++) {
                int t = Integer.parseInt(String.valueOf(time.charAt(i)));
                for (int j = i * PASSWD_LENGTH; j < chars.length; j++) {
                    if (j + t >= chars.length) {
                        passwd.append(chars[chars.length - 1]);
                        break;
                    } else {
                        passwd.append(chars[j + t % PASSWD_LENGTH]);
                        break;
                    }
                }
            }
            return passwd.toString();
        } catch (Exception e) {
            return key.substring(0, PASSWD_LENGTH);
        }

    }
}
