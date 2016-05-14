package com.semerad.rss;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

import com.semerad.rss.dao.RemoteFeedDao;
import com.semerad.rss.dao.exception.FeedUrlException;
import com.semerad.rss.guimodels.FeedGui;
import com.semerad.rss.model.Account;
import com.semerad.rss.model.Feed;
import com.semerad.rss.service.FeedService;

public class ManageFeedsPage extends WebPage {

	private static final long serialVersionUID = -2393491973831377023L;

	private final List<FeedGui> feeds = new ArrayList<>();

	private boolean nameInFocus = false;

	private String urlFieldValue;

	@SpringBean
	FeedService feedService;

	@SpringBean
	RemoteFeedDao remoteFeedDao;

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

		// Load the list of feeds for the current user
		final List<Feed> feedsFromDb = feedService.list(currentAccount);
		for (final Feed feed : feedsFromDb) {
			feeds.add(new FeedGui(feed));
		}

		add(new FeedForm("feedForm"));

		add(new PropertyListView<FeedGui>("feeds", feeds) {
			@Override
			public void populateItem(final ListItem<FeedGui> listItem) {

				listItem.add(new Label("name"));
				listItem.add(new Label("url"));
				listItem.add(new Link("delete") {

					@Override
					public void onClick() {
						feedService.delete(listItem.getModelObject().getId());
						feeds.remove(listItem.getModelObject());
					}

				});
			}
		}).setVersioned(false);

	}

	public final class FeedForm extends Form<ValueMap> {

		private static final long serialVersionUID = -3856831508185113893L;

		@SuppressWarnings("serial")
		public FeedForm(final String id) {
			super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
			setMarkupId("feedForm");

			final TextField<String> urlField = new TextField<>("url");
			urlField.setType(String.class);

			urlField.add(new AjaxFormComponentUpdatingBehavior("blur") {

				@SuppressWarnings("unchecked")
				@Override
				protected void onUpdate(final AjaxRequestTarget arg0) {
					urlFieldValue = ((TextField<String>) getComponent()).getModelObject();
				}

			});

			add(urlField);

			final TextField<String> nameField = new TextField<>("name");
			nameField.setType(String.class);

			nameField.add(new AjaxFormComponentUpdatingBehavior("focus") {

				@Override
				protected void onUpdate(final AjaxRequestTarget target) {
					if (!nameInFocus && StringUtils.isNotBlank(urlFieldValue)) {
						try {
							this.getComponent().setDefaultModelObject(remoteFeedDao.getFeedName(urlFieldValue));
						} catch (final FeedUrlException e) {
							this.getComponent().setDefaultModelObject(e.getMessage());
						}
						target.add(this.getComponent());
						nameInFocus = true;
					}

				}

			});

			nameField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
				@Override
				protected void onUpdate(final AjaxRequestTarget target) {
					nameInFocus = false;

				}
			});

			add(nameField);
		}

		@Override
		public final void onSubmit() {
			final ValueMap values = getModelObject();

			// get current logged in account
			final BasicAuthenticationSession basicSession = (BasicAuthenticationSession) AuthenticatedWebSession.get();
			final Account currentAccount = basicSession.getCurrentAccount();

			final Feed feed = new Feed();
			feed.setAccount(currentAccount);
			feed.setName((String) values.get("name"));
			feed.setUrl((String) values.get("url"));

			feedService.create(feed);
			feeds.add(new FeedGui(feed));

			// Clear out the text component
			values.put("name", "");
			values.put("url", "");

		}
	}
}
