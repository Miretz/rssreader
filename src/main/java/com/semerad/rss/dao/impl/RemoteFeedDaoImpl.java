package com.semerad.rss.dao.impl;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.semerad.rss.dao.MessageDao;
import com.semerad.rss.dao.RemoteFeedDao;
import com.semerad.rss.dao.exception.FeedUrlException;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Repository("remoteFeedDao")
public class RemoteFeedDaoImpl implements RemoteFeedDao {

    private static final Logger LOGGER = Logger.getLogger(RemoteFeedDaoImpl.class);
    private static final String IMAGE_TAG = "<img class=\"img-summary\" src=\"%s\"/>";

    @Autowired
    private MessageDao messageDao;

    @Override
    public void synchronizeFeed(final Feed feed) {

        final HttpUriRequest method = new HttpGet(feed.getUrl());

        try (CloseableHttpClient client = HttpClients.createMinimal();
             CloseableHttpResponse response = client.execute(method);
             InputStream stream = response.getEntity().getContent()) {

            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed syndFeed = input.build(new XmlReader(stream));
            final List<SyndEntry> entries = syndFeed.getEntries();

            final Set<String> guids = getExistingMessages(feed);

            // filter the existing messages and convert
            final List<Message> messages = entries.parallelStream().filter(e -> !guids.contains(e.getUri()))
                    .map(e -> convertToMessage(feed, e)).collect(Collectors.toList());

            messageDao.batchInsert(messages);

        } catch (final IllegalArgumentException | FeedException | ConstraintViolationException | IOException e) {
            LOGGER.error("Error downloading feeds", e);
        }
    }

    protected Set<String> getExistingMessages(final Feed feed) {
        final List<Message> existingMessages = messageDao.list(feed, null, null);
        return existingMessages.parallelStream().map(e -> e.getGuid()).collect(Collectors.toSet());
    }

    protected Message convertToMessage(final Feed feed, final SyndEntry entry) {
        final Message message = new Message();
        message.setFeed(feed);
        message.setAuthor(entry.getAuthor());
        message.setPublishDate(entry.getPublishedDate());
        message.setTitle(entry.getTitle());
        List<SyndEnclosure> enclosures = entry.getEnclosures();
        final String enclosure = enclosures.isEmpty() ? "" : String.format(IMAGE_TAG, enclosures.get(0).getUrl());
        message.setSummary(enclosure + entry.getDescription().getValue());
        message.setUrl(entry.getLink());
        message.setGuid(entry.getUri());
        return message;
    }

    @Override
    public String getFeedName(final String urlStr) throws FeedUrlException {

        final HttpUriRequest method = new HttpGet(urlStr);

        try (CloseableHttpClient client = HttpClients.createMinimal();
             CloseableHttpResponse response = client.execute(method);
             InputStream stream = response.getEntity().getContent()) {
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(new XmlReader(stream));
            return feed.getTitle();
        } catch (IllegalArgumentException | FeedException | IOException e) {
            throw new FeedUrlException("Error getting feed name", e);
        }

    }

}
