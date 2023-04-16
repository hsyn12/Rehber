package com.tr.hsyn.regex.dev;


import com.tr.hsyn.regex.Nina;
import com.tr.hsyn.regex.cast.RegexBuilder;
import com.tr.hsyn.regex.dev.regex.Regex;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class RegexGroup implements Group {
	
	@NotNull private final  String  group;
	@Nullable private final String  name;
	private final           boolean captured;
	private final           boolean atomic;
	
	public RegexGroup(@Nullable String group, @Nullable String name, boolean captured, boolean atomic) {
		
		this.group    = group == null ? "" : group;
		this.name     = name;
		this.captured = captured;
		this.atomic   = atomic;
	}
	
	@NotNull
	public static GroupBuilder builder() {return new GroupBuilder();}
	
	@Override
	public RegexBuilder toRegex() {
		
		return Nina.like(getText());
	}
	
	@Override
	public @NotNull String getText() {
		
		if (!Regex.isNoboe(name)) return String.format("(?<%s>%s)", name, group);
		return String.format("(%s%s)", captured ? "" : atomic ? "?>" : "?:", group);
	}
	
	public static final class GroupBuilder {
		
		private String  group    = "";
		private String  name;
		private boolean captured = true;
		private boolean atomic   = false;
		
		private GroupBuilder() {}
		
		public GroupBuilder group(String group) {
			
			this.group = group;
			return this;
		}
		
		public GroupBuilder name(String name) {
			
			this.name = name;
			return this;
		}
		
		public GroupBuilder captured(boolean captured) {
			
			this.captured = captured;
			return this;
		}
		
		public GroupBuilder atomic(boolean atomic) {
			
			this.atomic = atomic;
			return this;
		}
		
		public RegexGroup build() {return new RegexGroup(group, name, captured, atomic);}
	}
}
