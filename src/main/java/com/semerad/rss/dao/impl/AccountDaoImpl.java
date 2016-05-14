package com.semerad.rss.dao.impl;

import java.util.Collections;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.semerad.rss.dao.AccountDao;
import com.semerad.rss.model.Account;

@Transactional
@Repository("accountDao")
public class AccountDaoImpl implements AccountDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> list() {
		return sessionFactory.getCurrentSession().createCriteria(Account.class).list();
	}

	@Override
	public void create(final Account account) {
		sessionFactory.getCurrentSession().saveOrUpdate(account);
	}

	@Override
	public Account update(final Account account) {
		final int id = account.getId();
		sessionFactory.getCurrentSession().saveOrUpdate(account);
		return (Account) sessionFactory.getCurrentSession().load(Account.class, id);
	}

	@Override
	public void delete(final Account account) {
		if (null != account) {
			sessionFactory.getCurrentSession().delete(account);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Account> findByUsername(final String username) {
		if (StringUtils.isEmpty(username)) {
			return Collections.emptyList();
		}
		final Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Account.class);
		criteria.add(Restrictions.eq("username", username));
		final List<Account> accounts = criteria.list();
		if (accounts == null) {
			return Collections.emptyList();
		}
		return accounts;
	}
}
