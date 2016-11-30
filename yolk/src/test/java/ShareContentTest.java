import com.google.common.collect.Lists;
import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.SingleContent;
import com.ustc.yolk.service.ShareContentService;
import com.ustc.yolk.utils.common.ParamChecker;
import org.junit.Test;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */
public class ShareContentTest extends BaseTestSupport {

    @Test
    public void testAdd() {
        try {
            ShareContentService contentService = springContext.getBean(ShareContentService.class);
            ParamChecker.notNull(contentService, "获取ShareContentService Bean失败");
            ShareContent shareContent = new ShareContent();
            shareContent.setPublic0(false);
            shareContent.setSharedByUsername("test");
            List<SingleContent> singleContents = Lists.newArrayList();
            shareContent.setContents(singleContents);
            singleContents.add(new SingleContent("哈哈", "www.baidu.com"));

            long id = contentService.add(shareContent);
            print(contentService.queryById(id));

        } catch (Exception e) {
            e.printStackTrace();
            print("异常", e.getMessage());
            assertFalse(true);
        }
    }
}
