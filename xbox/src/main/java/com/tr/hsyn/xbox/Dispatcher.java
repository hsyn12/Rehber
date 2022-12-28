package com.tr.hsyn.xbox;


public interface Dispatcher {
	
	<O> O dispatch(Data data);
}
