package com.semerad.rss.service.impl;

import java.util.Collections;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.semerad.rss.dao.AccountDao;
import com.semerad.rss.model.Account;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {
	@Mock
	private AccountDao accountDao;
	@InjectMocks
	private AccountServiceImpl accountServiceImpl;

	@Test
	public void testLogin() throws Exception {

		final Account account = Mockito.mock(Account.class);
		final String hashedPassword = "$2a$12$uKpRbUMFnlF.cAxXBL7FkOARqvLlXgvrOGkSEVr2KKGK6mpdL6SUq";
		Mockito.when(account.getPassword()).thenReturn(hashedPassword);

		final String username = "user";
		final String password = "test";
		Mockito.when(accountDao.findByUsername(username)).thenReturn(Collections.singletonList(account));

		final Account result = accountServiceImpl.login(username, password);

		Assert.assertThat(result, CoreMatchers.notNullValue());

	}

	@Test
	public void testLoginFailure() throws Exception {

		final Account account = Mockito.mock(Account.class);
		final String hashedPassword = "$2a$12$uKpRbUMFnlF.cAxXBL7FkOARqvLlXgvrOGkSEVr2KKGK623131";
		Mockito.when(account.getPassword()).thenReturn(hashedPassword);

		final String username = "user";
		final String password = "test";
		Mockito.when(accountDao.findByUsername(username)).thenReturn(Collections.singletonList(account));

		final Account result = accountServiceImpl.login(username, password);

		Assert.assertThat(result, CoreMatchers.nullValue());

	}

}
