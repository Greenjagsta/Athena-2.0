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

package com.athena.attacks;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;
import com.athena.utils.HashManager;
import com.athena.utils.enums.Mode;

import java.io.File;
import java.util.ArrayList;

public class Dictionary extends Attack {
    private File wordlist;

    public Dictionary(String wordlist_filename, ArrayList<byte[]> hashes, int hashType) {
        this.wordlist = new File(wordlist_filename);
        super.setMode(Mode.DICTIONARY.getCode());
        super.setHashType(hashType, hashes);
        super.setHashman(new HashManager(hashes));
        super.initDigestInstance();

    }

    @Override
    public void attack() {
        for (byte[] fileBuffer : getNextCandidates()) {
            for (byte[] candidate : ArrayUtils.formatFileBytes(fileBuffer)) {
                if (!super.isAllCracked()) {
                    checkAttempt(candidate);
                } else {
                    return;
                }
            }
        }
    }

    public ArrayList<byte[]> getNextCandidates() {
        return FileUtils.getFileChunk(wordlist);
    }
}