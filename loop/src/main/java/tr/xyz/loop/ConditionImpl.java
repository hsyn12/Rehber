package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.function.Function;


public class ConditionImpl<T> implements Condition<T> {
	
	private T                    condition;
	private Function<T, Boolean> predicate;
	
	public ConditionImpl() {}
	
	public ConditionImpl(T condition, Function<T, Boolean> predicate) {
		
		this.condition = condition;
		this.predicate = predicate;
	}
	
	@Override
	public boolean test() {
		
		return predicate != null && predicate.apply(condition);
	}
	
	@Override
	public @NotNull Condition<T> condition(@NotNull T condition) {
		
		this.condition = condition;
		return this;
	}
	
	@Override
	public @NotNull Condition<T> predicate(@NotNull Function<T, Boolean> predicate) {
		
		this.predicate = predicate;
		return this;
	}
	
	@Override
	public T getCondition() {
		
		return condition;
	}
	
	@Override
	public void setCondition(T condition) {
		
		this.condition = condition;
	}
}
