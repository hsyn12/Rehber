package com.tr.hsyn.label;


import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * Interface for labeled objects
 */
public interface Labeled {
	
	/**
	 * @return list of labels
	 */
	@Nullable Set<Label> getLabels();
	
	/**
	 * Sets the labels.
	 *
	 * @param labels Labels
	 */
	void setLabels(@Nullable Set<Label> labels);
}
