package com.tr.hsyn.telefonrehberi.main.call.fragment;


/**
 * Manages the title and subtitle of the call log.
 */
public abstract class CallLogTitle extends CallLogView {
	
	/**
	 * That's the title of the call log.
	 */
	private CharSequence title;
	
	@Override
	public CharSequence getTitle() {
		
		return title;
	}
	
	/**
	 * Sets the title of the call log.
	 *
	 * @param title The title
	 */
	public void setTitle(CharSequence title) {
		
		if (isShowTime())
			header.setTitle(title);
		
		this.title = title;
	}
	
	/**
	 * Sets the subtitle of the call log.
	 *
	 * @param subTitle The subtitle
	 */
	protected void setSubTitle(CharSequence subTitle) {
		
		if (isShowTime())
			header.setSubTitle(subTitle);
	}
	
	/**
	 * Updates the subtitle of the call log according to the adapter size.
	 */
	public void updateSubTitle() {
		
		if (isShowTime())
			header.setSubTitle(String.valueOf(getAdapterSize()));
	}
	
	
}
