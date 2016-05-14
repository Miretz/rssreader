package com.semerad.rss.guimodels;

import org.apache.wicket.util.io.IClusterable;

import com.semerad.rss.model.Feed;

public class FeedGui implements IClusterable {

	private static final long serialVersionUID = 4064624101354820325L;

	private int id;
	private String name;
	private String url;

	public FeedGui() {
		// Empty
	}

	public FeedGui(final FeedGui feedGui) {
		this.id = feedGui.getId();
		this.name = feedGui.getName();
		this.url = feedGui.getUrl();
	}

	public FeedGui(final Feed feed) {
		this.id = feed.getId();
		this.name = feed.getName();
		this.url = feed.getUrl();
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "[name = " + name + ", url = " + url + "]";
	}

}
