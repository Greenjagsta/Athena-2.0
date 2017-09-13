import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MaskTest {
    static String mask = "?l?l?l?d1";

    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>(mask.length());
        HashMap<Character, Boolean> elements = new HashMap<>();
        integers.add(1, 2);

        System.out.println(integers.get(0));
        System.out.println(integers.get(1));

        char[] specials = new char[mask.length()];
        char[] normals = new char[mask.length()];

        for (int i = 0; i < mask.length(); i++) {
            if (mask.charAt(i) == '?') {
                elements.put(mask.charAt(i + 1), true);
                if (!(i + 1 > mask.length())) {
                    i++;
                }
            } else {
                elements.put(mask.charAt(i), false);
            }
        }

        System.out.println(new String(specials));
        System.out.println(new String(normals));

        System.out.println(Arrays.toString(specials));
        System.out.println(Arrays.toString(normals));
    }
}
