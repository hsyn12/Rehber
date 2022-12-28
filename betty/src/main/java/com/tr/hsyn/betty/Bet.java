package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


/**
 * {@link Betty}'nin izleyeceği adımları tanımlar.
 */
public abstract class Bet<R> {
	
	/**
	 * Çalıştırılan işin hatası
	 */
	@Nullable
	protected final Exception e;
	/**
	 * Çalıştırılan işin döndürdüğü nesne
	 */
	@Nullable
	protected final R         v;
	/**
	 * Bu değişken {@code false} değerinde ise verilen iş çalıştırılmaz.
	 */
	@Nullable
	protected final Boolean   out;
	
	protected Bet() {
		
		this(null, null, null);
	}
	
	protected Bet(boolean out) {
		
		this(null, null, out);
	}
	
	protected Bet(@Nullable final Exception e) {
		
		this(e, null, null);
	}
	
	protected Bet(@Nullable final Exception e, boolean out) {
		
		this(e, null, out);
	}
	
	protected Bet(@Nullable R v) {
		
		this(null, v, null);
	}
	
	protected Bet(@Nullable R v, boolean out) {
		
		this(null, v, out);
	}
	
	protected Bet(@Nullable final Exception e, @Nullable R v) {
		
		this(e, v, null);
	}
	
	protected Bet(@Nullable final Exception e, @Nullable R v, @Nullable Boolean out) {
		
		this.e   = e;
		this.v   = v;
		this.out = out;
	}
	
	/**
	 * Verilen iş çalıştırılırken bir hata meydana gelse de gelmese de {@link #call(Runnable)}
	 * metoduna yönlenerek her iki durumda da çalışması gereken kodun tanımlanmasını sağlar.
	 * Bu metot düzgün bir akış için ilk metottur. (Yada {@link #care(Callable)})<br>
	 * {@code care(Runnable)} --> {@link #call(Runnable)} --> {@link #onError(Consumer)}.<br>
	 * Akış bu sıra ile ilerler.<br>
	 *
	 * <ol>
	 *    <li>{@code care(Runnable)} ile çalıştırılacak iş belirtilir</li>
	 *    <li>Sonra {@link #call(Runnable)} metodu verilen iş tamamlandığında (hata olsa bile) çağrılır</li>
	 *    <li>En son, verilen iş çalıştırılırken bir hata olmuşsa {@link #onError(Consumer)} metodu hata ile çağrılır. Hata olmadıysa çağrılmaz.</li>
	 * </ol>
	 *
	 * @param runnable Çalıştırılacak iş
	 * @return Bet
	 */
	@NotNull
	public Bet<R> care(@NotNull final Runnable runnable) {
		
		if (out != null) {
			
			if (out) {
				
				Exception e = null;
				
				try {runnable.run();}
				catch (Exception ex) {e = ex;}
				
				return new BetRun<>(e, null, true);
			}
		}
		else {
			
			Exception e = null;
			
			try {runnable.run();}
			catch (Exception ex) {e = ex;}
			
			return new BetRun<>(e, null, null);
		}
		
		return this;
	}
	
	/**
	 * Aynı {@link #call(Runnable)} metodu gibidir ancak sonuç döndürür.
	 * Bu iki metot akış başlatır, bu metotlar arka arkaya çağrılırsa
	 * önceki akış kaybolur, yeni başlayan akış geçerli olur.
	 * Akış içince sonuçlar yada hatalar işlenerek ilerlenirse
	 * istenildiği kadar arka arkaya işlem tanımlanabilir.
	 *
	 * @param callable {@link Callable}
	 * @return Bet
	 */
	public Bet<R> care(@NotNull final Callable<R> callable) {
		
		Exception e = null;
		R         v = null;
		
		if (out != null) {
			
			if (out) {
				
				try {v = callable.call();}
				catch (Exception ex) {e = ex;}
				
				return new BetCall<>(e, v, true);
			}
		}
		else {
			
			try {v = callable.call();}
			catch (Exception ex) {e = ex;}
			
			return new BetCall<>(e, v, null);
		}
		
		return this;
	}
	
