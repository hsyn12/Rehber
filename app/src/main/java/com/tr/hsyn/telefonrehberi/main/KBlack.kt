package com.tr.hsyn.telefonrehberi.main

import tr.xyz.klog.debug

class KBlack : Black() {
	
	override fun onCreate() {
		
		super.onCreate()
	}
	
	private fun test() {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
	}
	
	private fun tester(): Int {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
		return 99
	}
}