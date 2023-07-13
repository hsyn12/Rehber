package tr.xyz.loop;


import java.util.function.Function;


@SuppressWarnings("StatementWithEmptyBody")
public class While {
	
	private int counter;
	
	public void with(Function<Integer, Boolean> condition) {
		
		while (condition.apply(counter++)) ;
	}
	
	public static While create() {
		
		return new While();
	}
}
