package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public interface ContactData {
	
	String getValue();
	
	int getType();
	
	@NotNull
	static ContactData newData(String value, int type) {
		
		class Data implements ContactData {
			
			private final String value;
			private final int    type;
			
			Data(String value, int type) {
				
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
			public int hashCode() {
				
				return Objects.hash(type, value);
			}
			
			@Override
			public boolean equals(Object obj) {
				
				return obj instanceof ContactData && type == ((ContactData) obj).getType() && value.equals(((ContactData) obj).getValue());
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
		
		return new Data(value, type);
	}
}
