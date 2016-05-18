package com.semerad.rss.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "MESSAGES", uniqueConstraints = { @UniqueConstraint(columnNames = { "GUID" }) })
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MESSAGE_ID")
	private int id;

	@Column(name = "URL")
	private String url;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "AUTHOR")
	private String author;

	@Column(name = "SUMMARY", columnDefinition = "TEXT")
	private String summary;

	@Column(name = "IS_READ")
	private boolean read;

	@Column(name = "IS_FAVOURITE")
	private boolean favourite;

	@Column(name = "PUBLISH_DATE")
	private Date publishDate;

	@Column(name = "GUID")
	private String guid;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FEED_ID")
	private Feed feed;

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

	public Feed getFeed() {
		return feed;
	}

	public void setFeed(final Feed feed) {
		this.feed = feed;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(final String guid) {
		this.guid = guid;
	}

}
