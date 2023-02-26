package com.tr.hsyn.contactdata;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Account
 */
public interface Account {
	
	@NotNull
	static Account newAccount(@Nullable String name, @Nullable String type) {
		
		return new Account() {
			
			@Override
			public String getName() {return name;}
			
			@Override
			public String getType() {return type;}
		};
	}
	
	/**
	 * @return Account name
	 */
	@Nullable
	String getName();
	
	/**
	 * @return Account type
	 */
	@Nullable
	String getType();
}
