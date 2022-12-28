package com.tr.hsyn.label;


import com.tr.hsyn.atom.Atom;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Etiket.<br>
 * Nesnelerin işareti.
 */
public class Label extends Atom {
	
	public Label(int id, @NotNull String name) {
		
		super(id, name);
	}
	
	@NotNull
	public static Label newLabel(int id, @NotNull String name) {
		
		return new Label(id, name);
	}
	
	@NotNull
	public static Set<Label> of(Label... labels) {
		
		return new HashSet<>(Arrays.asList(labels));
	}
	
}