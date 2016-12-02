import com.google.common.collect.Lists;
import com.ustc.yolk.dal.ShareContentDAO;
import com.ustc.yolk.model.ShareContent;
import com.ustc.yolk.model.ShareContentDO;
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
            for (int i = 0; i < 99; i++) {
                ShareContentService contentService = springContext.getBean(ShareContentService.class);
                ParamChecker.notNull(contentService, "获取ShareContentService Bean失败");
                ShareContent shareContent = new ShareContent();
                shareContent.setPublic0(false);
                shareContent.setSharedByUsername("test");
                List<SingleContent> singleContents = Lists.newArrayList();
                shareContent.setContents(singleContents);
                singleContents.add(new SingleContent("test_201612011939551_0004.jpg", "i am text and pic!"));
                singleContents.add(new SingleContent(null, "i am pure text!"));
                long id = contentService.add(shareContent);
            }
//            print(contentService.queryById(id));

        } catch (Exception e) {
            e.printStackTrace();
            print("异常", e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    public void testPage() {
        try {
            ShareContentDAO contentDAO = springContext.getBean(ShareContentDAO.class);
            ParamChecker.notNull(contentDAO, "获取ShareContentService Bean失败");
            List<ShareContentDO> result = contentDAO.queryByUsernameForPage("test", 0, 100);
            print(result);
            print(result.size());
        } catch (Exception e) {
            e.printStackTrace();
            print("异常", e.getMessage());
            assertFalse(true);
        }
    }

    @Test
    public void testPage1() {
        try {
            ShareContentDAO contentDAO = springContext.getBean(ShareContentDAO.class);
            ParamChecker.notNull(contentDAO, "获取ShareContentService Bean失败");
            List<ShareContentDO> result = contentDAO.queryPub(0, 2);
            print(result);
            print(result.size());
        } catch (Exception e) {
            e.printStackTrace();
            print("异常", e.getMessage());
            assertFalse(true);
        }
    }
}
