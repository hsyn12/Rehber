package com.tr.hsyn.telefonrehberi.main

import com.tr.hsyn.daytimes.DayTime
import tr.xyz.klog.info

class KBlack : Black() {
	
	override fun onCreate() {
		super.onCreate()
		DayTime.toString(this).info
	}
}