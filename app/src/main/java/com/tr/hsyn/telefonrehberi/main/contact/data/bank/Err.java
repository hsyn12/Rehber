package com.tr.hsyn.telefonrehberi.main.contact.data.bank;


import androidx.annotation.NonNull;

import com.tr.hsyn.roomstormanno.Column;
import com.tr.hsyn.roomstormanno.Default;
import com.tr.hsyn.roomstormanno.PrimaryKey;
import com.tr.hsyn.roomstormanno.Table;
import com.tr.hsyn.time.Time;

import java.util.Arrays;


@Table("Errors")
public class Err {
	
	@Column
	@PrimaryKey
	private final long   id = Time.now();
	@Column
	@Default("-")
	private       String message;
	@Column
	@Default("-")
	private       String cause;
	@Column
	@Default("-")
	private       String stackTrace;
	
	public Err() {}
	
	public Err(@NonNull Throwable throwable) {
		
		this.message    = throwable.getMessage();
		this.cause      = throwable.getCause() != null ? throwable.getCause().getMessage() : "-";
		this.stackTrace = Arrays.toString(throwable.getStackTrace());
	}
	
	public long getTime() {
		
		return id;
	}
	
	public String getMessage() {
		
		return message;
	}
	
	public String getCause() {
		
		return cause;
	}
	
	public String getStackTrace() {
		
		return stackTrace;
	}
}
