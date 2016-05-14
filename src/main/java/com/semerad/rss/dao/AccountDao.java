package com.semerad.rss.dao;

import java.util.List;

import com.semerad.rss.model.Account;

public interface AccountDao {

	List<Account> list();

	List<Account> findByUsername(String username);

	void create(Account account);

	Account update(Account account);

	void delete(Account account);

}