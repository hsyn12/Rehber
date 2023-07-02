package tr.xyz.stack.bool;


public class StackBool implements BoolStack {
	
	private final int       size;
	private final boolean[] stack;
	private       int       index;
	
	public StackBool(int size) {
		
		this.size = size;
		stack     = new boolean[size];
	}
	
	@Override
	public BoolStack push(boolean value) {
		
		stack[index++] = value;
		return this;
	}
	
	@Override
	public boolean pop() {
		
		return stack[--index];
	}
	
	@Override
	public boolean isEmpty() {
		
		return index == 0;
	}
	
	@Override
	public boolean isFull() {
		
		return index == size;
	}
	
	@Override
	public int size() {
		
		return index;
	}
	
	@Override
	public void clear() {
		
		index = 0;
	}
}