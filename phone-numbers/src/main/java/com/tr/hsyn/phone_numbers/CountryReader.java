package com.tr.hsyn.phone_numbers;


import com.google.gson.Gson;
import com.tr.hsyn.phone_numbers.country.Country;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.Nullable;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CountryReader {
	
	protected static final String PHONE_NUMBERS_SRC_MAIN_RESOURCES = "phone-numbers/src/main/resources/";
	
	private CountryReader() {
		
	}
	
	public static void main(String[] args) {
		
		System.out.println(getCountry("TR"));
	}
	
	public static @Nullable Country getCountry(String name) {
		
		Gson gson = new Gson();
		
		try (FileReader country = new FileReader(PHONE_NUMBERS_SRC_MAIN_RESOURCES + name + ".json")) {
			
			return gson.fromJson(country, Country.class);
		}
		catch (IOException e) {
			xlog.w(e);
		}
		
		return null;
	}
	
	
	private static void parse() {
		
		try (FileReader file = new FileReader("phone-numbers/src/main/resources/country_codes.json")) {
			
			int           counter        = 0;
			int           ch             = -1;
			int           bracketCounter = 0;
			StringBuilder buffer         = new StringBuilder();
			
			while (true) {
				
				ch = file.read();
				if (ch == -1) break;
				
				if (ch == '}') {
					
					if (++bracketCounter == 3) {
						
						buffer.append((char) ch);
						String name   = buffer.substring(2, 4);
						String object = buffer.substring(6, buffer.length());
						
						try (FileWriter writer = new FileWriter(PHONE_NUMBERS_SRC_MAIN_RESOURCES + name + ".json")) {
							
							writer.write(object);
							writer.flush();
						}
						
						bracketCounter = 0;
						counter++;
						buffer.setLength(0);
						continue;
					}
				}
				
				buffer.append((char) ch);
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
