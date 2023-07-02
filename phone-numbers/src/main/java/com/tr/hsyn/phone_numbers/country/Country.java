package com.tr.hsyn.phone_numbers.country;


import java.util.Arrays;
import java.util.Map;


public class Country {
	
	private String              name;
	private String              region;
	private String[]            phone;
	private Map<String, String> timezones;
	private String              emoji;
	private Map<String, String> iso;
	private String              image;
	
	@Override
	public String toString() {
		
		return "Country{" +
		       "name='" + name + '\'' +
		       ", region='" + region + '\'' +
		       ", phone=" + Arrays.toString(phone) +
		       ", timezones=" + timezones +
		       ", emoji='" + emoji + '\'' +
		       ", iso=" + iso +
		       ", image='" + image + '\'' +
		       '}';
	}
	
	public String getImage() {
		
		return image;
	}
	
	public Map<String, String> getTimezones() {
		
		return timezones;
	}
	
	public Map<String, String> getIso() {
		
		return iso;
	}
	
	public String getEmoji() {
		
		return emoji;
	}
	
	public String[] getPhone() {
		
		return phone;
	}
	
	public String getName() {
		
		return name;
	}
	
	public String getRegion() {
		
		return region;
	}
}