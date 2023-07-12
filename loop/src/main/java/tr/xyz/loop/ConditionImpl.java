package tr.xyz.loop;


import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;


public class ConditionImpl<T> implements Condition<T> {
	
	private T                condition;
	private Predicate<T>     predicate;
	private UnaryOperator<T> modifier;
	
	public ConditionImpl() {
		
	}
	
	public ConditionImpl(T condition, Predicate<T> predicate, UnaryOperator<T> modifier) {
		
		this.condition = condition;
		this.predicate = predicate;
		this.modifier  = modifier;
	}
	
	@Override
	public boolean isTrue() {
		
		if (predicate != null)
			return predicate.test(condition);
		return false;
	}
	
	@Override
	public void modify() {
		
		if (modifier != null) condition = modifier.apply(condition);
	}
	
	@Override
	public void modify(@NotNull UnaryOperator<T> modifier) {
		
		condition = modifier.apply(condition);
	}
	
	@Override
	public @NotNull Condition<T> condition(@NotNull T condition) {
		
		this.condition = condition;
		return this;
	}
	
	@Override
	public @NotNull Condition<T> predicate(@NotNull Predicate<T> predicate) {
		
		this.predicate = predicate;
		return this;
	}
	
	@Override
	public @NotNull Condition<T> modifier(@NotNull UnaryOperator<T> modifier) {
		
		this.modifier = modifier;
		return this;
	}
	
	@Override
	public T getCondition() {
		
		return condition;
	}
}
