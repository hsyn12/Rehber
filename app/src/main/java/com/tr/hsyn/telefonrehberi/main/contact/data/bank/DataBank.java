package com.tr.hsyn.telefonrehberi.main.contact.data.bank;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tr.hsyn.roomstorm.DatabaseManager;
import com.tr.hsyn.roomstorm.Storm;
import com.tr.hsyn.roomstorm.exc.StormException;
import com.tr.hsyn.roomstorm.query.Selection;

import java.util.Arrays;
import java.util.List;


public class DataBank {
	
	public <T> boolean add(DataBankClient<T> client, T value) {
		
		try {
			return Storm.newInsert(getDatabaseManager(client)).insert(value) != -1;
		}
		catch (StormException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Nullable
	private <T> DatabaseManager getDatabaseManager(@NonNull DataBankClient<T> client) {
		
		try {
			
			var database = new DatabaseManager(
					client.getContext(),
					client.getName(),
					client.getId(),
					new Class[]{client.getClazz()});
			
			database.open();
			return database;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public <T> boolean add(DataBankClient<T> client, List<T> values) {
		
		try {
			
			var r = Storm.newInsert(getDatabaseManager(client)).insert(values);
			
			return Arrays.stream(r).allMatch(i -> i != -1);
		}
		catch (StormException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public <T> boolean add(DataBankClient<T> client, List<T> values, boolean override) {
		
		try {
			
			var base = getDatabaseManager(client);
			
			if (override) {
				
				Storm.newDelete(base).deleteAll(client.getClazz());
			}
			
			var r = Storm.newInsert(base).insert(values);
			
			return Arrays.stream(r).allMatch(i -> i != -1);
		}
		catch (StormException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public <T> boolean update(DataBankClient<T> client, T value) {
		
		try {
			return Storm.newUpdate(getDatabaseManager(client)).update(value) > 0;
		}
		catch (StormException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public <T> boolean delete(DataBankClient<T> client, Selection selection) {
		
		try {
			return Storm.newDelete(getDatabaseManager(client)).delete(client.getClazz(), selection) > 0;
		}
		catch (StormException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public <T> T query(DataBankClient<T> client, Selection selection) {
		
		return Storm.newSelect(getDatabaseManager(client)).query(client.getClazz(), selection);
	}
	
	public <T> List<? extends T> queryAll(DataBankClient<T> client) {
		
		return Storm.newSelect(getDatabaseManager(client)).queryAll(client.getClazz());
	}
	
	@Nullable
	public <T> T rawQuery(DataBankClient<T> client, String selection, String[] selectionArgs) {
		
		return Storm.newSelect(getDatabaseManager(client)).customQuery(client.getClazz(), selection, selectionArgs);
	}
	
	@Nullable
	public <T> List<? extends T> rawQueryList(DataBankClient<T> client, String selection, String[] selectionArgs) {
		
		return Storm.newSelect(getDatabaseManager(client)).customQueryList(client.getClazz(), selection, selectionArgs);
	}
	
	
}
