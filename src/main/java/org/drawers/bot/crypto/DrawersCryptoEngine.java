package org.drawers.bot.crypto;


import org.drawers.bot.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by nishant.pathak on 28/03/16.
 */
public final class DrawersCryptoEngine implements IDrawersCryptoEngine {
    @Override
    public byte[] aesEncrypt(byte[] key, byte[] ctr, byte[] b) {

        if (ctr == null)
            ctr = ZERO_CTR;

        try {
            IvParameterSpec iv = new IvParameterSpec(ctr);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(b);
            return Base64.encode(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }

    public String aesDecrypt(String incomingStream) {
        return new String(aesDecrypt(KEY, null, incomingStream.getBytes()));
    }

    // TODO: implement session based encryption decryption
    public static final byte[] KEY = new byte[]{0x44, 0x52, 0x41, 0x57,
            0x45, 0x52, 0x53, 0x52, 0x45, 0x57, 0x41, 0x52, 0x44, 0x00, 0x00,
            0x00};

    public String aesEncrypt(String incomingStream) {
        return new String(aesEncrypt(KEY, null, incomingStream.getBytes()));
    }


    @Override
    public byte[] aesDecrypt(byte[] key, byte[] ctr, byte[] b) {

        if (ctr == null)
            ctr = ZERO_CTR;
        try {
            IvParameterSpec iv = new IvParameterSpec(ctr);
            SecretKeySpec skekSpec = new SecretKeySpec(key, "AES");


            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skekSpec, iv);

            return cipher.doFinal(Base64.decode(b, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return b;
        }
    }
}
