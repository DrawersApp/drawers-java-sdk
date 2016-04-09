package org.drawers.bot.crypto;

/**
 * Created by nishant.pathak on 05/04/16.
 */
public interface IDrawersCryptoEngine {
    public static final byte[] ZERO_CTR = new byte[] { 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00 };

    public byte[] aesDecrypt(byte[] key, byte[] ctr, byte[] b);
    public byte[] aesEncrypt(byte[] key, byte[] ctr, byte[] b);
}
