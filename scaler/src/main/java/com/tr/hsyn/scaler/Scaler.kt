package com.tr.hsyn.scaler

/**
 * > ## Ölçekleyici. ##
 * ===================
 *
 * > Bir şeyin miktar olarak az mı çok mu olduğunu nasıl
 * > anlarız?
 *
 * > 10 adet, bir şeyin çok __az__ olduğu anlamına gelebilirken,
 * > aynı adet başka bir şey için __çok__ olduğu anlamına
 * > gelebilir.
 *
 * > Yani ölçüm, ölçtüğümüz şeye göre değişir. Mesela bir
 * > kilo pamuk için **çok** diyebiliriz ama bir kilo demir
 * > **az** olabilir. Bu sebeple ölçülen şeye göre farklı bir
 * > ***ölçek*** baz alırız ve buna göre bir şeyin az yada çok
 * > olduğuna karar veririz.
 *
 * > [Scaler] arayüzü bu ölçeklemeyi yapmayı sağlar. Yani bir
 * > miktarın az mı çok mu olduğunu anlamımızı sağlar. Bunun için
 * > 4 çokluk belirlendi.
 *
 * 1. [MIN] çok az
 *
 * 2. [MID] normal
 *
 * 3. [MAX] çok
 *
 * 4. [LARGE] aşırı
 *
 * > [getQuantity] metodu bu 4 değişmezden birini döndürür.
 * Ve diğer metotlar bu değişmezleri sorgulayarak ne olduğunu anlamamızı sağlar.
 *
 *    val scaler = new Scaler(10, 2f);
 *    //bu nesne için minimum değer 10 ve aşağısıdır. (..., 10]
 *    //orta değer (10, 10 * scale] aralığıdır. (10, 20] on ve yirmi arası
 *    //yüksek değer (orta, 10 * scale * 2] aralığıdır. (20, 40] yirmi ve kırk arası
 *    //en yüksek değer (yüksek, 10 * scale * 3] aralığıdır. (40, ...) kırk ve üzeri
 *
 * @author hsyn 23.08.2022 18:14:23
 */
interface Scaler {
	
	/**
	 * Verilen değerin miktarını döndürür.
	 *
	 * @param size Miktarı öğrenilmek istenen değer
	 * @return miktar
	 * @see [MIN]
	 * @see [MID]
	 * @see [MAX]
	 * @see [LARGE]
	 */
	fun getQuantity(size: Int): Int
	
	/**
	 * @param quantity Miktar. [getQuantity] metodundan dönen değer
	 *     olmalı.
	 * @return
	 */
	fun isMin(quantity: Int): Boolean = quantity == MIN
	
	/**
	 * @param quantity Miktar. [getQuantity] metodundan dönen değer
	 *     olmalı.
	 * @return
	 */
	fun isMid(quantity: Int): Boolean = quantity == MID
	
	/**
	 * @param quantity Miktar. [getQuantity] metodundan dönen değer
	 *     olmalı.
	 * @return
	 */
	fun isMax(quantity: Int): Boolean = quantity == MAX
	
	/**
	 * @param quantity Miktar. [getQuantity] metodundan dönen değer
	 *     olmalı.
	 * @return
	 */
	fun isLarge(quantity: Int): Boolean = quantity == LARGE
	
	companion object {
		
		/**
		 * ## Yeni bir ölçekleyici oluşturur.
		 *
		 * [base] değeri taban değerdir.
		 * Yani [MIN] değerini belirler. [scale] ise [base] değerinin çarpılacağı katsayıdır.
		 
		 * [MID] için `1 * base * scale`
		 * # -------------------------
		 * [MAX] için `2 * base * scale`
		 * # -------------------------
		 * [LARGE] için `3 * base * scale`
		 * # -------------------------
		 *
		 *
		 *    val scaler = new Scaler(10, 2f);
		 *    //bu nesne için minimum değer 10 ve aşağısıdır. (..., 10]
		 *    //orta değer (10, 10 * scale] aralığıdır. (10, 20]
		 *    //yüksek değer (orta, 10 * scale * 2] aralığıdır. (20, 40]
		 *    //en yüksek değer (yüksek, 10 * scale * 3] aralığıdır. (40, ...)
		 *
		 * @param base Base
		 * @param scale Scale
		 * @return [Scaler]
		 */
		//region static fun createNewScaler(base: Int, scale: Float): Scaler {...}
		@JvmStatic
		fun createNewScaler(base: Int, scale: Float): Scaler {
			
			return Scale(base, scale)
		}
		//endregion
	}
}


private class Scale(val base: Int, val scale: Float) : Scaler {
	
	/**
	 * Verilen çokluğun niceliğini döndürür.
	 *
	 * @param size Niceliği öğrenilmek istenen çokluk
	 * @return [MIN], [MID], [MAX], [LARGE] değerlerinden biri
	 */
	//region override fun getQuantity(size: Int): Int {...}
	override fun getQuantity(size: Int): Int {
		
		if (size <= base) return MIN
		
		val _scale = (scale * base).toInt()
		
		if (size <= _scale) return MID
		return if (size <= _scale * 2) MAX else LARGE
	}
	//endregion
	
	override fun toString(): String = "Scaler{base=$base, scale=$scale}"
}

/**
 * En düşük değer. Buna ***az*** diyebiliriz
 */
const val MIN = 0

/**
 * Orta değer. Buna ***normal*** diyebiliriz
 */
const val MID = 1

/**
 * Maximum değer. Buna ***çok*** diyebiliriz
 */
const val MAX = 2

/**
 * Büyük değer. Buna ***aşırı*** diyebiliriz
 */
const val LARGE = 3