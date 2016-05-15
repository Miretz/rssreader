package com.semerad.rss.service;

import java.util.List;
import java.util.Set;

import com.semerad.rss.guimodels.Pagination;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

public interface MessageService {

	List<Message> list(Feed feed, Pagination paging, String textSearch);

	List<Message> list(Account account, Pagination paging, String textSearch);

	Long messageCount(Feed feed);

	Long messageCount(Account account);

	void create(Message message);

	Message update(Message message);

	void setRead(final Set<Integer> ids, final boolean value);

	void setFavourite(final int id, final boolean value);

	void synchronizeFeeds(Account account);

}
