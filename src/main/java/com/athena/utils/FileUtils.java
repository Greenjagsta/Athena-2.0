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
import java.nio.ByteBuffer;
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

    public static int getLineCount(File file) {
        try {
            is = new FileInputStream(file);
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

    public static int getBytes(File file) {
        try {
            is = new FileInputStream(file);
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

    public static int getUniques(File file) {
        try (Stream<String> stream = Files.lines(Paths.get(file.getName()))) {
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

    public static ArrayList<byte[]> getFileChunk(File file) {
        ArrayList<byte[]> barrays = new ArrayList<>();

        try (FileInputStream f = new FileInputStream(file)){
            FileChannel ch = f.getChannel();
            MappedByteBuffer mb;
            mb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
            int nGet;

            while (mb.hasRemaining()) {
                nGet = Math.min(mb.remaining(), 8000);
                byte[] barray = new byte[nGet];
                mb.get(barray, 0, nGet);
                barrays.add(barray);
            }

            ch.close();
            f.close();

            return barrays;
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void write(File file, ArrayList<String> data) {
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")){
            FileChannel ch = raf.getChannel();

            if (data.size() == 0) {
                ByteBuffer bb = ByteBuffer.allocate(1);
                bb.put("".getBytes());
                ch.write(bb);
            } else {
                ByteBuffer bb = ByteBuffer.allocate(96);
                for (int i = 0; i < data.size(); i++) {
                    bb.clear();
                    bb.put(data.get(i).getBytes());
                    if (i != data.size() - 1) {
                        bb.put(System.getProperty("line.separator").getBytes());
                    }
                    bb.flip();
                    while (bb.hasRemaining()) {
                        ch.write(bb);
                    }
                }
            }

            ch.close();
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}