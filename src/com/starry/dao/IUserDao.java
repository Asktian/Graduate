package com.starry.dao;

import java.util.List;

import com.starry.entity.User;
import com.starry.entity.UserRegister;

public interface IUserDao {
	public abstract List<User> selectAll();// ��ѯ�����û�

	public abstract int deleteById(int id);// ����idɾ��

	public abstract int register(User users);// ע��

	public abstract int update(User user);// �޸�
}
