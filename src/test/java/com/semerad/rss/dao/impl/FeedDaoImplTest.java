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
import com.semerad.rss.model.Feed;

@RunWith(MockitoJUnitRunner.class)
public class FeedDaoImplTest {
	@Mock
	private SessionFactory sessionFactory;
	@InjectMocks
	private FeedDaoImpl feedDaoImpl;

	private final Session session = Mockito.mock(Session.class);
	private final Criteria criteria = Mockito.mock(Criteria.class);

	@Before
	public void setUp() {
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		Mockito.when(session.createCriteria(Feed.class)).thenReturn(criteria);
	}

	@Test
	public void testList() throws Exception {

		final Account account = Mockito.mock(Account.class);
		final Feed feed = Mockito.mock(Feed.class);

		Mockito.when(criteria.add(Restrictions.eq("account", account))).thenReturn(criteria);
		Mockito.when(criteria.list()).thenReturn(Collections.singletonList(feed));

		final List<Feed> result = feedDaoImpl.list(account);

		Assert.assertThat(result, CoreMatchers.notNullValue());
		Assert.assertThat(result.size(), CoreMatchers.equalTo(1));

		Mockito.verify(criteria, Mockito.times(1)).list();
	}

	@Test
	public void testListNullInput() throws Exception {

		final List<Feed> result = feedDaoImpl.list(null);

		Assert.assertThat(result, CoreMatchers.notNullValue());
		Assert.assertThat(result.size(), CoreMatchers.equalTo(0));

		Mockito.verify(criteria, Mockito.times(0)).list();
	}

	@Test
	public void testUpdate() throws Exception {

		final Feed feed = Mockito.mock(Feed.class);

		Mockito.doNothing().when(session).saveOrUpdate(feed);
		Mockito.when(feed.getId()).thenReturn(1);
		Mockito.when(session.load(Feed.class, 1)).thenReturn(feed);

		final Feed result = feedDaoImpl.update(feed);

		Assert.assertThat(result, CoreMatchers.notNullValue());

		Assert.assertThat(result.getId(), CoreMatchers.equalTo(1));

		Mockito.verify(session, Mockito.times(1)).saveOrUpdate(feed);

	}

	@Test
	public void testDeleteNull() throws Exception {

		Mockito.when(session.load(Feed.class, 1)).thenReturn(null);

		feedDaoImpl.delete(1);

		Mockito.verify(session, Mockito.times(0)).delete(CoreMatchers.any(Feed.class));

	}

	@Test
	public void testDelete() throws Exception {

		final Feed feed = Mockito.mock(Feed.class);
		Mockito.doNothing().when(session).delete(feed);
		Mockito.when(session.load(Feed.class, 1)).thenReturn(feed);

		feedDaoImpl.delete(1);

		Mockito.verify(session, Mockito.times(1)).delete(feed);

	}
}
