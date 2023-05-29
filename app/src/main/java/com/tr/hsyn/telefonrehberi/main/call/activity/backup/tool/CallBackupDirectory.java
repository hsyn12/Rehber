package com.tr.hsyn.telefonrehberi.main.call.activity.backup.tool;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.telefonrehberi.dev.registery.Register;
import com.tr.hsyn.telefonrehberi.main.call.activity.backup.data.CallBackup;
import com.tr.hsyn.telefonrehberi.main.dev.backup.Backup;
import com.tr.hsyn.telefonrehberi.main.dev.backup.BackupDirectory;
import com.tr.hsyn.xbox.Blue;

import java.util.ArrayList;
import java.util.List;


/**
 * <h3><u>CallBackupDirectory</u></h3>
 * <p>
 * Bu sınıf, arama kayıtları yedeklerinin tutulduğu dizini yönetir.
 * Kayıtları yedekleme ve yedekleri geri alma hizmeti sunar.<br><br>
 *
 * @author hsyn 15 Mart 2022 Salı 21:06:10
 */
public class CallBackupDirectory implements BackupDirectory<Call> {
	
	/**
	 * Dizin
	 */
	private final Register book;
	
	public CallBackupDirectory(Register register) {
		
		this.book = register;
	}
	
	/**
	 * Sistemdeki tüm arama kayıtlarını yedekler.<br><br>
	 *
	 * @return Bu çağrı ile yedeklenen kayıtlar için {@linkplain CallBackup} nesnesi.
	 * 		Eğer kayıtlara erişim sağlanamazsa {@code null}.
	 * @see CallBackup
	 */
	@Override
	@Nullable
	@WorkerThread
	public final Backup<Call> newBackup() {
		
		List<Call> calls = Blue.getObject(Key.CALL_LOG);
		
		if (calls != null)
			return newBackup(calls);
		
		return null;
	}
	
	/**
	 * Verilen arama kayıtlarını yedekler.<br><br>
	 *
	 * @param calls Arama kayıtları listesi
	 * @return Bu çağrı ile yedeklenen kayıtlar için {@linkplain CallBackup} nesnesi
	 * @see CallBackup
	 */
	@Override
	@NonNull
	@WorkerThread
	public final Backup<Call> newBackup(@NonNull final List<Call> calls) {
		
		long   time = System.currentTimeMillis();
		String name = "cb" + time;
		
		//Register.on(Over.App.getContext(), "ppp").putObjectsList("o", calls).apply();
		book.write(name, calls);
		
		return new CallBackup(name, time, calls.size());
	}
	
	/**
	 * Verilen isimle kaydedilmiş yedeği siler.<br><br>
	 *
	 * @param name Yedeğin ismi
	 */
	@Override
	@WorkerThread
	public final void deleteBackup(@NonNull final String name) {
		
		book.delete(name);
	}
	
	@Override
	public final void override(@NonNull final String name, List<Call> calls) {
		
		book.write(name, calls);
	}
	
	/**
	 * @return Kayıtlı tüm yedeklerin isimleri
	 */
	@NonNull
	@WorkerThread
	private List<String> _getBackups() {
		
		return book.getKeys();
	}
	
	/**
	 * @return Kayıtlı tüm yedeklerin listesi
	 */
	@Override
	@NonNull
	@WorkerThread
	public final List<Backup<Call>> getBackups() {
		
		var                backups     = _getBackups();
		List<Backup<Call>> callBackups = new ArrayList<>(backups.size());
		
		for (String backup : backups) {
			
			List<Call> calls = getItems(backup);
			
			if (calls == null) continue;
			
			var bDate = Long.parseLong(backup.substring(2));
			
			Backup<Call> callBackup = new CallBackup(backup, bDate, calls.size());
			
			callBackups.add(callBackup);
		}
		
		return callBackups;
	}
	
	/**
	 * Verilen isme ait yedeğin arama kayıtlarını döndürür.<br><br>
	 *
	 * @param name Yedeğin ismi
	 * @return Arama kaydı listesi
	 */
	@WorkerThread
	@Override
	public List<Call> getItems(String name) {
		
		return book.readList(name, Call.class, new ArrayList<>(0));
	}
	
}
