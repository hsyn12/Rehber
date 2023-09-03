package tr.xyz.klext

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito

class ThenFunctionTest {
	
	@Test
	fun testThenWhenFunctionReturnsKnownValueThenReturnValue() {
		// Arrange
		val expectedValue = "Known Value"
		val functionUnderTest: () -> String = {expectedValue}
		val receiver = Any()
		
		// Act
		val actualValue = receiver then functionUnderTest
		
		// Assert
		assertEquals(expectedValue, actualValue)
	}
	
	@Test
	fun testThenWhenCalledThenFunctionIsInvoked() {
		// Arrange
		val mockFunction = Mockito.mock(Function0::class.java) as Function0<*>
		val receiver = Any()
		
		// Act
		receiver then mockFunction
		
		// Assert
		Mockito.verify(mockFunction).invoke()
	}
}