package com.tr.hsyn.telefonrehberi.main

import tr.xyz.klog.debug

class KBlack : Black() {
	
	override fun onCreate() {
		super.onCreate()
		/* 		val main = onWorker {
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
				} */
		
		
	}
	
	private fun test() {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
	}
}