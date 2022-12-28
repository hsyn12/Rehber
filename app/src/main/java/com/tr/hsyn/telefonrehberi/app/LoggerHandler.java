package com.tr.hsyn.telefonrehberi.app;


import android.util.Log;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;


public class LoggerHandler extends Handler {
	
	@Override
	public void publish(LogRecord record) {
		
		if (!super.isLoggable(record)) return;
		
		int    level = getAndroidLevel(record.getLevel());
		String tag   = record.getLoggerName();
		
		Log.println(level, tag, record.getMessage());
		
		if (record.getThrown() != null)
			Log.println(level, tag, Log.getStackTraceString(record.getThrown()));
		
	}
	
	static int getAndroidLevel(Level level) {
		
		int value = level.intValue();
		
		if (value >= Level.SEVERE.intValue()) {
			return Log.ERROR;
		}
		else if (value >= Level.WARNING.intValue()) {
			return Log.WARN;
		}
		else if (value >= Level.INFO.intValue()) {
			return Log.INFO;
		}
		else {
			return Log.DEBUG;
		}
	}
	
	@Override
	public void flush() {
		
	}
	
	@Override
	public void close() throws SecurityException {
		
	}
}
