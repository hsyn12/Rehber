package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


/**
 * Provides some get ranked lists according to the number of calls by using all call log calls.<br>
 *
 * <ul>
 *    <li>{@link #rankByIncoming(CallCollection)} returns a map object that ranked by calls size in incoming calls.</li>
 *    <li>{@link #rankByOutgoing(CallCollection)} returns a map object that ranked by calls size in outgoing calls.</li>
 *    <li>{@link #rankByMissed(CallCollection)} returns a map object that ranked by calls size in missed calls.</li>
 *    <li>{@link #rankByRejected(CallCollection)} returns a map object that ranked by calls size in rejected calls.</li>
 * </ul>
 *
 * <p>
 * <p>
 *  The ranking is based on the number of calls and ranks start from 1.
 *  So the first rank is 1, and it has the highest number of calls.
 */
public class RankByQuantity {
	
	/**
	 * Returns object that ranked by calls size.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by calls size.
	 */
	public static Map<Integer, List<CallRank>> rankByIncoming(@NotNull CallCollection callCollection) {
		
		List<Call>              incomingCalls = callCollection.getIncomingCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.mapNumberToCalls(incomingCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for outgoing calls.
	 *
	 * @return object that ranked by calls size for outgoing calls.
	 */
	public static Map<Integer, List<CallRank>> rankByOutgoing(@NotNull CallCollection callCollection) {
		
		List<com.tr.hsyn.calldata.Call>              outgoingCalls = callCollection.getOutgoingCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.mapNumberToCalls(outgoingCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for missed calls.
	 *
	 * @return object that ranked by calls size for missed calls.
	 */
	public static Map<Integer, List<CallRank>> rankByMissed(@NotNull CallCollection callCollection) {
		
		List<com.tr.hsyn.calldata.Call>              missedCalls   = callCollection.getMissedCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.mapNumberToCalls(missedCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for rejected calls.
	 *
	 * @return object that ranked by calls size for rejected calls.
	 */
	public static Map<Integer, List<CallRank>> rankByRejected(@NotNull CallCollection callCollection) {
		
		List<com.tr.hsyn.calldata.Call>              rejectedCalls = callCollection.getRejectedCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.mapNumberToCalls(rejectedCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull CallCollection callCollection) {
		
		return new RankList(callCollection.getMapNumberToCalls()).makeQuantityRanks().getRankMap();
	}
	
}
