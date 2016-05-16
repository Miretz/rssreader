package com.semerad.rss.dao.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.semerad.rss.dao.MessageDao;
import com.semerad.rss.guimodels.Pagination;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

@Transactional
@Repository("messageDao")
public class MessageDaoImpl implements MessageDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list(final Feed feed, final Pagination paging, final String textSearch) {
		if (feed == null) {
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteria.add(Restrictions.eq("feed", feed));

		if (paging != null) {
			criteria.setFirstResult(paging.getIndex());
			criteria.setMaxResults(paging.getPageSize());
		}

		if (StringUtils.isNotBlank(textSearch)) {
			addTextSearchCriteria(textSearch, criteria);
		}

		criteria.addOrder(Order.desc("publishDate"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> list(final Account account, final Pagination paging, final String textSearch) {
		if (account == null) {
			// if feed is unspecified return empty list
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteria.createAlias("feed", "f");
		criteria.add(Restrictions.eq("f.account", account));

		if (paging != null) {
			criteria.setFirstResult(paging.getIndex());
			criteria.setMaxResults(paging.getPageSize());
		}

		if (StringUtils.isNotBlank(textSearch)) {
			addTextSearchCriteria(textSearch, criteria);
		}

		criteria.addOrder(Order.desc("publishDate"));

		return criteria.list();

	}

	@Override
	public void create(final Message message) {
		sessionFactory.getCurrentSession().saveOrUpdate(message);
	}

	protected void addTextSearchCriteria(final String value, final Criteria criteria) {
		final Criterion price = Restrictions.like("summary", value, MatchMode.ANYWHERE);
		final Criterion name = Restrictions.like("title", value, MatchMode.ANYWHERE);
		final LogicalExpression orExp = Restrictions.or(price, name);
		criteria.add(orExp);
	}

	@Override
	public Long messageCount(final Feed feed) {
		if (feed == null) {
			// if feed is unspecified return empty list
			return 0L;
		}
		final Criteria criteriaCount = sessionFactory.getCurrentSession().createCriteria(Message.class);
		criteriaCount.add(Restrictions.eq("feed", feed));
		criteriaCount.setProjection(Projections.rowCount());
		return (Long) criteriaCount.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Long messageCount(final Account account) {
		if (account == null) {
			// if account is unspecified return empty list
			return 0L;
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Feed.class);
		criteria.add(Restrictions.eq("account", account));
		final List<Feed> feeds = criteria.list();

		return feeds.stream().mapToLong(e -> e.getMessages().size()).sum();
	}

	@Override
	public void batchInsert(final List<Message> messages) {
		final StatelessSession session = sessionFactory.openStatelessSession();
		final Transaction tx = session.beginTransaction();
		messages.forEach(session::insert);
		tx.commit();
		session.close();
	}

	@Override
	public Message get(final int id) {
		return sessionFactory.getCurrentSession().load(Message.class, id);
	}

	@Override
	public Message update(final Message message) {
		final int id = message.getId();
		sessionFactory.getCurrentSession().saveOrUpdate(message);
		return sessionFactory.getCurrentSession().load(Message.class, id);
	}

	@Override
	public void setRead(final Set<Integer> ids, final boolean value) {
		final Session session = sessionFactory.openSession();
		final Transaction tx = session.beginTransaction();
		ids.forEach(id -> {
			final Message m = session.load(Message.class, id);
			m.setRead(value);
			session.update(m);
		});
		tx.commit();
		session.close();
	}

	@Override
	public void setFavourite(final int id, final boolean value) {
		final Session session = sessionFactory.openSession();
		final Transaction tx = session.beginTransaction();
		final Message m = session.load(Message.class, id);
		m.setFavourite(value);
		session.update(m);
		tx.commit();
		session.close();
	}
}
