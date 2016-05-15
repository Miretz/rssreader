package com.semerad.rss.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.semerad.rss.dao.FeedDao;
import com.semerad.rss.dao.MessageDao;
import com.semerad.rss.dao.RemoteFeedDao;
import com.semerad.rss.guimodels.Pagination;
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
	public List<Message> list(final Feed feed, final Pagination paging, final String textSearch) {
		return messageDao.list(feed, paging, textSearch);
	}

	@Override
	public Long messageCount(final Feed feed) {
		return messageDao.messageCount(feed);
	}

	@Override
	public List<Message> list(final Account account, final Pagination paging, final String textSearch) {
		return messageDao.list(account, paging, textSearch);
	}

	@Override
	public Long messageCount(final Account account) {
		return messageDao.messageCount(account);
	}

	@Override
	public void create(final Message message) {
		messageDao.create(message);
	}

	@Override
	public void synchronizeFeeds(final Account account) {
		feedDao.list(account).forEach(remoteFeedDao::synchronizeFeed);
	}

	@Override
	public Message update(final Message message) {
		return messageDao.update(message);
	}

	@Override
	public void setRead(final Set<Integer> ids, final boolean value) {
		messageDao.setRead(ids, value);
	}

	@Override
	public void setFavourite(final int id, final boolean value) {
		messageDao.setFavourite(id, value);
	}

}
