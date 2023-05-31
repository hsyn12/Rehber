package com.tr.hsyn.nextension;


/**
 * Ek türlerini bildirir.<br>
 *
 * <ul>
 *    <li>{@link #TYPE_AT} : -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)</li>
 *    <li>{@link #TYPE_FROM} : -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)</li>
 *    <li>{@link #TYPE_TO} : -ye -ya -e -a (Yönelme Hali) (Datif - Kime, Nereye, Neye)</li>
 *    <li>{@link #TYPE_IN_TO} : -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)</li>
 *    <li>{@link #TYPE_POSS} : -in -ün -nin -nün -un -ın (İyelik Eki)</li>
 * </ul>
 */
public interface Extension {
	
	/**
	 * -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)
	 */
	int TYPE_AT       = 1000;
	/**
	 * -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)
	 */
	int TYPE_FROM     = 1001;
	/**
	 * -ye -ya -e -a (Yönelme Hali) (Datif - Kime, Nereye, Neye)
	 */
	int TYPE_TO       = 1002;
	/**
	 * -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)
	 */
	int TYPE_IN_TO    = 1003;
	/**
	 * -in -ün -nin -nün -un -ın (İyelik Eki)
	 */
	int TYPE_POSS     = 1004;
	/**
	 * -lık -lik -luk -lük
	 */
	int TYPE_ABSTRACT = 1005;
	
}
