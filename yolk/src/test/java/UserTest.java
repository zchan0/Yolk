import com.ustc.yolk.model.User;
import com.ustc.yolk.service.UserService;
import com.ustc.yolk.utils.common.ParamChecker;
import org.junit.Test;

/**
 * Created by Administrator on 2016/11/30.
 */
public class UserTest extends BaseTestSupport {

    @Test
    public void testAdd() {
        try {
            UserService userService = springContext.getBean(UserService.class);
            ParamChecker.notNull(userService, "获取userservice Bean失败");
            User user = new User();
            user.setUsername("test");
            user.encryptPasswd("test");
            userService.register(user);
            User qUser = userService.getUser("test");
            print(qUser);
        } catch (Exception e) {
            if (e.getMessage().equals("user already exists!")) {
                print("用户已经存在");
                assertTrue(true);
                return;
            }
            e.printStackTrace();
            print("异常", e.getMessage());
            assertFalse(true);
        }
    }
}
