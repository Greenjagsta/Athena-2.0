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

package com.athena.utils.output;

import com.athena.hashfamily.Hash;
import com.athena.utils.FileUtils;
import com.athena.utils.Timer;
import com.athena.utils.enums.Mode;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;

import static org.fusesource.jansi.Ansi.ansi;


public class Stdout {
    private static final String SPACING = "                    ";
    private static String hashfile_filename;
    private static int hashfile_bytes;
    private static int hashfile_uniques;
    private static int hashType;
    private static int mode;
    private static int lineCount;
    private static int complexity = 0;
    private static double hPS = 0;
    private static int recovered = 0;
    private static String current;
    private static long startTime = System.nanoTime();

    public static void initDetails(File hashfile_file_in, int hashType_in, int mode_in) {
        hashfile_filename = hashfile_file_in.getName();
        lineCount = FileUtils.getLineCount(hashfile_file_in);
        hashfile_bytes = FileUtils.getBytes(hashfile_file_in);
        hashfile_uniques = FileUtils.getUniques(hashfile_file_in);
        hashType = hashType_in;
        mode = mode_in;
    }

    public static void printInit(String version) {
        System.out.println("Starting AthenaHRT version " + version + "\n");
    }

    public static void printDetails(String status) {
        System.out.println("Session....: Athena");
        printStatus(status);
        printMode();
        printInput();
        printCurrent();
        printHashes();
        printHashType();
        printSpeed();
        printETA();
        printRecovered();
        //printDevice();

        resetCursorBeginning();
    }

    private static void printDevice(){
        System.out.println("Threads....: " + Runtime.getRuntime().availableProcessors());//.getProperty("os.name"));
        System.out.println("Memory.....: " + Runtime.getRuntime().totalMemory());
        System.out.println("Memory Used: " + System.getProperties().getProperty("os-name"));
    }

    private static void printStatus(String status) {
        System.out.println("Status.....: " + status + SPACING);
    }

    private static void printInput() {
        System.out.println("Input......: " + hashfile_filename + " (" + hashfile_bytes + " bytes)" + SPACING);
    }

    private static void printHashes() {
        System.out.println("Hashes.....: " + lineCount + " total, " + hashfile_uniques + " unique"  + SPACING);
    }

    private static void printHashType() {
        if (hashType == 0) {
            System.out.println("Hash Type..: Not Specified");
        } else {
            System.out.println("Hash Type..: " + Hash.getHash(hashType).getName() + SPACING);
        }
    }

    private static void printMode() {
        System.out.println("Mode.......: " + Mode.getMode(mode).getModeName() + SPACING);
    }

    private static void printMemUsed(){
        System.out.println("Memory Used: " + Runtime.getRuntime().freeMemory()/Runtime.getRuntime().totalMemory()  + SPACING);
    }

    private static void printSpeed(){
        double timeTaken = System.nanoTime() - startTime;
        hPS = Math.round(((float) 1 / (timeTaken * 10E-10)) * 100.0) / 100.0;
        System.out.println("\rSpeed......: " + hPS + "MH/s" + SPACING);
        startTime = System.nanoTime();
    }

    private static void printRecovered() {
        System.out.print("\rRecovered..: " + recovered + "/" + lineCount + " (" + (float) ((int) (((float) recovered / lineCount) * 10000)) / 100 + "%)"  + SPACING);
    }

    private static void printCurrent() {
        switch (mode) {
            case 101:
                complexity = FileUtils.getLineCount(new File(current));
                System.out.println("Dictionary.: " + current + SPACING);
                break;

            case 102:
                System.out.println("Mask.......: " + current + SPACING);
                break;

            case 105:
                System.out.println("Pattern....: " + current + SPACING);
                break;

            default:
                System.out.println("Current....: " + current + SPACING);
                break;
        }
    }

    private static void printETA() {
        if (complexity <= 0 || hPS <= 0) {
            System.out.println("\rETA........: " + "--:--:--");
        } else {
            System.out.println("\rETA........: " + Timer.formatTime((int) Math.round(complexity / (hPS * 1000000))));
            complexity -= 1000000; //complexity -= (hPS * 1000000);
            if (complexity < 1) {
                complexity = 1;
            }
        }
    }

    public static void printCracked(String hash, String plaintext) {
        System.out.println(hash + ":" + plaintext);
    }

    public static void printRemoved(int amount) {
        System.out.println(amount + " hashes removed from file");
    }

    public static void ansiSysInstall() {
        AnsiConsole.systemInstall();
    }

    private static void resetCursorBeginning() {
        System.out.println(ansi().cursorUp(10).cursorToColumn(0));
    }

    public static void resetCursorEnd() {
        System.out.println(ansi().cursorDown(10));
    }

    public static void updateRecovered() {
        recovered++;
    }

    public static void updateHashType(int hashType_in) {
        hashType = hashType_in;
    }

    public static void updateCurrent(String current_in) {
        current = current_in;
    }

    public static void updateComplexity(int complexity_in) {
        complexity = complexity_in;
    }
}