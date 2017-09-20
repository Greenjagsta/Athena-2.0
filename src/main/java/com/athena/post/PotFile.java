package com.athena.post;

import com.athena.utils.ArrayUtils;
import com.athena.utils.FileUtils;
import com.athena.utils.Output;
import com.athena.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PotFile {
    private static final File POTFILE = new File("athena.pot");
    private static final ArrayList<String> ENTRIES = new ArrayList<>();
    private final String SEPERATOR = ":";

    public PotFile() {
        try {
            if (!POTFILE.exists()) {
                POTFILE.createNewFile();
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(ArrayList<String> cracked, ArrayList<byte[]> plains) {
        if (cracked.isEmpty()) {
            return;
        }

        ENTRIES.clear();
        readEntries();

        for (int i = 0; i < cracked.size(); i++) {
            if (!ENTRIES.contains(cracked.get(i) + SEPERATOR + StringUtils.byteArrayToString(plains.get(i)))) {
                ENTRIES.add(cracked.get(i) + SEPERATOR + StringUtils.byteArrayToString(plains.get(i)));
            }
        }

        sort();
        FileUtils.write(POTFILE, ENTRIES);

    }

    public static ArrayList<byte[]> remove(ArrayList<byte[]> hashes) {
        for (byte[] fileBuffer : FileUtils.getFileChunk(POTFILE)) {
            for (byte[] entry : ArrayUtils.formatFileBytes(fileBuffer)) {
                ENTRIES.add(StringUtils.byteArrayToString(entry));
            }
        }

        List<String> potEntries = ENTRIES
                .stream()
                .map(e -> e.split(":")[0])
                .collect(Collectors.toList());

        ArrayList<byte[]> dupHashes = (ArrayList<byte[]>) hashes
                .stream()
                .filter(h -> potEntries.contains(StringUtils.byteArrayToHexString(h)))
                .collect(Collectors.toList());

        hashes.removeAll(dupHashes);

        Output.printRemoved(dupHashes.size());
        return hashes;
    }

    private void sort() {
        if (ENTRIES.isEmpty()) {
            return;
        }

        Collections.sort(ENTRIES);
    }

    private void readEntries() {
        for (byte[] fileBuffer : FileUtils.getFileChunk(POTFILE)) {
            for (byte[] entry : ArrayUtils.formatFileBytes(fileBuffer)) {
                ENTRIES.add(StringUtils.byteArrayToString(entry));
            }
        }
    }
}