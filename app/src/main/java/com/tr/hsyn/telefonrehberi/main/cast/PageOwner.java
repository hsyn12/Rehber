package com.tr.hsyn.telefonrehberi.main.cast;


/**
 * This is the owner of the main screen view pager.
 */
public interface PageOwner {
	
	/**
	 * Index value in {@code viewPager} of the 'contacts' page.
	 */
	int PAGE_CONTACTS = 0;
	/**
	 * Index value in {@code viewPager} of the 'call log' page.
	 */
	int PAGE_CALL_LOG = 1;
	
	/**
	 * Returns the current active page.
	 *
	 * @return the current active page
	 */
	int getCurrentPage();
	
	/**
	 * @return {@code true} if contacts page is active.
	 */
	default boolean contactsPage() {
		
		return getCurrentPage() == PAGE_CONTACTS;
	}
	
	/**
	 * @return {@code true} if call log page is active.
	 */
	default boolean callLogPage() {
		
		return getCurrentPage() == PAGE_CALL_LOG;
	}
}
