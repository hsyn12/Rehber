package com.tr.hsyn.calldata;


import com.tr.hsyn.datboxer.DatBoxer;
import com.tr.hsyn.identity.Identity;

import java.io.Serializable;
import java.util.Objects;


/**
 * Class for call data
 */
public class Call extends DatBoxer implements CallContact, CallTime, CallDuration, CallType, Serializable, Identity {
	
	private final String number;
	private final int    callType;
	private final long   time;
	private final int    duration;
	//- Sistemden alınacak bilgiler
	private       String name;
	/**
	 * Extra info
	 */
	private       String extra;//- random ve trackType burada olacak
	//-------------------------------
	
	public Call(String name, String number, int callType, long time, int duration) {
		
		this.name     = name;
		this.number   = number;
		this.time     = time;
		this.callType = callType;
		this.duration = duration;
	}
	
	public Call(String name, String number, int callType, long time, int duration, String extra) {
		
		this.name     = name;
		this.number   = number;
		this.time     = time;
		this.callType = callType;
		this.duration = duration;
		this.extra    = extra;
	}
	
	public String getExtra() {
		
		return extra;
	}
	
	public void setExtra(String extra) {
		
		this.extra = extra;
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
	public int getDuration() {
		
		return duration;
	}
	
	@Override
	public long getTime() {
		
		return time;
	}
	
	@Override
	public int getCallType() {
		
		return callType;
	}
	
	@Override
	public long getId() {
		
		return time;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(time);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		return obj instanceof Call && time == ((Call) obj).time;
	}
	
	@Override
	public String toString() {
		
		return "Call{" +
		       "number='" + number + '\'' +
		       ", type=" + callType +
		       ", time=" + time +
		       ", duration=" + duration +
		       ", name='" + name + '\'' +
		       ", extra='" + extra + '\'' +
		       '}';
	}
}