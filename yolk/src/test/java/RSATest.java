import com.ustc.yolk.utils.RSAUtil;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/11/28.
 */
public class RSATest extends BaseTestSupport {

    @Test
    public void test() throws UnsupportedEncodingException {
        print("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJrZuHYwZC9QU9T298qDwKadBKhwsmEAMJOhQ3VJKR1nYkG4hShcNEUTNBqsQov6WL/E9dHYx2JW4pUgD4ioiMJIExQD+nS2Tg9ZAkMf3eCw8ea2oli3UcHkNld4NjX+bcEtnB+vcSYy+kf0dHOqQJsiQyd+KW/K/f7mN3TrU/WjAgMBAAECgYBamVfbjqSPKihiSbzguAVXdjbrdkRnjHzfY1LcpFlx4rMSUKtX2dqbL4kksfSdgXO7f7lzdanyV3NqXL+57SFV4UPEZJJEE+1q8BozcwhPwBy4c07hnL1tpqkP3J2Sir5OZzXruc1P49d/ZCVB4BSPRAu78p+Gu/ZsiDGL0XsmgQJBAM5bBhfMxeeHStMxRjXXlMBi9nisJ12JaDJ8fqf9DzhJhUPbShW8AhHqVd0jtA6pvBdictlDU2sm9BB+M/N7iIkCQQDAGp0dpW0taUCPZWTe8YBHpGfvt/1AJtGg6PcqnLcyIiSyH5D4Bl5JSPefG/AfeNvHxfJYVl9GfRgyZ6sFyunLAkEAs7yOyw4eVrL91oP1rpIOy5+nJKZ2Rz+W1VqgizudFR7ch6Uqm1G9z9L3ZP1jKN5oqUmj1V+R+iPcs7zpDSvNWQJAV+hV2Ykn6s3tUzFM9ZCB6UbcoSAK7d5Vs1KoOIG6e2CQdkpnpngTkWmoltDMEzrVKBf3DGGJ+9GmzvoNkvsF/QJBAK/77GymfH6Yngj5oQGNnCPgw/SRYBwgzOueMIY/5qba8iXfi+fDmls2nxocJnlcJALiYT9u/iD8t0KY9aczibc=".length());
        print(URLEncoder.encode(RSAUtil.encrypt("42"), "utf-8"));
    }
}
