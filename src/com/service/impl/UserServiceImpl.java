package com.service.impl;

import java.util.List;

import com.dao.UserDao;
import com.model.User;
import com.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> showUsers() {
        return userDao.showUsers();
    }

}
