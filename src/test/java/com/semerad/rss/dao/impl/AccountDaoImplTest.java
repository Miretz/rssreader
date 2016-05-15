package com.semerad.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.semerad.rss.model.Account;

@RunWith(MockitoJUnitRunner.class)
public class AccountDaoImplTest {
	@Mock
	private SessionFactory sessionFactory;
	@InjectMocks
	private AccountDaoImpl accountDaoImpl;

	private final Session session = Mockito.mock(Session.class);
	private final Criteria criteria = Mockito.mock(Criteria.class);

	@Before
	public void setUp() {
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		Mockito.when(session.createCriteria(Account.class)).thenReturn(criteria);
	}

	@Test
	public void testList() throws Exception {

		Mockito.when(criteria.list()).thenReturn(Collections.emptyList());

		final List<Account> result = accountDaoImpl.list();

		Assert.assertThat(result, CoreMatchers.notNullValue());

		Mockito.verify(criteria, Mockito.times(1)).list();

	}

	@Test
	public void testCreate() throws Exception {
		final Account account = Mockito.mock(Account.class);
		Mockito.doNothing().when(session).saveOrUpdate(account);

		accountDaoImpl.create(account);

		Mockito.verify(session, Mockito.times(1)).saveOrUpdate(account);
	}

	@Test
	public void testUpdate() throws Exception {

		final Account account = Mockito.mock(Account.class);
		Mockito.doNothing().when(session).saveOrUpdate(account);
		Mockito.when(account.getId()).thenReturn(1);
		Mockito.when(session.load(Account.class, 1)).thenReturn(account);

		final Account result = accountDaoImpl.update(account);

		Assert.assertThat(result, CoreMatchers.notNullValue());

		Assert.assertThat(result.getId(), CoreMatchers.equalTo(1));

		Mockito.verify(session, Mockito.times(1)).saveOrUpdate(account);

	}

	@Test
	public void testDeleteNull() throws Exception {

		accountDaoImpl.delete(null);

		Mockito.verify(session, Mockito.times(0)).delete(CoreMatchers.any(Account.class));

	}

	@Test
	public void testDelete() throws Exception {

		final Account account = Mockito.mock(Account.class);
		Mockito.doNothing().when(session).delete(account);

		accountDaoImpl.delete(account);

		Mockito.verify(session, Mockito.times(1)).delete(account);

	}

	@Test
	public void testFindByUsernameEmpty() throws Exception {
		final List<Account> result = accountDaoImpl.findByUsername(null);
		final List<Account> result2 = accountDaoImpl.findByUsername(null);

		Assert.assertThat(result.size(), CoreMatchers.equalTo(0));
		Assert.assertThat(result2.size(), CoreMatchers.equalTo(0));

	}

	@Test
	public void testFindByUsernameNoResult() throws Exception {
		final String testUsername = "userTest";

		Mockito.when(criteria.add(Restrictions.eq("username", testUsername))).thenReturn(criteria);
		Mockito.when(criteria.list()).thenReturn(null);

		final List<Account> result = accountDaoImpl.findByUsername(testUsername);

		Assert.assertThat(result.size(), CoreMatchers.equalTo(0));

	}

	@Test
	public void testFindByUsername() throws Exception {

		final String testUsername = "userTest";

		final Account account = Mockito.mock(Account.class);
		Mockito.when(account.getUsername()).thenReturn(testUsername);

		Mockito.when(criteria.add(Restrictions.eq("username", testUsername))).thenReturn(criteria);

		Mockito.when(criteria.list()).thenReturn(Collections.singletonList(account));

		final List<Account> result = accountDaoImpl.findByUsername(testUsername);

		Assert.assertThat(result.size(), CoreMatchers.equalTo(1));
		Assert.assertThat(result.get(0).getUsername(), CoreMatchers.equalTo(testUsername));

	}

}
