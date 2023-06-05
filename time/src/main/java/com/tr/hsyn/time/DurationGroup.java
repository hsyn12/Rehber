package com.tr.hsyn.time;


import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


/**
 * Allows multiple {@link Duration} objects to be used.<br>
 * Each time unit can only be used <u>once</u> in a {@linkplain DurationGroup} object.<br>
 * Duration specified with this class can be converted to calendar time.
 *
 * @see Unit
 * @see Duration
 */
public class DurationGroup implements Comparable<DurationGroup> {
	
	/**
	 * Empty {@link DurationGroup} consists of {@link Duration} objects with value of zero
	 */
	public static final DurationGroup  EMPTY = new DurationGroup(new ArrayList<>(0));
	private final       Duration       year;
	private final       Duration       month;
	private final       Duration       day;
	private final       Duration       hour;
	private final       Duration       minute;
	private final       Duration       second;
	private final       Duration       mlSecond;
	private final       List<Duration> durations;
	
	/**
	 * Creates a new {@link DurationGroup}
	 *
	 * @param durations list of durations
	 */
	DurationGroup(@NotNull List<Duration> durations) {
		
		this.durations = durations;
		this.durations.sort(Comparator.comparing(Duration::getUnit));
		year     = getDuration(Unit.YEAR);
		month    = getDuration(Unit.MONTH);
		day      = getDuration(Unit.DAY);
		hour     = getDuration(Unit.HOUR);
		minute   = getDuration(Unit.MINUTE);
		second   = getDuration(Unit.SECOND);
		mlSecond = getDuration(Unit.MILLISECOND);
	}
	
	/**
	 * Creates a new {@link DurationGroup}
	 *
	 * @param builder {@link Builder}
	 */
	DurationGroup(@NotNull Builder builder) {
		
		year      = builder.year;
		month     = builder.month;
		day       = builder.day;
		hour      = builder.hour;
		minute    = builder.minute;
		second    = builder.second;
		mlSecond  = builder.mlSecond;
		durations = builder.durations;
	}
	
	/**
	 * Returns the duration of the specified time unit
	 *
	 * @param unit Unit of time
	 * @return {@link Duration}
	 */
	@NotNull
	public Duration getDuration(@NotNull Unit unit) {
		
		return durations.stream().filter(duration -> duration.getUnit().equals(unit)).findFirst().orElse(Duration.ofZero(unit));
	}
	
	/**
	 * Returns the total {@link Duration} of this {@link DurationGroup} as specified unit
	 *
	 * @param unit Unit of duration
	 * @return {@link Duration}
	 */
	public Duration getTotalDurationAs(@NotNull Unit unit) {
		
		long total = 0L;
		
		for (Duration duration : durations)
			total += duration.getValueAs(unit).getValue();
		
		return Duration.of(unit, total);
	}
	
	/**
	 * Picks the specified units from this {@link DurationGroup}
	 * and creates new {@link DurationGroup} with the picked ones.
	 *
	 * @param units units to pick from
	 * @return new {@link DurationGroup}
	 */
	@NotNull
	public DurationGroup pickFrom(Unit @NotNull ... units) {
		
		List<Duration> durations = new ArrayList<>(units.length);
		
		for (Unit unit : units)
			for (Duration duration : this.durations)
				if (duration.getUnit().equals(unit))
					durations.add(duration);
		
		return new DurationGroup(durations);
	}
	
	/**
	 * Normally the biggest unit is {@link Unit#YEAR}.
	 * But if its value is zero, then the biggest unit is {@link Unit#MONTH}.
	 * If its value is zero, then the biggest unit is {@link Unit#DAY} ... and so forth.
	 * İf all duration's value of this {@link DurationGroup} is zero,
	 * then the biggest unit is {@link Unit#YEAR} with value of zero.
	 *
	 * @return the biggest unit
	 */
	public Duration getGreatestUnit() {
		
		for (Duration duration : durations) {
			
			if (duration.isNotZero()) return duration;
		}
		
		return Duration.ofYear(0L);
	}
	
	/**
	 * @return {@code true} if all duration's value of this {@link DurationGroup} is zero
	 */
	public boolean isEmpty() {
		
		for (Duration duration : durations)
			if (duration.isNotZero()) return false;
		return true;
	}
	
	/**
	 * @return duration of year
	 */
	public Duration getYear() {
		
		return year;
	}
	
	/**
	 * @return duration of month
	 */
	public Duration getMonth() {
		
		return month;
	}
	
	/**
	 * @return duration of day
	 */
	public Duration getDay() {
		
		return day;
	}
	
	/**
	 * @return duration of hour
	 */
	public Duration getHour() {
		
		return hour;
	}
	
	/**
	 * @return duration of minute
	 */
	public Duration getMinute() {
		
		return minute;
	}
	
	/**
	 * @return duration of second
	 */
	public Duration getSecond() {
		
		return second;
	}
	
	/**
	 * @return duration of millisecond
	 */
	public Duration getMilliSecond() {
		
		return mlSecond;
	}
	
	/**
	 * @return duration list
	 */
	public List<Duration> getDurations() {
		
		return durations;
	}
	
	/**
	 * Checks if this {@link DurationGroup} contains the specified time unit.
	 *
	 * @param timeUnit time unit to check
	 * @return {@code true} if the unit exists and its value is not zero
	 */
	public boolean exists(@NotNull Unit timeUnit) {
		
		return durations.stream().anyMatch(duration -> duration.getUnit().equals(timeUnit) && duration.isNotZero());
	}
	
