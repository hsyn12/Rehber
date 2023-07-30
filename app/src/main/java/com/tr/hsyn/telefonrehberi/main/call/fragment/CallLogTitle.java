package com.tr.hsyn.telefonrehberi.main.call.fragment;


/**
 * Başlığı ve alt başlığı yönet
 */
public abstract class CallLogTitle extends CallLogView {
	
	private CharSequence title;
	
	@Override
	public CharSequence getTitle() {
		
		return title;
	}
	
	public void setTitle(CharSequence title) {
		
		if (isShowTime())
			header.setTitle(title);
		
		this.title = title;
	}
	
	protected void setSubTitle(CharSequence subTitle) {
		
		if (isShowTime())
			header.setSubTitle(subTitle);
	}
	
	public void updateSubTitle() {
		
		if (isShowTime())
			header.setSubTitle(String.valueOf(getAdapterSize()));
	}
	
	
}
