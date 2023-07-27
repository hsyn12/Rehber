package com.tr.hsyn.telefonrehberi.main.contact.data;


import com.tr.hsyn.contactdata.ContactDat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public interface ContactData {
	
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
			public int hashCode() {
				
				return Objects.hash(type, value);
			}
			
			@Override
			public boolean equals(Object obj) {
				
				return obj instanceof ContactDat && type == ((ContactDat) obj).getType() && value.equals(((ContactDat) obj).getValue());
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
