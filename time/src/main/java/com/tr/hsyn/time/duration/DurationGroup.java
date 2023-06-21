package com.tr.hsyn.time.duration;


import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.random.Randoom;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;


/**
 * Provides many {@link Duration} objects to be used.<br>
 * Each time unit can only be used <u>once</u> in a {@linkplain DurationGroup} object.<br>
 *
 * @see Unit
 * @see Duration
 */
public interface DurationGroup extends Comparable<DurationGroup>, Iterable<Duration> {
	
	/**
	 * Empty {@link DurationGroup} consists of {@link Duration} objects with value of zero
	 */
	DurationGroup EMPTY = new DurationGroupImp(new ArrayList<>(0));
	
	/**
	 * Returns the duration of the specified time unit
	 *
	 * @param unit Unit of time
	 * @return {@link Duration}
	 */
	@NotNull
	Duration getDuration(@NotNull Unit unit);
	
	/**
	 * Returns the duration of the specified time unit.
	 *
	 * @param unit      Unit
	 * @param durations list of durations to be used to get the duration
	 * @return {@link Duration}
	 */
	@NotNull
	default Duration getDuration(@NotNull Unit unit, @NotNull List<Duration> durations) {
		
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
	 * 	dg = dg.plus(Duration.ofYear(2)); // returns a new object, not the existing one
	 * 	System.out.println(dg); // Y3M2D3H4M5S6M7
	 * </pre>
	 *
	 * @param duration {@link Duration}
	 * @return new {@link DurationGroup}
	 */
	default DurationGroup plus(@NotNull Duration duration) {
		
		Duration thisDuration = getDurations().get(duration.getUnit().ordinal());
		
		switch (duration.getUnit()) {
			case MILLISECOND: return setDuration(Duration.of(Unit.MILLISECOND, thisDuration.getValue() + duration.getValue()));
			case SECOND: return setDuration(Duration.of(Unit.SECOND, thisDuration.getValue() + duration.getValue()));
			case MINUTE: return setDuration(Duration.of(Unit.MINUTE, thisDuration.getValue() + duration.getValue()));
			case HOUR: return setDuration(Duration.of(Unit.HOUR, thisDuration.getValue() + duration.getValue()));
			case DAY: return setDuration(Duration.of(Unit.DAY, thisDuration.getValue() + duration.getValue()));
			case MONTH: return setDuration(Duration.of(Unit.MONTH, thisDuration.getValue() + duration.getValue()));
			case YEAR: return setDuration(Duration.of(Unit.YEAR, thisDuration.getValue() + duration.getValue()));
		}
		
		throw new IllegalArgumentException("Unknown unit: " + duration.getUnit());
	}
	
	default DurationGroup plus(@NotNull DurationGroup durationGroup) {
		
		DurationGroup newDurationGroup = EMPTY;
		
		for (Duration duration : durationGroup) {
			
			if (duration.isZero()) continue;
			//@off
			switch (duration.getUnit()) {
				case MILLISECOND: newDurationGroup = setDuration(Duration.of(Unit.MILLISECOND, getDuration(Unit.MILLISECOND).getValue() + duration.getValue()));break;
				case SECOND: newDurationGroup = setDuration(Duration.of(Unit.SECOND, getDuration(Unit.SECOND).getValue() + duration.getValue()));break;
				case MINUTE: newDurationGroup = setDuration(Duration.of(Unit.MINUTE, getDuration(Unit.MINUTE).getValue() + duration.getValue()));break;
				case HOUR: newDurationGroup = setDuration(Duration.of(Unit.HOUR, getDuration(Unit.HOUR).getValue() + duration.getValue()));break;
				case DAY: newDurationGroup = setDuration(Duration.of(Unit.DAY, getDuration(Unit.DAY).getValue() + duration.getValue()));break;
				case MONTH: newDurationGroup = setDuration(Duration.of(Unit.MONTH, getDuration(Unit.MONTH).getValue() + duration.getValue()));break;
				case YEAR: newDurationGroup = setDuration(Duration.of(Unit.YEAR, getDuration(Unit.YEAR).getValue() + duration.getValue()));break;
			}//@on
		}
		
		return newDurationGroup;
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
	default DurationGroup setDuration(@NotNull Duration duration) {
		
		@NotNull List<Duration> newDurations = getDurations();
		newDurations.set(duration.getUnit().ordinal(), duration);
		return new DurationGroupImp(newDurations);
	}
	
	/**
	 * Returns the total {@link Duration} of this {@link DurationGroup} as specified unit.
	 *
	 * @param unit Unit of duration
	 * @return {@link Duration}
	 */
	default Duration getTotalDurationAs(@NotNull Unit unit) {
		
		long total = 0L;
		
		for (Duration duration : getDurations())
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
	default DurationGroup pickFrom(Unit @NotNull ... units) {
		
		List<Duration> durations     = new ArrayList<>(units.length);
		List<Duration> thisDurations = getDurations();
		
		for (Unit unit : units)
			durations.add(thisDurations.get(unit.ordinal()));
		
		return new DurationGroupImp(durations);
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
	default Duration getGreatestUnit() {
		
		return getDurations().stream().filter(duration -> !duration.isZero()).findFirst().orElse(Duration.ofYear(0L));
	}
	
	/**
	 * @return {@code true} if all duration's value of this {@link DurationGroup} is zero
	 */
	default boolean isZero() {
		
		return getDurations().stream().allMatch(Duration::isZero);
	}
	
	/**
	 * Determines if a {@link Unit} value is zero in this {@link DurationGroup}.
	 *
	 * @param unit unit
	 * @return {@code true} if given unit is zero in this {@link DurationGroup}
	 */
	default boolean isZeroByUnit(@NotNull Unit unit) {
		
		return getDuration(unit).isZero();
	}
	
	/**
	 * @return duration of year
	 */
	@NotNull
	Duration getYear();
	
	/**
	 * @return duration of month
	 */
	@NotNull
	Duration getMonth();
	
	/**
	 * @return duration of day
	 */
	@NotNull
	Duration getDay();
	
	/**
	 * @return duration of hour
	 */
	@NotNull
	Duration getHour();
	
	/**
	 * @return duration of minute
	 */
	@NotNull
	Duration getMinute();
	
	/**
	 * @return duration of second
	 */
	@NotNull
	Duration getSecond();
	
	/**
	 * @return duration of millisecond
	 */
	@NotNull
	Duration getMillisecond();
	
	/**
	 * @return duration list copy
	 */
	@NotNull
	List<Duration> getDurations();
	
	/**
	 * Checks if this {@link DurationGroup} has the specified time unit.
	 *
	 * @param unit time unit to select
	 * @return {@code true} if the unit exists and its value is not zero
	 */
	default boolean isNotZeroByUnit(@NotNull Unit unit) {
		
		return getDuration(unit).isNotZero();
	}
	
	/**
	 * Adds all durations to {@link LocalDateTime} now.
	 * Negative-valued durations move backwards in the calendar,
	 * positive values move forward.<br>
	 * Starting point is now.
	 *
	 * @return LocalDateTime
	 */
	@NotNull
	default LocalDateTime addToLocalDate() {
		
		LocalDateTime date = Time.localDateNow();
		
		for (Duration duration : getDurations()) {
			//@off
			switch (duration.getUnit()) {
				case YEAR:        date = date.plusYears(duration.getValue()); break;
				case MONTH:       date = date.plusMonths(duration.getValue()); break;
				case DAY:         date = date.plusDays(duration.getValue()); break;
				case HOUR:        date = date.plusHours(duration.getValue()); break;
				case MINUTE:      date = date.plusMinutes(duration.getValue()); break;
				case SECOND:      date = date.plusSeconds(duration.getValue()); break;
				case MILLISECOND: date = date.plusSeconds(duration.getValue() * 1000); break;
			}//@on
		}
		
		return date;
	}
	
	/**
	 * Adds all durations to calendar time.
	 * Negative-valued durations move backwards in the calendar,
	 * positive values move forwards in the calendar.
	 *
	 * @param date {@link LocalDateTime} as starting point for calculation
	 * @return LocalDateTime
	 */
	@NotNull
	default LocalDateTime addToLocalDate(@NotNull LocalDateTime date) {
		
		for (Duration duration : getDurations()) {
			//@off
			switch (duration.getUnit()) {
				case YEAR:        date = date.plusYears(duration.getValue()); break;
				case MONTH:       date = date.plusMonths(duration.getValue()); break;
				case DAY:         date = date.plusDays(duration.getValue()); break;
				case HOUR:        date = date.plusHours(duration.getValue()); break;
				case MINUTE:      date = date.plusMinutes(duration.getValue()); break;
				case SECOND:      date = date.plusSeconds(duration.getValue()); break;
				case MILLISECOND: date = date.plusSeconds(duration.getValue() * 1000); break;
			}//@on
		}
		
		return date;
	}
	
	/**
	 * Creates a {@link DurationGroup} builder
	 *
	 * @return {@link DurationGroup} builder
	 */
	@NotNull
	static Builder builder() {return new Builder();}
	
	/**
	 * Creates a {@link DurationGroup} builder
	 *
	 * @param object {@link DurationGroup}
	 * @return {@link DurationGroup} builder
	 */
	@NotNull
	static Builder builder(@NotNull DurationGroup object) {return new Builder(object);}
	
	static void main(String[] args) {
		
		@NotNull DurationGroup dg = DurationGroup.builder()
				.year(1)
				.month(2)
				.day(3)
				.hour(4)
				.minute(5)
				.second(6)
				.millisecond(7)
				.build();
		
		//System.out.println(dg.toString("%1$d years %2$d months %3$d days %4$d hours %5$d minutes"));
		
	}
	
	/**
	 * Creates a {@link DurationGroup}
	 *
	 * @param durations {@link Duration} list
	 * @return {@link DurationGroup}
	 */
	@NotNull
	static DurationGroup of(@NotNull List<Duration> durations) {
		
		return new DurationGroupImp(durations);
	}
	
	/**
	 * Creates a {@link DurationGroup}
	 *
	 * @param duration {@link Duration}
	 * @return {@link DurationGroup}
	 */
	@NotNull
	static DurationGroup of(@NotNull Duration duration) {
		
		return new DurationGroupImp(List.of(duration));
	}
	
	@NotNull
	static DurationGroup random() {
		
		return DurationGroup.builder()
				.year(Randoom.getLong(10))
				.month(Randoom.getLong(10))
				.day(Randoom.getLong(10))
				.hour(Randoom.getLong(10))
				.minute(Randoom.getLong(10))
				.second(Randoom.getLong(10))
				.millisecond(Randoom.getLong(10))
				.build();
	}
	
	/**
	 * Builder class for {@link DurationGroup}.
	 * Can be used to construct a {@link DurationGroup} unit by unit.
	 */
	@SuppressWarnings("UnusedReturnValue")
	final class Builder {
		
		final LinkedList<Duration> durations = new LinkedList<>();
		Duration year        = Duration.ofYear(0);
		Duration month       = Duration.ofMonth(0);
		Duration day         = Duration.ofDay(0);
		Duration hour        = Duration.ofHour(0);
		Duration minute      = Duration.ofMinute(0);
		Duration second      = Duration.ofSecond(0);
		Duration millisecond = Duration.ofMillisecond(0);
		
		private Builder() {// Use a static builder() method
		}
		
		private Builder(@NotNull DurationGroup object) {// Use a static builder() method
			year        = object.getYear();
			month       = object.getMonth();
			day         = object.getDay();
			hour        = object.getHour();
			minute      = object.getMinute();
			second      = object.getSecond();
			millisecond = object.getMillisecond();
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
		
		public Builder year(@NotNull Duration year) {
			
			this.year = year;
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
		
		public Builder month(@NotNull Duration month) {
			
			this.month = month;
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
		
		public Builder day(@NotNull Duration day) {
			
			this.day = day;
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
		
		public Builder hour(@NotNull Duration hour) {
			
			this.hour = hour;
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
		 * Sets the minute
		 *
		 * @param minute minute
		 * @return {@link Builder} object for chaining
		 */
		public Builder minute(@NotNull Duration minute) {
			
			this.minute = minute;
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
		 * Sets the second
		 *
		 * @param second second
		 * @return {@link Builder} object for chaining
		 */
		public Builder second(@NotNull Duration second) {
			
			this.second = second;
			return this;
		}
		
		/**
		 * Sets the millisecond
		 *
		 * @param mlSecond millisecond
		 * @return {@link Builder} object for chaining
		 */
		public Builder millisecond(long mlSecond) {
			
			this.millisecond = Duration.ofMillisecond(mlSecond);
			return this;
		}
		
		/**
		 * Sets the millisecond
		 *
		 * @param mlSecond millisecond
		 * @return {@link Builder} object for chaining
		 */
		public Builder millisecond(@NotNull Duration mlSecond) {
			
			this.millisecond = mlSecond;
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
			return new DurationGroupImp(this);
		}
		
		private void setDurationList() {
			
			//LinkedList<Duration> durations = new LinkedList<>();
			durations.add(year);
			durations.add(month);
			durations.add(day);
			durations.add(hour);
			durations.add(minute);
			durations.add(second);
			durations.add(millisecond);
		}
	}
	
	/**
	 * String convertor for {@link DurationGroup}.
	 */
	final class Stringer {
		
		private final List<Unit>                 units     = new ArrayList<>();
		private final LinkedList<Duration>       durations = new LinkedList<>();
		private       boolean                    zeros;
		private       int                        maxItemSize;
		private       String                     formattedString;
		private       Function<Duration, String> formatter;
		
		/**
		 * Sets the formatter.
		 *
		 * @param formatter formatter to format each duration.
		 * @return this {@link Stringer}
		 */
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
