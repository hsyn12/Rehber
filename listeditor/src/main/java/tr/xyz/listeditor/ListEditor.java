package tr.xyz.listeditor;


import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * The editor and owner of a list.
 * The encapsulation of the list.
 *
 * @param <T> the type of the list element
 */
public interface ListEditor<T> {
	
	/**
	 * Returns the list.
	 *
	 * @return the list
	 */
	@NotNull List<@NotNull T> getList();
	
	/**
	 * Sets the list.
	 *
	 * @param list the list
	 */
	void setList(List<@NotNull T> list);
	
	/**
	 * Returns the size of the list.
	 *
	 * @return the size
	 */
	int size();
	
	/**
	 * Returns the element at the given index.
	 *
	 * @param index the index
	 * @return the element
	 */
	T get(int index);
	
	/**
	 * Removes the element at the given index.
	 *
	 * @param index the index
	 * @return the removed element
	 */
	T remove(int index);
	
	/**
	 * Adds an element at the given index.
	 *
	 * @param index the index
	 * @param item  the item
	 */
	void add(int index, @NotNull T item);
	
	/**
	 * Adds the given item.
	 *
	 * @param item the item
	 * @return {@code true} if this list changed as a result of the call
	 */
	boolean add(@NotNull T item);
	
	/**
	 * Removes the given item.
	 *
	 * @param item the item
	 * @return {@code true} if this list contained the specified element
	 */
	boolean remove(@NotNull T item);
	
	/**
	 * Clears the list.
	 */
	void clear();
	
	/**
	 * Adds all the elements of the given list to the list.
	 *
	 * @param list the list
	 * @return {@code true} if this list changed as a result of the call
	 */
	boolean addAll(@NotNull List<@NotNull T> list);
	
	/**
	 * Removes all the elements of the given list from the list.
	 *
	 * @param list the list
	 * @return {@code true} if this list changed as a result of the call
	 */
	boolean removeAll(@NotNull List<@NotNull T> list);
	
	/**
	 * Returns a new {@link ListEditor} instance.
	 *
	 * @param list the list
	 * @param <T>  the type of the list element
	 * @return the editor
	 */
	static <T> @NotNull ListEditor<T> from(List<@NotNull T> list) {
		
		return new ListEditorImpl<>(list);
	}
}