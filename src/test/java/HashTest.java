import com.athena.hashfamily.Hash;
import com.athena.utils.StringUtils;

import java.lang.reflect.InvocationTargetException;

public class HashTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //System.out.println("SHA1 TEST : " + StringUtils.byteArrayToHexString(SHA1.digest("test".getBytes())));
        //System.out.println("MD5 TEST : " + StringUtils.byteArrayToHexString(MD5.digest("test".getBytes())));

        for (int i : Hash.getHashType("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3")) {
            Class c = Hash.getHash(i).getClassname();
            System.out.println(StringUtils.byteArrayToHexString((byte[]) c.getMethod("digest", byte[].class).invoke(null, (Object) "test".getBytes())));
        
        /*Method[] methods = c.getMethods();
        for (Method m : methods) {
            System.out.println(m);
            System.out.println(Arrays.toString(m.getParameterTypes()));
        }*/
        }
    }
}
