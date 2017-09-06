import com.athena.hashfamily.md.MD5;
import com.athena.hashfamily.sha.SHA1;
import com.athena.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class HashGen {
    private static final ArrayList<String> MESSAGES = new ArrayList<>(Arrays.asList("fire", "p4ssword", "salad"));

    public static void main(String[] args) {
        System.out.print("Messages: ");
        for (String message : MESSAGES) {
            System.out.print(message + ", ");
        }

        System.out.println("\n\nMD5:");
        for (String messageMD5 : MESSAGES) {
            System.out.println(StringUtils.byteArrayToHexString(MD5.digest(messageMD5.getBytes())));
        }

        System.out.println("\nSHA1:");
        for (String messageSHA1 : MESSAGES) {
            System.out.println(StringUtils.byteArrayToHexString(SHA1.digest(messageSHA1.getBytes())));
        }
    }
}