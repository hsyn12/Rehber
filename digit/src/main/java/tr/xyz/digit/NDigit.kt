package tr.xyz.digit

import kotlin.math.absoluteValue

class NDigit internal constructor(override val max: Int = Int.MAX_VALUE, override val min: Int = 0, digitValue: Int = 0) :
	Digit {
	
	override val range = (max - min).absoluteValue
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
			field = if (value in min..<max) value
			else {
				if (value == max) {
					// println("value == max")
					cycle = 1
					min
				}
				else {
					if (value > max) {
						// println("value > max. value = $value, max = $max, range = $range")
						cycle = value / range
						if (cycle == 0) cycle = 1
						min + ((if (value != 0) value else 1) % range)
					}
					else { //+ value < min
						// println("value < min. value = $value, min = $min, range = $range")
						cycle = min / range
						if (cycle == 0) cycle = -1
						max - ((if (min == 0) 1 else min) % range)
					}
				}
			}
		}
	
	init {
		
		require(max > min) {"Max value must be greater than min value, but [max : $max, min : $min]"}
		if (digitValue in min until max) this.digitValue = digitValue
		else this.digitValue = min
	}
	
	override fun toString(): String = "$digitValue"
	
	override fun equals(other: Any?): Boolean = other is Digit && digitValue == other.digitValue
	override fun hashCode(): Int = digitValue
}

fun main() {
	
	val row = Digit.newDigit(0, 10, 0)
	row -= 1
	println("row = $row")
}


