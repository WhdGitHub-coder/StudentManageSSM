package com.itwh.ssmproject.entity;

import org.springframework.stereotype.Component;

/*
 * 用户实体类
 */

//用到此注解，已经在容器spring中，自动加载
@Component
public class User {
	private Long id;//用户id，主键、自增
	private String username;//用户名
	private String password;//密码
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
