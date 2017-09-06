import com.athena.utils.enums.CharSet;

import java.util.Arrays;

public class BruteForceTest {
    public static void main(String[] args) {
        byte[] word = new byte[4];
        int i = 0;

        for (byte b = CharSet.MIXED_ALPHANUMERIC.getStartPos(); b <= CharSet.MIXED_ALPHANUMERIC.getEndPos(); b++) {
            if (Arrays.binarySearch(CharSet.MIXED_ALPHANUMERIC.getToSkip(), b) < 0) {
                if (word[i] == CharSet.MIXED_ALPHANUMERIC.getEndPos()) {
                    b = CharSet.MIXED_ALPHANUMERIC.getStartPos();
                }
                word[i] = b;
            }
            System.out.println(new String(word));
        }
    }
}