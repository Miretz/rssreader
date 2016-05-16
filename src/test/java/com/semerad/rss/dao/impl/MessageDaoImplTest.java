package com.semerad.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.semerad.rss.guimodels.Pagination;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

@RunWith(MockitoJUnitRunner.class)
public class MessageDaoImplTest {
	@Mock
	private SessionFactory sessionFactory;
	@InjectMocks
	private MessageDaoImpl messageDaoImpl;

	private final Session session = Mockito.mock(Session.class);
	private final Criteria criteria = Mockito.mock(Criteria.class);

	@Before
	public void setUp() {
		Mockito.when(sessionFactory.getCurrentSession()).thenReturn(session);
		Mockito.when(session.createCriteria(Message.class)).thenReturn(criteria);
	}

	@Test
	public void testListFeedPaginationString() throws Exception {

		final Feed feed = Mockito.mock(Feed.class);
		final Pagination paging = new Pagination(0, 100);

		Mockito.when(criteria.add(Restrictions.eq("feed", feed))).thenReturn(criteria);
		Mockito.when(criteria.setFirstResult(0)).thenReturn(criteria);
		Mockito.when(criteria.setMaxResults(100)).thenReturn(criteria);
		Mockito.when(criteria.addOrder(Order.desc("publishDate"))).thenReturn(criteria);
		Mockito.when(criteria.list()).thenReturn(Collections.singletonList(Mockito.mock(Message.class)));

		final List<Message> result = messageDaoImpl.list(feed, paging, "");

		Assert.assertThat(result, CoreMatchers.notNullValue());
		Assert.assertThat(result.size(), CoreMatchers.equalTo(1));

		Mockito.verify(criteria, Mockito.times(1)).setFirstResult(0);
		Mockito.verify(criteria, Mockito.times(1)).setMaxResults(100);

	}

	@Test
	public void testListAccountPaginationString() throws Exception {

		final Account account = Mockito.mock(Account.class);
		final Pagination paging = new Pagination(0, 100);

		Mockito.when(criteria.add(Restrictions.eq("feed", "f"))).thenReturn(criteria);
		Mockito.when(criteria.add(Restrictions.eq("f.account", account))).thenReturn(criteria);
		Mockito.when(criteria.setFirstResult(0)).thenReturn(criteria);
		Mockito.when(criteria.setMaxResults(100)).thenReturn(criteria);
		Mockito.when(criteria.addOrder(Order.desc("publishDate"))).thenReturn(criteria);
		Mockito.when(criteria.list()).thenReturn(Collections.singletonList(Mockito.mock(Message.class)));

		final List<Message> result = messageDaoImpl.list(account, paging, "");

		Assert.assertThat(result, CoreMatchers.notNullValue());
		Assert.assertThat(result.size(), CoreMatchers.equalTo(1));

		Mockito.verify(criteria, Mockito.times(1)).setFirstResult(0);
		Mockito.verify(criteria, Mockito.times(1)).setMaxResults(100);

	}

	@Test
	public void testMessageCountFeed() throws Exception {

		final Account acc = null;
		final Feed feed = null;

		final long result1 = messageDaoImpl.messageCount(acc);
		final long result2 = messageDaoImpl.messageCount(feed);

		Assert.assertThat(result1, CoreMatchers.equalTo(0L));
		Assert.assertThat(result2, CoreMatchers.equalTo(0L));

	}

	@Test
	public void testAddTextSearchCriteria() throws Exception {

		Mockito.when(criteria.add(Mockito.any())).thenReturn(criteria);

		final String searchValue = "search";

		messageDaoImpl.addTextSearchCriteria(searchValue, criteria);

		Mockito.verify(criteria, Mockito.times(1)).add(Mockito.any());

	}
}
