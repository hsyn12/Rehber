package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;

import java.util.List;
import java.util.Map;


public class HotListByDuration {
	
	private final CallCollection               callCollection;
	private       Map<Integer, List<CallRank>> mostDurationCalls;
	
	public HotListByDuration(CallCollection callCollection) {
		
		this.callCollection = callCollection;
		
	}
}
