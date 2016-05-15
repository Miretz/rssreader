package com.semerad.rss.service;

import java.util.List;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

public interface MessageService {

	List<Message> list(Feed feed, int firstResult, int pageSize);

	List<Message> list(Account account, int firstResult, int pageSize);

	int messageCount(Feed feed);

	int messageCount(Account account);

	List<Message> search(Feed feed, int firstResult, int pageSize, String value);

	List<Message> search(Account account, int firstResult, int pageSize, String value);

	void create(Message message);

	void synchronizeFeeds(Account account);

}
