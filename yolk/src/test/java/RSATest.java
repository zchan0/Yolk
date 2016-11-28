import com.ustc.yolk.utils.RSAUtil;
import org.junit.Test;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RSATest extends BaseTestSupport {

    @Test
    public void test() {
        print(RSAUtil.encrypt("151515151"));
    }
}
