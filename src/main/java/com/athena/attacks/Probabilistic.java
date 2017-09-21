package com.athena.attacks;

import com.athena.utils.*;
import com.athena.utils.enums.CharSet;
import com.athena.utils.enums.Mode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Probabilistic extends Attack {
    private final File PROBFILE = new File("resources/prob.txt");
    private final File WORDFILE = new File("resources/words.txt");
    private final File NAMEFILE = new File("resources/names.txt");

    private ArrayList<byte[]> words;
    private ArrayList<byte[]> names;
    private ArrayList<byte[]> candidates;
    private CounterList<byte[]> candidateElements;

    private int currentIndex = 0;

    public Probabilistic(ArrayList<byte[]> hashes, int hashType) {
        super.setMode(Mode.PROBABILISTIC.getCode());
        super.setHashType(hashType, hashes);
        super.setHashman(new HashManager(hashes));
        super.initDigestInstance();

        this.candidateElements = new CounterList<>();
        this.candidates = new ArrayList<>();
        this.words = new ArrayList<>();
        this.names = new ArrayList<>();

        initElements();
        initCandidates();
    }

    @Override
    public void attack() {
        while (isMoreCandidates()) {
            for (int i = 0; i < candidateElements.size(); i++) {
                if (!super.isAllCracked()) {
                    super.checkAttempt(ArrayUtils.stripList(candidateElements.get(i)));
                } else {
                    return;
                }
            }
        }
    }

    public boolean isMoreCandidates() {
        try {
            if (currentIndex < candidates.size()) {
                candidateElements.clear();
                parseCandidate(candidates.get(currentIndex));
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
                    addNames(element);
                    break;
                case 119:
                    addWords(element);
                    break;
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
                byte[] arr = ArrayUtils.stripList(nums.get(i));

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

    private void addNames(byte[] element) {
        if (element[element.length - 1] == 76) {
            int length = element[element.length - 2] - 48;
            candidateElements.add(l33tify(names.stream().filter(n -> n.length == length).collect(Collectors.toList())));
        } else {
            int length = element[element.length - 1] - 48;
            candidateElements.add(names.stream().filter(n -> n.length == length).collect(Collectors.toList()));
        }
    }

    private void addWords(byte[] element) {
        if (element[element.length - 1] == 76) {
            int length = element[element.length - 2] - 48;
            candidateElements.add(l33tify(words.stream().filter(w -> w.length == (length)).collect(Collectors.toList())));
        } else {
            int length = element[element.length - 1] - 48;
            candidateElements.add(words.stream().filter(w -> w.length == (length)).collect(Collectors.toList()));
        }
    }

    //TODO - Implement this
    private List<byte[]> l33tify(List<byte[]> candidates) {
        return candidates;
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

        } catch (NullPointerException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}