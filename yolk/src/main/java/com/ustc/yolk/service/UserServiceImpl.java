package com.ustc.yolk.service;

import com.ustc.yolk.dal.UserDAO;
import com.ustc.yolk.model.User;
import com.ustc.yolk.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/11/27.
 */
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public User getUser(String username) {
        UserDO uDO = userDAO.query(username);
        if (uDO == null) {
            return null;
        }
        User user = new User(uDO.getUsername());
        user.setEncryptedPasswd(uDO.getPassword());
        return user;
    }

    @Override
    public void register(User user) {
        UserDO userDO = new UserDO();
        userDO.setUsername(user.getUsername());
        userDO.setPassword(user.getEncryptedPasswd());
        userDAO.insert(userDO);
    }
}
