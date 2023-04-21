package com.tr.hsyn.regex.dev.regex.tool;


import com.tr.hsyn.regex.cast.Index;


public class HtmlTag {
	
	private final String tag;
	private final Index  indexContent;
	private final Index  indexTag;
	
	public HtmlTag(String tag, Index indexContent, Index indexTag) {
		
		this.tag          = tag;
		this.indexContent = indexContent;
		this.indexTag     = indexTag;
	}
	
	@Override
	public String toString() {
		
		return "HtmlTag{" +
		       "tag='" + tag + '\'' +
		       ", indexContent=" + indexContent +
		       ", indexTag=" + indexTag +
		       '}';
	}
	
	public Index getIndexTag() {
		
		return indexTag;
	}
	
	public String getTag() {
		
		return tag;
	}
	
	public Index getIndexContent() {
		
		return indexContent;
	}
}
