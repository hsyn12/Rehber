package com.tr.hsyn.time;


import com.tr.hsyn.random.Randoom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.function.Supplier;


/**
 * Zamanla ilgili tüm işlemler.
 *
 * @author hsyn 25 Kasım 2022 Cuma 14:07
 */
public class Time implements TimeMillis {
	
	/**
	 * <pre>
	 *    All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The following pattern letters are defined:
	 *
	 *   Symbol  Meaning                     Presentation      Examples
	 *   ------  -------                     ------------      -------
	 *    G       era                         text              AD; Anno Domini; A
	 *    u       year                        year              2004; 04
	 *    y       year-of-era                 year              2004; 04
	 *    D       day-of-year                 number            189
	 *    M/L     month-of-year               number/text       7; 07; Jul; July; J
	 *    d       day-of-month                number            10
	 *
	 *    Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
	 *    Y       week-based-year             year              1996; 96
	 *    w       week-of-week-based-year     number            27
	 *    W       week-of-month               number            4
	 *    E       day-of-week                 text              Tue; Tuesday; T
	 *    e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
	 *    F       week-of-month               number            3
	 *
	 *    a       am-pm-of-day                text              PM
	 *    h       clock-hour-of-am-pm (1-12)  number            12
	 *    K       hour-of-am-pm (0-11)        number            0
	 *    k       clock-hour-of-am-pm (1-24)  number            0
	 *
	 *    H       hour-of-day (0-23)          number            0
	 *    m       minute-of-hour              number            30
	 *    s       second-of-minute            number            55
	 *    S       fraction-of-second          fraction          978
	 *    A       milli-of-day                number            1234
	 *    n       nano-of-second              number            987654321
	 *    N       nano-of-day                 number            1234000000
	 *
	 *    V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
	 *    z       time-zone name              zone-name         Pacific Standard Time; PST
	 *    O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
	 *    X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
	 *    x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
	 *    Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
	 *
	 *    p       pad next                    pad modifier      1
	 *
	 *    '       escape for text             delimiter
	 *    ''      single quote                literal           '
	 *    [       optional section start
	 *    ]       optional section end
	 *    #       reserved for future use
	 *    {       reserved for future use
	 *    }       reserved for future use
	 * </pre>
	 *
	 * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html">DateTimeFormatter</a>
	 */
	public static final String DEFAULT_DATE_TIME_PATTERN = "d MMMM yyyy EEEE HH:mm";
	
	/**
	 * Zaman birimlerini değiştirir.
	 */
	public static final FromNow       FromNow = new FromNow() {};
	/**
	 * Zamanda bir noktaya işaret eder.
	 */
	public static final TimePointer   Pointer = new TimePointer() {};
	/**
	 * Gün ismi
	 */
	private final       String        dayName;
	/**
	 * Tutulan zamanın milisaniye karşılığı
	 */
	private final       long          millis;
	/**
	 * Tutulan zamanın tarih saat değeri
	 */
	private final       LocalDateTime dateTime;
	
	private Time() {this(new Date(now()));}
	
	private Time(@NotNull Date date) {
		
		millis  = date.getTime();
		dayName = format("%tA", date);
		//monthName = format("%tB", date);
		
		dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
	}
	
	private Time(int year) {this(1, 1, year, 0, 0);}
	
	private Time(
			@Range(from = 1, to = 31) int day,
			@Range(from = 1, to = 12) int month,
			int year,
			@Range(from = 0, to = 23) int hour,
			@Range(from = 0, to = 59) int minute) {
		
		this(LocalDateTime.of(year, month, day, hour, minute, 0).toEpochSecond(com.tr.hsyn.time.Date.DEFAULT_ZONE_OFFSET) * 1000);
	}
	
	private Time(long date) {this(new Date(date));}
	
	private Time(int day, int month, int year) {this(day, month, year, 0, 0);}
	
