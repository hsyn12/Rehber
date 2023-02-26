package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public interface ContactDat {
	
	String getValue();
	
	int getType();
	
	@NotNull
	static ContactDat newData(String value, int type) {
		
		class Dat implements ContactDat {
			
			private final String value;
			private final int    type;
			
			Dat(String value, int type) {
				
				this.value = value;
				this.type  = type;
			}
			
			@Override
			public String getValue() {
				
				return value;
			}
			
			@Override
			public int getType() {
				
				return type;
			}
			
			@Override
			public boolean equals(Object obj) {
				
				return obj instanceof ContactDat && type == ((ContactDat) obj).getType() && value.equals(((ContactDat) obj).getValue());
			}
			
			@Override
			public int hashCode() {
				
				return Objects.hash(type, value);
			}
			
			@NotNull
			@Override
			public String toString() {
				
				return "Data{" +
				       "value='" + value + '\'' +
				       ", type=" + type +
				       '}';
			}
		}
		
		return new Dat(value, type);
	}
}
