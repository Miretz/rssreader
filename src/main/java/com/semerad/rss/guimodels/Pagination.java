package com.semerad.rss.guimodels;

import java.io.Serializable;

public class Pagination implements Serializable {

	private static final long serialVersionUID = 992800402043947110L;
	private int index;
	private int pageSize;

	public Pagination(final int index, final int pageSize) {
		super();
		this.index = index;
		this.pageSize = pageSize;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}
}
