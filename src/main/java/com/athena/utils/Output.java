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

import com.athena.utils.enums.Mode;
import com.athena.hashfamily.Hash;


public class Output {
    private static final int lineCount = FileUtils.getLineCount("input.txt");
    private static int noCracked = 0;

    public static void printInit(String version) {
        System.out.println("Starting AthenaHRT version " + version + "\n");
    }

    public static void printStatus(String status, String hashfile_filename, int hashType, int mode) {

        System.out.print(
                "Session...: " + "Athena" +
                        "\nStatus....: " + status +
                        "\nInput.....: " + hashfile_filename + " (" + FileUtils.getBytes(hashfile_filename) + " bytes)" +
                        "\nHashes....: " + FileUtils.getLineCount(hashfile_filename) + " total, " + FileUtils.getUniques(hashfile_filename) + " unique" +
                        "\nHash Type.: " + Hash.getHash(hashType).getName() +
                        "\nMode......: " + Mode.getMode(mode).getModeName() +
                        "\nRecovered.: " + "0/" + FileUtils.getLineCount(hashfile_filename) + " (0%)");
    }
    // Add backspace


    public static void noRecoveredUpdate() {
        noCracked++;
        System.out.print("\rRecovered.: " + noCracked + "/" + lineCount + " (" + (float) ((int) (((float) noCracked / lineCount) * 10000)) / 100 + "%)");
    }

    public static void printCracked(String hash, String plaintext) {
        System.out.println(hash + ":" + plaintext);
    }

    public static void printRemoved(int amount) {
        System.out.println(amount + " hashes removed from file");
    }
}
