package com.tr.hsyn.execution;


public interface WorkType<T> {

	/**
	 * İşe öncelik tanımlar. İki öncelik vardır, biri
	 * normal öncelik, diğeri düşük öncelik.
	 * İşler vasayılan olarak normal öncelikte çalışır.
	 * Bu metot ile bunu değiştirebilirsin
	 *
	 * @param minPriority Öncelik düşük mü olsun?
	 * @return Work
	 */
	Work<T> priority(boolean minPriority);

	/**
	 * Çalıştırılacak iş için gecikme süresi tanımlar.
	 *
	 * @param delay Süre
	 * @return Work
	 */
	Work<T> delayOnWork(long delay);

	/**
	 * En son yapılacak olan iş için geçikme süresini ayrıca vermeyi sağlar.
	 * Ana thread üzerinde.
	 *
	 * @param delay Gecikme süresi
	 * @return Work
	 */
	Work<T> delayOnLast(long delay);
}
