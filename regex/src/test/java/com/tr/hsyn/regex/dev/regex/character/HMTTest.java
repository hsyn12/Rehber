package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.dev.regex.tool.RegexTool;

import org.junit.Test;


public class HMTTest {
	
	/**
	 * This function takes in a string of HTML content and extracts
	 * the content within the first occurrence of an open tag and its corresponding closing tag.
	 */
	@Test
	public void findHMT() {
		
		String html = "<em>hello</em>";
		
		var tag = RegexTool.getHtmlContent(html);
		
		if (tag != null) {
			
			Nina.pl("Result : '%s'", RegexTool.getHtmlContent(html));
			Nina.pl("Result : '%s'", tag.getIndexContent().stringOf(html));
		}
		else {
			
			Nina.pl("Result : null");
		}
	}
	
}
