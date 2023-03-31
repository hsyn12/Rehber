package com.tr.hsyn.telefonrehberi.code.call;


import com.tr.hsyn.calldata.Call;
import com.tr.hsyn.collection.Lister;
import com.tr.hsyn.phone_numbers.PhoneNumbers;
import com.tr.hsyn.telefonrehberi.main.code.call.cast.Group;
import com.tr.hsyn.telefonrehberi.main.dev.Over;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CallOver {
	
	/**
	 * Bir listeyi, liste elemanlarının belirli bir özelliğine göre gruplar.
	 *
	 * @param list       Liste
	 * @param groupMaker Gruplama kriteri
	 * @param <X>        Verilen listenin eleman türü
	 * @param <Y>        Gruplama için uygulanan fonksiyonun döndürdüğü nesne türü
	 * @return Grup listesi
	 * @see Group
	 */
	@NotNull
	public static <X, Y> List<Group<X>> group(@NotNull List<X> list, @NotNull Function<X, Y> groupMaker) {
		
		Map<Y, List<X>> map    = Lister.group(list, groupMaker);
		List<Group<X>>  groups = new ArrayList<>();
		
		Lister.loop(map.keySet(), key -> groups.add(new Group<>(map.get(key))));
		
		return groups;
	}
	
	/**
	 * Tüm arama kayıtlarını telefon numarasına göre gruplar.
	 * Grubun yapısı {@code Telefon numarası --> Numaraya ait kayıtlar}
	 *
	 * @return Gruplanmış arama kayıtları listesi
	 */
	@NotNull
	public static List<Group<Call>> groupByNumber() {
		
		var calls = Over.CallLog.Calls.getCalls();
		
		if (calls != null)
			return group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
		
		return new ArrayList<>(0);
	}
	
	
	/**
	 * Tüm arama kayıtlarını telefon numarasına göre gruplar.
	 * Grubun yapısı {@code Telefon numarası --> Numaraya ait kayıtlar}
	 *
	 * @param comparator Sıralama ölçütü
	 * @return Gruplanmış arama kayıtları listesi
	 */
	@NotNull
	public static List<Group<Call>> groupByNumber(@NotNull Comparator<Group<Call>> comparator) {
		
		var calls = Over.CallLog.Calls.getCalls();
		
		if (calls != null) {
			
			var list = group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
			
			list.sort(comparator);
			return list;
		}
		
		return new ArrayList<>();
	}
	
	
	/**
	 * Arama kayıtlarını telefon numarasına göre gruplar.
	 * Grubun yapısı {@code Telefon numarası --> Numaraya ait kayıtlar}
	 *
	 * @param calls Arama kayıtları
	 * @return Gruplanmış arama kayıtları listesi
	 */
	@NotNull
	public static List<Group<Call>> groupByNumber(@NotNull List<Call> calls) {
		
		return group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
	}
	
	/**
	 * Arama kayıtlarını telefon numarasına göre gruplar.
	 * Grubun yapısı {@code Telefon numarası --> Numaraya ait kayıtlar}
	 *
	 * @param calls      Arama kayıtları
	 * @param comparator Sıralama nesnesi
	 * @return Gruplanmış arama kayıtları listesi
	 */
	@NotNull
	public static List<Group<Call>> groupByNumber(@NotNull List<Call> calls, @NotNull Comparator<Group<Call>> comparator) {
		
		var list = group(calls, c -> PhoneNumbers.formatNumber(c.getNumber(), 10));
		
		list.sort(comparator);
		return list;
	}
	
	/**
	 * Tüm arama kayıtları içinden belirli telefon numaralarına ait arama kayıtlarını döndürür.
	 *
	 * @param numbers Telefon numaraları
	 * @return Arama kayıtları
	 */
	@NotNull
	public static List<Call> getCallsByNumber(@NotNull List<String> numbers) {
		
		var calls = Over.CallLog.Calls.getCalls();
		
		if (calls != null)
			return calls
					.stream()
					.filter(call -> PhoneNumbers.containsNumber(numbers, call.getNumber()))
					.collect(Collectors.toList());
		else return new ArrayList<>(0);
	}
	
	/**
	 * Gruplara sıralama puanı atar.
	 * Bu çağrıdan önce, ne tür bir sıralama yapılacaksa listenin ona göre sıralanmış olması gerekir.
	 *
	 * @param groups     Liste
	 * @param comparator Sıralayıcı
	 */
	public static void rankBy(@NotNull List<Group<Call>> groups, @NotNull Comparator<Group<Call>> comparator) {
		
		int rank = 1;
		
		for (int i = 0; i < groups.size(); i++) {
			
			var g = groups.get(i);
			
			g.setRank(rank);
			
			if (i == groups.size() - 1) break;
			
			if (comparator.compare(g, groups.get(i + 1)) < 0) rank++;
			//if (g.size() > groups.get(i + 1).size()) rank++;
		}
	}
	
	/**
	 * Liste elemanlarına {@code size} değerine göre sıralama puanı verir.
	 * Bu çağrıdan önce listenin {@code size} büyüklüğüne göre sıralanmış olması gerekir.
	 *
	 * @param groups Liste
	 */
	public static void rankBySize(@NotNull List<Group<Call>> groups) {
		
		rankBy(groups, Comparator.comparingInt(Group::size));
	}
	
	/**
	 * Liste elemanlarına {@code extra} değerine göre sıralama puanı verir.
	 * Bu çağrıdan önce listenin {@code extra} büyüklüğüne göre sıralanmış olması gerekir.
	 *
	 * @param groups Liste
	 */
	public static void rankByExtra(@NotNull List<Group<Call>> groups) {
		
		rankBy(groups, Comparator.comparingInt(Group::getExtra));
	}
	
	/**
	 * Grupları {@code size} büyüklüğüne göre, büyükten küçüğe sıralar.
	 *
	 * @param groups Grup listesi
	 */
	public static void sortBySize(@NotNull List<Group<Call>> groups) {
		
		sortBy(groups, (x, y) -> Integer.compare(y.size(), x.size()));
	}
	
	/**
	 * Grupları {@code extra} büyüklüğüne göre, büyükten küçüğe sıralar.
	 *
	 * @param groups Grup listesi
	 */
	public static void sortByExtra(@NotNull List<Group<Call>> groups) {
		
		sortBy(groups, (x, y) -> Integer.compare(y.getExtra(), x.getExtra()));
	}
	
	/**
	 * Grupları {@code rank} büyüklüğüne göre, büyükten küçüğe sıralar.
	 *
	 * @param groups Grup listesi
	 */
	public static void sortByRank(@NotNull List<Group<Call>> groups) {
		
		sortBy(groups, (x, y) -> Integer.compare(y.getRank(), x.getRank()));
	}
	
	/**
	 * Grupları bir sıralayıcı ile sıralar.
	 *
	 * @param groups     Grup listesi
	 * @param comparator Sıralayıcı
	 */
	public static void sortBy(@NotNull List<Group<Call>> groups, @NotNull Comparator<Group<Call>> comparator) {
		
		groups.sort(comparator);
	}
	
	/**
	 * Listeyi sıralama nesnesine göre sıralar ve her elemana sıralama puanı atar.
	 *
	 * @param groups     Liste
	 * @param comparator Sıralayıcı
	 */
	public static void makeBy(@NotNull List<Group<Call>> groups, @NotNull Comparator<Group<Call>> comparator) {
		
		sortBy(groups, comparator);
		rankBy(groups, comparator);
	}
	
	/**
	 * Listeyi {@code size} değerine göre sıralar ve her elemana sıralama puanı atar.
	 *
	 * @param groups Liste
	 */
	public static void makeBySize(@NotNull List<Group<Call>> groups) {
		
		sortBySize(groups);
		rankBySize(groups);
	}
	
	/**
	 * Listeyi {@code extra} değerine göre sıralar ve her elemana sıralama puanı atar.
	 *
	 * @param groups Liste
	 */
	public static void makeByExtra(@NotNull List<Group<Call>> groups) {
		
		sortByExtra(groups);
		rankByExtra(groups);
	}
	
	/**
	 * Verilen fonksiyon grup elemanlarına uygulanır ve dönen değerler toplanarak grubun {@code extra} değerine atanır.
	 *
	 * @param groups Liste
	 * @param mapper Değer fonksiyonu
	 */
	public static void accumulateBy(@NotNull List<Group<Call>> groups, @NotNull Function<Call, Integer> mapper) {
		
		Lister.loop(groups.size(), x -> {
			
			var g = groups.get(x);
			
			int duration = g.getList().stream().map(mapper).reduce(0, Integer::sum);
			
			g.setExtra(duration);
		});
	}
	
	/**
	 * Listedeki gruplara {@link Call#getDuration()} metodu uygulanır ve dönen değerler toplanarak grubun {@code extra} değerine atanır.
	 *
	 * @param groups Liste
	 */
	public static void accumulateByDuration(@NotNull List<Group<Call>> groups) {
		
		accumulateBy(groups, Call::getDuration);
	}
}
