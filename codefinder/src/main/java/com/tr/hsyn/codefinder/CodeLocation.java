package com.tr.hsyn.codefinder;


import org.jetbrains.annotations.NotNull;


public class CodeLocation {
	
	private final String fileName;
	private final String className;
	private final String methodName;
	private final int    lineNumber;
	
	public CodeLocation(String fileName, String className, String methodName, int lineNumber) {
		
		this.fileName   = fileName;
		this.className  = className;
		this.methodName = methodName;
		this.lineNumber = lineNumber;
	}
	
	public String getFileName() {
		
		return fileName;
	}
	
	public String getClassName() {
		
		return className;
	}
	
	public String getMethodName() {
		
		return methodName;
	}
	
	public int getLineNumber() {
		
		return lineNumber;
	}
	
	@NotNull
	@Override
	public String toString() {
		
		return "CodeLocation{" +
		       "fileName='" + fileName + '\'' +
		       ", className='" + className + '\'' +
		       ", methodName='" + methodName + '\'' +
		       ", lineNumber=" + lineNumber +
		       '}';
	}
}
