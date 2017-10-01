package com.athena.attacks;

import com.athena.attacks.extensions.CounterList;
import com.athena.attacks.extensions.HashManager;
import com.athena.attacks.extensions.rules.RulesProcessor;
import com.athena.utils.*;
import com.athena.utils.enums.CharSet;
import com.athena.utils.output.Stdout;

import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Probabilistic extends Attack {
    private final File PROBFILE = new File("resources/prob.txt");
    private final File WORDFILE = new File("resources/words.txt");
    private final File NAMEFILE = new File("resources/names.txt");
    private final File SURNAMEFILE = new File("resources/surnames.txt");

    private ArrayList<byte[]> words;
    private ArrayList<byte[]> names;
    private ArrayList<byte[]> surnames;
    private ArrayList<byte[]> candidates;
    private CounterList<byte[]> candidateElements;

    private HashMap<Byte, Byte[]> l33tMap;

    private boolean complexityUpdateRequired = true;
    private int currentIndex = 0;

    public Probabilistic(ArrayList<byte[]> hashes, int hashType, String[] rules) {
        super.setHashType(hashType, hashes);
        super.setHashman(new HashManager(hashes));
        super.setRulesProcessor(new RulesProcessor(rules));
        super.initDigestInstance();

        Stdout.printDetails("Active");

        this.candidateElements = new CounterList<>();
        this.candidates = new ArrayList<>();
        this.words = new ArrayList<>();
        this.names = new ArrayList<>();
        this.surnames = new ArrayList<>();
        this.l33tMap = new HashMap<>();

        initElements();
        initL33tMap();
        initCandidates();
    }

    @Override
    public void attack() {
        while (isMoreCandidates()) {
            if (complexityUpdateRequired) {
                Stdout.updateComplexity(candidateElements.size() * FileUtils.getLineCount(PROBFILE));
                complexityUpdateRequired = false;
            }

            while (candidateElements.hasMoreChunks()) {
                if (!super.isAllCracked()) {
                    super.checkAttempt(candidateElements.getChunk());
                } else {
                    return;
                }
            }
            /*for (int i = 0; i < candidateElements.size(); i++) {
                if (!super.isAllCracked()) {
                    super.checkAttempt(candidateElements.get(i));
                } else {
                    return;
                }
            }*/
        }
    }

    private boolean isMoreCandidates() {
        try {
            if (currentIndex < candidates.size()) {
                byte[] currentCandidate = candidates.get(currentIndex);
                candidateElements.clear();

                Stdout.updateCurrent(StringUtils.byteArrayToString(currentCandidate));
                parseCandidate(currentCandidate);
                currentIndex++;
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void parseCandidate(byte[] candidate) {
        ArrayList<byte[]> elements = ArrayUtils.split(candidate, (byte) 33);

        for (byte[] element : elements) {
            switch (element[0]) {
                case 108:
                    addStaticChars(element[0], element);
                    break;
                case 100:
                    addStaticChars(element[0], element);
                    break;
                case 115:
                    addStaticChars(element[0], element);
                    break;
                case 117:
                    addStaticChars(element[0], element);
                    break;
                case 110:
                    addDynamicChars(names, element);
                    break;
                case 119:
                    addDynamicChars(words, element);
                    break;
                // ** surnames ATM 'N', needs to change
                case 78:
                    addDynamicChars(surnames, element);
                default:
                    break;
            }
        }
    }

    private void addStaticChars(byte b, byte[] element) {
        List<byte[]> charset;

        switch (b) {
            case 108:
                charset = CharSet.LOWER_ALPHABETIC.getCharsList();
                break;
            case 100:
                charset = CharSet.NUMERIC.getCharsList();
                break;
            case 115:
                charset = CharSet.SPECIAL.getCharsList();
                break;
            case 117:
                charset = CharSet.UPPER_ALPHABETIC.getCharsList();
                break;
            default:
                charset = new ArrayList<>();
        }

        if (element[element.length - 1] != b) {
            CounterList<byte[]> nums = new CounterList<>();
            ArrayList<byte[]> temp = new ArrayList<>();
            ArrayList<byte[]> result = new ArrayList<>();

            int repeatLength = element[element.length - 1] - 48;
            for (int i = 0; i < element.length - 1; i++) {
                nums.add(charset);
            }

            for (int i = 0; i < nums.size(); i++) {
                int count = 0;
                byte[] arr = nums.get(i);

                for (int j = 1; j < arr.length; j++) {
                    if (arr[0] == arr[j]) {
                        count++;
                    }
                }

                if (count != arr.length - 1) {
                    temp.add(arr);
                } else if (arr.length == 1) {
                    temp.add(arr);
                }
            }

            for (byte[] t : temp) {
                byte[] resultArray = new byte[t.length * repeatLength];
                for (int i = 0; i < repeatLength; i++) {
                    System.arraycopy(t, 0, resultArray, i * t.length, t.length);
                }
                result.add(resultArray);
            }
            candidateElements.add(result);
        } else {
            for (byte ignored : element) {
                candidateElements.add(charset);
            }
        }
    }

    private void addDynamicChars(ArrayList<byte[]> arrayList, byte[] element){
        final int length;
        switch (element[element.length - 1]) {
            case 76:
                length = element[element.length - 2] - 48;
                candidateElements.add(l33tify(arrayList.stream().filter(w -> w.length == length).collect(Collectors.toList())));
                break;
            case 67:
                length = element[element.length - 2] - 48;
                candidateElements.add(capitalise(arrayList.stream().filter(w -> w.length == length).collect(Collectors.toList())));
                break;
            case 85:
                length = element[element.length - 2] - 48;
                candidateElements.add(uppercase(arrayList.stream().filter(w -> w.length == length).collect(Collectors.toList())));
                break;
            default:
                length = element[element.length - 1] - 48;
                candidateElements.add(arrayList.stream().filter(w -> w.length == length).collect(Collectors.toList()));
                break;
        }
    }

    //TODO - Implement this
    private List<byte[]> l33tify(List<byte[]> candidates) {
//        for (int i = 0; i < candidates.size(); i++) {
//            byte[] word = candidates.get(i);
//            for (int j = 0; j < word.length; j++) {
//                if (dict.get((int) word[j]) != null) {
//                    word[j] = (byte) (int) dict.get((int) word[j]);
//                }
//            }
//        }
        return candidates;
    }

    private List<byte[]> capitalise(List<byte[]> candidates) {
        for (int i = 0; i < candidates.size(); i++) {
            byte[] word = candidates.get(i);
            word[0] -= 32;
            candidates.set(i, word);
        }
        return candidates;
    }

    private List<byte[]> uppercase(List<byte[]> candidates) {
        for (int i = 0; i < candidates.size(); i++) {
            byte[] word = candidates.get(i);
            for (int j = 0; j < word.length; j++) {
                word[j] -= 32;
            }
            candidates.set(i, word);
        }
        return candidates;
    }

    private void initL33tMap() {
        l33tMap.put((byte) 97, new Byte[]{(byte) 52});   // A -> 4
        l33tMap.put((byte) 98, new Byte[]{(byte) 56});   // B -> 8
        l33tMap.put((byte) 101, new Byte[]{(byte) 51});  // E -> 3
        l33tMap.put((byte) 103, new Byte[]{(byte) 54});  // G -> 6
        l33tMap.put((byte) 105, new Byte[]{(byte) 49});  // I -> 1
        l33tMap.put((byte) 111, new Byte[]{(byte) 48});  // O -> 0
        l33tMap.put((byte) 115, new Byte[]{(byte) 53});  // S -> 5
        l33tMap.put((byte) 116, new Byte[]{(byte) 55});  // T -> 7
        l33tMap.put((byte) 122, new Byte[]{(byte) 50});  // Z -> 2
    }

    private void initCandidates() {
        try {
            for (byte[] fileBuffer : FileUtils.getFileChunk(PROBFILE)) {
                candidates.addAll(ArrayUtils.formatFileBytes(fileBuffer));
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void initElements() {
        try {
            for (byte[] fileBuffer : FileUtils.getFileChunk(WORDFILE)) {
                words.addAll(ArrayUtils.formatFileBytes(fileBuffer));
            }
            for (byte[] fileBuffer : FileUtils.getFileChunk(NAMEFILE)) {
                names.addAll(ArrayUtils.formatFileBytes(fileBuffer));
            }
            for (byte[] fileBuffer: FileUtils.getFileChunk(SURNAMEFILE)){
                surnames.addAll(ArrayUtils.formatFileBytes(fileBuffer));
            }

        } catch (NullPointerException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}