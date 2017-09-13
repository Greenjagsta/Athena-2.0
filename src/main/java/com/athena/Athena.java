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

package com.athena;

import com.athena.attacks.Dictionary;
import com.athena.attacks.Mask;
import com.athena.attacks.Probabilistic;
import com.athena.utils.Output;
import com.athena.utils.Timer;
import com.athena.utils.enums.Mode;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Athena {
    @Option(name = "-i", aliases = "--input", usage = "Input file to use")
    private static String hashFile_filename = "input.txt";
    @Option(name = "-d", aliases = "--dictionary-file", handler = StringArrayOptionHandler.class, usage = "Dictionary file to use")
    private static String[] wordlist_filename;
    @Option(name = "-m", aliases = "--mode", usage = "Attack mode to use")
    private static int mode;
    @Option(name = "-h", aliases = "--hash-type", usage = "Hash type in input file")
    private static int hashType;
    @Option(name = "-k", aliases = "--mask", usage = "Mask to use")
    private static String maskString;
    @Option(name = "--increment", usage = "Increment mask")
    private static boolean increment = false;
    //TODO Add --increment for mask attack (only if mask is the same (static chars can be at beginning or end though))
    
    private static final String VERSION = "2.0";
    private static Timer timer;

    private void parseArgs(String[] args) {
        try {
            CmdLineParser clp = new CmdLineParser(this);
            clp.parseArgument(args);
            //Output.printStatus("Initialising", hashFile_filename, hashType, mode, 0);

            //Change this to method that checks input for errors
            if (Mode.getMode(mode).requiresDict2() && wordlist_filename[1] == null) {
                throw new IOException();
            }

            if (Mode.getMode(mode).requiresMask() && maskString == null) {
                throw new IOException();
            }
        } catch (CmdLineException ex) {
            Logger.getLogger(Athena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Athena.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void initAttack() {
        timer = new Timer();
        timer.startTimer();

        switch (mode) {
            case 101:
                Dictionary dictionary = new Dictionary(wordlist_filename[0], hashFile_filename, hashType);
                dictionary.attack();
                break;

            case 102:
                Mask mask = new Mask(maskString, increment, hashFile_filename, hashType);
                mask.attack();
                break;

            case 105:
                Probabilistic probabilistic = new Probabilistic(hashFile_filename, hashType);
                probabilistic.attack();
                break;

            default:
                break;
        }
        timer.stopTimer();
    }

    private static void initPostProcessing() {
        System.out.println(
                        "\nStarted: " + timer.getStartDate() +
                        "\nStopped: " + timer.getEndDate() +
                        " (" + timer.getElapsedTimeSeconds() + " seconds)"
        );
    }

    public static void main(String[] args) {
        Output.printInit(VERSION);
        new Athena().parseArgs(args);
        initAttack();
        initPostProcessing();
    }
}