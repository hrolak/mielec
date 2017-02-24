package com.mielec.users.dao;

import com.mielec.users.model.User;

public interface UserDao {

    User findByUserName(String username);

}