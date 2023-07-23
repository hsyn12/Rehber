package com.tr.hsyn.calldata;


import com.tr.hsyn.datboxer.DatBoxer;
import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.tryme.Try;

import java.io.Serializable;
import java.util.Objects;


/**
 * Class for call data
 */
public class Call extends DatBoxer implements CallContact, CallTime, CallDuration, CallType, Serializable, Identity {
	
	//- Sistemden alınacak bilgiler
	private final String number;
	private final long   time;
	private final int    callType;
	private final int    duration;
	private       String name;
	/**
	 * Extra info
	 */
	private       String extra;
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
	
	public boolean isRandom() {
		
		var extra = getExtra();
		
		if (extra != null) {
			
			var info = extra.split(";");
			return Boolean.TRUE.equals(Try.ignore(() -> info[1].equals("t")));
		}
		
		return false;
	}
	
	public String getExtra() {
		
		return extra;
	}
	
	public void setExtra(String extra) {
		
		this.extra = extra;
	}
	
	public boolean isNoNamed() {
		
		return name == null || name.isEmpty();
	}
	
	public boolean isSpeaking() {
		
		return getDuration() > 0;
	}
}