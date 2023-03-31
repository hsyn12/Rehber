package com.tr.hsyn.time;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Birden fazla {@link Duration} nesnesinin kullanılabilmesini sağlar.<br>
 * Bir {@linkplain DurationGroup} nesnesinde her zaman birimi sadece <u>bir kez</u> kullanılabilir.<br>
 * Bu sınıf ile belirtilen süre takvim zamanına çevrilebilir.
 *
 * @see Unit
 * @see Duration
 */
public class DurationGroup {
	
	private final Duration       year;
	private final Duration       month;
	private final Duration       day;
	private final Duration       hour;
	private final Duration       minute;
	private final Duration       second;
	private final Duration       mlSecond;
	private final List<Duration> durations;
	
	DurationGroup(@NotNull List<Duration> durations) {
		
		this.durations = getNotZeroDurations(durations);
		year           = getDuration(Unit.YEAR);
		month          = getDuration(Unit.MONTH);
		day            = getDuration(Unit.DAY);
		hour           = getDuration(Unit.HOUR);
		minute         = getDuration(Unit.MINUTE);
		second         = getDuration(Unit.SECOND);
		mlSecond       = getDuration(Unit.MILLISECOND);
	}
	
	DurationGroup(@NotNull Builder builder) {
		
		year      = builder.year;
		month     = builder.month;
		day       = builder.day;
		hour      = builder.hour;
		minute    = builder.minute;
		second    = builder.second;
		mlSecond  = builder.mlSecond;
		durations = getNotZeroDurations();
	}
	
	public Duration getYear() {
		
		return year;
	}
	
	public Duration getMonth() {
		
		return month;
	}
	
	public Duration getDay() {
		
		return day;
	}
	
	public Duration getHour() {
		
		return hour;
	}
	
	public Duration getMinute() {
		
		return minute;
	}
	
	public Duration getSecond() {
		
		return second;
	}
	
	public Duration getMilliSecond() {
		
		return mlSecond;
	}
	
	public List<Duration> getDurations() {
		
		return durations;
	}
	
	@NotNull
	private List<Duration> getNotZeroDurations(@NotNull List<Duration> durations) {
		
		return durations.stream().filter(Duration::isNotZero).sorted(Comparator.comparing(Duration::getUnit)).collect(Collectors.toCollection(LinkedList::new));
	}
	
	@NotNull
	private LinkedList<Duration> getNotZeroDurations() {
		
		LinkedList<Duration> result = new LinkedList<>();
		
		if (year.isNotZero()) result.add(year);
		if (month.isNotZero()) result.add(month);
		if (day.isNotZero()) result.add(day);
		if (hour.isNotZero()) result.add(hour);
		if (minute.isNotZero()) result.add(minute);
		if (second.isNotZero()) result.add(second);
		if (mlSecond.isNotZero()) result.add(mlSecond);
		
		return result;
	}
	
	@NotNull
	public Duration getDuration(Unit timeUnit) {
		
		return durations.stream().filter(d -> d.getUnit().equals(timeUnit)).findFirst().orElse(Duration.ofZero(timeUnit));
	}
	
	/**
	 * Bir zaman biriminin kullanılıp kullanılmadığını bildirir.
	 *
	 * @param timeUnit Zaman birimi
	 * @return Belirtilen zaman birimi kullanılmış ve sıfırdan farklı bir değer verilmişse {@code true}, aksi halde {@code false}
	 */
	public boolean exists(@NotNull Unit timeUnit) {
		
		return durations.stream().anyMatch(duration -> duration.getUnit().equals(timeUnit) && duration.isNotZero());
	}
	
