package com.tr.hsyn.time;


import com.tr.hsyn.collection.Lister;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;


/**
 * Provides many {@link Duration} objects to be used.<br>
 * Each time unit can only be used <u>once</u> in a {@linkplain DurationGroup} object.<br>
 * Duration specified with this class can be converted to calendar time.
 *
 * @see Unit
 * @see Duration
 */
public class DurationGroup implements Comparable<DurationGroup>, Iterable<Duration> {
	
	/**
	 * Empty {@link DurationGroup} consists of {@link Duration} objects with value of zero
	 */
	public static final DurationGroup        EMPTY = new DurationGroup(new ArrayList<>(0));
	private final       Duration             year;
	private final       Duration             month;
	private final       Duration             day;
	private final       Duration             hour;
	private final       Duration             minute;
	private final       Duration             second;
	private final       Duration             mlSecond;
	private final       LinkedList<Duration> durations;
	
	/**
	 * Creates a new {@link DurationGroup}
	 *
	 * @param durations list of durations
	 */
	DurationGroup(@NotNull List<Duration> durations) {
		
		//durations.sort(Comparator.comparing(Duration::getUnit));
		this.durations = new LinkedList<>();
		year           = getDuration(Unit.YEAR, durations);
		month          = getDuration(Unit.MONTH, durations);
		day            = getDuration(Unit.DAY, durations);
		hour           = getDuration(Unit.HOUR, durations);
		minute         = getDuration(Unit.MINUTE, durations);
		second         = getDuration(Unit.SECOND, durations);
		mlSecond       = getDuration(Unit.MILLISECOND, durations);
		
		this.durations.add(year);
		this.durations.add(month);
		this.durations.add(day);
		this.durations.add(hour);
		this.durations.add(minute);
		this.durations.add(second);
		this.durations.add(mlSecond);
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
		
		return durations.get(unit.ordinal());
	}
	
	@NotNull
	private Duration getDuration(@NotNull Unit unit, @NotNull List<Duration> durations) {
		
		return durations.stream().filter(duration -> duration.getUnit().equals(unit)).findFirst().orElse(Duration.ofZero(unit));
	}
	
	/**
	 * Adds the given {@link Duration} to this {@link DurationGroup}.
	 * Adding a duration is adding the value to the existing duration.<br><br>
	 *
	 * <pre>
	 * var dg = DurationGroup.builder()
	 * 	.year(1)
	 * 	.month(2)
	 * 	.day(3)
	 * 	.hour(4)
	 * 	.minute(5)
	 * 	.second(6)
	 * 	.milliSecond(7)
	 * 	.build();
	 *
	 * 	System.out.println(dg); // Y1M2D3H4M5S6M7
	 * 	dg = dg.addDuration(Duration.ofYear(2)); // returns a new object, not the existing one
	 * 	System.out.println(dg); // Y3M2D3H4M5S6M7
	 * </pre>
	 *
	 * @param duration {@link Duration}
	 * @return new {@link DurationGroup}
	 */
	public DurationGroup addDuration(@NotNull Duration duration) {
		
		Duration thisDuration = durations.get(duration.getUnit().ordinal());
		
		switch (duration.getUnit()) {
			case MILLISECOND: return setDuration(Duration.of(Unit.MILLISECOND, thisDuration.getValue() + duration.getValue()));
			case SECOND: return setDuration(Duration.of(Unit.SECOND, thisDuration.getValue() + duration.getValue()));
			case MINUTE: return setDuration(Duration.of(Unit.MINUTE, thisDuration.getValue() + duration.getValue()));
			case HOUR: return setDuration(Duration.of(Unit.HOUR, thisDuration.getValue() + duration.getValue()));
			case DAY: return setDuration(Duration.of(Unit.DAY, thisDuration.getValue() + duration.getValue()));
			case MONTH: return setDuration(Duration.of(Unit.MONTH, thisDuration.getValue() + duration.getValue()));
			case YEAR: return setDuration(Duration.of(Unit.YEAR, thisDuration.getValue() + duration.getValue()));
		}
		;
		
		throw new IllegalArgumentException("Unknown unit: " + duration.getUnit());
	}
	
	/**
	 * Sets the duration of this {@link DurationGroup}.
	 * Setting a duration is replacing the existing duration.<br><br>
	 *
	 * <pre>
	 * var dg = DurationGroup.builder()
	 * 	.year(1)
	 * 	.month(2)
	 * 	.day(3)
	 * 	.hour(4)
	 * 	.minute(5)
	 * 	.second(6)
	 * 	.milliSecond(7)
	 * 	.build();
	 *
	 * 	System.out.println(dg); // Y1M2D3H4M5S6M7
	 * 	dg = dg.setDuration(Duration.ofYear(2)); // returns a new object, not the existing one
	 * 	System.out.println(dg); // Y2M2D3H4M5S6M7
	 * </pre>
	 *
	 * @param duration {@link Duration}
	 * @return new {@link DurationGroup}
	 */
	public DurationGroup setDuration(@NotNull Duration duration) {
		
		@NotNull List<Duration> newDurations = Lister.listOf(durations);
		newDurations.set(duration.getUnit().ordinal(), duration);
		return new DurationGroup(newDurations);
	}
	
