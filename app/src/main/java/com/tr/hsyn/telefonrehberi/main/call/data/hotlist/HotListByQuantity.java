package com.tr.hsyn.telefonrehberi.main.call.data.hotlist;


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
 *    <li>{@link #getMostCalls()} returns a map object that ranked by calls size in all calls.</li>
 *    <li>{@link #getMostIncomingCalls()} returns a map object that ranked by calls size in incoming calls.</li>
 *    <li>{@link #getMostOutgoingCalls()} returns a map object that ranked by calls size in outgoing calls.</li>
 *    <li>{@link #getMostMissedCalls()} returns a map object that ranked by calls size in missed calls.</li>
 *    <li>{@link #getMostRejectedCalls()} returns a map object that ranked by calls size in rejected calls.</li>
 * </ul>
 *
 * <p>
 * <p>
 *  The ranking is based on the number of calls and ranks start from 1.
 *  So the first rank is 1, and it has the highest number of calls.
 */
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
		mostCalls           = new RankList(callCollection.getNumberedCalls()).makeQuantityRanks().getRankMap();
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
		
		List<com.tr.hsyn.calldata.Call>              incomingCalls = callCollection.getIncomingCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.makeNumberedCalls(incomingCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for outgoing calls.
	 *
	 * @return object that ranked by calls size for outgoing calls.
	 */
	private Map<Integer, List<CallRank>> makeMostOutgoingCalls() {
		
		List<com.tr.hsyn.calldata.Call>              outgoingCalls = callCollection.getOutgoingCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.makeNumberedCalls(outgoingCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for missed calls.
	 *
	 * @return object that ranked by calls size for missed calls.
	 */
	private Map<Integer, List<CallRank>> makeMostMissedCalls() {
		
		List<com.tr.hsyn.calldata.Call>              missedCalls   = callCollection.getMissedCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.makeNumberedCalls(missedCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
	/**
	 * Returns object that ranked by calls size for rejected calls.
	 *
	 * @return object that ranked by calls size for rejected calls.
	 */
	private Map<Integer, List<CallRank>> makeMostRejectedCalls() {
		
		List<com.tr.hsyn.calldata.Call>              rejectedCalls = callCollection.getRejectedCalls();
		Map<String, List<com.tr.hsyn.calldata.Call>> numberedCalls = CallCollection.makeNumberedCalls(rejectedCalls);
		RankList                                     rankList      = new RankList(numberedCalls);
		return rankList.makeQuantityRanks().getRankMap();
	}
	
}
