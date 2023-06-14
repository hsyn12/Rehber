package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


/**
 * Provides some get ranked lists according to the duration of calls
 * by using all call log calls.<br>
 *
 *
 * <ul>
 *     <li>{@link #rankByIncoming(CallCollection)} returns a map object that ranked by calls duration in incoming calls.</li>
 *     <li>{@link #rankByOutgoing(CallCollection)} returns a map object that ranked by calls duration in outgoing calls.</li>
 *     <li>{@link #rankByMissed(CallCollection)} returns a map object that ranked by calls duration in missed calls.</li>
 *     <li>{@link #rankByRejected(CallCollection)} returns a map object that ranked by calls duration in rejected calls.</li>
 *     </ul>
 * <p>
 * <p>
 * The ranking is based on the number of calls and ranks start from 1.
 * So the first rank is 1, and it has the highest duration of calls.
 * The duration consists of the total duration of incoming-outgoing calls.
 */
public class RankByDuration {
	
	/**
	 * Returns a map object that ranked by calls duration in incoming calls.
	 *
	 * @return a map object that ranked by calls duration in incoming calls.
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankByIncoming(@NotNull CallCollection callCollection) {
		
		List<Call>              incomingCalls = callCollection.getIncomingCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.mapNumberToCalls(incomingCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in outgoing calls.
	 *
	 * @return a map object that ranked by calls duration in outgoing calls.
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankByOutgoing(@NotNull CallCollection callCollection) {
		
		List<Call>              outgoingCalls = callCollection.getOutgoingCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.mapNumberToCalls(outgoingCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in missed calls.
	 *
	 * @return a map object that ranked by calls duration in missed calls.
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankByMissed(@NotNull CallCollection callCollection) {
		
		List<Call>              missedCalls   = callCollection.getMissedCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.mapNumberToCalls(missedCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in rejected calls.
	 *
	 * @return a map object that ranked by calls duration in rejected calls.
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> rankByRejected(@NotNull CallCollection callCollection) {
		
		List<Call>              rejectedCalls = callCollection.getRejectedCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.mapNumberToCalls(rejectedCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration by descending.
	 *
	 * @param callCollection the call collection
	 * @return a map object that ranked by calls duration by descending
	 */
	@NotNull
	public static Map<Integer, List<CallRank>> createRankMap(@NotNull CallCollection callCollection) {
		
		return new RankList(callCollection.getNumberedCalls()).makeDurationRanks().getRankMap();
	}
	
	
}
