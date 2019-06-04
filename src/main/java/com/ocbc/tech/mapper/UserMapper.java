package com.ocbc.tech.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ocbc.tech.entity.User;

@Mapper
public interface UserMapper {

	int insertUser(User user);

	int deleteUser(Integer id);

	User selectUser(Integer id);

	List<User> selectAll();

	int updateUser(User user);
	
	
}
