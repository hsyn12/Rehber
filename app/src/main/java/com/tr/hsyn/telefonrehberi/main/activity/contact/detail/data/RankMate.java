package com.tr.hsyn.telefonrehberi.main.activity.contact.detail.data;


import com.tr.hsyn.phone_numbers.PhoneNumbers;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


public class RankMate {
	
	private final Map<Integer, List<CallRank>> rankMap;
	
	public RankMate(Map<Integer, List<CallRank>> rankMap) {
		
		this.rankMap = rankMap;
	}
	
	
	public int getRank(@NotNull String number) {
		
		var _number = PhoneNumbers.formatNumber(number, PhoneNumbers.N_MIN);
		
		for (var entry : rankMap.entrySet()) {
			
			if (entry.getValue().stream().anyMatch(p -> p.getKey().equals(_number))) {
				
				return entry.getKey();
			}
		}
		
		return -1;
	}
	
}