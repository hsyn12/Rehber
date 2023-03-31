package com.tr.hsyn.calldata;


/**
 * Arama Kaydı Türleri.<br>
 * Bir arama kaydı için geçerli olabilecek {@code 5} temel arama türü vardır.<br><br>
 *
 * <ol>
 *    <li>{@link #INCOMING}</li>
 *    <li>{@link #OUTGOING}</li>
 *    <li>{@link #MISSED}</li>
 *    <li>{@link #REJECTED}</li>
 *    <li>{@link #BLOCKED}</li>
 * </ol><br>
 * <p>
 * Bunlara ek olarak {@code 3} arama türü daha vardır.<br><br>
 *
 * <ol>
 *    <li>{@link #UNREACHED}</li>
 *    <li>{@link #UNRECEIVED}</li>
 *    <li>{@link #GET_REJECTED}</li>
 * </ol><br>
 * <p>
 * Ve iki internet araması türü.<br><br>
 *
 * <ol>
 *    <li>{@link #INCOMING_WIFI}</li>
 *    <li>{@link #OUTGOING_WIFI}</li>
 * </ol>
 */
public interface CallType {
	
	/**
	 * Gelen Arama Türü
	 */
	int INCOMING      = 1;
	/**
	 * Giden Arama Türü
	 */
	int OUTGOING      = 2;
	/**
	 * Cevapsız Arama Türü
	 */
	int MISSED        = 3;
	/**
	 * Reddedilen Arama Türü
	 */
	int REJECTED      = 5;
	/**
	 * Engellenen Arama Türü
	 */
	int BLOCKED       = 6;
	/**
	 * Gelen internet araması Türü
	 */
	int INCOMING_WIFI = 1000;
	/**
	 * Giden internet araması Türü
	 */
	int OUTGOING_WIFI = 1001;
	/**
	 * Ulaşmayan Giden Arama Türü.
	 */
	int UNREACHED     = 9001;
	/**
	 * Ulaşmayan Gelen Arama Türü.
	 */
	int UNRECEIVED    = 9002;
	/**
	 * Reddedilen Giden Arama Türü
	 */
	int GET_REJECTED  = 9003;
	
	int getType();
	
	/**
	 * @return Gelen Arama ise {@code true}
	 */
	default boolean isIncoming() {
		
		return getType() == INCOMING || getType() == INCOMING_WIFI;
	}
	
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
