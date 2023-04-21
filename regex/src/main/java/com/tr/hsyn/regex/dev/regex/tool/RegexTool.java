package com.tr.hsyn.regex.dev.regex.tool;


import com.tr.hsyn.regex.Nina;

import org.jetbrains.annotations.NotNull;


public interface RegexTool {
	
	/**
	 * This function takes in a string of HTML content and extracts the content within the first occurrence of an open tag and its corresponding closing tag.
	 * It uses regular expressions to find the open and close tags, and counts the number of nested tags to ensure that the correct closing tag is found.
	 *
	 * @param html A non-null string of HTML content to extract content from.
	 * @return A string containing the content within the first occurrence of an open tag and its corresponding closing tag, or an empty string if no such tags are found.
	 */
	static String getHtmlContent(@NotNull String html) {
		
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
					
					for (int i = 1; i < tagCount; i++) {
						
						cTagIndex = Nina.like(closeTag).find((CharSequence) html, cTagIndex.end);
						
						Nina.pl("%s", cTagIndex.stringOf(html));
					}
					
					return html.substring(startIndexTag.end, cTagIndex.start);
				}
			}
			while (true);
			
		}
		
		return "";
	}
}
