package tr.xyz.digit

import org.junit.Assert.assertEquals
import org.junit.Test

class NDigitTest {
	
	@Test
	fun testNDigitSetValue() {
		val digitValue = 5
		val max = 5
		val min = 0
		
		val digit = NDigit(max, min, digitValue)
		digit.digitValue = 1
		val expected = min + 1
		assertEquals(expected, digit.digitValue)
	}
	
	@Test
	fun testNDigitPositiveOverflow() {
		
		val digit = Digit.newDigit(0, 5, 5)
		digit += 1
		println("$digit") // 0
		digit -= 1
		val expected = 5
		println("$digit") // 0
		assertEquals(expected, digit.digitValue)
	}
	
	@Test
	fun testNDigitNegativeOverflow() {
		val digitValue = 0
		val max = 5
		val min = 0
		
		val digit = NDigit(max, min, digitValue)
		digit -= 1
		val expected = 5
		assertEquals(expected, digit.digitValue)
	}
	
	@Test
	fun testNDigitTimes() {
		
		val row = Digit.newDigit(min = 0, max = 9, digitValue = 4)
		val col = Digit.newDigit(6)
		val anotherRow = Digit.newDigit(6)
		row.left = anotherRow
		row *= col
		println(row) // 4
		println(anotherRow) // 8
		val expected = 4
		assertEquals(expected, row.digitValue)
	}
}