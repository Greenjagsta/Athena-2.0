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
    Timer calcTime = new Timer();
    private static final int lineCount = FileUtils.getLineCount("input.txt");
    private static int noCracked = -1;
    private static long startTime = System.nanoTime();

    public static void printInit(String version) {
        System.out.println("Starting AthenaHRT version " + version + "\n");
    }


    public static void printDetails(String status, String hashfile_filename, int hashType, int mode) {
        System.out.println("Breaching the mainframe...\n");
        System.out.println("Session...: Athena");
//        printStatus(status);
//        printInput("Input.txt");
//        printHashes(hashfile_filename);
//        printHashType(hashType);
//        printMode(mode);
//        printDevice();

        // Speed
        // input type (current)
        // time started
        // time taken
        // device
    }

//    public static void continuousUpdate();

    private static void printDevice(){
        System.out.println("Threads...: " + Runtime.getRuntime().availableProcessors());//.getProperty("os.name"));
        System.out.println("Memory....: " + Runtime.getRuntime().totalMemory());
        System.out.println("Memory Used: " + System.getProperties().getProperty("os-name"));
    }

    private static void printStatus(String status) {
        System.out.println("Status....: " + status);
    }

    private static void printInput(String inputFile) {
        System.out.println("Input.....: " + inputFile + " (" + FileUtils.getBytes(inputFile) + " bytes");
    }

    private static void printHashes(String hashFile) {
        System.out.println("Hashes....: " + hashFile + " total, " + FileUtils.getUniques(hashFile) + " unique");
    }

    private static void printHashType(int hashType) {
        System.out.println("Hash Type.: " + Hash.getHash(hashType).getName());
    }

    private static void printMode(int mode) {
        System.out.println("Mode......: " + Mode.getMode(mode).getModeName());
    }

    private static void printMemUsed(){
        System.out.println("Memory Used: " + (Runtime.getRuntime().freeMemory()/Runtime.getRuntime().totalMemory()));
    }

    public static void printSpeed(){
        double timeTaken = System.nanoTime() - startTime;
        double hPS = ((float)1/(timeTaken*10E-10));
        System.out.print("\rSpeed....: " + hPS + "MH/s");
        startTime = System.nanoTime();
    }

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
