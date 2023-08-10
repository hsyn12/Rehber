package com.tr.hsyn.telefonrehberi.main

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import tr.xyz.klog.debug
import tr.xyz.klog.error
import tr.xyz.koncurrent.newWorker
import tr.xyz.koncurrent.onWorker

class KBlack : Black() {
	
	override fun onCreate() {
		
		super.onCreate()
		
		val main = newWorker("TestingWorker") {
			
			try {
				test()
				delay(10000L)
				// if (true) throw CancellationException("This is a fucking exception")
				test()
			}
			catch (e: CancellationException) {
				e.error
				throw e
			}
		}
		
		onWorker {
			delay(8000L)
			
			main.cancel(CancellationException("I cancelled"))
			main.join()
			"The end".debug
		}
		
		
	}
	
	private fun test() {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
	}
	
	private fun tester(): Int {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
		return 99
	}
}