package com.itwh.ssmproject.entity;

import org.springframework.stereotype.Component;

/*
 * �û�ʵ����
 */

//�õ���ע�⣬�Ѿ�������spring�У��Զ�����
@Component
public class User {
	private Long id;//�û�id������������
	private String username;//�û���
	private String password;//����
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