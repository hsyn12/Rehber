package com.tr.hsyn.scaler

/**
 * > ## Ölçekleyici. ##
 * =====================================================================
 *
 * How do we know if something is more or less in quantity?
 *
 * While 10 can mean something is too __few__,
 * the same quantity can mean __many__ for something else.
 *
 * So the measurement depends on what we are measuring.
 * For example, we can say a kilo of cotton is a lot, but a kilo of iron may be less.
 * For this reason we take a different scale based on what is being measured,
 * and accordingly we decide if something is more or less.
 *
 * The [Scaler] interface enables this scaling.
 * In other words,
 * a [Scaler] allows us to understand whether the amount is more or less.
 * For this, 4 quantities were determined.
 *
 * 1. [Quantity.MIN] few
 *
 * 2. [Quantity.MID] normal
 *
 * 3. [Quantity.MAX] a lot
 *
 * 4. [Quantity.LARGE] excessive
 *
 * [getQuantity] method returns a [Quantity] one of these.
 *
 *    val scaler = new Scaler(10, 2f);
 *    //the minimum value for this object is 10 and below (..., 10]
 *    //the middle value is the range (10, 10 * scale] (10, 20] between ten and twenty
 *    //high value (medium, 10 * scale * 2] range (20, 40] twenty to forty
 *    //the highest value is (high, 10 * scale * 3] range (40, ...) forty and above
 *
 * @author hsyn 23.08.2022 18:14:23
 */
interface Scaler {
	
	/**
	 * Returns the [Quantity] for the [size].
	 *
	 * @param size size to get the [Quantity]
	 * @return the [Quantity] for the [size]
	 * @see [Quantity]
	 */
	fun getQuantity(size: Int): Quantity
	
	companion object {
		
		/**
		 * ## Creates new [Scaler] object.
		 *
		 * [base] value determines the [Quantity#MIN] value.
		 * And [scale] is the value to be multiplied with the [base] value.
		 * # -------------------------
		 * [Quantity.MIN] --> `base`
		 * # -------------------------
		 * [Quantity.MID] --> `1 * base * scale`
		 * # -------------------------
		 *  [Quantity.MAX] --> `2 * base * scale`
		 * # -------------------------
		 *  [Quantity.LARGE] --> `3 * base * scale`
		 * # -------------------------
		 *
		 *
		 *    val scaler = new Scaler(10, 2f);
		 *    //the minimum value for this object is 10 and below (..., 10]
		 *    //the middle value is the range (10, 10 * scale] (10, 20] between ten and twenty
		 *    //high value (medium, 10 * scale * 2] range (20, 40] twenty to forty
		 *    //the highest value is (high, 10 * scale * 3] range (40, ...) forty and above
		 *
		 * @param base Base
		 * @param scale Scale
		 * @return [Scaler]
		 */
		@JvmStatic
		fun createNewScaler(base: Int, scale: Float): Scaler = Scale(base, scale)
		
		/**
		 * Calculates the [Quantity] of the [size] value.
		 *
		 *    val quantity = Scaler.getQuantity(8, 10, 2f);
		 *    //the minimum value for this object is 10 and below (..., 10]
		 *    //the middle value is the range (10, 10 * scale] (10, 20] between ten and twenty
		 *    //high value (medium, 10 * scale * 2] range (20, 40] twenty to forty
		 *    //the highest value is (high, 10 * scale * 3] range (40, ...) forty and above
		 *
		 *    quantity.isMin();// returns true because 8 is less than 10 (base value 10)
		 *
		 * @param size size to calculate
		 * @param base base value
		 * @param scale scale value
		 * @return [Quantity] object
		 */
		//region fun getQuantity(size: Int, base: Int, scale: Float): Quantity {...}
		fun getQuantity(size: Int, base: Int, scale: Float): Quantity {
			
			if (size <= base) return Quantity.MIN
			
			val _scale = (scale * base).toInt()
			
			if (size <= _scale) return Quantity.MID
			return if (size <= _scale * 2) Quantity.MAX else Quantity.LARGE
		}
		//endregion
	}
}


private class Scale(val base: Int, val scale: Float) : Scaler {
	
	/**
	 * Returns the [Quantity] for the [size].
	 *
	 * @param size size to get the [Quantity]
	 * @return [Quantity]
	 */
	override fun getQuantity(size: Int): Quantity = Scaler.getQuantity(size, base, scale)
	
	override fun toString(): String = "Scaler{base=$base, scale=$scale}"
}

enum class Quantity {
	/**
	 * En düşük değer. Buna ***az*** diyebiliriz
	 */
	MIN,
	
	/**
	 * Orta değer. Buna ***normal*** diyebiliriz
	 */
	MID,
	
	/**
	 * Maximum değer. Buna ***çok*** diyebiliriz
	 */
	MAX,
	
	/**
	 * Büyük değer. Buna ***aşırı*** diyebiliriz
	 */
	LARGE;
	
	fun isMin(): Boolean = this == MIN
	fun isMid(): Boolean = this == MID
	fun isMax(): Boolean = this == MAX
	fun isLarge(): Boolean = this == LARGE
}