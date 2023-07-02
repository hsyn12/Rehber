package com.tr.hsyn.phone_numbers.country;


import java.util.Arrays;
import java.util.Map;


public class Country {
	
	private String              image;
	private Map<String, String> timezones;
	private Map<String, String> iso;
	private String              emoji;
	private String[]            phone;
	private String              name;
	private String              region;
	
	@Override
	public String toString() {
		
		return
				"Country{" +
				"image ='" + image + '\'' +
				",timezones='" + timezones + '\'' +
				",iso='" + iso + '\'' +
				",emoji='" + emoji + '\'' +
				",phone='" + Arrays.toString(phone) + '\'' +
				",name='" + name + '\'' +
				",region='" + region + '\'' +
				"}";
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