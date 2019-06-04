package com.ocbc.tech.service;

import java.util.List;

import com.ocbc.tech.entity.User;

public interface UserService {
	
	int addUser(User user);
    int deleteUser(Integer id);
    User selectUser(Integer id);
    int updateUser(User user);
    List<User> findAll();

}
