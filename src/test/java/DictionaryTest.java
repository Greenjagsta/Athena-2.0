import com.athena.attacks.Dictionary;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by Administrator on 01/08/2017.
 */
public class DictionaryTest {

    @Test
    public void getNextCandidatesTest() {
        Dictionary d = new Dictionary("test.txt", new ArrayList<>(), 100, new String[]{"rule.txt"});
    }
}
