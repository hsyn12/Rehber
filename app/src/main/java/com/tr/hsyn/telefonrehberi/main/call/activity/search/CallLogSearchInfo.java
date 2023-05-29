package com.tr.hsyn.telefonrehberi.main.call.activity.search;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.keep.Keep;

import java.util.List;


@Keep
public class CallLogSearchInfo {
	
	private final List<Call> calls;
	private final int        filter;
	
	public CallLogSearchInfo(List<Call> calls, int filter) {
		
		this.calls  = calls;
		this.filter = filter;
	}
	
	public List<Call> getCalls() {
		
		return calls;
	}
	
	public int getFilter() {
		
		return filter;
	}
}
