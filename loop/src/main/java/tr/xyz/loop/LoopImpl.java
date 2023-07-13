package tr.xyz.loop;


public class LoopImpl implements Loop {
	
	private Condition condition;
	private int       cycle;
	
	public LoopImpl() {
		
	}
	
	public LoopImpl(Condition condition) {
		
		this.condition = condition;
	}
	
	@Override
	public int cycle() {
		
		return cycle++;
	}
	
	@Override
	public int getCycle() {
		
		return cycle;
	}
	
	@Override
	public Runnable getRunnable() {
		
		return null;
	}
}
