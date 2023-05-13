package com.tr.hsyn.xbox;


import com.tr.hsyn.execution.Runny;
import com.tr.hsyn.key.Key;
import com.tr.hsyn.registery.cast.Database;
import com.tr.hsyn.time.Time;
import com.tr.hsyn.xbox.definition.Visitor;
import com.tr.hsyn.xbox.definition.Writer;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * <h2>Alice</h2>
 * <p>
 * 23 yaşında bir üniversite öğrencisi.<br>
 * Okula gitmediği zamanlarda arkadaşlarının çocukça aktivitelerine katılmamak için
 * bir otelde yarı zamanlı kayıt görevlisi ({@link Writer}) olarak çalışmakta.<br>
 * Elbette bu iş sadece arkadaşlarından kaçmak için değil,
 * Alice takıntılı denecek kadar disiplinli ve düzenli bir genç kız.
 * Bir konferansa giderken otobüste fazla yolcunun olmaması nedeniyle herkesin heryerde
 * oturabildiğini görünce panik atak geçirerek hızla giden otobüsten aşağı atladığı için ölen amcasından
 * miras kaldığını düşünmüştür hep bu düzen takıntısının.<br>
 * Ve otelde yaptığı iş onu büyük ölçüde rahatlatmaktadır onu.<br>
 * <p>
 * Alice otelde oda anahtarının durumlarını izler ve giriş-çıkış işlemlerini kaydeder.<br>
 * Bir anahtar bir kapıyı açtığında Alice bunu kesinlikle bilir.
 *
 * @author hsyn 9 Ocak 2023 Pazartesi 18:01
 * @see Writer
 */
@SuppressWarnings("DefaultLocale")
public class Alice implements Writer {
	
	/**
	 * Kayıt işlemlerini gerçekleştirecek veri tabanı
	 */
	protected final                                    Database<Visitor> register;
	@SuppressWarnings("FieldCanBeLocal") private final int               DEBUG_DEGREE = 0;
	/**
	 * Anahtarların anlık olarak durumlarını tutar
	 */
	private final                                      Map<Key, Visitor> keyMap       = new HashMap<>();
	
	public Alice(Database<Visitor> database) {
		
		this.register = database;
		Runny.run(this::registerControlResult, false);
	}
	
	/**
	 * Veri tabanının büyüklüğünü kontrol eder ve
	 * yer açmak gerekiyorsa kayıtları siler.
	 */
	@SuppressWarnings("DefaultLocale")
	private void registerControlResult() {
		
		if (register != null) {
			
			long   sizeLimit = 50_000_000L;
			String key       = "time_enter";
			long   bytes     = register.getSizeInBytes();
			long   rawCount  = register.getRawCount();
			
			List<Visitor> visitors = register.queryAll(null, key + " desc");
			
			checkVisitors(visitors);
			
			var           ruleSize = 50;
			StringBuilder sb       = new StringBuilder(String.format("\n%s\n", "=".repeat(ruleSize)));
			
			sb.append(Time.ToString()).append("\n")
					.append(String.format("%s\n", "-".repeat(ruleSize)))
					.append(String.format("Kayıt sayısı               : %d\n", visitors.size()))
					.append(String.format("Satır sayısı               : %d\n", rawCount))
					.append(String.format("Byte sayısı                : %d [%.2fMB]\n", bytes, bytes / (float) (1024 * 1024)))
					.append(String.format("Byte limiti                : %d [%.2fMB]\n", sizeLimit, sizeLimit / (float) (1024 * 1024)));
			
			
			long interval = bytes - sizeLimit;
			
			if (interval <= 0) {
				
				sb.append(String.format("Kullanılabilir byte sayısı : %d [%.2fMB]\n", -interval, -interval / (float) (1024 * 1024)));
			}
			else {
				
				sb.append(String.format("Aşılan byte sayısı : %d\n", interval));
				
				visitors.retainAll(visitors.subList(0, 20));
				
				int deleted = register.delete(visitors);
				
				sb.append(String.format("%d kayıt silindi\n", deleted));
			}
			
			sb.append(String.format("%s\n", "=".repeat(ruleSize)));
			
			xlog.i(sb.toString());
		}
	}
	
