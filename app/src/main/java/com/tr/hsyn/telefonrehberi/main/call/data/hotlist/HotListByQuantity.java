package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


import com.tr.hsyn.telefonrehberi.main.call.data.CallCollection;
import com.tr.hsyn.telefonrehberi.main.contact.comment.CallRank;
import com.tr.hsyn.telefonrehberi.main.contact.comment.RankList;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;


public class HotListByQuantity {
	
	/**
	 * The most calls by quantity
	 */
	private final Map<Integer, List<CallRank>> mostCalls;
	/**
	 * The most incoming calls by quantity
	 */
	private final Map<Integer, List<CallRank>> mostIncomingCalls;
	/**
	 * The most outgoing calls by quantity
	 */
	private final Map<Integer, List<CallRank>> mostOutgoingCalls;
	/**
	 * The most missed calls by quantity
	 */
	private final Map<Integer, List<CallRank>> mostMissedCalls;
	/**
	 * The most rejected calls by quantity
	 */
	private final Map<Integer, List<CallRank>> mostRejectedCalls;
	/**
	 * {@link CallCollection} object
	 */
	private final CallCollection               callCollection;
	
	/**
	 * Constructor of HotList
	 *
	 * @param callCollection {@link CallCollection} object
	 */
	public HotListByQuantity(@NotNull CallCollection callCollection) {
		
		this.callCollection = callCollection;
		mostCalls           = new RankList(callCollection.getNumberedCalls()).makeRanks().getRankMap();
		mostIncomingCalls   = makeMostIncomingCalls();
		mostOutgoingCalls   = makeMostOutgoingCalls();
		mostMissedCalls     = makeMostMissedCalls();
		mostRejectedCalls   = makeMostRejectedCalls();
	}
	
	/**
	 * Returns object that ranked by calls size.
	 * This object has only rejected calls.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by calls size.
	 */
	public Map<Integer, List<CallRank>> getMostRejectedCalls() {
		
		return mostRejectedCalls;
	}
	
	/**
	 * Returns object that ranked by calls size.
	 * This object has only missed calls.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by calls size.
	 */
	public Map<Integer, List<CallRank>> getMostMissedCalls() {
		
		return mostMissedCalls;
	}
	
	/**
	 * Returns object that ranked by calls size.
	 * This object has only outgoing calls.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by calls size.
	 */
	public Map<Integer, List<CallRank>> getMostOutgoingCalls() {
		
		return mostOutgoingCalls;
	}
	
	/**
	 * Returns object that ranked by incoming calls size.
	 * This object has only incoming calls.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by incoming calls size.
	 */
	public Map<Integer, List<CallRank>> getMostIncomingCalls() {
		
		return mostIncomingCalls;
	}
	
	/**
	 * Returns object that has a rank as an identifier and value as a list of {@link CallRank} that has that rank.
	 * Ranks start from 1.
	 * So the first rank is 1, and it has the highest number of calls.
	 *
	 * @return object that has a rank as an identifier and value as a list of {@link CallRank} that has that rank.
	 */
	public Map<Integer, List<CallRank>> getMostCalls() {
		
		return mostCalls;
	}
	
	/**
	 * Returns object that ranked by calls size.
	 * The first rank is 1, and it has the highest number of calls.
	 * Maybe there are more than one rank with the same number of calls.
	 *
	 * @return object that ranked by calls size.
	 */
	private Map<Integer, List<CallRank>> makeMostIncomingCalls() {
		
		var incomingCalls = callCollection.getIncomingCalls();
		var numberedCalls = CallCollection.makeNumberedCalls(incomingCalls);
		var rankList      = new RankList(numberedCalls);
		return rankList.makeRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for outgoing calls.
	 *
	 * @return object that ranked by calls size for outgoing calls.
	 */
	private Map<Integer, List<CallRank>> makeMostOutgoingCalls() {
		
		var outgoingCalls = callCollection.getOutgoingCalls();
		var numberedCalls = CallCollection.makeNumberedCalls(outgoingCalls);
		var rankList      = new RankList(numberedCalls);
		return rankList.makeRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for missed calls.
	 *
	 * @return object that ranked by calls size for missed calls.
	 */
	private Map<Integer, List<CallRank>> makeMostMissedCalls() {
		
		var missedCalls   = callCollection.getMissedCalls();
		var numberedCalls = CallCollection.makeNumberedCalls(missedCalls);
		var rankList      = new RankList(numberedCalls);
		return rankList.makeRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for rejected calls.
	 *
	 * @return object that ranked by calls size for rejected calls.
	 */
	private Map<Integer, List<CallRank>> makeMostRejectedCalls() {
		
		var rejectedCalls = callCollection.getRejectedCalls();
		var numberedCalls = CallCollection.makeNumberedCalls(rejectedCalls);
		var rankList      = new RankList(numberedCalls);
		return rankList.makeRanks().getRankMap();
	}
	
}
