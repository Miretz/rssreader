package com.semerad.rss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semerad.rss.dao.FeedDao;
import com.semerad.rss.dao.MessageDao;
import com.semerad.rss.dao.RemoteFeedDao;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;
import com.semerad.rss.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private FeedDao feedDao;

	@Autowired
	private RemoteFeedDao remoteFeedDao;

	@Override
	public List<Message> list(final Feed feed, final int firstResult, final int pageSize) {
		return messageDao.list(feed, firstResult, pageSize);
	}

	@Override
	public int messageCount(final Feed feed) {
		return messageDao.messageCount(feed);
	}

	@Override
	public List<Message> list(final Account account, final int firstResult, final int pageSize) {
		return messageDao.list(account, firstResult, pageSize);
	}

	@Override
	public int messageCount(final Account account) {
		return messageDao.messageCount(account);
	}

	@Override
	public List<Message> search(final Feed feed, final int firstResult, final int pageSize, final String column,
			final String value) {
		return messageDao.search(feed, firstResult, pageSize, column, value);
	}

	@Override
	public List<Message> search(final Account account, final int firstResult, final int pageSize, final String column,
			final String value) {
		return messageDao.search(account, firstResult, pageSize, column, value);
	}

	@Override
	public void create(final Message message) {
		messageDao.create(message);
	}

	@Override
	public void synchronizeFeeds(final Account account) {
		final List<Feed> feeds = feedDao.list(account);
		for (final Feed feed : feeds) {
			remoteFeedDao.synchronizeFeed(feed);
		}
	}

}
