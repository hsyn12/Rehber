package com.tr.hsyn.life;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.db.cast.DB;
import com.tr.hsyn.db.cast.DBColumn;
import com.tr.hsyn.db.column.Number;
import com.tr.hsyn.db.column.Text;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Yaşam sürelerini kaydeder.
 */
public class LifeDatabase extends DBBase<Life> implements LifeRecorder {
	
	private static final DB     dbInterface = new DBInterface();
	private final        String NAME        = "name";
	private final        String START       = "start";
	private final        String END         = "end";
	
	public LifeDatabase(@NotNull Context context) {
		
		super(context, dbInterface);
	}
	
	@Override
	@NotNull
	public ContentValues contentValuesOf(@NotNull Life life) {
		
		var values = new ContentValues();
		
		values.put(NAME, life.getName());
		values.put(START, life.getStartTime());
		values.put(END, life.getEndTime());
		
		return values;
	}
	
	@NotNull
	@Override
	@SuppressLint("Range")
	protected Life createObject(@NotNull Cursor cursor) {
		
		String name  = cursor.getString(cursor.getColumnIndex(NAME));
		long   start = cursor.getLong(cursor.getColumnIndex(START));
		long   end   = cursor.getLong(cursor.getColumnIndex(END));
		
		return Life.newLife(name, start, end);
	}
	
	/**
	 * Verilen nesnenin bitiş zamanını kaydeder.
	 *
	 * @param live Nesne
	 * @return İşlem başarılı ise {@code true}. Nesne zaten sonlanmış ise veya nesne bulunamazsa {@code false}
	 */
	@Override
	public boolean endLife(@NotNull Life live) {
		
		return update(live.endLife(), String.valueOf(live.getStartTime()));
	}
	
	/**
	 * Tüm yaşamları döndürür.
	 *
	 * @return Yaşam bilgileri
	 */
	@Override
	@NotNull
	public List<Life> getLifes() {
		
		return query(this::createObject, null, null, START + " DESC");
	}
	
	/**
	 * Verilen isme ait bilgileri döndürür.
	 *
	 * @param name Hayat sahibi
	 * @return Hayat bilgileri
	 */
	@Override
	@NotNull
	public List<Life> getLifes(@NotNull String name) {
		
		return query(this::createObject, NAME + " =?", new String[]{name}, START + " DESC");
	}
	
	/**
	 * Başlangıç zamanına göre nesneyi bul.
	 *
	 * @param start Başlangıç zamanı
	 * @return Nesne
	 */
	@Override
	@Nullable
	public Life findLife(long start) {
		
		return find(start);
	}
	
	/**
	 * Verilen nesnenin bilgilerini bul.
	 *
	 * @param aLife Aranacak nesne
	 * @return Nesnenin bilgileri
	 */
	@Override
	@Nullable
	public Life findLife(@NotNull Life aLife) {
		
		return find(START + " = " + aLife.getStartTime() + " and " + NAME + " = ?", new String[]{aLife.getName()}, this::createObject);
	}
	
	@Override
	public boolean update(@NotNull Life item) {
		
		return update(item, item.getStartTime());
	}
	
	@Override
	public int update(@NotNull List<? extends Life> items) {
		
		return (int) items.stream().filter(this::update).count();
	}
	
	@Override
	public boolean delete(Life item) {
		
		return deleteByPrimaryKey(Objects.requireNonNull(dbInterface.getPrimaryKey()));
	}
	
	@Override
	public int delete(@NotNull List<? extends Life> items) {
		
		var ids = items.stream().map(Life::getStartTime).map(String::valueOf).collect(Collectors.toList());
		
		return delete(DBBase.createSelection(Objects.requireNonNull(dbInterface.getPrimaryKey()), ids));
	}
}

/**
 * LifeTime kayıtları için veri tabanı sabitleri
 */
class DBInterface implements DB {
	
	@NotNull
	@Override
	public String getTableName() {
		
		return "Lives";
	}
	
	@NotNull
	@Override
	public String getDatabaseName() {
		
		return "LifeRecords";
	}
	
	@NotNull
	@Override
	public DBColumn[] getColumns() {
		
		return new DBColumn[]{
				
				new Text("name").notNull(),
				new Number("start").primaryKey(),
				new Number("end").defaultValue(0L)
		};
	}
	
	@NotNull
	@Override
	public String getPrimaryKey() {
		
		return "start";
	}
} 
