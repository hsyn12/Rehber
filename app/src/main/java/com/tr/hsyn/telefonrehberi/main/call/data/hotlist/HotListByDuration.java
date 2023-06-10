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
 *     <li>{@link #getRankMap()} returns a map object that ranked by calls duration in all calls.</li>
 *     <li>{@link #getRankByIncoming()} returns a map object that ranked by calls duration in incoming calls.</li>
 *     <li>{@link #getRankByOutgoing()} returns a map object that ranked by calls duration in outgoing calls.</li>
 *     <li>{@link #getRankByMissed()} returns a map object that ranked by calls duration in missed calls.</li>
 *     <li>{@link #getRankByRejected()} returns a map object that ranked by calls duration in rejected calls.</li>
 *     </ul>
 * <p>
 * >
 *         The ranking is based on the number of calls and ranks start from 1.
 *         So the first rank is 1, and it has the highest duration of calls.
 *         The duration consists of the total duration of incoming or outgoing or both calls.
 */
public class HotListByDuration {
	
	private final CallCollection               callCollection;
	private final Map<Integer, List<CallRank>> rankMap;
	private final Map<Integer, List<CallRank>> rankByIncoming;
	private final Map<Integer, List<CallRank>> rankByOutgoing;
	private final Map<Integer, List<CallRank>> rankByMissed;
	private final Map<Integer, List<CallRank>> rankByRejected;
	
	/**
	 * Constructor of HotList
	 *
	 * @param callCollection {@link CallCollection} object
	 */
	public HotListByDuration(@NotNull CallCollection callCollection) {
		
		this.callCollection = callCollection;
		rankMap             = new RankList(callCollection.getNumberedCalls()).makeDurationRanks().getRankMap();
		rankByIncoming      = rankByIncoming();
		rankByOutgoing      = rankByOutgoing();
		rankByMissed        = rankByMissed();
		rankByRejected      = rankByRejected();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in all calls.
	 *
	 * @return a map object that ranked by calls duration in all calls.
	 */
	public Map<Integer, List<CallRank>> getRankMap() {
		
		return rankMap;
	}
	
	/**
	 * Returns a map object that ranked by calls duration in incoming calls.
	 *
	 * @return a map object that ranked by calls duration in incoming calls.
	 */
	public Map<Integer, List<CallRank>> getRankByIncoming() {
		
		return rankByIncoming;
	}
	
	/**
	 * Returns a map object that ranked by calls duration in incoming calls.
	 *
	 * @return a map object that ranked by calls duration in incoming calls.
	 */
	private Map<Integer, List<CallRank>> rankByIncoming() {
		
		List<Call>              incomingCalls = callCollection.getIncomingCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.makeNumberedCalls(incomingCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in outgoing calls.
	 *
	 * @return a map object that ranked by calls duration in outgoing calls.
	 */
	public Map<Integer, List<CallRank>> getRankByOutgoing() {
		
		return rankByOutgoing;
	}
	
	/**
	 * Returns a map object that ranked by calls duration in outgoing calls.
	 *
	 * @return a map object that ranked by calls duration in outgoing calls.
	 */
	private Map<Integer, List<CallRank>> rankByOutgoing() {
		
		List<Call>              outgoingCalls = callCollection.getOutgoingCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.makeNumberedCalls(outgoingCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in missed calls.
	 *
	 * @return a map object that ranked by calls duration in missed calls.
	 */
	public Map<Integer, List<CallRank>> getRankByMissed() {
		
		return rankByMissed;
	}
	
	/**
	 * Returns a map object that ranked by calls duration in missed calls.
	 *
	 * @return a map object that ranked by calls duration in missed calls.
	 */
	private Map<Integer, List<CallRank>> rankByMissed() {
		
		List<Call>              missedCalls   = callCollection.getMissedCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.makeNumberedCalls(missedCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	/**
	 * Returns a map object that ranked by calls duration in rejected calls.
	 *
	 * @return a map object that ranked by calls duration in rejected calls.
	 */
	public Map<Integer, List<CallRank>> getRankByRejected() {
		
		return rankByRejected;
	}
	
	/**
	 * Returns a map object that ranked by calls duration in rejected calls.
	 *
	 * @return a map object that ranked by calls duration in rejected calls.
	 */
	private Map<Integer, List<CallRank>> rankByRejected() {
		
		List<Call>              rejectedCalls = callCollection.getRejectedCalls();
		Map<String, List<Call>> numberedCalls = CallCollection.makeNumberedCalls(rejectedCalls);
		RankList                rankList      = new RankList(numberedCalls);
		return rankList.makeDurationRanks().getRankMap();
	}
	
	
}
