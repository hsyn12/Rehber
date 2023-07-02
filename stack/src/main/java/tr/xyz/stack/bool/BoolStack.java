package tr.xyz.stack.bool;


public interface BoolStack {
	
	BoolStack push(boolean value);
	
	boolean pop();
	
	boolean isEmpty();
	
	boolean isFull();
	
	int size();
	
	void clear();
}
