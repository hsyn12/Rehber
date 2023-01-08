package com.tr.hsyn.buildkeys;


import com.tr.hsyn.reflection.Clazz;


/**
 * <h1>BuildKeys</h1>
 * <p>
 * Uygulama genelinde haberleşmek için kullanılacak değişmez kodların tanımlarını sağlar.
 *
 * @author hsyn 31 Mayıs 2022 Salı 19:10
 */
public class Key {
	
	/**
	 * Kişiler listesi
	 */
	public static final int CONTACTS               = 0b1011000010000001111010;
	/**
	 * Arama Kayıtları listesi
	 */
	public static final int CALL_LOG               = 0b100000100011010110001101;
	/**
	 * Arama yapılacak arama kayıtları
	 */
	public static final int CALL_LOG_SEARCH_INFO   = 0b1010000110110010100000010000001;
	/**
	 * App context
	 */
	public static final int CONTEXT                = 0b11101011010100001;
	/**
	 * Bazı özel bilgileri tutan uygulamanın ana tablosu
	 */
	public static final int MAIN_TABLE             = 0b101101111011111011110011;
	/**
	 * Arama kayıtları yöneticisi
	 */
	public static final int CALL_STORY             = 0b100001011101101110001110000010;
	/**
	 * Rehber yöneticisi
	 */
	public static final int CONTACT_STORY          = 0b111000000101110111010;
	/**
	 * Seçilmiş kişi
	 */
	public static final int CONTACT_SELECTED       = 0b10000101101101001011101;
	/**
	 * Seçilmiş arama kaydı
	 */
	public static final int CALL_SELECTED          = 0b10010000001101000000100;
	/**
	 * Kişi listesinin yenilenmesi gerekip gerekmediğini işaret eder.
	 */
	public static final int SIGN_REFRESH_CONTACTS  = 0b100101000001001011011000;
	/**
	 * Arama kayıtlarının yenilenmesi gerekip gerekmediğine işaret eder.<br>
	 * Eğer anahtara {@code true} değeri kaydedilmişse kayıtların yenilenmesi gerekir.
	 * Bu anahtar {@code null} veya {@code false} ise yenilemeye gerek yoktur.
	 * Yenilemeye gerek olmayan durumlarda genellikle {@code null} değerindedir.
	 */
	public static final int REFRESH_CALL_LOG       = 0b101111100010101001110010;
	/**
	 * Kullanıcının uygulama ile ilişki derecesi
	 */
	public static final int RELATION_DEGREE        = 0b1011000011101001;
	/**
	 * Bazı yerlerde gösterilmesi gereken arama kayıtları listesi
	 */
	public static final int SHOW_CALLS             = 0b10000100001000101011110010110;
	/**
	 * Tek bir kişiye ait arama geçmişi
	 */
	public static final int CALL_HISTORY           = 0b1010000101101011011100011111000;
	public static final int DATA_BANK              = 0b111100100010100000001;
	public static final int CONTACT_LIST_UPDATED   = 0b100100110000101011101;
	public static final int DELETED_CONTACTS       = 0b100111101010101;
	public static final int CALL_LOG_UPDATED       = 0b11001000000101101000;
	public static final int MOST_CALLS_FILTER_TYPE = 0b10101100010100010000;
	public static final int NEW_CONTACTS           = 852498944;
	
	/**
	 * Verilen kodun ismini döndürür.
	 *
	 * @param code code
	 * @return codeName or {@code null}
	 */
	public static String getName(int code) {
		
		var fields = Clazz.getDeclaredFields(Key.class);
		
		for (int i = 0; i < fields.size(); i++) {
			
			try {
				
				int _code = fields.get(i).getInt(null);
				
				if (_code == code) return fields.get(i).getName();
				
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
}