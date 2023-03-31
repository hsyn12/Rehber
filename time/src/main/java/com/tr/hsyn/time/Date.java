package com.tr.hsyn.time;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;


public interface Date {
	
	ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of(ZoneId.systemDefault().getRules().getOffset(Instant.now()).getId());
	
	DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy EEEE HH:mm");
	
	
}
