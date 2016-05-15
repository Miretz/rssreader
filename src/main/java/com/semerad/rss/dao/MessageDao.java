package com.semerad.rss.dao;

import java.util.List;
import java.util.Set;

import com.semerad.rss.guimodels.Pagination;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;

public interface MessageDao {

	void batchInsert(List<Message> messages);

	List<Message> list(Feed feed, Pagination paging, String textSearch);

	List<Message> list(Account account, Pagination paging, String textSearch);

	Long messageCount(Feed feed);

	Long messageCount(Account account);

	Message get(int id);

	void create(Message message);

	Message update(Message message);

	void setRead(Set<Integer> ids, boolean value);

	void setFavourite(int id, boolean value);

}
