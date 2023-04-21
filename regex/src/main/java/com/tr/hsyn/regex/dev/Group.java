package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.cast.Text;

import org.jetbrains.annotations.NotNull;


/**
 *
 */
public interface Group extends Text {
	
	static @NotNull Group group(@NotNull String group, Object... args) {
		
		return builder()
				.group(String.format(group, args))
				.build();
	}
	
	static @NotNull Group nonCaptured(@NotNull String group, Object... args) {
		
		return builder()
				.group(String.format(group, args))
				.captured(false)
				.build();
	}
	
	static <T extends Text> @NotNull Group nonCaptured(@NotNull T group, Object... args) {
		
		return builder()
				.group(String.format(group.getText(), args))
				.captured(false)
				.build();
	}
	
	static @NotNull Group atomic(@NotNull String group, Object... args) {
		
		return builder()
				.group(String.format(group, args))
				.atomic(true)
				.build();
	}
	
	static @NotNull RegexGroup.GroupBuilder builder() {
		
		return RegexGroup.builder();
	}
	
	RegexBuilder toRegex();
}
