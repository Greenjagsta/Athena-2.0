package com.athena.hashfamily;

import com.athena.hashfamily.DigestEngine;

public abstract class MDHelper extends DigestEngine {
    private boolean littleEndian;
    private byte[] countBuf;
    private byte fbyte;

    public MDHelper(boolean littleEndian, int lenlen) {
        this(littleEndian, lenlen, (byte)0x80);
    }

    private MDHelper(boolean littleEndian, int lenlen, byte fbyte) {
        this.littleEndian = littleEndian;
        countBuf = new byte[lenlen];
        this.fbyte = fbyte;
    }

    protected void makeMDPadding() {
        int dataLen = flush();
        int blen = getBlockLength();
        long currentLength = getBlockCount() * (long)blen;
        currentLength = (currentLength + (long)dataLen) * 8L;
        int lenlen = countBuf.length;
        if (littleEndian) {
            encodeLEInt((int)currentLength, countBuf, 0);
            encodeLEInt((int)(currentLength >>> 32), countBuf, 4);
        } else {
            encodeBEInt((int)(currentLength >>> 32),
                    countBuf, lenlen - 8);
            encodeBEInt((int)currentLength,
                    countBuf, lenlen - 4);
        }
        int endLen = (dataLen + lenlen + blen) & ~(blen - 1);
        update(fbyte);
        for (int i = dataLen + 1; i < endLen - lenlen; i ++)
            update((byte)0);
        update(countBuf);
    }

    private static final void encodeLEInt(int val, byte[] buf, int off) {
        buf[off + 0] = (byte)val;
        buf[off + 1] = (byte)(val >>> 8);
        buf[off + 2] = (byte)(val >>> 16);
        buf[off + 3] = (byte)(val >>> 24);
    }

    private static final void encodeBEInt(int val, byte[] buf, int off) {
        buf[off + 0] = (byte)(val >>> 24);
        buf[off + 1] = (byte)(val >>> 16);
        buf[off + 2] = (byte)(val >>> 8);
        buf[off + 3] = (byte)val;
    }
}