package com.tr.hsyn.telefonrehberi.main.call.data;


import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;

import java.util.List;
import java.util.Map;


public class RankMap {
	
	private Map<Integer, List<CallRank>> rankMap;
	
	public RankMap(Map<Integer, List<CallRank>> rankMap) {
		
		this.rankMap = rankMap;
	}
	
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	public void setRankMap(Map<Integer, List<CallRank>> rankMap) {
		
		this.rankMap = rankMap;
	}
	
	public List<CallRank> getRank(int key) {
		
		return rankMap.get(key);
	}
	
	public int size() {
		
		return rankMap.size();
	}
	
	public boolean isEmpty() {
		
		return rankMap.isEmpty();
	}
	
	
}
