package com.accounting.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.accounting.dao.UsersDao;
import com.accounting.model.User;

@Service("usersDao")
public class UsersDaoImpl implements UsersDao {
	
	@Autowired
	private Environment env;
	
	private JdbcTemplate userTemplate;
	
	public UsersDaoImpl(DataSource userDataSource) {
		userTemplate = new JdbcTemplate(userDataSource);
	}

	@Override
	public List<User> getAllUsers() {
		String query = env.getProperty("get_all_users");
		List<Map<String, Object>> userList = userTemplate.queryForList(query);
		List<User> allUsers = new ArrayList<User>();
		if(userList != null && !userList.isEmpty()) {
			for(Map<String, Object> eachUser: userList) {
				User user = new User();
				user.setUserId(Long.parseLong(String.valueOf(eachUser.get("user_id"))));
				user.setUserName(String.valueOf(eachUser.get("user_name")));
				user.setEmailId(String.valueOf(eachUser.get("email_id")));
				allUsers.add(user);
			}
		}
		return allUsers;
	}

}
