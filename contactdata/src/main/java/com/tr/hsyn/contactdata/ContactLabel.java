package com.tr.hsyn.contactdata;


import static com.tr.hsyn.label.Label.newLabel;

import com.tr.hsyn.label.Label;
import com.tr.hsyn.label.Mabel;
import com.tr.hsyn.tryme.Try;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;


/**
 * Bu sınıf, kişi için standart etiketleri ve etiket id değerlerini tanımlar.<br>
 *
 * @see Label
 */
public interface ContactLabel extends Mabel {
	
	//- Etiket id değerleri
	/**
	 * Bu etiket kişinin aileden biri olduğunu gösterir
	 */
	int FAMILY         = 0;
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin annesi olduğunu gösterir
	 */
	int MY_MUM         = 1;
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin babası olduğunu gösterir
	 */
	int MY_DAD         = 2;
	/**
	 * Bu etiket kişinin kız kardeşi olduğunu gösterir
	 */
	int MY_SISTER      = 3;
	/**
	 * Bu etiket kişinin erkek kardeşi olduğunu gösterir
	 */
	int MY_BROTHER     = 4;
	/**
	 * Bu etiket kişinin en iyi arkadaşı olduğunu gösterir
	 */
	int MY_BEST_FRIEND = 5;
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin sevgilisi olduğunu gösterir
	 */
	int MY_DARLING     = 6;
	/**
	 * Bu etiket kişinin normal arkadaşı olduğunu gösterir
	 */
	int MY_FRIEND      = 7;
	
	//- Etiketler
	
	/**
	 * Bu etiket kişinin aileden biri olduğunu gösterir
	 */
	Label LABEL_FAMILY         = newLabel(0, "Aile");
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin annesi olduğunu gösterir
	 */
	Label LABEL_MY_MUM         = newLabel(1, "Annem");
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin babası olduğunu gösterir
	 */
	Label LABEL_MY_DAD         = newLabel(2, "Babam");
	/**
	 * Bu etiket kişinin kız kardeşi olduğunu gösterir
	 */
	Label LABEL_MY_SISTER      = newLabel(3, "Kız Kardeşim");
	/**
	 * Bu etiket kişinin erkek kardeşi olduğunu gösterir
	 */
	Label LABEL_MY_BROTHER     = newLabel(4, "Erkek Kardeşim");
	/**
	 * Bu etiket kişinin en iyi arkadaşı olduğunu gösterir
	 */
	Label LABEL_MY_BEST_FRIEND = newLabel(5, "En İyi Arkadaşım");
	/**
	 * Bu etikete tek bir kişi sahip olabilir ve kişinin sevgilisi olduğunu gösterir
	 */
	Label LABEL_MY_DARLING     = newLabel(6, "Sevgilim");
	/**
	 * Bu etiket kişinin normal arkadaşı olduğunu gösterir
	 */
	Label LABEL_MY_FRIEND      = newLabel(7, "Arkadaşım");
	
	/**
	 * Returns the {@link Label} for the given {@code labelId}.
	 *
	 * @param labelId the label id
	 * @return the label
	 * @throws IllegalArgumentException if the given {@code labelId} is not a standard label id
	 */
	static Label getLabel(int labelId) {
		
		//@off
		switch (labelId) {
            case FAMILY:            return LABEL_FAMILY;
            case MY_BEST_FRIEND:    return LABEL_MY_BEST_FRIEND;
            case MY_BROTHER:        return LABEL_MY_BROTHER;
            case MY_DAD:            return LABEL_MY_DAD;
            case MY_DARLING:        return LABEL_MY_DARLING;
            case MY_FRIEND:         return LABEL_MY_FRIEND;
            case MY_MUM:            return LABEL_MY_MUM;
            case MY_SISTER:         return LABEL_MY_SISTER;
		}
		//@on
		
		throw new IllegalArgumentException("This is not a standart label id : " + labelId);
	}
	
	/**
	 * Returns a {@link Set} of {@link Label}s for the given IDs.
	 *
	 * @param labelIds the IDs
	 * @return a {@link Set} of {@link Label}s for the given IDs
	 */
	@NotNull
	static Set<Label> getLabels(int @NotNull [] labelIds) {
		
		Set<Label> labels = new HashSet<>(labelIds.length);
		
		for (var id : labelIds) {
			
			Try.ignore(() -> labels.add(getLabel(id)));
		}
		
		return labels;
	}
	
	default long @Nullable [] getLabelIds() {
		
		var labels = getLabels();
		
		if (labels != null) {
			
			var ids = new long[labels.size()];
			
			int i = 0;
			
			for (var label : labels)
				ids[i++] = label.getId();
			
			
			return ids;
		}
		
		return null;
	}
	
}
