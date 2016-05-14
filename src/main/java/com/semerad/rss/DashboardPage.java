package com.semerad.rss;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.semerad.rss.guimodels.MessageGui;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Message;
import com.semerad.rss.service.MessageService;

public class DashboardPage extends WebPage {

	private static final long serialVersionUID = -2393491973831377023L;

	private final List<MessageGui> messages = new ArrayList<>();

	@SpringBean
	MessageService messageService;

	@Override
	protected void onConfigure() {
		super.onConfigure();
		final AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
		// if user is not signed in, redirect him to sign in page
		if (!AuthenticatedWebSession.get().isSignedIn()) {
			app.restartResponseAtSignInPage();
		}
	}

	@SuppressWarnings({ "rawtypes", "serial" })
	@Override
	protected void onInitialize() {
		super.onInitialize();

		// get current logged in account
		final BasicAuthenticationSession basicSession = (BasicAuthenticationSession) AuthenticatedWebSession.get();
		final Account currentAccount = basicSession.getCurrentAccount();

		loadMessages(currentAccount);

		// Add synchronize link
		add(new Link("synchronize") {

			@Override
			public void onClick() {
				synchronizeFeeds();
			}
		});

		// add to Page
		add(new PropertyListView<MessageGui>("messages", messages) {
			@Override
			public void populateItem(final ListItem<MessageGui> listItem) {

				final Label read = new Label("exclamation", "");
				if (listItem.getModelObject().isRead()) {
					read.add(new AttributeAppender("style", "display: none;", " "));
				}
				listItem.add(read);

				listItem.add(new Label("publishDate"));
				listItem.add(
						new ExternalLink("messageUrl", listItem.getModelObject().getUrl()).add(new Label("title")));
				listItem.add(new Label("summary").setEscapeModelStrings(false));

				final Label favourite = new Label("heart", "");
				if (listItem.getModelObject().isFavourite()) {
					favourite.add(new AttributeAppender("style", "color: red;", " "));
				} else {
					favourite.add(new AttributeAppender("style", "color: #ddd;", " "));
				}
				listItem.add(favourite);

			}
		}).setVersioned(false);

		// Add logout link
		add(new Link("logOut") {

			@Override
			public void onClick() {
				AuthenticatedWebSession.get().invalidate();
				setResponsePage(getApplication().getHomePage());
			}
		});
	}

	private void loadMessages(final Account currentAccount) {

		messages.clear();

		// Load messages from Database
		final int size = messageService.messageCount(currentAccount);
		final List<Message> messagesFromDb = messageService.list(currentAccount, 0, size);
		for (final Message feed : messagesFromDb) {
			messages.add(new MessageGui(feed));
		}

	}

	protected void synchronizeFeeds() {
		// get current logged in account
		final BasicAuthenticationSession basicSession = (BasicAuthenticationSession) AuthenticatedWebSession.get();
		final Account currentAccount = basicSession.getCurrentAccount();

		// synchronize feeds
		messageService.synchronizeFeeds(currentAccount);

		loadMessages(currentAccount);
	}

}
