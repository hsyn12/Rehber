package tr.xyz.loop;


public class ConditionImpl implements Condition {
	
	private boolean condition = true;
	
	@Override
	public boolean getCondition() {
		
		return condition;
	}
	
	@Override
	public void setCondition(boolean condition) {
		
		this.condition = condition;
	}
}
