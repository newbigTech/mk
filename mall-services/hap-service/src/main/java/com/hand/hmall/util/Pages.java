package com.hand.hmall.util;

import com.hand.hmall.dto.Params;

import java.util.Map;

public class Pages extends Params { 

	 private Map<String,Object> pages;

	public Map<String, Object> getPages() {
		return pages;
	}

	public void setPages(Map<String, Object> pages) {
		this.pages = pages;
	}
}
