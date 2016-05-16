package com.semerad.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.semerad.rss.guicomponents.DashboardDataProvider;
import com.semerad.rss.guicomponents.DashboardList;
import com.semerad.rss.guimodels.MessageGui;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.model.Message;
import com.semerad.rss.service.FeedService;
import com.semerad.rss.service.MessageService;

@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
public class DashboardPage extends WebPage {

	public static final String ALL_FEEDS = "All";

	@SpringBean
	protected MessageService messageService;

	@SpringBean
	protected FeedService feedService;

	protected final List<MessageGui> messages = new ArrayList<>();
	protected String searchField;

	protected DashboardList messagesList;
	protected DashboardDataProvider dataProvider;
	protected String currentFeed = ALL_FEEDS;

	@Override
	protected void onConfigure() {
		super.onConfigure();
		final AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
		// if user is not signed in, redirect him to sign in page
		if (!AuthenticatedWebSession.get().isSignedIn()) {
			app.restartResponseAtSignInPage();
		}
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// get current logged in account
		final Account currentAccount = ((BasicAuthenticationSession) AuthenticatedWebSession.get()).getCurrentAccount();

		// load messages from DB
		loadMessages(currentAccount);

		// display data with paging
		dataProvider = new DashboardDataProvider(currentAccount, null, messageService);
		messagesList = new DashboardList("messages", dataProvider, 10);
		messagesList.setOutputMarkupId(true);
		add(messagesList);
		final PagingNavigator pager = new PagingNavigator("navigation", messagesList);
		add(pager);

		addButtons(currentAccount);

		// add the search form
		add(createSearchForm());

		// add the feed filter
		add(createFeedFilter(currentAccount));

	}

	protected void loadMessages(final Account currentAccount) {
		messages.clear();
		// Load messages from Database
		final List<Message> messagesFromDb = messageService.list(currentAccount, null, null);
		messagesFromDb.forEach(e -> messages.add(new MessageGui(e)));
	}

	protected void synchronizeFeeds(final Account currentAccount) {
		// synchronize feeds from the web
		messageService.synchronizeFeeds(currentAccount);
		loadMessages(currentAccount);
	}

	private StatelessForm createSearchForm() {
		final StatelessForm searchForm = new StatelessForm("searchForm") {
			@Override
			protected void onSubmit() {
				dataProvider.setTextSearch(searchField);
				setResponsePage(getPage());
			}

		};
		searchForm.setDefaultModel(new CompoundPropertyModel(this));
		searchForm.add(new TextField("searchField"));
		return searchForm;
	}

	protected FormComponent<String> createFeedFilter(final Account currentAccount) {

		final List<Feed> feeds = feedService.list(currentAccount);
		final List<String> feedNames = feeds.stream().map(e -> e.getName()).collect(Collectors.toList());

		feedNames.add(ALL_FEEDS);

		final DropDownChoice<String> choice = new DropDownChoice<>("feedFilter",
				new PropertyModel<String>(this, "currentFeed"), feedNames);

		choice.add(new AjaxFormComponentUpdatingBehavior("change") {

			@Override
			protected void onUpdate(final AjaxRequestTarget target) {
				final String selectedName = ((DropDownChoice<String>) getComponent()).getModelObject();
				onFeedFilterChange(feeds, selectedName);
			}
		});

		return choice;

	}

	protected void onFeedFilterChange(final List<Feed> feeds, final String selectedName) {
		final Optional<Feed> maybeFeed = feeds.stream().filter(e -> StringUtils.equals(e.getName(), selectedName))
				.findFirst();
		if (maybeFeed.isPresent()) {
			dataProvider.setFeed(maybeFeed.get());
		} else {
			dataProvider.setFeed(null);
			currentFeed = ALL_FEEDS;
		}
		setResponsePage(getPage());
	}

	protected void addButtons(final Account currentAccount) {
		// Add synchronize link
		add(new Link("synchronize") {

			@Override
			public void onClick() {
				synchronizeFeeds(currentAccount);
			}
		});

		// Add refresh link
		add(new Link("refresh") {
			@Override
			public void onClick() {
				loadMessages(currentAccount);
			}
		});

		// page sizes
		add(new Link("pageSize10") {
			@Override
			public void onClick() {
				messagesList.setItemsPerPage(10);
			}
		});

		add(new Link("pageSize20") {
			@Override
			public void onClick() {
				messagesList.setItemsPerPage(20);
			}
		});

		add(new Link("pageSize50") {
			@Override
			public void onClick() {
				messagesList.setItemsPerPage(50);
			}
		});

		// mark as read links
		add(new Link("markAsRead") {
			@Override
			public void onClick() {
				messagesList.changeSelectionReadStatus(true);
			}
		});
		add(new Link("markAsUnread") {
			@Override
			public void onClick() {
				messagesList.changeSelectionReadStatus(false);
			}
		});

		// Add logout link
		add(new Link("logOut") {

			@Override
			public void onClick() {
				AuthenticatedWebSession.get().invalidate();
				setResponsePage(getApplication().getHomePage());
			}
		});
	}

}
