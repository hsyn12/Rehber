package com.tr.hsyn.regex.dev.regex.tool;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.Index;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public interface RegexTool {
	
	/**
	 * This method takes in a string of HTML and returns an {@link HtmlTag} object that represents the first HTML tag found in the string.
	 * The method uses regular expressions to search for the opening and closing tags of the HTML element.
	 * If a valid opening tag is found, the method searches for the corresponding closing tag and returns an {@link HtmlTag} object that
	 * represents the HTML element. If no valid opening tag is found, the method returns null.
	 *
	 * @param html A string of HTML to search for an HTML element.
	 * @return An HtmlTag object that represents the first HTML element found in the input string, or null if no valid HTML element is found.
	 */
	@Nullable
	static HtmlTag getHtmlContent(@NotNull String html) {
		
		var str = html;
		
		var openTag = Nina.group("<%s\\b*?%s>", Nina.group("tagName", Nina.manythingsBut(">")), Nina.anythingsBut(">")).toRegex();
		
		var    indexTagName  = openTag.find(str, "tagName");
		var    startIndexTag = openTag.find(str, 1);
		var    indexTag      = startIndexTag;
		String closeTag;
		
		if (indexTagName.isValid()) {
			
			String tagName  = indexTagName.stringOf(str);
			int    tagCount = 0;
			closeTag = String.format("</%s>", tagName);
			var tName = Nina.group("<%s\\b*?%s>", tagName, Nina.anythingsBut(">")).toRegex();
			
			do {
				
				if (indexTag.isValid()) {
					
					tagCount++;
					str      = str.substring(indexTag.end);
					indexTag = tName.find(str, 1);
				}
				else {
					
					var cTagIndex = Nina.like(closeTag).find(html);
					
					for (int i = 1; i < tagCount; i++)
					     cTagIndex = Nina.like(closeTag).find((CharSequence) html, cTagIndex.end);
					
					if (cTagIndex.isValid())
						return new HtmlTag(tagName, Index.of(startIndexTag.end, cTagIndex.start), Index.of(startIndexTag.start, cTagIndex.end));
					return null;
				}
			}
			while (true);
			
		}
		
		return null;
	}
}
