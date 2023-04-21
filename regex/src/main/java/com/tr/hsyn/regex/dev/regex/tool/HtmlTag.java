package com.tr.hsyn.regex.dev.regex.tool;


import com.tr.hsyn.regex.cast.Index;


public class HtmlTag {
	
	private final String tag;
	private final Index  index;
	
	public HtmlTag(String tag, Index index) {
		
		this.tag   = tag;
		this.index = index;
	}
	
	public String getTag() {
		
		return tag;
	}
	
	public Index getIndex() {
		
		return index;
	}
}
