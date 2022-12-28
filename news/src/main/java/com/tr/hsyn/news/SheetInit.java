package com.tr.hsyn.news;


import com.tr.hsyn.historic.Historic;
import com.tr.hsyn.label.Label;
import com.tr.hsyn.label.Mabel;

import org.jetbrains.annotations.Nullable;

import java.util.Set;


public class SheetInit implements Historic, Mabel {
	
	/**
	 * Haberin etiketleri
	 */
	private Set<Label> labels;
	/**
	 * Haberin tarihi
	 */
	private long       time;
	
	/**
	 * @return Haberin tarihi
	 */
	@Override
	public long getTime() {return time;}
	
	/**
	 * Haber tarihini kaydet.
	 *
	 * @param time Tarih (milisaniye)
	 */
	public void setTime(long time) {this.time = time;}
	
	/**
	 * @return Haberin etiketleri
	 */
	@Override
	public @Nullable Set<Label> getLabels() {return labels;}
	
	/**
	 * @param labels Labels Haberin etiketleri
	 */
	@Override
	public void setLabels(@Nullable Set<Label> labels) {this.labels = labels;}
	
	
}
