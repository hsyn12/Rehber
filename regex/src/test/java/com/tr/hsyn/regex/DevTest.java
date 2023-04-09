package com.tr.hsyn.regex;


import org.junit.Assert;
import org.junit.Test;


public class DevTest {
	
	@Test
	public void devAfterLast() throws Exception {
		
		String result = Regex.Dev.afterLast("hsyntr@33@gmail.com", "@");
		Assert.assertEquals("gmail.com", result);
	}
	
	@Test
	public void devAfterFirst() throws Exception {
		
		String result = Regex.Dev.afterFirst("hsyntr@33@gmail.com", "@");
		Assert.assertEquals("33@gmail.com", result);
	}
	
	@Test
	public void devBeforeLast() throws Exception {
		
		String result = Regex.Dev.beforeLast("hsyntr@33@gmail.com", "@");
		Assert.assertEquals("hsyntr@33", result);
	}
	
	@Test
	public void devBeforeFirst() throws Exception {
		
		String result = Regex.Dev.beforeFirst("hsyntr@33@gmail.com", "@");
		Assert.assertEquals("hsyntr", result);
	}
	
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme