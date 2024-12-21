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
	public static void main(String[] args) {
		System.out.println(new AccountDAO().isValidate(new Account("minhncph13186@fpt.edu.vn", "123456", false)));
	}
}
