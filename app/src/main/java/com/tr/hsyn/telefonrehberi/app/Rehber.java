package com.tr.hsyn.telefonrehberi.app;


import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.tr.hsyn.buildkeys.BuildKeys;
import com.tr.hsyn.colors.Colors;
import com.tr.hsyn.daytimes.DayTime;
import com.tr.hsyn.executors.MainExecutor;
import com.tr.hsyn.keep.Keep;
import com.tr.hsyn.message.Show;
import com.tr.hsyn.xbox.Blue;
import com.tr.hsyn.xlog.xlog;

import io.paperdb.Paper;


@Keep
public class Rehber extends Application {
	
	private final Handler handler = new Handler(Looper.getMainLooper());
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		xlog.addHandler(new LoggerHandler());
		Paper.init(this);
		setExecutor();
		
		//- Bulutların üstünde
		Blue.box(BuildKeys.CONTEXT, getApplicationContext());
		
		//- Have a nice day
		xlog.w(DayTime.toString(this));
		
		
		com.tr.hsyn.colors.Rehber.Color.init(this);
		Show.setPrimaryColor(Colors.getPrimaryColor());
	}
	
	private void setExecutor() {
		
		MainExecutor.HOLDER.setMainExecutor(new MainExecutor() {
			
			@Override
			public void cancel(Runnable command) {
				
				handler.removeCallbacks(command);
			}
			
			@Override
			public void execute(Runnable command, long delay) {
				
				handler.postDelayed(command, delay);
			}
			
			@Override
			public void execute(Runnable command) {
				
				handler.post(command);
			}
		});
	}
	
}
