package com.tr.hsyn.time;


import java.time.Instant;
import java.time.LocalDateTime;


/**
 * Zamanı ileri yada geri alır. Geri için negatif, ileri için pozitif değerler kullan.
 * Zamandaki bu değişiklik, zamanın belirli birimlerine yapılır.<br>
 * Bu birimler: {@code yıl, ay, gün, saat, dakika, saniye.}<br>
 * Bu zaman birimlerine yapılan ekleme sonucunda eğer zaman birimi
 * döngü sınırının dışına çıkıyorsa, bu birimin bir üst birimi de güncellenir.
 * Mesela zamanın dakika birimine {@code 60 * 24 + 60 * 2 + 8} (1 gün 2 saat 8 dakika (toplamda 1568 dakika))
 * ekleme yapılırsa,
 * hem gün hem saat hem dakika birimi eklenen değere göre güncellenir,
 * gün değeri 1 artar, saat değeri 2 artar, dakika değeri 8 artar.<br><br>
 *
 * <pre>
 * // 1568 dakika sonraki zaman
 * var day = FromNow.minutes(1568);
 * // Şimdiki zaman
 * System.out.println(Time.toString("d MMMM yyyy EEEE HH:mm")); // 24 Kasım 2022 Perşembe 18:50
 * System.out.println(Time.toString(day, "d MMMM yyyy EEEE HH:mm")); // 25 Kasım 2022 Cuma 20:58
 * </pre>
 * <p>
 * <p>
 * Aynı mantıkla {@code 1568} değil de {@code -1568} (negatif) eklenirse,
 * bu kez zaman geriye doğru gider ve yine aynı şekilde döngü birimini aşan zaman birimleri
 * bir üstteki birimi de günceller.<br>
 * Mesela şimdiki zamandan 10000 (on bin) saniye öncesine gitmek istersek :<br><br>
 *
 * <pre>
 * var day = FromNow.seconds(-10_000); // Kabaca 3 saat kadar öncesi
 * // Şimdiki zaman
 * System.out.println(Time.toString("d MMMM yyyy EEEE HH:mm")); // 24 Kasım 2022 Perşembe 19:02
 * // On bin saniye önceki zaman
 * System.out.println(Time.toString(day, "d MMMM yyyy EEEE HH:mm")); // 24 Kasım 2022 Perşembe 16:15
 * </pre>
 *
 * @author hsyn 25 Kasım 2022 Cuma 14:07
 */
public interface FromNow {
	
	/**
	 * Şimdiki zamanın yılına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 *
	 * @param years Yıl
	 * @return Time millis
	 */
	default long years(int years) {
		
		return years(Instant.now(), years);
	}
	
	/**
	 * Verilen zamanın yılına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 *
	 * @param time  Değiştirilecek zaman
	 * @param years Yıl
	 * @return Time millis
	 */
	default long years(long time, int years) {
		
		return years(Instant.ofEpochMilli(time), years);
	}
	
	/**
	 * Verilen zamanın yılına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 *
	 * @param time  Değiştirilecek zaman
	 * @param years Yıl
	 * @return Time millis
	 */
	default long years(Instant time, int years) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusYears(years).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	/**
	 * Şimdiki zamanın ayına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * yıl değeri de güncellenir.
	 * Mesela pozitifte Aralık ayından Ocak ayına her geçildiğinde yıl 1 artar,
	 * negatifte Ocak ayından Aralık ayına her geçildiğinde yıl 1 eksilir.
	 *
	 * @param months Ay
	 * @return Time millis
	 */
	default long months(int months) {
		
		return months(Instant.now(), months);
	}
	
	/**
	 * Verilen zamanın ayına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * yıl değeri de güncellenir.
	 * Mesela pozitifte Aralık ayından Ocak ayına her geçildiğinde yıl 1 artar,
	 * negatifte Ocak ayından Aralık ayına her geçildiğinde yıl 1 eksilir.
	 *
	 * @param time   Değiştirilecek zaman
	 * @param months Ay
	 * @return Time millis
	 */
	default long months(long time, int months) {
		
		return months(Instant.ofEpochMilli(time), months);
	}
	
	/**
	 * Verilen zamanın ayına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * yıl değeri de güncellenir.
	 * Mesela pozitifte Aralık ayından Ocak ayına her geçildiğinde yıl 1 artar,
	 * negatifte Ocak ayından Aralık ayına her geçildiğinde yıl 1 eksilir.
	 *
	 * @param time   Değiştirilecek zaman
	 * @param months Ay
	 * @return Time millis
	 */
	default long months(Instant time, int months) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusMonths(months).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	/**
	 * Şimdiki zamanın gününe ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * ay değeri de güncellenir.
	 * Mesela pozitifte (ayın kaç çektiğine bağlı olarak) 28-30-31 değeri her geçildiğinde ay 1 artar,
	 * negatifte ayın birinden 28-30-31'ine her geçildiğinde ay 1 eksilir.
	 *
	 * @param days Ay
	 * @return Time millis
	 */
	default long days(int days) {
		
		return days(Instant.now(), days);
	}
	
