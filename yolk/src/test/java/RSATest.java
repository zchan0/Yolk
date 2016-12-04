import com.google.common.collect.Maps;
import com.ustc.yolk.utils.RSAUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RSATest extends BaseTestSupport {

    @Test
    public void test() throws UnsupportedEncodingException {
        print(URLEncoder.encode(RSAUtil.encrypt("42"), "utf-8"));
        Map<Integer, String> text = Maps.newHashMap();
        text.put(1, "我是要发布的text内容1");
        text.put(2, "我是要发布的text内容2");
        print(text);
        print(RSAUtil.decrypt("VZm2y70kBv9Q4UM8UvdNNJ6dqBkVUbq8003/PPvlsyozuiRtKScLYOZXVBXBrL0+0YKuAq03xfMyV+YKfUJyD8H5VT6Xs3aJ9Tf9lVUjIR14/3ufHN0U3iZ6AA5+MaofitJYAIhDCBiFdk1zo0mzBM/JlbNU9iR0QrV/QR4JJg4="));
    }
}
