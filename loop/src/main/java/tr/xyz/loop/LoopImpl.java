package tr.xyz.loop;


public class LoopImpl<T> implements Loop<T> {
	
	private final Condition<T> condition;
	
	public LoopImpl(Condition<T> condition) {
		
		this.condition = condition;
	}
	
	@Override
	public Condition<T> getCondition() {
		
		return condition;
	}
}
