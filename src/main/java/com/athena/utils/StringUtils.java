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

package com.athena.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringUtils {
    private static StringBuilder sb = new StringBuilder();
    private static ArrayList<byte[]> arrlist = new ArrayList<>();

    public static String byteArrayToHexString(byte[] b) {
        sb.setLength(0);
        for (byte aB : b) {
            sb.append(Integer.toHexString((aB & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToString(byte[] b) {
        try {
            return new String(b, "ASCII");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(StringUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static ArrayList<byte[]> formatFileBytes(byte[] b) {
        int high, low = 0;

        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0) {
                return arrlist;
            }

            if (b[i] == 13) {
                high = i;
                arrlist.add(Arrays.copyOfRange(b, low, high));
                low = high + 2;
            }

            if (i == b.length - 1) {
                high = i + 1;
                if (low > high) {
                    low = high;
                }
                arrlist.add(Arrays.copyOfRange(b, low, high));
            }
        }
        return arrlist;
    }

    public static byte[] stripList(List<byte[]> list) {
        int length = 0;
        for (byte[] b : list) {
            length += b.length;
        }

        byte[] result = Arrays.copyOf(list.get(0), length);
        int offset = list.get(0).length;

        for (int i = 1; i < list.size(); i++) {
            System.arraycopy(list.get(i), 0, result, offset, list.get(i).length);
            offset += list.get(i).length;
        }
        return result;
    }
}