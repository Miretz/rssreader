package com.semerad.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.semerad.rss.dao.FeedDao;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;

@Transactional
@Repository("feedDao")
public class FeedDaoImpl implements FeedDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Feed> list(final Account account) {
		if (account == null) {
			// if account is unspecified return empty list
			return Collections.emptyList();
		}
		return sessionFactory.getCurrentSession().createCriteria(Feed.class).add(Restrictions.eq("account", account))
				.list();
	}

	@Override
	public void create(final Feed feed) {
		sessionFactory.getCurrentSession().saveOrUpdate(feed);
	}

	@Override
	public Feed update(final Feed feed) {
		final int id = feed.getId();
		sessionFactory.getCurrentSession().saveOrUpdate(feed);
		return (Feed) sessionFactory.getCurrentSession().load(Feed.class, id);
	}

	@Override
	public void delete(final int id) {
		final Feed feed = (Feed) sessionFactory.getCurrentSession().load(Feed.class, id);
		if (null != feed) {
			sessionFactory.getCurrentSession().delete(feed);
		}
	}

}
