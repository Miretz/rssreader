package com.semerad.rss.service;

import com.semerad.rss.model.Account;

public interface AccountService {

	void create(String username, String password);

	Account login(String username, String password);

}
