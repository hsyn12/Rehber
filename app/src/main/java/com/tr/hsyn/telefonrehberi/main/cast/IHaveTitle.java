package com.tr.hsyn.telefonrehberi.main.cast;


import com.tr.hsyn.holder.Holder;


public interface IHaveTitle {
	
	Holder<CharSequence> titleHolder = Holder.newHolder("");
	
	default CharSequence getTitle() {
		
		return titleHolder.getValue();
	}
}