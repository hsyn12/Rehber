package com.tr.hsyn.telefonrehberi.main.code.call.act;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.calldata.CallType;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.string.Stringx;
import com.tr.hsyn.telefonrehberi.R;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.CallKey;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public interface Calls {
	
	/**
	 * Ektra bilgilerin tutulacağı yer ({@link CallLog.Calls#PHONE_ACCOUNT_ID}).<br>
	 * Ekstra bilgiler bu string ile başlar.
	 */
	String   ACCOUNT_ID = "xyz";
	/**
	 * Sistem veri tabanından arama alınacak bilgilerin kolonları.
	 */
	String[] COLUMNS    = {
			
			CallLog.Calls.CACHED_NAME,
			CallLog.Calls.NUMBER,
			CallLog.Calls.DATE,
			CallLog.Calls.TYPE,
			CallLog.Calls.DURATION,
			CallLog.Calls.PHONE_ACCOUNT_ID
	};
	
	/**
	 * Tüm arama kayıtlarını döndürür.
	 *
	 * @param resolver {@link ContentResolver}
	 * @return Arama kayıtları listesi
	 */
	@NonNull
	static List<Call> getCalls(@NonNull ContentResolver resolver) {
		
		var cursor = resolver.query(
				CallLog.Calls.CONTENT_URI,
				COLUMNS,
				null,
				null,
				null
		);
		
		if (cursor != null) {
			
			int nameCol     = cursor.getColumnIndex(COLUMNS[0]);
			int numberCol   = cursor.getColumnIndex(COLUMNS[1]);
			int dateCol     = cursor.getColumnIndex(COLUMNS[2]);
			int typeCol     = cursor.getColumnIndex(COLUMNS[3]);
			int durationCol = cursor.getColumnIndex(COLUMNS[4]);
			int extraCol    = cursor.getColumnIndex(COLUMNS[5]);
			
			List<Call> calls = new ArrayList<>(cursor.getCount());
			
			while (cursor.moveToNext()) {
				
				//String extra = cursor.getString(extraCol);
				
				var c = new Call(
						cursor.getString(nameCol),
						cursor.getString(numberCol),
						cursor.getInt(typeCol),
						cursor.getLong(dateCol),
						cursor.getInt(durationCol),
						cursor.getString(extraCol)
				);
				
				//xlog.d("%s %s - Extra : %s", c.getName(), c.getNumber(), extra);
				calls.add(c);
			}
			
			cursor.close();
			return calls;
		}
		
		return new ArrayList<>(0);
	}
	
	/**
	 * Sistemin arama kayıtlarına ekleme yapar.
	 *
	 * @param contentResolver Veri tabanı giriş bileti
	 * @param phoneCall       Eklenecek arama kaydı
	 * @return Ekleme başarılı ise {@code Uri}, değilse {@code null}
	 */
	static Uri add(@NonNull ContentResolver contentResolver, @NonNull Call phoneCall) {
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMNS[0], phoneCall.getName());
		values.put(COLUMNS[1], phoneCall.getNumber());
		values.put(COLUMNS[2], phoneCall.getTime());
		values.put(COLUMNS[3], phoneCall.getCallType());
		values.put(COLUMNS[4], phoneCall.getDuration());
		values.put(COLUMNS[5], createExtraInfo(phoneCall));
		values.put(CallLog.Calls.IS_READ, 1);
		
		return contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
	}
	
	/**
	 * Arama kaydı için extra bilgileri döndürür.
	 *
	 * @param call Arama kaydı
	 * @return Ekstra
	 */
	@NonNull
	@SuppressLint("DefaultLocale")
	static String createExtraInfo(@NonNull Call call) {
		
		//! true için 't', false için 'f' kullanılacak
		//! Bilgileri ayıran karakter ise ';' işareti
		
		//- İlk bilgi ACCOUNT_ID
		//- Sonra random t/f
		//- track type int
		
		return String.format("%s;%s;%d", ACCOUNT_ID, call.getBool(CallKey.RANDOM) ? "t" : "f", call.getInt(CallKey.TRACK_TYPE, 0));
	}
	
	/**
	 * Verilen listeyi sistem kayıtlarına ekler.
	 *
	 * @param contentResolver ContentResolver
	 * @param calls           calls
	 * @return Başarılı bir şekilde eklenen kayıt sayısı
	 */
	static int add(@NonNull ContentResolver contentResolver, @NonNull List<? extends Call> calls) {
		
		ContentValues[] contentValues = new ContentValues[calls.size()];
		
		int i = 0;
		for (Call phoneCall : calls) {
			
			ContentValues values = new ContentValues();
			
			values.put(COLUMNS[0], phoneCall.getName());
			values.put(COLUMNS[1], phoneCall.getNumber());
			values.put(COLUMNS[2], phoneCall.getTime());
			values.put(COLUMNS[3], phoneCall.getCallType());
			values.put(COLUMNS[4], phoneCall.getDuration());
			values.put(COLUMNS[5], createExtraInfo(phoneCall));
			values.put(CallLog.Calls.IS_READ, 1);
			
			contentValues[i++] = values;
			
		}
		
		return contentResolver.bulkInsert(CallLog.Calls.CONTENT_URI, contentValues);
	}
	
	/**
	 * Android sistemindeki kaydı siler.
	 *
	 * @param contentResolver cr
	 * @param date            Silinecek olan kaydın milisaniye tarihi
	 * @return Silinirse {@code true}, böyle bir kayıt yoksa ya da herhangi bir sebeple silinemezse {@code false}.
	 */
	static boolean delete(@NonNull final ContentResolver contentResolver, final long date) {
		
		int r = contentResolver.delete(
				CallLog.Calls.CONTENT_URI,
				COLUMNS[2] + "=?",
				new String[]{String.valueOf(date)});
		
		return r > 0;
	}
	
	static int delete(@NonNull final ContentResolver contentResolver, @NonNull List<? extends Call> calls) {
		
		var    dates       = calls.stream().map(call -> String.valueOf(call.getTime())).collect(Collectors.toList());
		String selectionIn = Stringx.joinToString(dates);
		
		return contentResolver.delete(
				CallLog.Calls.CONTENT_URI,
				COLUMNS[2] + " in(" + selectionIn + ")",
				null
		);
	}
	
	static boolean deleteAll(@NonNull final ContentResolver contentResolver) {
		
		long date = System.currentTimeMillis();
		
		return contentResolver.delete(
				CallLog.Calls.CONTENT_URI,
				COLUMNS[2] + "<?",
				new String[]{String.valueOf(date)}) > 0;
	}
	
	/**
	 * Sistemin arama kayıtlarındaki bir aramayı günceller.
	 * Güncellenecek olan kayıt, verilen arama kaydının tarihi ile aynı olan kayıt olacak.
	 * Eğer o tarihle eşleşen bir kayıt yoksa güncelleme başarısız olur ve {@code false} döner.
	 *
	 * @param contentResolver veri tabanı giriş bileti
	 * @param phoneCall       güncel değerleri taşıyan arama kaydı
	 * @return güncelleme yapılır ise {@code true}
	 */
	static boolean update(@NonNull ContentResolver contentResolver, @NonNull Call phoneCall) {
		
		ContentValues values = new ContentValues();
		
		values.put(COLUMNS[0], phoneCall.getName());
		values.put(COLUMNS[1], phoneCall.getNumber());
		values.put(COLUMNS[2], phoneCall.getTime());
		values.put(COLUMNS[3], phoneCall.getCallType());
		values.put(COLUMNS[4], phoneCall.getDuration());
		values.put(COLUMNS[5], createExtraInfo(phoneCall));
		
		return contentResolver.update(CallLog.Calls.CONTENT_URI, values, COLUMNS[2] + "=?", new String[]{String.valueOf(phoneCall.getTime())}) > 0;
	}
	
	/**
	 * Verilen numaranın rehberde kayıtlı ismini döndür.
	 *
	 * @param contentResolver contentResolver
	 * @param number          Telefon numarası
	 * @return İsim. Kişi bulunamazsa {@code null}
	 */
	@SuppressLint("Range")
	@Nullable
	static String getContactName(@NonNull ContentResolver contentResolver, @NonNull String number) {
		
		if (number.isEmpty()) {
			
			xlog.d("number empty");
			return null;
		}
		
		String[] cols = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY, ContactsContract.CommonDataKinds.Phone.NUMBER};
		
		final Cursor cursor = contentResolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				cols,
				null,
				null,
				null
		
		);
		
		if (cursor == null) return null;
		
		if (cursor.getCount() == 0) {
			
			cursor.close();
			return null;
		}
		
		String    name      = null;
		final int numberCol = cursor.getColumnIndex(cols[1]);
		
		while (cursor.moveToNext()) {
			
			String _number = cursor.getString(numberCol);
			
			if (PhoneNumbers.equals(_number, number)) {
				
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
				break;
			}
		}
		
		cursor.close();
		return name;
	}
	
	static int getCallTypeIcon(int type) {
		
		switch (type) {
			
			case CallType.INCOMING:
			case CallType.INCOMING_WIFI:
				return com.tr.hsyn.resdrawcalltype.R.drawable.incoming_call;
			case CallType.OUTGOING:
			case CallType.OUTGOING_WIFI:
				return com.tr.hsyn.resdrawcalltype.R.drawable.outgoing_call;
			case CallType.MISSED:
				return com.tr.hsyn.resdrawcalltype.R.drawable.missed_call;
			case CallType.REJECTED:
				return com.tr.hsyn.resdrawcalltype.R.drawable.rejected_call;
			case CallType.BLOCKED:
				return com.tr.hsyn.resdrawcalltype.R.drawable.blocked_call;
            /*case CallType.GET_REJECTED:
                return com.tr.hsyn.resdrawcalltype.R.drawable.get_rejected_call;
            case CallType.UNREACHED:
                return com.tr.hsyn.resdrawcalltype.R.drawable.un_reached_call;
            case CallType.UNRECEIVED:
                return com.tr.hsyn.resdrawcalltype.R.drawable.un_recieved_call;*/
		}
		
		return R.drawable.ring;
	}
	
	static void updateExtra(@NotNull List<Call> calls) {
		
		for (var call : calls) {
			
			
		}
		
	}
	
}
