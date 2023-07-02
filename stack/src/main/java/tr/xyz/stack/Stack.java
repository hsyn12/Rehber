package tr.xyz.stack;


import tr.xyz.stack.bool.BoolStack;
import tr.xyz.stack.bool.StackBool;


public interface Stack {
	
	static BoolStack newBoolStack(int size) {
		
		return new StackBool(size);
	}
}
