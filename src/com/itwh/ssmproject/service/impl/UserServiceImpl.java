package com.itwh.ssmproject.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwh.ssmproject.dao.UserDao;
import com.itwh.ssmproject.entity.User;
import com.itwh.ssmproject.service.UserService;
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userdao;
	@Override
	public User findByUserName(String username) {
		// TODO Auto-generated method stub
		return userdao.findByUserName(username);
	}
	@Override
	public int add(User user) {
		// TODO Auto-generated method stub
		return userdao.add(user);
	}
	@Override
	public List<User> findList(Map<String,Object> queryMap) {
		// TODO Auto-generated method stub
		return userdao.findList(queryMap);
	}
	@Override
	public int getTotal(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return userdao.getTotal(queryMap);
	}
	@Override
	public int edit(User user) {
		// TODO Auto-generated method stub
		return userdao.edit(user);
	}
	@Override
	public int delete(String ids) {
		// TODO Auto-generated method stub
		return userdao.delete(ids);
	}

}
