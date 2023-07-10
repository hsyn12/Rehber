package com.tr.hsyn.collection;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
 * Provides helper methods for lists.
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public final class Lister {
	
	/**
	 * Checks if the given list has the given value.
	 *
	 * @param list  list
	 * @param value value
	 * @param <T>   type of the list element
	 * @return {@code true} if the given list contains the given value
	 */
	public static <T> boolean contains(Iterable<T> list, T value) {
		
		if (list == null) return false;
		
		for (T t : list)
			if (t.equals(value)) return true;
		
		return false;
	}
	
	/**
	 * Creates a new list from the given list.
	 *
	 * @param list list
	 * @param <T>  type of the list element
	 * @return new list
	 */
	@NotNull
	public static <T> List<T> listOf(List<? extends T> list) {
		
		if (list != null) return new ArrayList<>(list);
		
		return new ArrayList<>(0);
	}
	
	/**
	 * Checks if the given lists are equal.
	 *
	 * @param i1  list
	 * @param i2  list
	 * @param <T> type of the list element
	 * @return {@code true} if the given lists are equal. No matter the order of the elements.
	 */
	public static <T> boolean equals(Iterable<? extends T> i1, Iterable<? extends T> i2) {
		
		if (i1 == null && i2 == null) return true;
		
		if (i1 == null || i2 == null) return false;
		
		@NotNull Set<? extends T> set1  = setOf(i1);
		@NotNull Set<? extends T> set2  = setOf(i2);
		int                       match = 0;
		int                       size1 = set1.size(), size2 = set2.size();
		
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
	
	/**
	 * Creates a new {@link Set} from the given list.
	 *
	 * @param iterable list
	 * @param <T>      type of the list element
	 * @return {@link Set}
	 */
	@NotNull
	public static <T> Set<T> setOf(@NotNull Iterable<? extends T> iterable) {
		
		Set<T> l = new HashSet<>();
		
		for (T e : iterable) l.add(e);
		
		return l;
	}
	
	/**
	 * Creates a new {@link LinkedList} from the given elements.
	 *
	 * @param t   elements
	 * @param <T> type of the element
	 * @return {@link LinkedList}
	 */
	@NotNull
	@SafeVarargs
	public static <T> LinkedList<T> linkOf(@NotNull T @NotNull ... t) {
		
		if (t.length == 0) return new LinkedList<>();
		
		return new LinkedList<>(Arrays.asList(t));
	}
	
	/**
	 * Creates a new array from the given elements.
	 *
	 * @param t   elements
	 * @param <T> type of the list element
	 * @return array of the element
	 */
	@NotNull
	@SuppressWarnings("unchecked")
	public static <T> T[] arrayOf(@NotNull T @NotNull ... t) {
		
		return t;
	}
	
	/**
	 * Creates a new array from the given list.
	 *
	 * @param iterable list
	 * @return array
	 */
	public static int @NotNull [] toIntArray(@NotNull Iterable<Integer> iterable) {
		
		int   size = getSize(iterable);
		int[] ar   = new int[size];
		
		for (int i = 0; i < size; i++) ar[i] = iterable.iterator().next();
		
		return ar;
	}
	
	/**
	 * Returns the size of the given list.
	 *
	 * @param iterable list
	 * @param <T>      type of the list element
	 * @return size
	 */
	public static <T> int getSize(@NotNull Iterable<T> iterable) {
		
		int size = 0;
		
		for (T ignored : iterable) size++;
		
		return size;
	}
	
	/**
	 * Checks if the given index is valid.
	 *
	 * @param list  list
	 * @param index index
	 * @param <T>   type of the list element
	 * @return {@code true} if the given index is valid
	 */
	public static <T> boolean isValidIndex(Collection<T> list, int index) {
		
		return index >= 0 && index < list.size();
	}
	
	/**
	 * A defense B (A–B)
	 *
	 * @param A   A
	 * @param B   B
	 * @param <T> Type
	 * @return A difference B
	 */
	@NotNull
	public static <T> List<T> difference(@NotNull Iterable<T> A, @NotNull Iterable<T> B) {
		
		@NotNull List<T> s1 = listOf(A);
		@NotNull List<T> s2 = listOf(B);
		
		if (s1.isEmpty()) return new ArrayList<>(0);
		if (s2.isEmpty()) return s1;
		
		List<T> difference = new ArrayList<>();
		
		for (T t : s1)
			if (!s2.contains(t)) difference.add(t);
		
		return difference;
	}
	
	/**
	 * Creates a new list from the given list.
	 *
	 * @param iterable list
	 * @param <T>      type of the list element
	 * @return new list
	 */
	@NotNull
	public static <T> List<T> listOf(@NotNull Iterable<? extends T> iterable) {
		
		List<T> l = new ArrayList<>();
		
		for (T e : iterable) l.add(e);
		
		return l;
	}
	
	/**
	 * Creates a loop with the given list and uses each item through the given consumer.
	 *
	 * @param list     list
	 * @param consumer loop consumer
	 */
	public static void loopWith(int[] list, @NotNull IntConsumer consumer) {
		
		for (int i = 0; i < list.length; i++) consumer.accept(list[i]);
	}
	
	/**
	 * Creates a loop with the given list and uses each item through the given consumer.
	 *
	 * @param list     list
	 * @param consumer loop consumer
	 * @param <T>      element type
	 */
	public static <T> void loopWith(T @NotNull [] list, @NotNull Consumer<T> consumer) {
		
		for (int i = 0; i < list.length; i++) consumer.accept(list[i]);
	}
	
	/**
	 * Removes the given items from the given list.
	 *
	 * @param mainList list
	 * @param toRemove list to remove
	 * @param <T>      element type
	 * @return number of items removed
	 */
	public static <T> int removeItems(@NotNull List<? super T> mainList, @NotNull Iterable<T> toRemove) {
		
		AtomicInteger counter = new AtomicInteger();
		
		loopWith(toRemove, item -> {
			
			int index = indexOf(mainList, item);
			
			if (index != -1) {
				
				mainList.remove(index);
				counter.getAndIncrement();
			}
		});
		
		return counter.get();
	}
	
	/**
	 * Creates a loop with the given list and uses each item through the given consumer.
	 *
	 * @param list     list
	 * @param consumer loop consumer
	 * @param <T>      element type
	 */
	public static <T> void loopWith(@NotNull Iterable<? extends T> list, @NotNull Consumer<T> consumer) {
		
		for (T i : list) consumer.accept(i);
	}
	
	/**
	 * Returns the index of the given item in the given list.
	 *
	 * @param list         list
	 * @param itemToSearch item to search
	 * @param <T>          element type
	 * @return index or -1
	 */
	public static <T> int indexOf(@NotNull List<T> list, @NotNull T itemToSearch) {
		
		for (int i = 0; i < list.size(); i++)
			if (itemToSearch.equals(list.get(i)))
				return i;
		
		return -1;
	}
	
	/**
	 * Apply the given function to each item in the given list.
	 *
	 * @param list    list
	 * @param applier function
	 * @param <X>     return type
	 * @param <Y>     element type
	 * @return new list
	 */
	@NotNull
	public static <X, Y> List<X> map(@NotNull Collection<? extends Y> list, @NotNull Function<Y, ? extends X> applier) {
		
		return list.stream().map(applier).collect(Collectors.toList());
	}
	
	/**
	 * Apply the given function to each item in the given list.
	 *
	 * @param list    list
	 * @param applier function
	 * @param <Y>     argument type
	 * @param <X>     return type
	 * @return new list
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
	 * @param <T>  type of the list element
	 * @return {@code true} if the given list is greater than the given size
	 */
	public static <T> boolean gtSize(@NotNull List<T> list, int size) {
		
		return list.size() > size;
	}
	
	/**
	 * Less than?
	 *
	 * @param list List
	 * @param size Size
	 * @param <T>  type of the list element
	 * @return {@code true} if the given list is less than the given size
	 */
	public static <T> boolean ltSize(@NotNull List<T> list, int size) {
		
		return list.size() < size;
	}
	
	/**
	 * Equal?
	 *
	 * @param list List
	 * @param size Size
	 * @param <T>  type of the list element
	 * @return {@code true} if the given list size is equal to the given size.
	 */
	public static <T> boolean eqSize(@NotNull List<T> list, int size) {
		
		return list.size() == size;
	}
	
	@SuppressWarnings("UseOfSystemOutOrSystemErr")
	public static void main(String... args) {
		
		var list = listOf(1, 2, 3);
		System.out.println(list);
		loopWith(list.size(), list::add);
		System.out.println(list);
	}
	
	/**
	 * Creates a new list from the given elements.
	 *
	 * @param t   elements
	 * @param <T> element type
	 * @return new list
	 */
	@SafeVarargs
	@NotNull
	public static <T> List<T> listOf(@NotNull T... t) {
		
		return new ArrayList<>(Arrays.asList(t));
	}
	
	/**
	 * Groups the given list by the given function.<br><br>
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
	 * @param list      list
	 * @param keyMapper function
	 * @param <T>       element type
	 * @param <R>       return type
	 * @return group
	 */
	public static <T, R> @NotNull Map<R, List<T>> group(@NotNull List<? extends T> list, @NotNull Function<? super T, ? extends R> keyMapper) {
		
		Map<R, List<T>> groups = new HashMap<>();
		
		loopWith(list.size(), i -> {
			
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
	 * Creates a loop with the given counter and executes the given runnable in each cycle.
	 * The loop starts at 0, goes to {@code loop}.<br><br>
	 *
	 * <pre>
	 * var list = listOf(1, 2, 3);
	 * System.out.println(list); // [1, 2, 3]
	 * loopWithout(list.size(), () -> list.add(0));
	 * System.out.println(list); // [1, 2, 3, 0, 0, 0]
	 * </pre>
	 *
	 * @param loop     loop counter
	 * @param runnable loop runnable
	 */
	public static void loopWithout(int loop, @NotNull Runnable runnable) {
		
		for (int i = 0; i < loop; i++) runnable.run();
	}
	
	/**
	 * Creates a loop with the given size and uses the size through the given consumer.
	 * If not used the size use {@link #loopWithout(int, Runnable)} instead.<br><br>
	 *
	 * <pre>
	 * var list = listOf(1, 2, 3);
	 * System.out.println(list); // [1, 2, 3]
	 * loopWith(list.size(), list::add);
	 * System.out.println(list); // [1, 2, 3, 0, 1, 2]
	 * </pre>
	 *
	 * @param loop     loop count
	 * @param consumer consumer
	 */
	public static void loopWith(int loop, @NotNull IntConsumer consumer) {
		
		for (int i = 0; i < loop; i++) consumer.accept(i);
	}
	
	/**
	 * @param list list
	 * @return {@code true} if {@code list} is not {@code null} and not empty, {@code false} otherwise
	 */
	public static boolean exist(@Nullable List<?> list) {
		
		return list != null && !list.isEmpty();
	}
	
	/**
	 * Returns the count of the given item in the given list.
	 *
	 * @param list list
	 * @param item item
	 * @param <T>  element type
	 * @return count
	 */
	public static <T> long count(@NotNull Iterable<T> list, T item) {
		
		long count = 0L;
		
		for (T t : list)
			if (t.equals(item)) count++;
		
		return count;
	}
	
	/**
	 * Counts an item in a list.
	 *
	 * @param list   list
	 * @param item   item
	 * @param mapper function to extract the specific information from the item to count its equality.
	 *               If this function is {@code null} the items are compared by their equality.
	 * @param <T>    element type
	 * @param <R>    return type
	 * @return count
	 */
	public static <T, R> long count(@NotNull Iterable<T> list, @NotNull T item, @Nullable Function<T, R> mapper) {
		
		if (mapper == null) return count(list, item);
		
		long count = 0L;
		R    r     = mapper.apply(item);
		
		for (T t : list)
			if (mapper.apply(t).equals(r)) count++;
		
		return count;
	}
	
	/**
	 * Removes the given item at the given index.
	 * This action never throws an {@link IndexOutOfBoundsException}.
	 *
	 * @param list  list
	 * @param index index
	 * @param <T>   element type
	 * @return removed item or {@code null}
	 */
	@Nullable
	public static <T> T remove(List<T> list, int index) {
		
		if (isValidIndex(list, index)) return list.remove(index);
		
		return null;
	}
	
	public interface IntArray {
		
		static boolean contains(int @NotNull [] array, int value) {
			
			for (int i : array)
				if (i == value) return true;
			
			return false;
		}
	}
	
	
}
