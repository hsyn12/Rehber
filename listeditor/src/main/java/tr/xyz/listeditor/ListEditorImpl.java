package tr.xyz.listeditor;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ListEditorImpl<T> implements ListEditor<T> {
	
	private List<@NotNull T> list;
	
	protected ListEditorImpl() {
		
		list = new ArrayList<>(0);
	}
	
	protected ListEditorImpl(List<@NotNull T> list) {
		
		this.list = list != null ? list : new ArrayList<>(0);
	}
	
	@Override
	public @NotNull List<@NotNull T> getList() {
		
		return list;
	}
	
	@Override
	public void setList(List<@NotNull T> list) {
		
		this.list = list != null ? list : new ArrayList<>(0);
	}
	
	@Override
	public int size() {
		
		return list.size();
	}
	
	@Override
	public T get(int index) {
		
		return list.get(index);
	}
	
	@Override
	public T remove(int index) {
		
		return list.remove(index);
	}
	
	@Override
	public void add(int index, @NotNull T item) {
		
		list.add(index, item);
	}
	
	@Override
	public boolean add(@NotNull T item) {
		
		return list.add(item);
	}
	
	@Override
	public boolean remove(@NotNull T item) {
		
		return list.remove(item);
	}
	
	@Override
	public void clear() {
		
		list.clear();
	}
	
	@Override
	public boolean addAll(@NotNull List<@NotNull T> list) {
		
		return this.list.addAll(list);
	}
	
	@Override
	public boolean removeAll(@NotNull List<@NotNull T> list) {
		
		return this.list.removeAll(list);
	}
}