	/**
	 * Sürenin takvimdeki karşılığını döndürür.
	 * Negatif değerli süreler takvimde geriye doğru ilerler,
	 * pozitif değerler ileri doğru ilerler.<br>
	 * Hesaplamanın yapılacağı başlangıç zamanı şimdiki zaman olarak seçilir.
	 *
	 * @return LocalDateTime
	 */
	public LocalDateTime toLocalDateTime() {
		
		var date = Time.time().getDateTime();
		
		for (var duration : durations) {
			
			switch (duration.getUnit()) {
				case YEAR:
					date = date.plusYears(duration.getValue());
					break;
				
				case MONTH:
					date = date.plusMonths(duration.getValue());
					break;
				
				case DAY:
					date = date.plusDays(duration.getValue());
					break;
				
				case HOUR:
					date = date.plusHours(duration.getValue());
					break;
				
				case MINUTE:
					date = date.plusMinutes(duration.getValue());
					break;
				
				case SECOND:
					date = date.plusSeconds(duration.getValue());
					break;
				
				case MILLISECOND:
					date = date.plusSeconds(duration.getValue() * 1000);
					break;
			}
		}
		
		return date;
	}
	
	/**
	 * Bildirilen sürenin takvimdeki karşılığını döndürür.
	 * Negatif değerli süreler takvimde geriye doğru ilerler,
	 * pozitif değerler ileri doğru ilerler.
	 *
	 * @param date Hesaplamanın başlatılacağı başlangıç zamanı.
	 * @return LocalDateTime
	 */
	public LocalDateTime toLocalDateTime(@NotNull LocalDateTime date) {
		
		for (var duration : durations) {
			
			switch (duration.getUnit()) {
				case YEAR:
					date = date.plusYears(duration.getValue());
					break;
				
				case MONTH:
					date = date.plusMonths(duration.getValue());
					break;
				
				case DAY:
					date = date.plusDays(duration.getValue());
					break;
				
				case HOUR:
					date = date.plusHours(duration.getValue());
					break;
				
				case MINUTE:
					date = date.plusMinutes(duration.getValue());
					break;
				
				case SECOND:
					date = date.plusSeconds(duration.getValue());
					break;
				
				case MILLISECOND:
					date = date.plusSeconds(duration.getValue() * 1000);
					break;
			}
		}
		
		return date;
	}
	
	@Override
	public String toString() {return this.durations.toString();}
	
	@NotNull
	public static Builder builder() {return new Builder();}
	
	@NotNull
	public static Builder builder(@NotNull DurationGroup object) {return new Builder(object);}
	
	@SuppressWarnings("UnusedReturnValue")
	public static final class Builder {
		
		private Duration year     = Duration.ofYear(0);
		private Duration month    = Duration.ofMonth(0);
		private Duration day      = Duration.ofDay(0);
		private Duration hour     = Duration.ofHour(0);
		private Duration minute   = Duration.ofMinute(0);
		private Duration second   = Duration.ofSecond(0);
		private Duration mlSecond = Duration.ofMillisecond(0);
		
		private Builder() {// Use static builder() method
		}
		
		private Builder(@NotNull DurationGroup object) {// Use static builder() method
			year     = object.year;
			month    = object.month;
			day      = object.day;
			hour     = object.hour;
			minute   = object.minute;
			second   = object.second;
			mlSecond = object.mlSecond;
		}
		
		public Builder year(long year) {
			
			this.year = Duration.ofYear(year);
			return this;
		}
		
		public Builder month(@Range(from = 0, to = Long.MAX_VALUE) long month) {
			
			this.month = Duration.ofMonth(month);
			return this;
		}
		
		public Builder day(@Range(from = 0, to = Long.MAX_VALUE) long day) {
			
			this.day = Duration.ofDay(day);
			return this;
		}
		
		public Builder hour(@Range(from = 0, to = Long.MAX_VALUE) long hour) {
			
			this.hour = Duration.ofHour(hour);
			return this;
		}
		
		public Builder minute(@Range(from = 0, to = Long.MAX_VALUE) long minute) {
			
			this.minute = Duration.ofMinute(minute);
			return this;
		}
		
		public Builder second(@Range(from = 0, to = Long.MAX_VALUE) long second) {
			
			this.second = Duration.ofSecond(second);
			return this;
		}
		
		public Builder milliSecond(@Range(from = 0, to = Long.MAX_VALUE) long mlSecond) {
			
			this.mlSecond = Duration.ofMillisecond(mlSecond);
			return this;
		}
		
		@NotNull
		public DurationGroup build() {
			
			return new DurationGroup(this);
		}
	}
}
