package com.tr.hsyn.counter;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;


/**
 * Bir listede, bir elemanın kaç kez geçtiğini hesaplar.
 *
 * @param <T> liste eleman türü
 */
public class Counter<T> {
	
	private final List<T> list;
	
	public Counter(List<T> list) {
		
		this.list = list;
	}
	
	/**
	 * Verilen elemanı listede sayar.
	 *
	 * @param item Sayılacak eleman
	 * @return Elemanın listede geçme sayısı
	 */
	public long count(@NotNull T item) {
		
		long count = 0L;
		
		for (var t : list)
			if (t.equals(item)) count++;
		
		return count;
	}
	
	/**
	 * Verilen elemanı listede sayar.<br>
	 * {@link #count(Object)} metodundan farkı,
	 * sayılacak elemanın belirli bir özelliğini baz alması.
	 * Yani elemanlar karşılaştırılırken önce fonksiyondan geçirilir ve
	 * dönen nesne karşılaştırılır, eşit olanlar sayılır.
	 *
	 * @param item   sayılacak eleman
	 * @param mapper saymak için kriter
	 * @param <S>    saymak için karşılaştırılacak nesnenin türü
	 * @return verilen fonksiyonun döndürdüğü nesnenin listedeki sayısı
	 */
	public <S> long count(@NotNull T item, @NotNull Function<T, S> mapper) {
		
		long count = 0L;
		S    s     = mapper.apply(item);
		
		for (var t : list)
			if (mapper.apply(t).equals(s)) count++;
		
		return count;
	}
	
	
}
