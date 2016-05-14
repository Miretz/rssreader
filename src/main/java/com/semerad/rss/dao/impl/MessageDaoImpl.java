package com.semerad.rss.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.semerad.rss.dao.MessageDao;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

@Transactional
@Repository("messageDao")
public class MessageDaoImpl implements MessageDao {

	private static final Logger LOGGER = Logger.getLogger(MessageDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list(final Feed feed, final int firstResult, final int pageSize) {
		if (feed == null) {
			// if feed is unspecified return empty list
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteria.add(Restrictions.eq("feed", feed));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pageSize);
		criteria.addOrder(Order.desc("publishDate"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list(final Account account, final int firstResult, final int pageSize) {
		if (account == null) {
			// if feed is unspecified return empty list
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Feed.class);
		criteria.add(Restrictions.eq("account", account));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pageSize);

		final List<Feed> feeds = criteria.list();
		final List<Message> messages = new ArrayList<>();
		for (final Feed feed : feeds) {
			messages.addAll(feed.getMessages());
		}

		messages.sort((p1, p2) -> p2.getPublishDate().compareTo(p1.getPublishDate()));

		return messages;

	}

	@Override
	public void create(final Message message) {
		sessionFactory.getCurrentSession().saveOrUpdate(message);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> search(final Feed feed, final int firstResult, final int pageSize, final String column,
			final String value) {
		if (feed == null) {
			// if feed is unspecified return messages from all feeds
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteria.add(Restrictions.eq("feed", feed));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pageSize);
		criteria.add(Restrictions.like(column, value));
		return criteria.list();

	}

	@Override
	public int messageCount(final Feed feed) {
		if (feed == null) {
			// if feed is unspecified return empty list
			return 0;
		}
		final Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteriaCount.add(Restrictions.eq("feed", feed));
		criteriaCount.setProjection(Projections.rowCount());
		return (Integer) criteriaCount.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public int messageCount(final Account account) {
		if (account == null) {
			// if account is unspecified return empty list
			return 0;
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Feed.class);
		criteria.add(Restrictions.eq("account", account));

		final List<Feed> feeds = criteria.list();
		int size = 0;
		for (final Feed feed : feeds) {
			size += feed.getMessages().size();
		}
		return size;
	}

	@Override
	public List<Message> search(final Account account, final int firstResult, final int pageSize, final String column,
			final String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void batchInsert(final List<Message> messages) {

		final StatelessSession session = sessionFactory.openStatelessSession();

		for (final Message m : messages) {
			final Transaction tx = session.beginTransaction();
			try {
				tx.setTimeout(5);
				session.insert(m);
				tx.commit();
			} catch (final ConstraintViolationException e) {
				// Ignore
				tx.rollback();
				LOGGER.warn("Ignoring duplicate: " + m);
			}
		}

		session.close();
	}
}
