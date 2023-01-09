package com.tr.hsyn.calldata;


import com.tr.hsyn.label.Label;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;


public class BCall implements Call, Serializable {
	
	private final String     number;
	private final int        type;
	private final long       time;
	private final int        duration;
	private final long       ringingDuration;
	private       Set<Label> labels;
	private       String     note;
	private       String     name;
	private       String     extra;
	private       long       contactId;
	private       long       deletedDate;
	private       boolean    isRandom;
	private       int        trackType;
	
	public BCall(String name, String number, int type, long time, int duration, String extra) {
		
		this(name, number, type, time, duration, extra, 0L, 0L, null, 0L);
		
	}
	
	public BCall(String name, String number, int type, long time, int duration, String extra, long contactId, long deletedDate, String note, long ringingDuration) {
		
		this.name            = name;
		this.number          = number;
		this.type            = type;
		this.time            = time;
		this.duration        = duration;
		this.extra           = extra;
		this.contactId       = contactId;
		this.note            = note;
		this.ringingDuration = ringingDuration;
		this.deletedDate     = deletedDate;
		setExtra(extra);
	}
	
	@Override
	public long getContactId() {
		
		return contactId;
	}
	
	@Override
	public void setContactId(long id) {
		
		contactId = id;
	}
	
	@Override
	public String getNote() {
		
		return note;
	}
	
	@Override
	public void setNote(String note) {
		
		this.note = note;
	}
	
	@Override
	public long getDeletedDate() {
		
		return deletedDate;
	}
	
	@Override
	public void setDeletedDate(long date) {
		
		this.deletedDate = date;
	}
	
	@Override
	public long getRingingDuration() {
		
		return ringingDuration;
	}
	
	@Override
	public @NotNull
	Call withDeletedDate(long date) {
		
		return new BCall(name, number, type, time, duration, extra, contactId, deletedDate, note, ringingDuration);
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public void setName(String name) {
		
		this.name = name;
	}
	
	@Override
	public String getNumber() {
		
		return number;
	}
	
	@Override
	public boolean isRandom() {
		
		return isRandom;
	}
	
	@Override
	public int getTrackType() {
		
		return trackType;
	}
	
	@Override
	public String getExtra() {
		
		return extra;
	}
	
	@Override
	public void setExtra(String extra) {
		
		this.extra = extra;
		
		if (extra == null) {
			
			isRandom  = false;
			trackType = 0;
		}
		else if (extra.startsWith("xyz")) {
			
			try {
				
				isRandom  = extra.split("\\|\\|")[1].equals("t");
				trackType = Integer.parseInt(extra.split("\\|\\|")[2]);
			}
			catch (Exception e) {
				
				isRandom  = extra.split(";")[1].equals("t");
				trackType = Integer.parseInt(extra.split(";")[2]);
			}
			
		}
	}
	
	@Override
	public int getDuration() {
		
		return duration;
	}
	
	@Override
	public long getTime() {
		
		return time;
	}
	
	@Override
	public int getType() {
		
		return type;
	}
	
	@Override
	public boolean equals(final long contactId) {
		
		return contactId == this.contactId;
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Call && time == ((Call) o).getTime();
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(time);
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "Call{" +
		       "name='" + name + '\'' +
		       ", number='" + number + '\'' +
		       ", type=" + type +
		       ", time=" + time +
		       ", duration=" + duration +
		       ", extra='" + extra + '\'' +
		       ", contactId=" + contactId +
		       ", note='" + note + '\'' +
		       ", deletedDate=" + deletedDate +
		       ", ringingDuration=" + ringingDuration +
		       ", isRandom=" + isRandom +
		       ", trackType=" + trackType +
		       '}';
	}
	
	@Override
	public @NotNull
	Set<Label> getLabels() {
		
		return labels;
	}
	
	@Override
	public void setLabels(@Nullable Set<Label> labels) {
		
		this.labels = labels;
	}
	
	@Override
	public long getId() {
		
		return getTime();
	}
}
