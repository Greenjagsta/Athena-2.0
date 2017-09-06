import com.athena.hashfamily.Hash;
import com.athena.hashfamily.md.MD5;
import com.athena.utils.FileUtils;
import com.athena.utils.StringUtils;

import java.util.Arrays;

public class FileUtilsTest {
    public static void main(String[] args) {
        /*for (byte[] b : StringUtils.formatFileBytes(FileUtils.getFileChunk("common.txt"))) {
            System.out.println(StringUtils.byteArrayToString(b));
        }*/

        for (byte[] bouter : FileUtils.getFileChunk("common.txt")) {
            for (byte[] binner : StringUtils.formatFileBytes(bouter)) {
                System.out.println(StringUtils.byteArrayToString(binner));
            }
        }
    }
}