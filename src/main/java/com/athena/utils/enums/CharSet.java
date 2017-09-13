/*
 * Copyright (C) 2017 Jack Green
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.athena.utils.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum CharSet {
    NUMERIC(1, "Numeric", 10, "0123456789", (byte) 48, (byte) 57, new byte[0]),
    SPECIAL(2, "Special", 33, "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~", (byte) 32, (byte) 96, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes()),

    LOWER_ALPHABETIC(3, "Alphabetic", 26, "abcdefghijklmnopqrstuvwxyz", (byte) 97, (byte) 122, new byte[0]),
    UPPER_ALPHABETIC(4, "Upper Alphabetic", 26, "ABCDEFGHIJKLMNOPQRSTUVWXYZ", (byte) 65, (byte) 90, new byte[0]),

    LOWER_ALPHANUMERIC(5, "Alphanumeric", 36, "abcdefghijklmnopqrstuvwxyz0123456789", (byte) 48, (byte) 122, ":;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`".getBytes()),
    UPPER_ALPHANUMERIC(6, "Upper Alphanumeric", 36, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", (byte) 48, (byte) 90, ":;<=>?@".getBytes()),

    MIXED_ALPHABETIC(7, "Mixed Alphabetic", 52, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", (byte) 65, (byte) 122, "[\\]^_`".getBytes()),
    MIXED_ALPHANUMERIC(8, "Mixed Alphanumeric", 62, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", (byte) 48, (byte) 122, ":;<=>?@[\\]^_`".getBytes());

    private final int code;
    private final String charsetName;
    private final int length;
    private final String chars;
    private final byte startPos;
    private final byte endPos;
    private final byte[] toSkip;

    private static Map<Integer, CharSet> codeToCharsetMapping;

    CharSet(int code, String charsetName, int length, String chars, byte startPos, byte endPos, byte[] toSkip) {
        this.code = code;
        this.charsetName = charsetName;
        this.length = length;
        this.chars = chars;
        this.startPos = startPos;
        this.endPos = endPos;
        this.toSkip = toSkip;
    }

    public static CharSet getCharSet(int i) {
        if (codeToCharsetMapping == null) {
            initMapping();
        }
        return codeToCharsetMapping.get(i);
    }

    private static void initMapping() {
        codeToCharsetMapping = new HashMap<>();
        for (CharSet s : values()) {
            codeToCharsetMapping.put(s.code, s);
        }
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return charsetName;
    }

    public int getLength() {
        return length;
    }

    public String getChars() {
        return chars;
    }

    public byte getStartPos() {
        return startPos;
    }

    public byte getEndPos() {
        return endPos;
    }

    public byte[] getToSkip() {
        return toSkip;
    }

    public ArrayList<byte[]> getCharsList() {
        ArrayList<byte[]> arrays = new ArrayList<>();

        for (byte b : chars.getBytes()) {
            arrays.add(new byte[]{b});
        }
        return arrays;
    }
}