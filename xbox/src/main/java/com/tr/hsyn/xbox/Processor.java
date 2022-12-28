package com.tr.hsyn.xbox;


public interface Processor {
	
	<I, O> O process(I input);
}
