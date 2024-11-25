package com.java.dao;

import com.java.model.Account;
import com.java.model.User;

public class AccountDAO {
	// hàm xác thực tài khoản
	public boolean isValidate(Account account) {
		UserDAO userDAO = new UserDAO();

		User user = userDAO.getUser(account.getEmail(), account.getPassword());

		if (user == null)
			return false;
		return true;
	}
}
