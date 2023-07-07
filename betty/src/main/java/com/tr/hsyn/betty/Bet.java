package com.tr.hsyn.betty;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


/**
 * Provides practical handling of code that is likely to produce errors.
 */
public class Bet<R> {
	
	/**
	 * The error if there is one.
	 */
	@Nullable
	protected final Exception exception;
	/**
	 * Object to be returned from the job.
	 */
	@Nullable
	protected final R         returnValue;
	/**
	 * Indicates whether the job will run or not.
	 * If this variable is {@code false}, the given job will not run.
	 */
	@Nullable
	protected final Boolean   out;
	
	protected Bet() {
		
		this(null, null, null);
	}
	
	protected Bet(boolean out) {
		
		this(null, null, out);
	}
	
	protected Bet(@Nullable final Exception exception) {
		
		this(exception, null, null);
	}
	
	protected Bet(@Nullable final Exception exception, boolean out) {
		
		this(exception, null, out);
	}
	
	protected Bet(@Nullable R returnValue) {
		
		this(null, returnValue, null);
	}
	
	protected Bet(@Nullable R returnValue, boolean out) {
		
		this(null, returnValue, out);
	}
	
	protected Bet(@Nullable final Exception exception, @Nullable R returnValue) {
		
		this(exception, returnValue, null);
	}
	
	/**
	 * Creates a Bet.
	 *
	 * @param exception   exception
	 * @param returnValue return value
	 * @param out         out flag
	 */
	protected Bet(@Nullable final Exception exception, @Nullable R returnValue, @Nullable Boolean out) {
		
		this.exception   = exception;
		this.returnValue = returnValue;
		this.out         = out;
	}
	
	/**
	 * Executes the runnable.
	 *
	 * @param runnable runnable to be executed
	 * @return if the runnable executed then returns a new {@code Bet} object
	 * 		that holds the result for the further processing chain.
	 * 		If the runnable does not execute, then returns this Bet object.
	 * 		To be able to execute the runnable, when creating this Bet object,
	 * 		the constructor must take the {@code true} value for the {@code out} variable.
	 */
	@NotNull
	public Bet<R> care(@NotNull final Runnable runnable) {
		
		if (out == null || out) {
			
			Exception e = null;
			
			try {runnable.run();}
			catch (Exception ex) {e = ex;}
			
			return new Bet<>(e, null, out);
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
	public Bet<R> onSuccess(@NotNull final Runnable action) {
		
		if (out == null || (out && exception == null))
			action.run();
		
		return this;
	}
	
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
		
		if (exception != null)
			consumer.accept(exception);
		
		return this;
	}
	
	
}
