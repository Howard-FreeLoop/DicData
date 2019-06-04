package com.ocbc.tech.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ocbc.tech.entity.User;
import com.ocbc.tech.mapper.UserMapper;
import com.ocbc.tech.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	@Override
    public int addUser(User user) {
        return userMapper.insertUser(user);
    }
 
    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }
 
    @Override
    public User selectUser(Integer id) {
        return userMapper.selectUser(id);
    }
 
    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }
 
    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }
}