	/**
	 * Converts this {@link DurationGroup} to calendar time.
	 * Negative-valued durations move backwards in the calendar,
	 * positive values move forwards in the calendar.
	 *
	 * @param date {@link LocalDateTime} as starting point for calculation
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
	
	/**
	 * Returns a string representation of this {@link DurationGroup}.<br>
	 *
	 * @return a string representation
	 */
	@Override
	public String toString() {
		
		var sb = new StringBuilder();
		
		for (Duration duration : durations) {
			
			if (duration.isNotZero())
				sb.append(duration.getValue())
						.append(" ")
						.append(duration.getUnit()).append(" ");
		}
		
		
		return sb.toString().trim();
	}
	
	public String toString(Unit... units) {
		
		var durations = pickFrom(units);
		return durations.toString();
	}
	
	/**
	 * Compares this {@link DurationGroup} with another {@link DurationGroup}
	 *
	 * @param durationGroup {@link DurationGroup}
	 * @return -1, 0 or 1 if this {@link DurationGroup} is less than,
	 * 		equal to or greater than the specified {@link DurationGroup}
	 */
	@Override
	public int compareTo(@NotNull DurationGroup durationGroup) {
		
		var comp = Long.compare(year.getValue(), durationGroup.year.getValue());
		
		if (comp == 0L) comp = Long.compare(month.getValue(), durationGroup.month.getValue());
		if (comp == 0L) comp = Long.compare(day.getValue(), durationGroup.day.getValue());
		if (comp == 0L) comp = Long.compare(hour.getValue(), durationGroup.hour.getValue());
		if (comp == 0L) comp = Long.compare(minute.getValue(), durationGroup.minute.getValue());
		if (comp == 0L) comp = Long.compare(second.getValue(), durationGroup.second.getValue());
		if (comp == 0L) comp = Long.compare(mlSecond.getValue(), durationGroup.mlSecond.getValue());
		
		return comp;
	}
	
	/**
	 * Returns the calendar equivalent of the durations.
	 * Negative-valued durations move backwards in the calendar,
	 * positive values move forward.<br>
	 * The start time for the calculation is selected as the present time.
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
	 * Creates a {@link DurationGroup} builder
	 *
	 * @return {@link DurationGroup} builder
	 */
	@NotNull
	public static Builder builder() {return new Builder();}
	
	/**
	 * Creates a {@link DurationGroup} builder
	 *
	 * @param object {@link DurationGroup}
	 * @return {@link DurationGroup} builder
	 */
	@NotNull
	public static Builder builder(@NotNull DurationGroup object) {return new Builder(object);}
	
	/**
	 * Builder class for {@link DurationGroup}.
	 * Can be used to construct a {@link DurationGroup} unit by unit.
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static final class Builder {
		
		private final LinkedList<Duration> durations = new LinkedList<>();
		private       Duration             year      = Duration.ofYear(0);
		private       Duration             month     = Duration.ofMonth(0);
		private       Duration             day       = Duration.ofDay(0);
		private       Duration             hour      = Duration.ofHour(0);
		private       Duration             minute    = Duration.ofMinute(0);
		private       Duration             second    = Duration.ofSecond(0);
		private       Duration             mlSecond  = Duration.ofMillisecond(0);
		
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
		
		/**
		 * Sets the year
		 *
		 * @param year year
		 * @return {@link Builder} object for chaining
		 */
		public Builder year(long year) {
			
			this.year = Duration.ofYear(year);
			return this;
		}
		
		/**
		 * Sets the month
		 *
		 * @param month month
		 * @return {@link Builder} object for chaining
		 */
		public Builder month(long month) {
			
			this.month = Duration.ofMonth(month);
			return this;
		}
		
		/**
		 * Sets the day
		 *
		 * @param day day
		 * @return {@link Builder} object for chaining
		 */
		public Builder day(long day) {
			
			this.day = Duration.ofDay(day);
			return this;
		}
		
		/**
		 * Sets the hour
		 *
		 * @param hour hour
		 * @return {@link Builder} object for chaining
		 */
		public Builder hour(long hour) {
			
			this.hour = Duration.ofHour(hour);
			return this;
		}
		
		/**
		 * Sets the minute
		 *
		 * @param minute minute
		 * @return {@link Builder} object for chaining
		 */
		public Builder minute(long minute) {
			
			this.minute = Duration.ofMinute(minute);
			return this;
		}
		
		/**
		 * Sets the second
		 *
		 * @param second second
		 * @return {@link Builder} object for chaining
		 */
		public Builder second(long second) {
			
			this.second = Duration.ofSecond(second);
			return this;
		}
		
		/**
		 * Sets the millisecond
		 *
		 * @param mlSecond millisecond
		 * @return {@link Builder} object for chaining
		 */
		public Builder milliSecond(long mlSecond) {
			
			this.mlSecond = Duration.ofMillisecond(mlSecond);
			return this;
		}
		
		/**
		 * Builds the {@link DurationGroup}
		 *
		 * @return {@link DurationGroup}
		 */
		@NotNull
		public DurationGroup build() {
			
			setDurationList();
			return new DurationGroup(this);
		}
		
		private void setDurationList() {
			
			//LinkedList<Duration> durations = new LinkedList<>();
			durations.add(year);
			durations.add(month);
			durations.add(day);
			durations.add(hour);
			durations.add(minute);
			durations.add(second);
			durations.add(mlSecond);
		}
	}
}