	/**
	 * Gelmiş geçmiş tüm otel misafirleri hakkında inceleme yapar.
	 * Hangi odaya kaç kez erişildiğini not eder.
	 *
	 * @param visitors Misafirler
	 */
	private void checkVisitors(@NotNull List<Visitor> visitors) {
		
		if (visitors.size() > 100) {
			
			Visitor last  = visitors.get(0);
			Visitor first = visitors.get(visitors.size() - 1);
			
			var           ruleSize = 80;
			StringBuilder sb       = new StringBuilder(String.format("\n%s\n", "=".repeat(ruleSize)));
			
			sb.append(String.format(
					"Kayıt zaman aralığı %s - %s\n",
					Time.toString(last.getTimeEnter(), "d.M.yyyy"),
					Time.toString(first.getTimeEnter(), "d.M.yyyy")));
			
			sb.append(String.format("%s\n", "-".repeat(ruleSize)));
			
			var group = visitors.stream().collect(Collectors.groupingBy(Visitor::getKey));
			
			sb.append("\n          Key                  Visitors\n");
			
			for (var entry : group.entrySet()) {
				
				Key           key         = entry.getKey();
				List<Visitor> keyVisitors = entry.getValue();
				
				sb.append(String.format("%-20s", key.getName()));
				
				long totalInteraction = keyVisitors.stream().map(Visitor::getInteraction).reduce(0L, Long::sum);
				
				sb.append(String.format("%15d\n", totalInteraction));
			}
			
			sb.append(String.format("%s\n", "=".repeat(ruleSize)));
			xlog.i(sb.toString());
		}
		
	}
	
	@Override
	public void remove(@NotNull Key key) {
		
		var data = keyMap.get(key);
		data.setExit();
		
		if (register != null) {
			
			if (register.update(data)) {
				
				xlog.i("The visitor exited : [%s]", key.getName());
			}
			else {
				
				xlog.i("The visitor exited but could not registered : [%s]", key.getName());
			}
		}
		else {
			
			xlog.i("The visitor exited but no register to register data : [%s]", key.getName());
		}
	}
	
	@SuppressWarnings("ConstantValue")
	@Override
	public void interact(@NotNull Key key) {
		
		var data = keyMap.get(key);
		data.interact();
		
		if (DEBUG_DEGREE > 1)
			xlog.i("The data has interacted : [%s]", key.getName());
		
		if (register != null) {
			
			if (register.update(data)) {
				
				if (DEBUG_DEGREE > 1)
					xlog.i("Data interaction is registered : [%s]", key.getName());
			}
			else {
				if (DEBUG_DEGREE > 0)
					xlog.i("Data interaction could not registered : [%s]", key.getName());
			}
		}
		else {
			
			xlog.i("No register to register interaction : [%s]", key.getName());
		}
		
		
	}
	
	@SuppressWarnings("ConstantValue")
	@Override
	public void add(@NotNull Key key) {
		
		var data = keyMap.get(key);
		
		if (data == null) {
			data = new Visitor(key);
			keyMap.put(key, data);
		}
		else data.setReEnter();
		
		if (register != null) {
			
			if (register.add(data)) {
				if (DEBUG_DEGREE > 1)
					xlog.i("Key is registered [%s]", key.getName());
			}
			else {
				if (DEBUG_DEGREE > 0)
					xlog.i("Key registering is failed [%s]", key.getName());
			}
		}
		else {
			
			xlog.i("No register to register the key [%s]", data);
		}
	}
	
	@Override
	public void close() {
		
		if (register != null) {
			register.close();
		}
	}
}
