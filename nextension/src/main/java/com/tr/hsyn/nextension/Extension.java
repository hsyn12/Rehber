package com.tr.hsyn.nextension;


/**
 * Ek türlerini bildirir.<br>
 *
 * <ul>
 *    <li>{@link #TYPE_LOCATIVE} : -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)</li>
 *    <li>{@link #TYPE_ABLATIVE} : -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)</li>
 *    <li>{@link #TYPE_DATIVE} : -ye -ya -e -a (Yönelme Hali) (Datif - Kime, Nereye, Neye)</li>
 *    <li>{@link #TYPE_ACCUSATIVE} : -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)</li>
 *    <li>{@link #TYPE_POSSESSIVE} : -in -ün -nin -nün -un -ın (İyelik Eki)</li>
 * </ul>
 */
public interface Extension {
	
	/**
	 * -de -da -te -ta (Bulunma Hali) (Lokatif - Nerede, Kimde, Nede)
	 */
	int TYPE_LOCATIVE   = 1000;
	/**
	 * -den -ten -dan -tan (Çıkma Hali) (Ablatif - Nereden, Kimden, Neden)
	 */
	int TYPE_ABLATIVE   = 1001;
	/**
	 * -ye -ya -e -a (Yönelme Hali) (Datif - Kime, Nereye, Neye)
	 */
	int TYPE_DATIVE     = 1002;
	/**
	 * -ü -i -u -ı -yü -yi (Belirtme Hali) (Akuzatif - Neyi, Kimi)
	 */
	int TYPE_ACCUSATIVE = 1003;
	/**
	 * -in -ün -nin -nün -un -ın (İyelik Eki)
	 */
	int TYPE_POSSESSIVE = 1004;
	/**
	 * -lık -lik -luk -lük
	 */
	int TYPE_ABSTRACT   = 1005;
	/**
	 * -ler -lar (Çoğul eki)
	 */
	int TYPE_PLURAL     = 1006;
	
}
