package com.tr.hsyn.colors;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.tr.hsyn.db.DBBase;
import com.tr.hsyn.db.cast.DB;
import com.tr.hsyn.db.cast.DBColumn;
import com.tr.hsyn.db.column.Number;
import com.tr.hsyn.db.column.Text;
import com.tr.hsyn.life.Life;

import java.util.List;
import java.util.stream.Collectors;


@SuppressLint("Range")
public class Colorabi extends DBBase<ColorRegister> implements ColorRegisters {
	
	/**
	 * Rengin ismi
	 */
	private static final String NAME            = "name";
	/**
	 * Rengin değeri
	 */
	private static final String COLOR           = "color";
	/**
	 * Rengin seçilme zamanı
	 */
	private static final String TIME_SELECTED   = "time_selected";
	/**
	 * Rengin kullanımının bittiği zaman
	 */
	private static final String TIME_UNSELECTED = "time_unselected";
	
	public Colorabi(@NonNull Context context) {
		
		super(context, new ColorDBInterface());
	}
	
	@Override
	protected ContentValues contentValuesOf(@NonNull ColorRegister colorData) {
		
		var value = new ContentValues();
		
		value.put(NAME, colorData.getName());
		value.put(COLOR, colorData.getColor());
		value.put(TIME_SELECTED, colorData.getLifeTime().getStartTime());
		value.put(TIME_UNSELECTED, colorData.getLifeTime().getEndTime());
		
		return value;
	}
	
	@NonNull
	@Override
	protected ColorRegister createObject(@NonNull Cursor cursor) {
		
		String name           = cursor.getString(cursor.getColumnIndex(NAME));
		int    color          = cursor.getInt(cursor.getColumnIndex(COLOR));
		long   timeSelected   = cursor.getLong(cursor.getColumnIndex(TIME_SELECTED));
		long   timeUnselected = cursor.getLong(cursor.getColumnIndex(TIME_UNSELECTED));
		
		return ColorRegister.newRegister(name, color, Life.newLife(COLOR, timeSelected, timeUnselected));
	}
	
	@Override
	public boolean update(@NonNull ColorRegister item) {
		
		return update(item, item.getLifeTime().getStartTime());
	}
	
	@Override
	public int update(@NonNull List<? extends ColorRegister> items) {
		
		return (int) items.stream().filter(this::update).count();
	}
	
	@Override
	public boolean delete(@NonNull ColorRegister item) {
		
		return deleteByPrimaryKey(String.valueOf(item.getLifeTime().getStartTime()));
	}
	
	@Override
	public int delete(List<? extends ColorRegister> items) {
		
		List<String> keys = items.stream().map(c -> String.valueOf(c.getLifeTime().getStartTime())).collect(Collectors.toList());
		return delete(DBBase.createSelection(TIME_SELECTED, keys));
	}
	
	/**
	 * Veri tabanı bilgileri
	 */
	public static final class ColorDBInterface implements DB {
		
		@NonNull
		@Override
		public String getTableName() {
			
			return "xyz_Color";
		}
		
		@NonNull
		@Override
		public String getDatabaseName() {
			
			return "xyz_Colors";
		}
		
		@NonNull
		@Override
		public DBColumn[] getColumns() {
			
			//- Veri tabanı bilgileri
			//- 4 bilgi kaydedilecek
			//- Seçilme zamanları önemli
			return new DBColumn[]{
					new Text(NAME).notNull(),
					new Number(COLOR),
					new Number(TIME_SELECTED).primaryKey(),
					new Number(TIME_UNSELECTED).defaultValue(0L)
			};
		}
		
		@NonNull
		@Override
		public String getPrimaryKey() {
			//- Hiçbir seçim aynı anda gerçekleşemeyeceği için güvenli
			return TIME_SELECTED;
		}
	}
}
