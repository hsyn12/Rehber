package com.tr.hsyn.time.duration;


import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.time.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class DurationGroupImp implements DurationGroup {
	
	
	private final Duration       year;
	private final Duration       month;
	private final Duration       day;
	private final Duration       hour;
	private final Duration       minute;
	private final Duration       second;
	private final Duration       millisecond;
	private final List<Duration> durations;
	
	DurationGroupImp(@NotNull List<Duration> durations) {
		
		//durations.sort(Comparator.comparing(Duration::getUnit));
		this.durations = new LinkedList<>();
		year           = getDuration(Unit.YEAR, durations);
		month          = getDuration(Unit.MONTH, durations);
		day            = getDuration(Unit.DAY, durations);
		hour           = getDuration(Unit.HOUR, durations);
		minute         = getDuration(Unit.MINUTE, durations);
		second         = getDuration(Unit.SECOND, durations);
		millisecond    = getDuration(Unit.MILLISECOND, durations);
		
		this.durations.add(year);
		this.durations.add(month);
		this.durations.add(day);
		this.durations.add(hour);
		this.durations.add(minute);
		this.durations.add(second);
		this.durations.add(millisecond);
	}
	
	/**
	 * Creates a new {@link DurationGroup}
	 *
	 * @param builder {@link Builder}
	 */
	DurationGroupImp(@NotNull Builder builder) {
		
		year        = builder.year;
		month       = builder.month;
		day         = builder.day;
		hour        = builder.hour;
		minute      = builder.minute;
		second      = builder.second;
		millisecond = builder.millisecond;
		durations   = builder.durations;
	}
	
	public DurationGroupImp(DurationGroup other) {
		
		this.year        = other.getYear();
		this.month       = other.getMonth();
		this.day         = other.getDay();
		this.hour        = other.getHour();
		this.minute      = other.getMinute();
		this.second      = other.getSecond();
		this.millisecond = other.getMillisecond();
		
		this.durations = other.getDurations();
	}
	
	@Override
	@NotNull
	public Duration getDuration(@NotNull Unit unit) {
		
		return durations.get(unit.ordinal());
	}
	
	@Override
	public @NotNull Duration getYear() {
		
		return year;
	}
	
	@Override
	public @NotNull Duration getMonth() {
		
		return month;
	}
	
	@Override
	public @NotNull Duration getDay() {
		
		return day;
	}
	
	@Override
	public @NotNull Duration getHour() {
		
		return hour;
	}
	
	@Override
	public @NotNull Duration getMinute() {
		
		return minute;
	}
	
	@Override
	public @NotNull Duration getSecond() {
		
		return second;
	}
	
	@Override
	public @NotNull Duration getMillisecond() {
		
		return millisecond;
	}
	
	public @NotNull List<Duration> getDurations() {
		
		return Lister.listOf(durations);
	}
	
	/**
	 * Returns a string representation of this {@link DurationGroup}.<br>
	 *
	 * @return a string representation
	 */
	@SuppressWarnings("DefaultLocale")
	@Override
	public String toString() {
		
		return String.format("Y%dM%dD%dH%dM%dS%dM%d", year.getValue(), month.getValue(), day.getValue(), hour.getValue(), minute.getValue(), second.getValue(), millisecond.getValue());
	}
	
	
	/**
	 * Returns a string representation of this {@link DurationGroup}.<br>
	 * Formatted string is: <br>
	 * <code>Y{year}M{month}D{day}H{hour}M{minute}S{second}M{millisecond}</code> respectively.<br>
	 * The order starts from <code>1</code>
	 * and increments by <code>1</code> until it reaches <code>7</code>.<br>
	 * And can write like this {@code month} and {@code day} <br>
	 * {@code '%2$d months %3$d days'} as formatted string.<br><br>
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
	 * 	System.out.println(dg.toString("%2$d months %3$d days")); // 2 month 3 day
	 * </pre>
	 *
	 * @param formatted formatted string
	 * @return a string representation
	 */
	public String toString(String formatted) {
		
		return String.format(formatted, year.getValue(), month.getValue(), day.getValue(), hour.getValue(), minute.getValue(), second.getValue());
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
		
		int comp = Long.compare(year.getValue(), durationGroup.getYear().getValue());
		
		if (comp == 0L) comp = Long.compare(month.getValue(), durationGroup.getMonth().getValue());
		if (comp == 0L) comp = Long.compare(day.getValue(), durationGroup.getDay().getValue());
		if (comp == 0L) comp = Long.compare(hour.getValue(), durationGroup.getHour().getValue());
		if (comp == 0L) comp = Long.compare(minute.getValue(), durationGroup.getMinute().getValue());
		if (comp == 0L) comp = Long.compare(second.getValue(), durationGroup.getSecond().getValue());
		if (comp == 0L) comp = Long.compare(millisecond.getValue(), durationGroup.getMillisecond().getValue());
		
		return comp;
	}
	
	@NotNull
	@Override
	public Iterator<Duration> iterator() {
		
		return durations.iterator();
	}
	
}
