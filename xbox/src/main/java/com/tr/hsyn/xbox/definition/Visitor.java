package com.tr.hsyn.xbox.definition;


import com.tr.hsyn.identity.Identity;
import com.tr.hsyn.key.Key;

import java.util.Objects;


/**
 * Otel müşterisi.
 */
public class Visitor implements Identity {
	
	private final Key  key;
	private       long timeEnter;
	private       long timeExit;
	private       long interaction;
	
	public Visitor(Key key) {
		
		this.key         = key;
		this.timeEnter   = System.currentTimeMillis();
		this.timeExit    = 0;
		this.interaction = 0;
	}
	
	public Visitor(Key key, long timeEnter) {
		
		this.key         = key;
		this.timeEnter   = timeEnter;
		this.timeExit    = 0;
		this.interaction = 0;
	}
	
	public Visitor(Key key, long timeEnter, long timeExit, long interaction) {
		
		this.key         = key;
		this.timeEnter   = timeEnter;
		this.timeExit    = timeExit;
		this.interaction = interaction;
	}
	
	/**
	 * Çıkış zamanını kaydeder.
	 */
	public void setExit() {
		
		this.timeExit = System.currentTimeMillis();
	}
	
	/**
	 * @return Misafir ile kaç kez iletişim kurulduğunu bildirir
	 */
	public long getInteraction() {
		
		return interaction;
	}
	
	/**
	 * İletişim kaydını yaz
	 */
	public void interact() {
		
		interaction++;
	}
	
	/**
	 * @return Misafirin odaya giriş zamanı
	 */
	public long getTimeEnter() {
		
		return timeEnter;
	}
	
	/**
	 * @param timeEnter Misafirin odaya giriş zamanı
	 */
	public void setTimeEnter(long timeEnter) {
		
		this.timeEnter = timeEnter;
	}
	
	/**
	 * Odaya yeniden giriş yapıldığını bildirir.
	 * Giriş zamanı ve iletişim bilgisi güncellenir.
	 */
	public void setReEnter() {
		
		this.timeEnter = System.currentTimeMillis();
		timeExit       = 0;
		interaction    = 0;
	}
	
	/**
	 * @return Misafirin odadan çıkış yaptığı zaman
	 */
	public long getTimeExit() {
		
		return timeExit;
	}
	
	/**
	 * @param timeExit Çıkış zamanı
	 */
	public void setTimeExit(long timeExit) {
		
		this.timeExit = timeExit;
	}
	
	/**
	 * @return Oda anahtarı
	 */
	public Key getKey() {
		
		return key;
	}
	
	@Override
	public boolean equals(Object o) {
		
		return o instanceof Visitor && timeEnter == ((Visitor) o).timeEnter;
	}
	
	@Override
	public int hashCode() {
		
		return Objects.hash(timeEnter);
	}
	
	@Override
	public String toString() {
		
		return "Data{" +
		       "key=" + key +
		       ", timeEnter=" + timeEnter +
		       ", timeExit=" + timeExit +
		       ", interaction=" + interaction +
		       '}';
	}
	
	@Override
	public long getId() {
		
		return timeEnter;
	}
}
