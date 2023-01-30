package com.tr.hsyn.daytimes;


import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.tr.hsyn.time.DurationGroup;
import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.LongSupplier;


/**
 * Zamanı string olarak dönüştürmeyi sağlayan metotlar tanımlar.
 */
public class DayTime {
	
	/**
	 * Varsayılan tarih formatı.
	 */
	public static final String DEFAULT_DATE_PATTERN = "d MMMM yyyy EEEE";
	/**
	 * Varsayılan saat formatı
	 */
	public static final String DEFAULT_TIME_PATTERN = "HH:mm";
	
	/**
	 * Şimdiki zamanı string olarak döndürür.<br><br>
	 *
	 * <pre>
	 * DayTime.toString(context); // '27 Ocak 2023 Cuma, saat sabah 10:48'
	 * </pre>
	 *
	 * @param context Kaynak erişimi için context
	 * @return Şimdiki zaman
	 */
	@NonNull
	public static String toString(Context context) {
		
		if (context == null)
			return LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault())
					.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN + " " + DEFAULT_TIME_PATTERN));
		
		return toString(context, System.currentTimeMillis());
	}
	
	/**
	 * Verilen zamanı string olarak döndürür.<br><br>
	 *
	 * <pre>
	 * DayTime.toString(context, Time.now()); // '27 Ocak 2023 Cuma, saat öğlene doğru 11:00'
	 * </pre>
	 *
	 * @param context Kaynak erişimi için context
	 * @param time    Zaman
	 * @return Şimdiki zaman
	 */
	@NonNull
	public static String toString(Context context, long time) {
		
		Instant       instant = Instant.ofEpochMilli(time);
		LocalDateTime date    = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		String dayTime = getDayTime(context, date.getHour(), date.getMinute());
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);
		
		String clock = context.getString(R.string.clock);
		
		return String.format("%s, %s %s %s", date.format(dateFormatter), clock, dayTime, date.format(timeFormatter));
	}
	
	/**
	 * Verilen zamanı string olarak döndürür.<br><br>
	 *
	 * <pre>
	 * DayTime.toString(context, Time::now); // '27 Ocak 2023 Cuma, saat öğlene doğru 11:03'
	 * </pre>
	 *
	 * @param context Kaynak erişimi için context
	 * @param time    Zaman
	 * @return Şimdiki zaman
	 */
	@NonNull
	public static String toString(Context context, @NotNull LongSupplier time) {
		
		Instant       instant = Instant.ofEpochMilli(time.getAsLong());
		LocalDateTime date    = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		String dayTime = getDayTime(context, date.getHour(), date.getMinute());
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);
		
		String clock = context.getString(R.string.clock);
		
		return String.format("%s, %s %s %s", date.format(dateFormatter), clock, dayTime, date.format(timeFormatter));
	}
	
	/**
	 * Verilen saatin, günün hangi zamanına geldiğini belirten bir string döndürür.<br><br>
	 *
	 * <pre>
	 * var dayTime = getDayTime(9,0);  // 'sabah'
	 * var dayTime = getDayTime(12,0); // 'öğlen'
	 * var dayTime = getDayTime(4,0);  // 'sabaha karşı'
	 * var dayTime = getDayTime(5,0);  // 'sabahın körü'
	 * </pre>
	 *
	 * @param context Kaynak erişimi için context
	 * @param hour    Saat
	 * @param minute  dakika
	 * @return Zaman bildiren string
	 */
	@NonNull
	public static String getDayTime(
			@NonNull final Context context,
			@IntRange(from = 0, to = 23) final int hour,
			@IntRange(from = 0, to = 59) final int minute) {
		
		if (hour >= 5 && hour <= 7) {return context.getString(R.string.cockcrow);}
		if (hour >= 8 && hour <= 10) {return context.getString(R.string.morning);}
		if (hour == 11 && minute <= 40) {return context.getString(R.string.toward_noon);}
		if (hour >= 11 && hour < 13) {return context.getString(R.string.noon);}
		if (hour >= 13 && hour < 17) {return context.getString(R.string.after_noon);}
		if (hour == 17) {return context.getString(R.string.toward_evening);}
		if (hour >= 18 && hour < 22) {return context.getString(R.string.evening);}
		if (hour >= 22 && hour <= 23) {return context.getString(R.string.night);}
		if (hour == 0) {return context.getString(R.string.middle_night);}
		if (hour >= 1 && hour <= 3) {return context.getString(R.string.after_middle_night);}
		if (hour >= 3 && hour < 5) {return context.getString(R.string.toward_morning);}
		
		return "";
	}
	
	
	public interface DayPart {
		
		int VERY_MORNING = 0, MORNING = 1, TOWARD_NOON = 2, NOON = 3, AFTER_NOON = 4, TOWARD_NIGHT = 5, NIGHT = 6, MIDDLE_NIGHT = 7, AFTER_NIGHT = 8, TOWARD_MORNING = 9;
	}
	
	/**
	 * Verilen zaman grubunun tüm zaman birimleri dahil edilerek string bir nesne döndürür.
	 *
	 * @param context       Context
	 * @param durationGroup Zaman grubu
	 * @return String
	 */
	@NotNull
	public static String toString(@NotNull Context context, @NotNull DurationGroup durationGroup) {
		
		LinkedList<String> list = new LinkedList<>();
		String             str;
		
		for (var part : durationGroup.getDurations()) {
			
			str = part.getValue() + " ";
			
			//@off
			switch (part.getUnit()) {
				
				case YEAR: str += context.getString(R.string.year); break;
				case MONTH: str += context.getString(R.string.month); break;
				case DAY: str += context.getString(R.string.day); break;
				case HOUR: str += context.getString(R.string.hour); break;
				case MINUTE: str += context.getString(R.string.minute); break;
				case SECOND: str += context.getString(R.string.second); break;
				case MILLISECOND: str += context.getString(R.string.millisecond); break;
			}
			
			list.add(str);
		}
		//@on
		return String.join(" ", list);
	}
	
	/**
	 * Verilen zaman grubunu, sadece belirtilen birimleri kullanarak bir string döndürür.
	 *
	 * @param context       Context
	 * @param durationGroup Zaman grubu
	 * @param units         Zaman birimleri
	 * @return String
	 */
	@NotNull
	public static String toString(@NotNull Context context, @NotNull DurationGroup durationGroup, Unit... units) {
		
		var                _units = Arrays.asList(units);
		LinkedList<String> list   = new LinkedList<>();
		String             str;
		
		for (var part : durationGroup.getDurations()) {
			
			str = part.getValue() + " ";
			
			switch (part.getUnit()) {
				
				case YEAR:
					if (_units.contains(Unit.YEAR))
						str += context.getString(R.string.year);
					else continue;
					break;
				case MONTH:
					if (_units.contains(Unit.MONTH))
						str += context.getString(R.string.month);
					else continue;
					break;
				case DAY:
					if (_units.contains(Unit.DAY))
						str += context.getString(R.string.day);
					else continue;
					break;
				case HOUR:
					if (_units.contains(Unit.HOUR))
						str += context.getString(R.string.hour);
					else continue;
					break;
				case MINUTE:
					if (_units.contains(Unit.MINUTE))
						str += context.getString(R.string.minute);
					else continue;
					break;
				case SECOND:
					if (_units.contains(Unit.SECOND))
						str += context.getString(R.string.second);
					else continue;
					break;
				case MILLISECOND:
					if (_units.contains(Unit.MILLISECOND))
						str += context.getString(R.string.millisecond);
					else continue;
					break;
			}
			
			list.add(str);
		}
		
		return String.join(" ", list);
	}
}
