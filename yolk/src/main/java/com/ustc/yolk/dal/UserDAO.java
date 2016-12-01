package com.ustc.yolk.dal;

import com.ustc.yolk.model.UserDO;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/11/30.
 */
@Component
public class UserDAO extends BaseDAO {

    /*根据用户名查询UserDO*/
    public UserDO query(String username) {
        try {
            return (UserDO) sqlMapClient.queryForObject("queryByUsername", username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*插入用户记录*/
    public void insert(UserDO userDO) {
        try {
            sqlMapClient.startTransaction();
            UserDO existedUserDO = (UserDO) sqlMapClient.queryForObject("queryUserWithLock", userDO.getUsername());
            if (existedUserDO != null) {
                throw new RuntimeException("user already exists!");
            }
            sqlMapClient.insert("insertUserDO", userDO);
            sqlMapClient.commitTransaction();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
