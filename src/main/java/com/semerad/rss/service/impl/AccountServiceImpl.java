package com.semerad.rss.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
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
	public boolean create(final String username, final String password) {

		// hash the password into DB
		final String hashedPassword = PasswordHashUtil.createHash(password);

		final List<Account> accountsFound = accountDao.findByUsername(username);

		if (CollectionUtils.isNotEmpty(accountsFound)) {
			return false;
		}

		final Account account = new Account();
		account.setUsername(username);
		account.setPassword(hashedPassword);
		accountDao.create(account);
		return true;
	}

	@Override
	public Account login(final String username, final String password) {
		final List<Account> accountsWithUsername = accountDao.findByUsername(username);

		final Optional<Account> maybeAccount = accountsWithUsername.stream()
				.filter(acc -> PasswordHashUtil.validatePassword(password, acc.getPassword())).findFirst();

		if (maybeAccount.isPresent()) {
			return maybeAccount.get();
		}
		return null;
	}

}
