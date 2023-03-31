/*
 * Copyright 2015 Dimitry Ivanov (mail@dimitryivanov.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tr.hsyn.roomstorm.op;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.tr.hsyn.roomstorm.CachedTable;
import com.tr.hsyn.roomstorm.ContentValuesCreator;
import com.tr.hsyn.roomstorm.DatabaseManager;
import com.tr.hsyn.roomstorm.exc.StormException;

import java.util.List;


/**
 * Created by Dimitry Ivanov (mail@dimitryivanov.ru) on 06.02.2015.
 */
public class InsertImpl extends AbsSQLiteOp implements Insert {
	
	public InsertImpl(DatabaseManager manager) {
		
		super(manager);
	}
	
	@Override
	public long insert(Object value) throws StormException {
		
		return insert(value, false, false, 0);
	}
	
	@Override
	public long insert(Object value, int onConflict) throws StormException {
		
		return insert(value, false, false, 0);
	}
	
	@Override
	public long insert(Object value, boolean shouldNotify) throws StormException {
		
		return insert(value, shouldNotify, false, 0);
	}
	
	@Override
	public long insert(Object value, boolean shouldNotify, int onConflict) throws StormException {
		
		return insert(value, shouldNotify, false, onConflict);
	}
	
	@Override
	public long insert(Object value, boolean shouldNotify, boolean setPrimaryKey, int onConflict) throws StormException {
		
		final CachedTable table = super.getCachedTable(value);
		
		final ContentValuesCreator creator = new ContentValuesCreator();
		final ContentValues        cv      = creator.toContentValues(table, value, setPrimaryKey, null);
		
		long id;
		
		try {
			id = manager.getDataBase().insertWithOnConflict(table.getTableName(), null, cv, onConflict);
			//id = manager.insert(table.getTableName(), cv);
		}
		catch (SQLiteException e) {
			throw new StormException(e);
		}
		
		if (shouldNotify) {
			manager.notifyChange(table.getNotificationUri());
		}
		
		return id;
	}
	
	@Override
	public long insert(Object value, boolean shouldNotify, boolean setPrimaryKey) throws StormException {
		
		return insert(value, shouldNotify, setPrimaryKey, SQLiteDatabase.CONFLICT_NONE);
	}
	
	@Override
	public long[] insert(List<?> values) throws StormException {
		
		return insert(values, false);
	}
	
	@Override
	public long[] insert(List<?> values, int onConflict) throws StormException {
		
		return insert(values, 0);
	}
	
	@Override
	public long[] insert(List<?> values, boolean shouldNotify, int onConflict) throws StormException {
		
		return insert(values, shouldNotify, false);
	}
	
	@Override
	public long[] insert(List<?> values, boolean shouldNotify) throws StormException {
		
		return insert(values, shouldNotify, 0);
	}
	
	@Override
	public long[] insert(List<?> values, boolean shouldNotify, boolean setPrimaryKey, int onConflict) throws StormException {
		
		final CachedTable table = super.getCachedTable(values);
		
		final ContentValuesCreator creator = new ContentValuesCreator();
		ContentValues              cv;
		
		final int    size = values.size();
		final long[] ids  = new long[size];
		
		beginTransaction();
		
		try {
			
			Object value;
			for (int i = 0; i < size; i++) {
				value  = values.get(i);
				cv     = creator.toContentValues(table, value, setPrimaryKey, null);
				ids[i] = manager.getDataBase().insertWithOnConflict(table.getTableName(), null, cv, onConflict);
			}
			setTransactionSuccessful();
		}
		catch (SQLiteException e) {
			throw new StormException(e);
		}
		finally {
			endTransaction();
		}
		
		if (shouldNotify) {
			manager.notifyChange(table.getNotificationUri());
		}
		
		return ids;
	}
	
	@Override
	public long[] insert(List<?> values, boolean shouldNotify, boolean setPrimaryKey) throws StormException {
		
		return insert(values, shouldNotify, setPrimaryKey, 0);
	}
}
