package com.tr.hsyn.warnerlabel;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
public @interface Remember {
	
	String note() default "";
	
	String time() default "";
	
	String writer() default "";
}