	/**
	 * Kontrol değişkenini kontrol et.
	 *
	 * @param consumer Kontrolü yapacak olan nesne
	 * @return Bet
	 */
	public Bet<R> checkIn(@NotNull Consumer<Boolean> consumer) {
		
		if (out != null) {
			
			consumer.accept(out);
		}
		
		return this;
	}
	
	public Bet<R> care(boolean checkout) {
		
		return new Betty<>(checkout);
	}
	
	/**
	 * {@link #care(Runnable)} metodunun arkasından çağrılırsa,
	 * orada çalıştırılan kod tamamlandıktan sonra çağrılır (hata olsa da olmasa da önce çağrılır) ve
	 * burada verilen iş çalıştırılır. Akışa göre bu metodun arkasından {@link #onError(Consumer)}
	 * metodu çağrılmalı, ve {@link #care(Runnable)} metodunda bir hata olmuşsa bu hata
	 * {@link #onError(Consumer)} metoduna kadar aktarılır. Bu şekilde {@link #care(Runnable)} metodunda
	 * verilen esas işin neticesi tam olarak alınır.<br>
	 * Ayrıca bu metoda verilen işin hata üretmeyecek olmasına dikkat edilmeli çünkü
	 * bu metot herhangi bir hata gözlemez. Sadece esas işin ürettiği bir hata varsa
	 * {@link #onError(Consumer)} metoduna aktarır. Bu aktarım son satırda yapıldığı için
	 * eğer bu metoda verilen iş hata üretirse akış bu hata ile sonlanır.
	 *
	 * @param runnable Hata olsa da olmasa da çalıştırılacak kod
	 * @return Bet
	 */
	@NotNull//Finally
	public Bet<R> call(@NotNull final Runnable runnable) {
		
		runnable.run();
		return this;
	}
	
	/**
	 * Verilen iş hata üretmeden tamamlandıysa bu metoda verilen iş çağrılır.
	 * <p>
	 * Eğer bir kontrol değişkeni verilmişse ({@link #care(boolean)}) bu değişken
	 * ancak ve ancak {@code true} ise çağrılır.
	 *
	 * @param action İş
	 * @return Bet
	 */
	public Bet<R> onSuccess(@NotNull final Runnable action) {return this;}
	
	/**
	 * Verilen iş hata üretmeden tamamlandıysa bu metoda verilen nesne sonuç ile çağrılır.
	 * <p>
	 * Eğer bir kontrol değişkeni verilmişse ({@link #care(boolean)}) bu değişken
	 * ancak ve ancak {@code true} ise çağrılır.
	 *
	 * @param consumer Sonucu işleyecek nesne
	 * @return Bet
	 */
	public Bet<R> onSuccess(@NotNull final Consumer<R> consumer) {return this;}
	
	/**
	 * Bu metot {@link #care(Runnable)} metodunun ardından da çağrılabilir {@link #call(Runnable)} metodunun
	 * ardından da çağrılabilir. Her iki durumda da {@link #care(Runnable)} metodundaki işin hata
	 * nesnesini verir. Eğer hata yoksa hata işleyici nesne çağrılmaz.<br>
	 * Bu metot akışın son halkasıdır. Bu metottan sonra {@link #care(Runnable)} veya {@link #care(Callable)}
	 * metotları çağrılarak yeni bir akış oluşturulabilir.
	 * Bu iki metot dışındaki metotlar çağrılırsa eski sonuçlar döner.
	 *
	 * @param consumer Hata işleyicisi
	 * @return Bet
	 */
	@NotNull//Exeptional
	public Bet<R> onError(@NotNull final Consumer<@NotNull Throwable> consumer) {
		
		if (e != null)
			consumer.accept(e);
		
		return this;
	}
	
	
}
