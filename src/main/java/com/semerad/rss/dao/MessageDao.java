package com.semerad.rss.dao;

import java.util.List;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

public interface MessageDao {

	// batch insert
	void batchInsert(List<Message> messages);

	// list by feed
	List<Message> list(Feed feed, int firstResult, int pageSize);

	int messageCount(Feed feed);

	// list from all feeds for the account
	List<Message> list(Account account, int firstResult, int pageSize);

	int messageCount(Account account);

	List<Message> search(Feed feed, int firstResult, int pageSize, String column, String value);

	List<Message> search(Account account, int firstResult, int pageSize, String column, String value);

	void create(Message message);

}
