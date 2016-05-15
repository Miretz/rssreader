package com.semerad.rss;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;

import com.semerad.rss.model.Account;
import com.semerad.rss.service.AccountService;

public class BasicAuthenticationSession extends AuthenticatedWebSession {

	private static final long serialVersionUID = 1524216743247996335L;

	private AccountService accountService;

	private Account currentAccount;

	public BasicAuthenticationSession(final Request request) {
		super(request);
	}

	public void setAccountService(final AccountService accountService) {
		this.accountService = accountService;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	@Override
	public boolean authenticate(final String username, final String password) {
		final Account account = accountService.login(username, password);
		this.currentAccount = account;
		return null != account;
	}

	@Override
	public Roles getRoles() {
		return null;
	}
}
