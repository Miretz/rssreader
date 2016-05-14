package com.semerad.rss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semerad.rss.dao.FeedDao;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.service.FeedService;

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	private FeedDao feedDao;

	@Override
	public List<Feed> list(final Account account) {
		return feedDao.list(account);
	}

	@Override
	public void create(final Feed feed) {
		feedDao.create(feed);

	}

	@Override
	public void delete(final int id) {
		feedDao.delete(id);
	}

}
