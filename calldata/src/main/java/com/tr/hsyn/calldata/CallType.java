package com.tr.hsyn.calldata;


/**
 * Call types.<br>
 * There are five base call types.<br><br>
 *
 * <ol>
 *    <li>{@link #INCOMING}</li>
 *    <li>{@link #OUTGOING}</li>
 *    <li>{@link #MISSED}</li>
 *    <li>{@link #REJECTED}</li>
 *    <li>{@link #BLOCKED}</li>
 * </ol><br>
 * <p>
 * Additionally, there are three call types.<br><br>
 *
 * <ol>
 *    <li>{@link #UNREACHED}</li>
 *    <li>{@link #UNRECEIVED}</li>
 *    <li>{@link #GET_REJECTED}</li>
 * </ol><br>
 * <p>
 * And there are two internet call types.<br><br>
 *
 * <ol>
 *    <li>{@link #INCOMING_WIFI}</li>
 *    <li>{@link #OUTGOING_WIFI}</li>
 * </ol>
 */
public interface CallType {
	
	/**
	 * Incoming call type
	 */
	int INCOMING      = 1;
	/**
	 * Outgoing call type
	 */
	int OUTGOING      = 2;
	/**
	 * Missed a call type
	 */
	int MISSED        = 3;
	/**
	 * Rejected call type
	 */
	int REJECTED      = 5;
	/**
	 * Blocked call type
	 */
	int BLOCKED       = 6;
	/**
	 * Incoming internet call type
	 */
	int INCOMING_WIFI = 1000;
	/**
	 * Outgoing internet call type
	 */
	int OUTGOING_WIFI = 1001;
	/**
	 * Unreached call type
	 */
	int UNREACHED     = 9001;
	/**
	 * Unreceived call type
	 */
	int UNRECEIVED    = 9002;
	/**
	 * Get rejected call type
	 */
	int GET_REJECTED  = 9003;
	/**
	 * Unknown call type
	 */
	int UNKNOWN       = 545454;
	
	/**
	 * @return Gelen Arama ise {@code true}
	 */
	default boolean isIncoming() {
		
		return getType() == INCOMING || getType() == INCOMING_WIFI;
	}
	
	int getType();
	
	/**
	 * @return Giden Arama ise {@code true}
	 */
	default boolean isOutgoing() {
		
		return getType() == OUTGOING || getType() == OUTGOING_WIFI;
	}
	
	/**
	 * @return Cevapsız Arama ise {@code true}
	 */
	default boolean isMissed() {
		
		return getType() == MISSED;
	}
	
	/**
	 * @return Reddedilen Arama ise {@code true}
	 */
	default boolean isRejected() {
		
		return getType() == REJECTED;
	}
	
	/**
	 * @return Engellenen Arama ise {@code true}
	 */
	default boolean isBlocked() {
		
		return getType() == BLOCKED;
	}
	
	/**
	 * @return Ulaşmayan Giden Arama ise {@code true}
	 */
	default boolean isUnReached() {
		
		return getType() == UNREACHED;
	}
	
	/**
	 * @return Ulaşmayan Gelen Arama ise {@code true}
	 */
	default boolean isUnReceived() {
		
		return getType() == UNRECEIVED;
	}
	
	/**
	 * @return Reddedilen Giden Arama ise {@code true}
	 */
	default boolean isGetRejected() {
		
		return getType() == GET_REJECTED;
	}
	
	default boolean isType(int type) {
		
		return type == getType();
	}
	
}
