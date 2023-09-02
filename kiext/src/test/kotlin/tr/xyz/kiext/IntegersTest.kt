package tr.xyz.kiext

import org.junit.Assert.assertEquals
import org.junit.Test

class IfZeroThenTest {
	
	@Test
	fun testIfZeroThenWhenInputIsZeroThenReturnsFunctionReturnValue() {
		// Arrange
		val input = 0
		val expectedOutput = 10
		val function: (Int) -> Int = {expectedOutput}
		
		// Act
		val actualOutput = input.ifZeroThen(function)
		
		// Assert
		assertEquals(expectedOutput, actualOutput)
	}
	
	@Test
	fun testIfZeroThenWhenInputIsNotZeroThenReturnsInputValue() {
		// Arrange
		val input = 5
		val function: (Int) -> Int = {it * 2}
		
		// Act
		val actualOutput = input.ifZeroThen(function)
		
		// Assert
		assertEquals(input, actualOutput)
	}
}

class IfZeroThenTest2 {
	
	@Test
	fun testIfZeroThenWhenReceiverIsZeroThenReturnsThenValue() {
		// Arrange
		val receiver = 0
		val thenValue = 5
		
		// Act
		val result = receiver.ifZeroThen(thenValue)
		
		// Assert
		assertEquals(thenValue, result)
	}
	
	@Test
	fun testIfZeroThenWhenReceiverIsNotZeroThenReturnsReceiver() {
		// Arrange
		val receiver = 3
		val thenValue = 5
		
		// Act
		val result = receiver.ifZeroThen(thenValue)
		
		// Assert
		assertEquals(receiver, result)
	}
}