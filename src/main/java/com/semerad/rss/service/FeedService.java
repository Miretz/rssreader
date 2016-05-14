package com.semerad.rss.service;

import java.util.List;

import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;

public interface FeedService {

	List<Feed> list(Account account);

	void create(Feed feed);

	void delete(int id);

}
