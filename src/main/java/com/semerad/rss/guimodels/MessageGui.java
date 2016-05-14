package com.semerad.rss.guimodels;

import java.util.Date;

import org.apache.wicket.util.io.IClusterable;

import com.semerad.rss.model.Message;

public class MessageGui implements IClusterable {

	private static final long serialVersionUID = 7242320606091520814L;

	private int id;
	private String url;
	private String title;
	private String author;
	private String summary;

	private boolean read;
	private boolean favourite;

	private Date publishDate;

	private String guid;

	public MessageGui() {
		// Empty
	}

	public MessageGui(final MessageGui messageGui) {
		this.id = messageGui.getId();
		this.url = messageGui.getUrl();
		this.title = messageGui.getTitle();
		this.author = messageGui.getAuthor();
		this.summary = messageGui.getSummary();

		this.read = messageGui.isRead();
		this.favourite = messageGui.isFavourite();

		if (messageGui.getPublishDate() != null) {
			this.publishDate = new Date(messageGui.getPublishDate().getTime());
		}

		this.guid = messageGui.getGuid();

	}

	public MessageGui(final Message message) {
		this.id = message.getId();
		this.url = message.getUrl();
		this.title = message.getTitle();
		this.author = message.getAuthor();
		this.summary = message.getSummary();

		this.read = message.isRead();
		this.favourite = message.isFavourite();

		if (message.getPublishDate() != null) {
			this.publishDate = new Date(message.getPublishDate().getTime());
		}

		this.guid = message.getGuid();
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(final String author) {
		this.author = author;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(final boolean read) {
		this.read = read;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(final boolean favourite) {
		this.favourite = favourite;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(final Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(final String guid) {
		this.guid = guid;
	}

}