	@NotNull
	@Override
	public String toString() {
		
		return dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));
	}
	
	@NotNull
	public String toString(@Nullable String pattern) {
		
		return pattern != null ? dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN)) : toString();
	}
	
	/**
	 * @return LocalDateTime
	 */
	public LocalDateTime getDateTime() {
		
		return dateTime;
	}
	
	public long getMillis() {
		
		return millis;
	}
	
	/**
	 * @return {@link DayOfWeek}
	 */
	public DayOfWeek getDayOfWeek() {
		
		return dateTime.getDayOfWeek();
	}
	
	/**
	 * @return Name of the day
	 */
	public String getDayName() {
		
		return dayName;
	}
	
	/**
	 * @return DayOfMonth [1-31]
	 */
	public int getDayOfMonth() {
		
		return dateTime.getDayOfMonth();
	}
	
	/**
	 * @return Year
	 */
	public int getYear() {
		
		return dateTime.getYear();
	}
	
	/**
	 * @return {@link Month}
	 */
	public Month getMonth() {
		
		return dateTime.getMonth();
	}
	
	/**
	 * @return MonthValue [1-12]
	 */
	public int getMonthValue() {
		
		return dateTime.getMonthValue();
	}
	
	/**
	 * Zamanı string'e çevirir.
	 *
	 * @param millis Zaman
	 * @return {@link #DEFAULT_DATE_TIME_PATTERN} formatında bir string
	 */
	public static @NotNull String toString(long millis) {
		
		return toString(millis, DEFAULT_DATE_TIME_PATTERN);
	}
	
	/**
	 * @return Şimdiki zaman nesnesi
	 */
	@NotNull
	public static Time time() {return new Time();}
	
	/**
	 * @param date Tarih
	 * @return Verilen tarihe ait zaman nesnesi
	 */
	@NotNull
	public static Time of(@NotNull Date date) {return new Time(date);}
	
	/**
	 * @param date Zaman (Epoch millis)
	 * @return Verilen tarihe ait zaman nesnesi
	 */
	@NotNull
	public static Time of(long date) {return new Time(date);}
	
	/**
	 * @param day    1-31
	 * @param month  1-12
	 * @param year   Year
	 * @param hour   0-23
	 * @param minute 0-59
	 * @return Verilen tarihe ait zaman nesnesi
	 */
	@NotNull
	public static Time of(
			@Range(from = 1, to = 31) int day,
			@Range(from = 1, to = 12) int month,
			int year,
			@Range(from = 0, to = 23) int hour,
			@Range(from = 0, to = 59) int minute) {return new Time(day, month, year, hour, minute);}
	
	/**
	 * @param day   1-31
	 * @param month 1-12
	 * @param year  Year
	 * @return Verilen tarihe ait zaman nesnesi
	 */
	@NotNull
	public static Time of(
			@Range(from = 1, to = 31) int day,
			@Range(from = 1, to = 12) int month,
			int year) {return new Time(day, month, year);}
	
	/**
	 * Yıl dışındaki tüm zaman birimleri ilk değerlerini alır.
	 * Mesela 1981 için {@code 1.1.1981 00:00:00}
	 *
	 * @param year Year
	 * @return Verilen tarihe ait zaman nesnesi
	 */
	@NotNull
	public static Time of(int year) {return new Time(year);}
	
	/**
	 * @return Zamanın başlangıcından şimdiye kadar geçen milisaniye sayısı.
	 */
	public static long now() {return System.currentTimeMillis();}
	
	/**
	 * Verilen saniyeyi {@code 00:00:00:00} formatında
	 * {@code gün:saat:dakika:saniye} olarak döndürür.
	 * Eğer verilen saniye gün etmiyorsa {@code saat:dakika:saniye} olarak verir.
	 * Eğer verilen saniye saat etmiyorsa {@code dakika:saniye} olarak verir.
	 *
	 * @param seconds Saniye
	 * @return Duration string
	 */
	@NotNull
	public static String formatSeconds(int seconds) {return formatMilliseconds(seconds * SECOND);}
	
	/**
	 * Verilen milisaniyeyi {@code 00:00:00:00} formatında
	 * {@code gün:saat:dakika:saniye} olarak döndürür.
	 * Eğer verilen milisaniye gün etmiyorsa {@code saat:dakika:saniye} olarak verir.
	 * Eğer verilen milisaniye saat etmiyorsa {@code dakika:saniye} olarak verir.
	 *
	 * @param milliseconds Milisaniye
	 * @return Duration string
	 */
	@NotNull
	public static String formatMilliseconds(long milliseconds) {
		
		long day    = milliseconds / DAY;
		long hour   = (milliseconds % DAY) / HOUR;
		long minute = ((milliseconds % DAY) % HOUR) / MINUTE;
		long second = (((milliseconds % DAY) % HOUR) % MINUTE) / SECOND;
		
		if (day == 0)
			return hour == 0 ?
					format("%02d:%02d", minute, second) :
					format("%02d:%02d:%02d", hour, minute, second);
		
		return format("%02d:%02d:%02d:%02d", day, hour, minute, second);
	}
	
	/**
	 * Zamanın başlangıcından verilen zamana kadar olan zaman içinde rastgele bir zaman döndür.
	 *
	 * @param end Zamanın sınır noktası
	 * @return Verilen sınır noktasından önce bir zaman
	 */
	public static long randomDate(long end) {return Randoom.getLong(end);}
	
	/**
	 * Verilen iki zaman arasında rastgele bir zaman döndür.
	 *
	 * @param start Zamanın başlangıç noktası
	 * @param end   Zamanın bitiş noktası
	 * @return İki zaman arasında bir zaman
	 */
	public static long randomDate(long start, long end) {return Randoom.getLong(start, end);}
	
	/**
	 * Zamanın başlangıcından şimdiye kadar olan zaman içinde rastgele bir zaman.
	 *
	 * @return Bir zaman
	 */
	public static long randomDate() {return Randoom.getLong(now());}
	
	/**
	 * Şimdiki zamanı verilen formata göre string olarak döndürür.
	 *
	 * @param pattern Format pattern (d MMMM yyyy EEEE HH:mm:ss:SSS:nnnnnnnnn)
	 * @return Zaman
	 */
	@NotNull
	public static String ToString(@NotNull String pattern) {return toString(System.currentTimeMillis(), pattern);}
	
	@NotNull
	public static String ToString() {return toString(now(), DEFAULT_DATE_TIME_PATTERN);}
	
	@NotNull
	public static String ToString(@NotNull LocalDateTime date) {return date.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_PATTERN));}
	
	/**
	 * Verilen zamana formatı uygular.
	 *
	 * @param time    Zaman
	 * @param pattern Format pattern (d MMMM yyyy EEEE HH:mm:ss:SSS:nnnnnnnnn)
	 * @return Zaman
	 */
	@NotNull
	public static String toString(final long time, @NotNull String pattern) {
		
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault())
				.format(DateTimeFormatter.ofPattern(pattern));
	}
	
	/**
	 * Verilen milisaniyelerin, zamanın başlangıcından itibaren
	 * ne kadar zaman ettiğini en büyük zaman birimi ile döndürür.
	 *
	 * <pre>
	 *
	 * var myBirthDay  = Pointer.at(1981);
	 * var startOfTime = getDate(0L);
	 *
	 * System.out.printf("%s\n", startOfTime);
	 * System.out.printf("%s", howLongTime(myBirthDay));
	 * // 1 Ocak 1970 Perşembe
	 * // TimeInfo{type=YEAR, value=11}
	 * // Zamanın başlangıcından 1981 yılına kadar 11 yıl varmış
	 *
	 * </pre>
	 * <p>
	 * Burada aslında zamanla ilgili bir şey yok,
	 * sadece milisaniyelerin ne kadar süre ettiğini buluyoruz çünkü başlangıç noktası hep sıfır.
	 * Yani zamanın başlangıcını düşünmemize gerek yok.<br>
	 * Mesela 1000 milisaniye 1 saniye eder.<br>
	 * Mesela 1000 * 60 milisaniye 1 dakika eder.<br>
	 * Süre daima en büyük zaman birimi ile döner.
	 * <u>Milisaniyeyi temsil edilebilecek en büyük zaman birimine çeviriyoruz</u> kısaca.
	 * Ve bu çeviri çok kaba bir şekilde yapılıyor.
	 * Mesela 7 Gün eden bir milisaniye bize 1 Hafta döner.
	 * Aynı zamanda 8-9-10-11-12-13 Gün eden milisaniyeler de bize 1 Hafta döner.
	 * 14. günde 2 Hafta döner ve 15-16-17-18-19-20 gün eden milisaniyeler de 2 Hafta döner.
	 * Aynı şekilde aylar da böyle.
	 * Mesela gün sayısı 30 ile 59 arasında ise hep 1 Ay döner.
	 * Ay sayısı 12'yi geçince de yıl dönmeye başlar.<br>
	 * Elimizdeki milisaniyeleri en genel en kaba şekilde ve en büyük zaman birimi olarak alıyoruz.
	 *
	 * @param duration Süre (milisaniye)
	 * @return Zaman bilgisi
	 */
	@Deprecated
	@NotNull
	public static Duration howLongTime(long duration) {
		
		if (duration < 0) throw new IllegalArgumentException("Zaman negatif olamaz : " + duration);
		
		long day = duration / DAY;
		
		if (day == 0) {
			
			if (duration < MINUTE) {
				
				return Duration.of(Unit.SECOND, duration / SECOND);
			}
			else if (duration < HOUR) {
				
				return Duration.of(Unit.MINUTE, duration / MINUTE);
			}
			
			return Duration.of(Unit.HOUR, duration / HOUR);
		}
		
		if (day < 30) {
			
			return Duration.of(Unit.DAY, day);
		}
		
		if (day < 365) {
			
			return Duration.of(Unit.MONTH, day / 30);
		}
		
		return Duration.of(Unit.YEAR, day / 365);
	}
	
	/**
	 * Verilen milisaniyelerin, zamanın başlangıcından itibaren
	 * ne kadar zaman ettiğini en büyük zaman birimi ile döndürür.
	 *
	 * <pre>
	 *
	 * var myBirthDay  = Pointer.at(1981);
	 * var startOfTime = getDate(0L);
	 *
	 * System.out.printf("%s\n", startOfTime);
	 * System.out.printf("%s", howLongTime(myBirthDay));
	 * // 1 Ocak 1970 Perşembe
	 * // TimeInfo{type=YEAR, value=11}
	 * // Zamanın başlangıcından 1981 yılına kadar 11 yıl varmış
	 *
	 * </pre>
	 * <p>
	 * Burada aslında zamanla ilgili bir şey yok,
	 * sadece milisaniyelerin ne kadar süre ettiğini buluyoruz çünkü başlangıç noktası hep sıfır.
	 * Yani zamanın başlangıcını düşünmemize gerek yok.<br>
	 * Mesela 1000 milisaniye 1 saniye eder.<br>
	 * Mesela 1000 * 60 milisaniye 1 dakika eder.<br>
	 * Süre daima en büyük zaman birimi ile döner.
	 * <u>Milisaniyeyi temsil edilebilecek en büyük zaman birimine çeviriyoruz</u> kısaca.
	 * Ve bu çeviri çok kaba bir şekilde yapılıyor.
	 * Mesela 7 Gün eden bir milisaniye bize 1 Hafta döner.
	 * Aynı zamanda 8-9-10-11-12-13 Gün eden milisaniyeler de bize 1 Hafta döner.
	 * 14. günde 2 Hafta döner ve 15-16-17-18-19-20 gün eden milisaniyeler de 2 Hafta döner.
	 * Aynı şekilde aylar da böyle.
	 * Mesela gün sayısı 30 ile 59 arasında ise hep 1 Ay döner.
	 * Ay sayısı 12'yi geçince de yıl dönmeye başlar.<br>
	 * Elimizdeki milisaniyeleri en genel en kaba şekilde ve en büyük zaman birimi olarak alıyoruz.
	 *
	 * @param longSupplier Süre (milisaniye)
	 * @return Zaman bilgisi
	 */
	@Deprecated
	@NotNull
	public static Duration howLongTime(@NotNull Supplier<Long> longSupplier) {
		
		long duration = longSupplier.get();
		
		if (duration < 0) throw new IllegalArgumentException("Zaman negatif olamaz : " + duration);
		
		long day = duration / DAY;
		
		if (day == 0) {
			
			if (duration < MINUTE) {
				
				return Duration.of(Unit.SECOND, duration / SECOND);
			}
			else if (duration < HOUR) {
				
				return Duration.of(Unit.MINUTE, duration / MINUTE);
			}
			
			return Duration.of(Unit.HOUR, duration / HOUR);
		}
		
		if (day < 30) {
			
			return Duration.of(Unit.DAY, day);
		}
		
		if (day < 365) {
			
			return Duration.of(Unit.MONTH, day / 30);
		}
		
		return Duration.of(Unit.YEAR, day / 365);
	}
	
	/**
	 * Verilen zamanın şimdiye oranla ne kadar zaman önce olduğunu
	 * mümkün olan en büyük birimle döndürür.<br>
	 * <br>
	 * <pre>
	 * var myBirthDay = Pointer.at(1981);
	 * System.out.printf("%s", howLongBefore(myBirthDay));
	 * //TimeInfo{type=YEAR, value=41} - yani 41 yıl önce
	 * </pre>
	 * --------------------------------<br>
	 *
	 * @param aDate Zaman
	 * @return Zamanın ne kadar önce olduğunu bildiren bir nesne
	 * @see Unit
	 */
	@NotNull
	public static Duration howLongBefore(long aDate) {
		
		LocalDateTime now  = Time.time().dateTime;
		LocalDateTime date = LocalDateTime.ofInstant(Instant.ofEpochMilli(aDate), com.tr.hsyn.time.Date.DEFAULT_ZONE_OFFSET);
		
		if (date.isAfter(now)) {
			
			throw new IllegalArgumentException("Verilen zaman geçmiş bir zaman olmalı");
		}
		
		int year = now.getYear() - date.getYear();
		
		if (year >= 1) {
			
			return Duration.of(Unit.YEAR, year);
		}
		
		int days = now.getDayOfYear() - date.getDayOfYear();
		
		if (days > 30) {
			
			return Duration.of(Unit.MONTH, days / 30);
		}
		
		if (days >= 1) {
			
			return Duration.of(Unit.DAY, days);
		}
		
		int hours = now.minusHours(date.getHour()).getHour();
		
		if (hours >= 1) {
			
			return Duration.of(Unit.HOUR, hours);
		}
		
		int minute = now.minusMinutes(date.getMinute()).getMinute();
		
		if (minute >= 1) {
			
			return Duration.of(Unit.MINUTE, minute);
		}
		
		int seconds = now.minusSeconds(date.getSecond()).getSecond();
		
		return Duration.of(Unit.SECOND, seconds);
	}
	
	/**
	 * Returns exactly how long the given milliseconds are.<br><br>
	 *
	 * <pre>
	 * var myBirthDay = Pointer.at(12, 4, 1981, 23, 30);
	 * var p = currentMillis() - myBirthDay;
	 * System.out.printf("%s", toDuration(p));
	 * // output : [Duration{type=YEAR, value=40}, Duration{type=MONTH, value=9}, Duration{type=DAY, value=6}, Duration{type=HOUR, value=17}, Duration{type=MINUTE, value=3}, Duration{type=SECOND, value=8}, Duration{type=MILLISECOND, value=568}]
	 * // So the duration from my birthday to now is 40 years 9 months 3 weeks 6 days 17 hours 3 minutes 8 seconds 568 milliseconds
	 * </pre>
	 *
	 * <p>
	 * This method can work with negative values.
	 *
	 * @param duration duration
	 * @return List of {@link Duration} objects declaring how long the duration is
	 */
	@NotNull
	public static DurationGroup toDuration(long duration) {
		
		DurationGroup.@NotNull Builder durationBuilder = DurationGroup.builder();
		boolean                        isNegative      = duration < 0L;
		duration = Math.abs(duration);
		
		while (true) {
			
			if (duration >= YEAR) {
				long year = duration / YEAR;
				if (isNegative) year = -year;
				durationBuilder.year(year);
				//durations.add(Duration.of(Unit.YEAR, duration / YEAR));
				duration %= YEAR;
			}
			else if (duration >= MONTH) {
				long month = duration / MONTH;
				if (isNegative) month = -month;
				durationBuilder.month(month);
				//durations.add(Duration.of(Unit.MONTH, duration / MONTH));
				duration %= MONTH;
			}
			else if (duration >= DAY) {
				long day = duration / DAY;
				if (isNegative) day = -day;
				durationBuilder.day(day);
				duration %= DAY;
			}
			else if (duration >= HOUR) {
				long hour = duration / HOUR;
				if (isNegative) hour = -hour;
				durationBuilder.hour(hour);
				duration %= HOUR;
			}
			else if (duration >= MINUTE) {
				
				long minute = duration / MINUTE;
				if (isNegative) minute = -minute;
				durationBuilder.minute(minute);
				duration %= MINUTE;
			}
			else {
				
				long second = duration / SECOND;
				if (isNegative) second = -second;
				durationBuilder.second(second);
				duration %= SECOND;
				break;
			}
		}
		
		if (isNegative) duration = -duration;
		durationBuilder.milliSecond(duration);
		//durations.add(Duration.of(Unit.MILLISECOND, duration));
		return durationBuilder.build();
	}
	
	public static void main(String[] args) {
		
		long now = System.currentTimeMillis();
		
		System.out.println(toString(now, "d.M.yyyy"));
		//System.out.println(toDuration(-now));
	}
	
	@NotNull
	private static String format(String msg, Object... args) {
		
		return String.format(Locale.getDefault(), msg, args);
	}
	
}
