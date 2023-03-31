package com.tr.hsyn.collection;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;


/**
 * Liste ve benzeri yapılar için işlevler sunar.
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public final class Lister {
	
	/**
	 * Verilen liste elemanlarıyla yeni bir liste oluşturur.
	 *
	 * @param list Liste
	 * @param <T>  Listenin eleman türü
	 * @return Eğer liste {@code null} ise boş liste döner, aksi halde verilen listenin elemanlarından oluşan yeni bir liste döner
	 */
	@NotNull
	public static <T> List<T> listOf(List<? extends T> list) {
		
		if (list != null) return new ArrayList<>(list);
		
		return new ArrayList<>(0);
	}
	
	/**
	 * İki listeyi eşitlik için karşılaştırır.
	 *
	 * @param i1  Liste
	 * @param i2  Liste
	 * @param <T> Eleman türü
	 * @return İki liste, eleman sıralamaları önemli olmaksızın aynı elemanları tutuyorlarsa {@code true}
	 */
	public static <T> boolean equals(Iterable<? extends T> i1, Iterable<? extends T> i2) {
		
		if (i1 == null && i2 == null) return true;
		
		if (i1 == null || i2 == null) return false;
		
		var set1  = setOf(i1);
		var set2  = setOf(i2);
		int match = 0;
		int size1 = set1.size(), size2 = set2.size();
		
		if (size1 == size2) {
			
			SET_1:
			for (T t1 : set1) {
				
				for (T t2 : set2) {
					
					if (t1.equals(t2)) {
						
						match++;
						continue SET_1;
					}
				}
			}
			
			return size1 == match;
		}
		
		return false;
	}
	
	public static <T> int getSize(@NotNull Iterable<T> iterable) {
		
		int size = 0;
		
		for (var ignored : iterable) size++;
		
		return size;
	}
	
	@NotNull
	public static <T> List<T> listOf(@NotNull Iterable<? extends T> iterable) {
		
		List<T> l = new ArrayList<>();
		
		for (var e : iterable) l.add(e);
		
		return l;
	}
	
	/**
	 * Yeni bir liste oluştur.
	 *
	 * @param t   Liste elemanları
	 * @param <T> Eleman türü
	 * @return {@link ArrayList}
	 */
	@SafeVarargs
	@NotNull
	public static <T> List<T> listOf(@NotNull T... t) {
		
		return new ArrayList<>(Arrays.asList(t));
	}
	
	@NotNull
	public static <T> Set<T> setOf(@NotNull Iterable<? extends T> iterable) {
		
		Set<T> l = new HashSet<>();
		
		for (var e : iterable) l.add(e);
		
		return l;
	}
	
	@NotNull
	@SafeVarargs
	public static <T> LinkedList<T> linkOf(@NotNull T @NotNull ... t) {
		
		if (t.length == 0) return new LinkedList<>();
		
		return new LinkedList<>(Arrays.asList(t));
	}
	
	@NotNull
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayOf(@NotNull T @NotNull ... t) {
		
		return t;
	}
	
	/**
	 * Verilen index liste için geçerli mi?
	 *
	 * @param list  Liste
	 * @param index Index
	 * @param <T>   Tür
	 * @return Index geçerli ise {@code true}
	 */
	public static <T> boolean isIndex(Collection<T> list, int index) {
		
		return index >= 0 && index < list.size();
	}
	
	/**
	 * A fark B (A - B)
	 *
	 * @param A   A
	 * @param B   B
	 * @param <T> Type
	 * @return A'nın B'den farkı. Yani A'da olup B'de olmayan elemanlar
	 */
	@NotNull
	public static <T> List<T> difference(@NotNull Iterable<T> A, @NotNull Iterable<T> B) {
		
		var s1 = listOf(A);
		var s2 = listOf(B);
		
		if (s1.isEmpty()) return new ArrayList<>(0);
		if (s2.isEmpty()) return s1;
		
		List<T> difference = new ArrayList<>();
		
		for (var t : s1)
			if (!s2.contains(t)) difference.add(t);
		
		return difference;
	}
	
	/**
	 * Liste üzerinde döngü oluştur.
	 *
	 * @param list     Liste
	 * @param consumer Döngü işleyicisi
	 * @param <T>      Liste elemanı türü
	 */
	public static <T> void loop(@NotNull Iterable<? extends T> list, @NotNull Consumer<T> consumer) {
		
		for (var i : list) consumer.accept(i);
	}
	
	/**
	 * Dizi üzerinde döngü oluşturur.
	 *
	 * @param list     Dizi
	 * @param consumer Döngü işleyicisi
	 */
	public static void loop(int[] list, @NotNull IntConsumer consumer) {
		
		for (int i = 0; i < list.length; i++) consumer.accept(list[i]);
	}
	
	/**
	 * Dizi üzerinde döngü oluşturur.
	 *
	 * @param list     Dizi
	 * @param consumer Döngü işleyicisi
	 */
	public static <T> void loop(T @NotNull [] list, @NotNull Consumer<T> consumer) {
		
		for (int i = 0; i < list.length; i++) consumer.accept(list[i]);
	}
	
	/**
	 * Sıfırdan başlayarak verilen sayıya kadar döngü oluşturur.
	 *
	 * @param loop     Döngü sayısı
	 * @param consumer Döngü işleyicisi
	 */
	public static void loop(int loop, @NotNull IntConsumer consumer) {
		
		for (int i = 0; i < loop; i++) consumer.accept(i);
	}
	
	
	/**
	 * Verilen listedeki tüm elemanları diğer listeden çıkarır.
	 *
	 * @param mainList Elemanların çıkarılacağı ana liste
	 * @param toRemove Çıkarılması istenen elemanların listesi
	 * @param <T>      Elemen türü
	 * @return Çıkarılabilen eleman sayısı. Tüm elemanlar listede bulunup çıkarılırsa {@code toRemove.size()} değerini döner
	 */
	public static <T> int removeItems(@NotNull List<? super T> mainList, @NotNull Iterable<T> toRemove) {
		
		AtomicInteger counter = new AtomicInteger();
		
		loop(toRemove, item -> {
			
			int index = indexOf(mainList, item);
			
			if (index != -1) {
				
				mainList.remove(index);
				counter.getAndIncrement();
			}
		});
		
		return counter.get();
	}
	
	/**
	 * Verilen bir elemanın listedeki index eğerini döndürür.
	 *
	 * @param list         Aramanın yapılacağı liste
	 * @param itemToSearch Aranacak eleman
	 * @param <T>          Eleman türü
	 * @return Elemanın listedeki index değeri, eleman listede yoksa {@code -1}
	 */
	public static <T> int indexOf(@NotNull List<T> list, @NotNull T itemToSearch) {
		
		for (int i = 0; i < list.size(); i++)
			if (itemToSearch.equals(list.get(i)))
				return i;
		
		return -1;
	}
	
	/**
	 * Liste elemanlarını belirtilen fonksiyona göre gruplar.<br><br>
	 *
	 * <pre>{@code
	 * var list = listOf("a", "ab", "abc", "abcd", "e", "ef", "efg", "efgh");
	 * var g = group(list, String::length);
	 * System.out.printf("%s\n", g);
	 * // {1=[a, e], 2=[ab, ef], 3=[abc, efg], 4=[abcd, efgh]}
	 *
	 * System.out.printf("%s\n", group(list, s -> s.charAt(0)));
	 * // {a=[a, ab, abc, abcd], e=[e, ef, efg, efgh]}
	 * }</pre>
	 *
	 * @param list      liste
	 * @param keyMapper gruplama kriteri için fonksiyon
	 * @param <T>       liste eleman türü
	 * @param <R>       gruplama kriterinin türü
	 * @return Verilen kritere göre gruplanmış elemanlardan oluşan bir {@code Map} nesnesi
	 */
	public static <T, R> @NotNull Map<R, List<T>> group(@NotNull List<? extends T> list, @NotNull Function<? super T, ? extends R> keyMapper) {
		
		Map<R, List<T>> groups = new HashMap<>();
		
		loop(list.size(), i -> {
			
			T t = list.get(i);
			R r = keyMapper.apply(t);
			
			List<T> items = groups.get(r);
			
			if (items != null) {
				
				items.add(t);
			}
			else {
				
				items = new ArrayList<>();
				items.add(t);
				groups.put(r, items);
			}
		});
		
		return groups;
	}
	
	/**
	 * Verilen fonksiyonu listeye uygulayarak yeni bir liste döndürür.
	 *
	 * @param list    liste
	 * @param applier listeye uygulanacak fonksiyon
	 * @param <X>     fonksiyonun döndürdüğü nesne türü ve doğal olarak dönecek olan listenin eleman türü
	 * @param <Y>     verilen listenin eleman türü
	 * @return uygulanan fonksiyondan dönen nesnelerin listesi
	 */
	@NotNull
	public static <X, Y> List<X> map(@NotNull Collection<? extends Y> list, @NotNull Function<Y, ? extends X> applier) {
		
		return list.stream().map(applier).collect(Collectors.toList());
	}
	
	/**
	 * Verilen fonksiyonu listeye uygulayarak yeni bir liste döndürür.
	 *
	 * @param list    liste
	 * @param applier listeye uygulanacak fonksiyon
	 * @param <X>     fonksiyonun döndürdüğü nesne türü ve doğal olarak dönecek olan listenin eleman türü
	 * @param <Y>     verilen listenin eleman türü
	 * @return uygulanan fonksiyondan dönen nesnelerin listesi
	 */
	@NotNull
	public static <X, Y> List<X> mapNotNull(@NotNull Collection<? extends Y> list, @NotNull Function<Y, ? extends X> applier) {
		
		return list.stream().map(applier).filter(Objects::nonNull).collect(Collectors.toList());
	}
	
	/**
	 * Greater than?
	 *
	 * @param list List
	 * @param size Size
	 * @param <T>  Liste eleman türü
	 * @return Listenin eleman sayısı belirtilen sayıdan büyükse {@code true}
	 */
	public static <T> boolean gtSize(@NotNull List<T> list, int size) {
		
		return list.size() > size;
	}
	
	/**
	 * Less than?
	 *
	 * @param list List
	 * @param size Size
	 * @param <T>  Liste eleman türü
	 * @return Listenin eleman sayısı belirtilen sayıdan küçükse {@code true}
	 */
	public static <T> boolean ltSize(@NotNull List<T> list, int size) {
		
		return list.size() < size;
	}
	
	/**
	 * Equal?
	 *
	 * @param list List
	 * @param size Size
	 * @param <T>  Liste eleman türü
	 * @return Listenin eleman sayısı belirtilen sayıya eşitse {@code true}
	 */
	public static <T> boolean eqSize(@NotNull List<T> list, int size) {
		
		return list.size() == size;
	}
	
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public static void main(String... args) {
		
		var list = listOf("a", "ab", "abc", "abcd", "e", "ef", "efg", "efgh");
		
		var g = group(list, String::length);
		
		System.out.printf("%s\n", g);
		System.out.printf("%s\n", group(list, s -> s.charAt(0)));
		
	}
	
	
}
