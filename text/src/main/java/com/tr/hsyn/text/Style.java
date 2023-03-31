package com.tr.hsyn.text;

import java.util.ArrayList;
import java.util.List;


public final class Style {
	
	
	private final boolean           bold;
	private final boolean           italic;
	private final boolean           underline;
	private final int               color;
	private final int               backgroundColor;
	private final RoundedBackground roundedBackground;
	
	
	//@Builder
	private Style(boolean bold, boolean italic, boolean underline, int color, int backgroundColor, RoundedBackground roundedBackground) {
		
		this.bold              = bold;
		this.italic            = italic;
		this.underline         = underline;
		this.color             = color;
		this.backgroundColor   = backgroundColor;
		this.roundedBackground = roundedBackground;
	}
	
	
	public Span[] spans() {
		
		List<Span> spanList = new ArrayList<>();
		
		if (bold) spanList.add(Spans.bold());
		if (italic) spanList.add(Spans.italic());
		if (underline) spanList.add(Spans.underline());
		
		if (color != -1) spanList.add(Spans.foreground(color));
		
		if (backgroundColor != -1) spanList.add(Spans.background(backgroundColor));
		
		if (roundedBackground != null) spanList.add(new Span(roundedBackground));
		
		return spanList.toArray(new Span[0]);
	}
	
	
	public static class StyleBuilder {
		
		private final boolean bold            = false;
		private final boolean italic          = false;
		private final boolean underline       = false;
		private final int     color           = -1;
		private final int     backgroundColor = -1;
		//private RoundedBackground roundedBackground = RoundedBackground.builder().build();
	}
	
	
}
