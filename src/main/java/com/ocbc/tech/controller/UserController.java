package com.ocbc.tech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocbc.tech.entity.User;
import com.ocbc.tech.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }
 
    @RequestMapping(value = "add")
    public int addUser(){
        User u = new User(99,"oooo",45);
        int stat = userService.addUser(u);
        return stat;
    }
}
