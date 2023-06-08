package com.tr.hsyn.label;


import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Interface for labelled object to manage labels
 */
public interface Mabel extends Labeled, Labeller {
	
	/**
	 * Adds the label.
	 *
	 * @param label the label to add
	 */
	@Override
	default Mabel addLabel(@NotNull final Label label) {
		
		if (getLabels() == null) setLabels(new HashSet<>());
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> labels = getLabels();
		
		labels.add(label);
		return this;
	}
	
	/**
	 * Adds the labels.
	 *
	 * @param labels the labels to add
	 */
	default void addLabels(@NotNull final Label... labels) {
		
		if (getLabels() == null) setLabels(new HashSet<>());
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> _labels = getLabels();
		
		_labels.addAll(Arrays.asList(labels));
	}
	
	/**
	 * Remove the label.
	 *
	 * @param label label to remove
	 */
	default boolean removeLabel(@NotNull final Label label) {
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> labels = getLabels();
		
		if (labels != null)
			return labels.remove(label);
		
		return false;
	}
	
	/**
	 * Checks the label.
	 *
	 * @param label label to check
	 * @return {@code true} if specified label is exist
	 */
	default boolean hasLabel(@NotNull final Label label) {
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> labels = getLabels();
		
		if (labels != null)
			return labels.contains(label);
		
		return false;
	}
	
	/**
	 * Checks the label.
	 *
	 * @param labelName label name to check
	 * @return {@code true} if specified label is exist
	 */
	default boolean hasLabel(@NotNull final String labelName) {
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> labels = getLabels();
		
		if (labels != null)
			return labels.stream().anyMatch(l -> l.getName().equals(labelName));
		
		return false;
	}
	
	/**
	 * Checks the label.
	 *
	 * @param labelId label id to check
	 * @return {@code true} if specified label is exist
	 */
	default boolean hasLabel(int labelId) {
		
		java.util.@org.jetbrains.annotations.Nullable Set<Label> labels = getLabels();
		
		if (labels != null)
			return labels.stream().anyMatch(l -> l.getId() == labelId);
		
		return false;
	}
	
}
