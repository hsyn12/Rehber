package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.dev.regex.tool.RegexTool;

import org.junit.Test;


public class HMTTest {
	
	@Test
	public void findHMT() {
		
		String html = "<em>hello</em>";
		
		Nina.pl("Result : '%s'", RegexTool.getHtmlContent(html));
	}
	
}
