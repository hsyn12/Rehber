package com.tr.hsyn.telefonrehberi.main

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import tr.xyz.klog.debug
import tr.xyz.klog.error
import tr.xyz.koncurrent.onWorker

class KBlack : Black() {
	
	override fun onCreate() {
		super.onCreate()
		
		var one: Job? = null
		
		val main = onWorker {
			
			one = onWorker(6000L) {
				try {
					test()
				} catch (e: Exception) {
					e.error
				}
			}
		}
		
		onWorker {
			onWorker(5000L) {
				main.cancelAndJoin()
				"The end".debug
			}
		}
		
	}
	
	private fun test() {
		
		"This is a test function on [${Thread.currentThread().name}]".debug
	}
}