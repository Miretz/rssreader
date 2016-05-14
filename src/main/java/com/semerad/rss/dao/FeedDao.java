package com.semerad.rss.dao;

import java.util.List;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;

public interface FeedDao {

	List<Feed> list(Account account);

	void create(Feed feed);

	Feed update(Feed feed);

	void delete(int id);

}
