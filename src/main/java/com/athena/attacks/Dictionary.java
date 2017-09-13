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

import com.athena.utils.FileUtils;
import com.athena.utils.HashManager;
import com.athena.utils.StringUtils;

import java.util.ArrayList;

public class Dictionary extends Attack {
    private String wordlist_filename;

    public Dictionary(String wordlist_filename, String hashes_filename, int hashType) {
        this.wordlist_filename = wordlist_filename;
        super.setHashType(hashType, hashes_filename);
        super.setHashman(new HashManager(hashes_filename));
    }

    @Override
    public void attack() {
        for (byte[] fileBuffer : getNextCandidates())
            for (byte[] candidate : StringUtils.formatFileBytes(fileBuffer)) {
                if (!super.isAllCracked()) {
                    checkAttempt(candidate);
                } else {
                    return;
                }
            }
    }
    
    public ArrayList<byte[]> getNextCandidates() {
        return FileUtils.getFileChunk(wordlist_filename);
    }
}
