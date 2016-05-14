package com.semerad.rss.dao;

import com.semerad.rss.dao.exception.FeedUrlException;
import com.semerad.rss.model.Feed;

public interface RemoteFeedDao {

	void synchronizeFeed(Feed feed);

	String getFeedName(String url) throws FeedUrlException;

}