	/**
	 * Verilen zamanın gününe ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * ay değeri de güncellenir.
	 * Mesela pozitifte (ayın kaç çektiğine bağlı olarak) 28-30-31 değeri her geçildiğinde ay 1 artar,
	 * negatifte ayın birinden 28-30-31'ine her geçildiğinde ay 1 eksilir.
	 *
	 * @param days Ay
	 * @return Time millis
	 */
	default long days(long time, int days) {
		
		return days(Instant.ofEpochMilli(time), days);
	}
	
	/**
	 * Verilen zamanın gününe ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * ay değeri de güncellenir.
	 * Mesela pozitifte (ayın kaç çektiğine bağlı olarak) 28-30-31 değeri her geçildiğinde ay 1 artar,
	 * negatifte ayın birinden 28-30-31'ine her geçildiğinde ay 1 eksilir.
	 *
	 * @param days Ay
	 * @return Time millis
	 */
	default long days(Instant time, int days) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusDays(days).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	/**
	 * Şimdiki zamanın saatine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * gün değeri de güncellenir.
	 * Mesela pozitifte 23 değeri her geçildiğinde gün 1 artar,
	 * negatifte 0'dan 23'e her geçildiğinde gün 1 eksilir.
	 *
	 * @param hours Ay
	 * @return Time millis
	 */
	default long hours(int hours) {
		
		return hours(Instant.now(), hours);
	}
	
	/**
	 * Verilen zamanın saatine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * gün değeri de güncellenir.
	 * Mesela pozitifte 23 değeri her geçildiğinde gün 1 artar,
	 * negatifte 0'dan 23'e her geçildiğinde gün 1 eksilir.
	 *
	 * @param hours Ay
	 * @return Time millis
	 */
	default long hours(long time, int hours) {
		
		return hours(Instant.ofEpochMilli(time), hours);
	}
	
	/**
	 * Verilen zamanın saatine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * gün değeri de güncellenir.
	 * Mesela pozitifte 23 değeri her geçildiğinde gün 1 artar,
	 * negatifte 0'dan 23'e her geçildiğinde gün 1 eksilir.
	 *
	 * @param hours Ay
	 * @return Time millis
	 */
	default long hours(Instant time, int hours) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusHours(hours).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	/**
	 * Şimdiki zamanın dakikasına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * saat değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde saat 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde saat 1 eksilir.
	 *
	 * @param minutes Ay
	 * @return Time millis
	 */
	default long minutes(int minutes) {
		
		return minutes(Instant.now(), minutes);
	}
	
	/**
	 * Verilen zamanın dakikasına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * saat değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde saat 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde saat 1 eksilir.
	 *
	 * @param minutes Ay
	 * @return Time millis
	 */
	default long minutes(long time, int minutes) {
		
		return minutes(Instant.ofEpochMilli(time), minutes);
	}
	
	/**
	 * Verilen zamanın dakikasına ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * saat değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde saat 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde saat 1 eksilir.
	 *
	 * @param minutes Ay
	 * @return Time millis
	 */
	default long minutes(Instant time, int minutes) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusMinutes(minutes).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	/**
	 * Şimdiki zamanın saniyesine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * dakika değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde dakika 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde dakika 1 eksilir.
	 *
	 * @param seconds Ay
	 * @return Time millis
	 */
	default long seconds(int seconds) {
		
		return seconds(Instant.now(), seconds);
	}
	
	/**
	 * Verilen zamanın saniyesine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * dakika değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde dakika 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde dakika 1 eksilir.
	 *
	 * @param seconds Ay
	 * @return Time millis
	 */
	default long seconds(long time, int seconds) {
		
		return seconds(Instant.ofEpochMilli(time), seconds);
	}
	
	/**
	 * Verilen zamanın saniyesine ekleme yapar.
	 * Pozitif ileri, negatif geri gider.
	 * Zaman birimi döngüsünü tamamladığında
	 * dakika değeri de güncellenir.
	 * Mesela pozitifte 59 değeri her geçildiğinde dakika 1 artar,
	 * negatifte 0'dan 59'a her geçildiğinde dakika 1 eksilir.
	 *
	 * @param seconds Ay
	 * @return Time millis
	 */
	default long seconds(Instant time, int seconds) {
		
		return LocalDateTime.ofInstant(time, Date.DEFAULT_ZONE_OFFSET).plusSeconds(seconds).toEpochSecond(Date.DEFAULT_ZONE_OFFSET) * 1000L;
	}
	
	
}
