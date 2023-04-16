package com.tr.hsyn.regex.dev.regex.character;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.dev.regex.Regex;

import org.junit.Test;


public class HMTTest {
	
	@Test
	public void findHMT() {
		
		var str = "<em>hello</em>";
		
		var head   = Nina.like("<%s>", Nina.group(Nina.anymany()));
		var middle = Nina.group(Nina.anythings());
		var tail   = Nina.like("</").refereTo(1).with(">");
		
		var main = head.with(middle).with(tail);
		
		Nina.pl("Regex : %s", main.getText());
		Nina.pl("Result: " + main.findGroup(str, 1));
		Nina.pl("Result: " + Regex.getStringParts(str, main.findGroup(str, 2)));
		
	}
	
}
