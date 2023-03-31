package com.tr.hsyn.telefonrehberi.main.activity.call.backup.data;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;


/**
 * <h3>CallBackup</h3>
 * <p>
 * Bu sınıf, yedeklenen bir arama kaydı listesi için bilgiler taşır.
 *
 * @author hsyn 15 Mart 2022 Salı 21:34:48
 */
public class CallBackup implements Backup<Call> {
	
	private final String name;
	private final long   date;
	private final int    size;
	
	public CallBackup(String name, long date, int size) {
		
		this.name = name;
		this.date = date;
		this.size = size;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	@Override
	public long getDate() {
		
		return date;
	}
	
	@Override
	public int getSize() {
		
		return size;
	}
}
