package com.semerad.rss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semerad.rss.dao.AccountDao;
import com.semerad.rss.model.Account;
import com.semerad.rss.service.AccountService;
import com.semerad.rss.service.utils.PasswordHashUtil;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDao accountDao;

	@Override
	public void create(final String username, final String password) {

		// hash the password into DB
		final String hashedPassword = PasswordHashUtil.createHash(password);

		final Account account = new Account();
		account.setUsername(username);
		account.setPassword(hashedPassword);
		accountDao.create(account);

	}

	@Override
	public Account login(final String username, final String password) {
		final List<Account> accountsWithUsername = accountDao.findByUsername(username);
		for (final Account account : accountsWithUsername) {
			// validate hashed password
			if (PasswordHashUtil.validatePassword(password, account.getPassword())) {
				return account;
			}
		}
		return null;
	}

}
