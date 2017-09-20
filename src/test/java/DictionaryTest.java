import com.athena.attacks.Dictionary;
import com.athena.utils.HashManager;
import com.athena.utils.StringUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 01/08/2017.
 */
public class DictionaryTest {

    @Test
    public void getNextCandidatesTest() {
        Dictionary d = new Dictionary("test.txt", new ArrayList<byte[]>(), 100);
    }
}
