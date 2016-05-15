package com.semerad.rss.guicomponents;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.semerad.rss.guimodels.MessageGui;
import com.semerad.rss.service.MessageService;

public class DashboardList extends DataView<MessageGui> {

	private static final long serialVersionUID = -6100492392205489796L;

	@SpringBean
	private MessageService messageService;

	private static final String STYLE_FONT_WEIGHT_BOLD = "font-weight: bold;";
	private static final String STYLE = "style";
	private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm:ss";
	private static final String STYLE_GRAY = "color: #ddd; cursor: pointer;";
	private static final String STYLE_RED = "color: red; cursor: pointer;";

	private final Set<Integer> selectedItems = new HashSet<>();

	public DashboardList(final String id, final IDataProvider<MessageGui> dataProvider, final int i) {
		super(id, dataProvider, i);
	}

	public void changeSelectionReadStatus(final boolean value) {
		messageService.setRead(selectedItems, value);
		selectedItems.clear();
	}

	@Override
	protected void populateItem(final Item<MessageGui> item) {
		populateListItem(item);
	}

	protected void populateListItem(final ListItem<MessageGui> listItem) {

		final MessageGui messageGui = listItem.getModelObject();

		// column 1 - Selection CheckBox
		final CheckBox selected = createSelectionCheckBox(messageGui);
		listItem.add(selected);

		// column 2 - Published Date
		final DateLabel publishedDate = new DateLabel("publishDate", Model.of(messageGui.getPublishDate()),
				new PatternDateConverter(DATE_FORMAT, true));
		listItem.add(publishedDate);

		// column 3 - Exclammation Icon and Title Link
		final Label exclammation = new Label("exclamation", "");
		listItem.add(exclammation);

		final ExternalLink titleLink = new ExternalLink("messageUrl", messageGui.getUrl());
		titleLink.add(new Label("title", messageGui.getTitle()));
		listItem.add(titleLink);

		// column 4 - Summary
		final Label summary = new Label("summary", messageGui.getSummary());
		summary.setEscapeModelStrings(false);
		listItem.add(summary);

		// column 5 - Like
		final Link<String> favourite = new Link<String>("like") {
			private static final long serialVersionUID = -5684185837555368892L;

			@Override
			public void onClick() {
				messageService.setFavourite(messageGui.getId(), !messageGui.isFavourite());
			}
		};
		listItem.add(favourite);

		// enable / disable like
		if (listItem.getModelObject().isFavourite()) {
			favourite.add(new AttributeAppender(STYLE, STYLE_RED, " "));
		} else {
			favourite.add(new AttributeAppender(STYLE, STYLE_GRAY, " "));
		}

		// change styles based on message read status
		if (messageGui.isRead()) {
			exclammation.add(new AttributeAppender(STYLE, "display: none;", " "));
		} else {
			exclammation.add(new AttributeAppender(STYLE, STYLE_FONT_WEIGHT_BOLD, " "));
			publishedDate.add(new AttributeAppender(STYLE, STYLE_FONT_WEIGHT_BOLD, " "));
			titleLink.add(new AttributeAppender(STYLE, STYLE_FONT_WEIGHT_BOLD, " "));
			summary.add(new AttributeAppender(STYLE, STYLE_FONT_WEIGHT_BOLD, " "));
		}

	}

	protected CheckBox createSelectionCheckBox(final MessageGui messageGui) {
		final CheckBox selected = new CheckBox("selected", Model.of(Boolean.FALSE));
		selected.add(new AjaxFormComponentUpdatingBehavior("change") {
			private static final long serialVersionUID = 4216992291176427577L;

			@Override
			protected void onUpdate(final AjaxRequestTarget target) {
				final Boolean enabled = (Boolean) this.getComponent().getDefaultModelObject();
				if (BooleanUtils.isTrue(enabled)) {
					selectedItems.add(messageGui.getId());
				} else {
					if (selectedItems.contains(messageGui.getId())) {
						selectedItems.remove(messageGui.getId());
					}
				}
			}
		});
		return selected;
	}
}