	/**
	 * Returns the total {@link Duration} of this {@link DurationGroup} as specified unit.
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
			durations.add(this.durations.get(unit.ordinal()));
		
		return new DurationGroup(durations);
	}
	
	/**
	 * Normally the biggest unit is {@link Unit#YEAR}.
	 * If its value is zero, then the biggest unit is {@link Unit#MONTH}.
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
	public boolean isZero() {
		
		return durations.stream().allMatch(Duration::isZero);
	}
	
	/**
	 * Determines if a {@link Unit} value is zero in this {@link DurationGroup}.
	 *
	 * @param unit unit
	 * @return {@code true} if given unit is zero in this {@link DurationGroup}
	 */
	public boolean isZeroByUnit(@NotNull Unit unit) {
		
		return getDuration(unit).isZero();
	}
	
	//region GETTERS
	
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
	//endregion
	
	/**
	 * @return duration list
	 */
	public List<Duration> getDurations() {
		
		return durations;
	}
	
	/**
	 * Checks if this {@link DurationGroup} has the specified time unit.
	 *
	 * @param unit time unit to select
	 * @return {@code true} if the unit exists and its value is not zero
	 */
	public boolean isNotZeroByUnit(@NotNull Unit unit) {
		
		return getDuration(unit).isNotZero();
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
		
		for (Duration duration : durations) {
			
			switch (duration.getUnit()) {
				case YEAR: return date.plusYears(duration.getValue());
				case MONTH: return date.plusMonths(duration.getValue());
				case DAY: return date.plusDays(duration.getValue());
				case HOUR: return date.plusHours(duration.getValue());
				case MINUTE: return date.plusMinutes(duration.getValue());
				case SECOND: return date.plusSeconds(duration.getValue());
				case MILLISECOND: return date.plusSeconds(duration.getValue() * 1000);
			}
		}
		
		return date;
	}
	
	/**
	 * Returns a string representation of this {@link DurationGroup}.<br>
	 *
	 * @return a string representation
	 */
	@SuppressWarnings("DefaultLocale")
	@Override
	public String toString() {
		
		return String.format("Y%dM%dD%dH%dM%dS%dM%d", year.getValue(), month.getValue(), day.getValue(), hour.getValue(), minute.getValue(), second.getValue(), mlSecond.getValue());
	}
	
	/**
	 * Returns a string representation of this {@link DurationGroup}.<br>
	 * Formatted string is: <br>
	 * <code>Y{year}M{month}D{day}H{hour}M{minute}S{second}M{millisecond}</code> respectively.<br>
	 * The order starts from <code>1</code>
	 * and increments by <code>1</code> until it reaches <code>7</code>.<br>
	 * And can write like this {@code month} and {@code day} <br>
	 * {@code '%2$d months %3$d days'} as formatted string.
	 *
	 * <p>
	 * <p>
	 * <pre>
	 * var dg = DurationGroup.builder()
	 * 	.year(1)
	 * 	.month(2)
	 * 	.day(3)
	 * 	.hour(4)
	 * 	.minute(5)
	 * 	.second(6)
	 * 	.milliSecond(7)
	 * 	.build();
	 *
	 * 	System.out.println(dg); // Y1M2D3H4M5S6M7
	 * 	System.out.println(dg.toString("%2$d months %3$d days")); // 2 month 3 day
	 * </pre>
	 *
	 * @param formatted formatted string
	 * @return a string representation
	 */
	public String toString(String formatted) {
		
		return String.format(formatted, year.getValue(), month.getValue(), day.getValue(), hour.getValue(), minute.getValue(), second.getValue());
	}
	
	public String toString(Unit... units) {
		
		@NotNull DurationGroup durations = pickFrom(units);
		StringBuilder          sb        = new StringBuilder();
		
		Lister.loopWith(durations, duration -> {
			
			duration.isNotZero(d -> {
				
			});
		});
		
		
		return sb.toString();
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
		
		int comp = Long.compare(year.getValue(), durationGroup.year.getValue());
		
		if (comp == 0L) comp = Long.compare(month.getValue(), durationGroup.month.getValue());
		if (comp == 0L) comp = Long.compare(day.getValue(), durationGroup.day.getValue());
		if (comp == 0L) comp = Long.compare(hour.getValue(), durationGroup.hour.getValue());
		if (comp == 0L) comp = Long.compare(minute.getValue(), durationGroup.minute.getValue());
		if (comp == 0L) comp = Long.compare(second.getValue(), durationGroup.second.getValue());
		if (comp == 0L) comp = Long.compare(mlSecond.getValue(), durationGroup.mlSecond.getValue());
		
		return comp;
	}
	
