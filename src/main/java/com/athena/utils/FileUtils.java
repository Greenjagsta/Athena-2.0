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

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class FileUtils {
    private static InputStream is;

    static int getLineCount(String filename) {
        try {
            is = new FileInputStream(filename);
            LineNumberReader lnr = new LineNumberReader(new BufferedReader(new InputStreamReader(is, "UTF-8")));
            lnr.skip(Long.MAX_VALUE);
            return lnr.getLineNumber() + 1;

        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static int getBytes(String filename) {
        try {
            is = new FileInputStream(filename);
            return is.available();

        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    static int getUniques(String filename) {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            return (int)stream
                    .distinct()
                    .count();

        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public static byte[] getFileChunk_OLD(String filename) {
        FileInputStream f = null;
        try {
            f = new FileInputStream(filename);
            FileChannel ch = f.getChannel();
            MappedByteBuffer mb;
            mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
            byte[] barray = new byte[8000];
            int nGet;

            while (mb.hasRemaining()) {
                nGet = Math.min(mb.remaining(), 8000);
                mb.get(barray, 0, nGet);
            }
            return barray;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    public static ArrayList<byte[]> getFileChunk(String filename) {
        FileInputStream f = null;
        ArrayList<byte[]> barrays = new ArrayList<>();

        try {
            f = new FileInputStream(filename);
            FileChannel ch = f.getChannel();
            MappedByteBuffer mb;
            mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
            int nGet;

            while (mb.hasRemaining()) {
                byte[] barray = new byte[8000];
                nGet = Math.min(mb.remaining(), 8000);
                mb.get(barray, 0, nGet);
                barrays.add(barray);
            }
            return barrays;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (f != null) {
                    f.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}