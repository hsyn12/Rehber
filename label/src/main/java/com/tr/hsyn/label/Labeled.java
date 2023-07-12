package com.tr.hsyn.label;


import org.jetbrains.annotations.Nullable;

import java.util.Set;


/**
 * Labels the objects.<br>
 * The classes that implement this interface mean that they are labeled.
 */
public interface Labeled {
	
	/**
	 * @return set of labels
	 */
	@Nullable Set<Label> getLabels();
	
	/**
	 * Sets the labels.
	 *
	 * @param labels set of labels
	 */
	void setLabels(@Nullable Set<Label> labels);
}
