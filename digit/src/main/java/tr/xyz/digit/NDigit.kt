package tr.xyz.digit

import kotlin.math.absoluteValue

class NDigit internal constructor(override val max: Int = Int.MAX_VALUE, override val min: Int = 0, digitValue: Int = 0) :
	Digit {
	
	override val range = (max - min).absoluteValue + 1
	override var left: Digit? = null
	override var right: Digit? = null
	override var cycle = 0
		private set(value) {
			field = value
			if (value > 0) left?.onCycle(value)
			else if (value < 0) right?.onCycle(value)
		}
	override var digitValue: Int = 0
		set(value) {
			cycle = 0
			field = if (value in min..max) value
			else {
				if (value > max) {
					cycle = value / range
					if (cycle == 0) cycle = 1
					min + (value % range)
				}
				else { //+ value < min
					cycle = min / range
					if (cycle == 0) cycle = -1
					max - (min % range)
				}
			}
		}
	
	init {
		
		require(max > min) {"Max must be greater than min, but [max : $max, min : $min]"}
		if (digitValue in min..max) this.digitValue = digitValue
		else this.digitValue = min
	}
	
	override fun toString(): String = "$digitValue"
}


