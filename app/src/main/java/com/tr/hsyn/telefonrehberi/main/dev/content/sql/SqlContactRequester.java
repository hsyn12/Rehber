package com.tr.hsyn.telefonrehberi.main.dev.content.sql;


import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.tr.hsyn.contactdata.Contact;
import com.tr.hsyn.content.handler.SQLContentHandler;
import com.tr.hsyn.content.requestor.SQLContentRequester;


public class SqlContactRequester implements SQLContentRequester<Contact> {
	
	private final SQLiteDatabase             readableDatabase;
	private final String                     tableName;
	private final SQLContentHandler<Contact> contentHandler;
	
	public SqlContactRequester(SQLiteDatabase readableDatabase, String tableName, SQLContentHandler<Contact> contentHandler) {
		
		this.readableDatabase = readableDatabase;
		this.tableName        = tableName;
		this.contentHandler   = contentHandler;
	}
	
	@NonNull
	@Override
	public String getTableName() {
		
		return tableName;
	}
	
	@NonNull
	@Override
	public SQLiteDatabase getReadableDatabase() {
		
		return readableDatabase;
	}
	
	@NonNull
	@Override
	public SQLContentHandler<Contact> getContentHandler() {
		
		return contentHandler;
	}
}