	/**
	 * Returns the calendar equal of the durations.
	 * Negative-valued durations move backwards in the calendar,
	 * positive values move forward.<br>
	 * The start time for the calculation is selected as the now.
	 *
	 * @return LocalDateTime
	 */
	public LocalDateTime toLocalDateTime() {
		
		LocalDateTime date = Time.time().getDateTime();
		
		for (Duration duration : durations) {
			
			switch (duration.getUnit()) {
				case YEAR: return date.plusYears(duration.getValue());
				case MONTH: return date.plusMonths(duration.getValue());
				case DAY: return date.plusDays(duration.getValue());
				case HOUR: return date.plusHours(duration.getValue());
				case MINUTE: return date.plusMinutes(duration.getValue());
				case SECOND: return date.plusSeconds(duration.getValue());
				case MILLISECOND: return date.plusSeconds(duration.getValue() * 1000);
			}
		}
		
		return date;
	}
	
	@NotNull
	@Override
	public Iterator<Duration> iterator() {
		
		return durations.iterator();
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
	
	public static void main(String[] args) {
		
		@NotNull DurationGroup dg = DurationGroup.builder()
				.year(1)
				.month(2)
				.day(3)
				.hour(4)
				.minute(5)
				.second(6)
				.milliSecond(7)
				.build();
		
		System.out.println(dg);
		dg = dg.setDuration(Duration.ofYear(2));
		System.out.println(dg);
		
	}
	
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
	
	public static final class Stringer {
		
		private final List<Unit>                 units     = new ArrayList<>();
		private final LinkedList<Duration>       durations = new LinkedList<>();
		private       boolean                    zeros;
		private       int                        maxItemSize;
		private       String                     formattedString;
		private       Function<Duration, String> formatter;
		
		public Stringer formatter(Function<Duration, String> formatter) {
			
			this.formatter = formatter;
			return this;
		}
		
		/**
		 * Sets the formatted string.
		 *
		 * @param formatted formatted
		 * @return this {@link Stringer}
		 * @see Duration#toString(String)
		 */
		public Stringer formattedString(String formatted) {
			
			this.formattedString = formatted;
			return this;
		}
		
		/**
		 * Sets the zero flag.
		 *
		 * @param zeros {@code true} if zero durations should be used, {@code false} otherwise
		 * @return this {@link Stringer}
		 */
		public Stringer zeros(boolean zeros) {
			
			this.zeros = zeros;
			return this;
		}
		
		/**
		 * Sets the maximum item size.
		 *
		 * @param maxUnit maximum item size. Used when not set the {@link #units}.
		 * @return this {@link Stringer}
		 */
		public Stringer maxItemSize(int maxUnit) {
			
			this.maxItemSize = maxUnit;
			return this;
		}
		
		/**
		 * Sets the units to be used.
		 *
		 * @param units units
		 * @return this {@link Stringer}
		 */
		@NotNull
		public Stringer units(Unit @NotNull ... units) {
			
			this.units.clear();
			this.units.addAll(Arrays.asList(units));
			return this;
		}
		
		/**
		 * Sets the durations to be used.
		 *
		 * @param durations durations
		 * @return this {@link Stringer}
		 */
		public Stringer duration(Duration @NotNull ... durations) {
			
			this.durations.clear();
			this.durations.addAll(Arrays.asList(durations));
			return this;
			
		}
		
		/**
		 * Sets the durations to be used.
		 *
		 * @param durations durations
		 * @return this {@link Stringer}
		 */
		public Stringer durations(@NotNull List<Duration> durations) {
			
			this.durations.clear();
			this.durations.addAll(durations);
			return this;
		}
		
		/**
		 * Returns the string of the {@link DurationGroup}
		 *
		 * @return the string
		 */
		@NotNull
		public String toString() {
			
			if (units.isEmpty()) {
				//+ this is the item size to be removed
				int itemSize = durations.size() - maxItemSize;
				//+ remove from last until the desired size
				Lister.loopWithout(itemSize, durations::removeLast);
			}
			else {
				//+ remove undesired units
				this.durations.removeIf(d -> !units.contains(d.getUnit()));
			}
			
			//+ if zero value then remove if need be
			if (!zeros) durations.removeIf(Duration::isZero);
			
			StringBuilder builder = new StringBuilder();
			
			for (Duration duration : durations) {
				
				if (formatter != null) builder.append(formatter.apply(duration)).append(" ");
				else if (formattedString != null) builder.append(duration.toString(formattedString)).append(" ");
				else builder.append(duration).append(" ");
			}
			
			return builder.toString().trim();
		}
		
		/**
		 * Returns a builder.
		 *
		 * @return {@link Stringer}
		 */
		@NotNull
		public static Stringer builder() {return new Stringer();}
		
	}
	
}
