package com.tr.hsyn.nextension;


import com.tr.hsyn.nextension.extension.AblativeNumber;
import com.tr.hsyn.nextension.extension.AccusativeNumber;
import com.tr.hsyn.nextension.extension.DativeNumber;
import com.tr.hsyn.nextension.extension.DayNumber;
import com.tr.hsyn.nextension.extension.LocativeNumber;
import com.tr.hsyn.nextension.extension.PossessiveNumber;

import org.jetbrains.annotations.NotNull;


/**
 * Sayılara ek bulan sınıfları tanımlar ve bu iş için gerekli bazı yardımcı metotlar sağlar.
 */
public interface NumberExtention {
	
	/**
	 * Gün bildiren ek türü (1'i, 2'si, 24'i, 29'u vb)
	 */
	int TYPE_DAY = 3000;
	
	/**
	 * Sayının sonuna eklenecek uygun eki döndürür.
	 *
	 * @param number Sayı
	 * @return Sayının eki
	 */
	@NotNull String getExt(long number);
	
	/**
	 * @param number Sayı
	 * @return Son karakteri sıfır ise {@code true}
	 */
	static boolean isLastZero(@NotNull String number) {
		
		if (number.length() <= 1) return false;
		
		return number.charAt(number.length() - 1) == '0';
	}
	
	/**
	 * Sayının sonundaki sıfırları sayar.
	 *
	 * @param number Sayı
	 * @return Sondaki sifır adedi
	 */
	static int countLastZero(@NotNull String number) {
		
		int count = 0;
		
		if (number.length() <= 1) return count;
		
		for (int i = number.length() - 1; i > 0; i--)
			if (number.charAt(i) == '0')
				count++;
			else break;
		return count;
	}
	
	/**
	 * Ekin türüne göre uygun eklenti nesnesi oluşturur.
	 *
	 * @param type Ek türü (mesela {@link Extension#TYPE_AT})
	 * @return {@link NumberExtention} nesnesi
	 */
	@NotNull
	static NumberExtention create(int type) {
		
		switch (type) {
			
			case Extension.TYPE_FROM: return new AblativeNumber();
			case Extension.TYPE_TO: return new DativeNumber();
			case Extension.TYPE_POSS: return new PossessiveNumber();
			case Extension.TYPE_IN_TO: return new AccusativeNumber();
			case Extension.TYPE_AT: return new LocativeNumber();
			case NumberExtention.TYPE_DAY: return new DayNumber();
			
			default: throw new IllegalArgumentException("There is no extension class for type : " + type);
		}
	}
	
	/**
	 * Sayının, sonu sıfırla biten kısmını döndürür. (1230 -> 30, 2050 -> 50, 1200 -> 200 vb.)
	 *
	 * @param number Sayı
	 * @return Sıfırla biten sayı
	 */
	static @NotNull String getLastByZero(@NotNull String number) {
		
		return number.substring(number.length() - (countLastZero(number) + 1));
	}
	
	/**
	 * Sayının sonuna uygun eki döndürür.
	 *
	 * @param number        Sayı
	 * @param extensionType Ek türü (see {@link Extension})
	 * @return Ek
	 */
	static @NotNull String getNumberExt(long number, int extensionType) {
		
		return create(extensionType).getExt(number);
	}
}
