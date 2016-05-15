package com.semerad.rss.guicomponents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.semerad.rss.guimodels.MessageGui;
import com.semerad.rss.guimodels.Pagination;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;
import com.semerad.rss.service.MessageService;

public class DashboardDataProvider implements IDataProvider<MessageGui> {

	private static final long serialVersionUID = 1778928003353640070L;

	private final MessageService messageService;
	private final Account currentAccount;
	private Feed feed;
	private String textSearch;

	public DashboardDataProvider(final Account currentAccount, final Feed feed, final MessageService messageService) {
		this.currentAccount = currentAccount;
		this.feed = feed;
		this.messageService = messageService;
	}

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(final Feed feed) {
		this.feed = feed;
	}

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(final String textSearch) {
		this.textSearch = textSearch;
	}

	@Override
	public Iterator<? extends MessageGui> iterator(final long first, final long count) {

		final List<Message> messagesFromDb;
		if (feed == null) {
			messagesFromDb = messageService.list(currentAccount, new Pagination((int) first, (int) count), textSearch);
		} else {
			messagesFromDb = messageService.list(feed, new Pagination((int) first, (int) count), textSearch);
		}
		final List<MessageGui> messages = new ArrayList<>(messagesFromDb.size());
		messagesFromDb.forEach(e -> messages.add(new MessageGui(e)));
		return messages.iterator();
	}

	@Override
	public long size() {
		return messageService.messageCount(currentAccount);
	}

	@Override
	public IModel<MessageGui> model(final MessageGui requestEntity) {
		return new LoadableDetachableModel<MessageGui>() {
			private static final long serialVersionUID = 3004664071635994058L;

			@Override
			protected MessageGui load() {
				return requestEntity;
			}
		};
	}

	@Override
	public void detach() {
		// Empty
	}

}
