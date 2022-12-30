package com.tr.hsyn.news;


import com.tr.hsyn.codefinder.CodeFinder;
import com.tr.hsyn.codefinder.CodeLocation;
import com.tr.hsyn.label.Label;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashSet;


/**
 * Haber.<br>
 * Bu sınıf, yayınlanacak haberi temsil eder.
 * {@link CodeLocation}  haber kaynağını tutar.
 * Haber kaynağı, haberin ilk duyrulduğu kaynak kod satırıdır.<br>
 * Haberin bir <em>konusu</em> ve bir <em>nesnesi</em> olabilir.<br>
 * Yayınlanan haber için <em>etiketler</em> de eklenebilir.
 * Bu şekilde haberler, konusunun yanında etiketleri ile de birbirinden ayrılabilir (filtrelenebilir).<br><br>
 * Bir haber için, diğer haberlerden kesinlikle farklı olacak olan değer ise <em>zamandır.</em>
 * Haber zamanı her haber için benzersiz bir sayı sağlar.<br>
 */
public class Sheet extends SheetInit {
	
	private static final int STACK_INDEX = 4;
	
	/**
	 * Haberin kaynağı
	 */
	private final CodeLocation from;
	/**
	 * Haberin konusu
	 */
	private final CharSequence subject;
	/**
	 * Haberin nesnesi
	 */
	private final Object       object;
	
	/**
	 * Constructor for a Sheet object.
	 */
	Sheet() {
		
		this.from    = CodeFinder.getLocation(STACK_INDEX);
		this.subject = "-";
		this.object  = null;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Constructor for a Sheet object.
	 *
	 * @param stackIndex The stack index of the caller.
	 */
	Sheet(int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = "-";
		this.object  = null;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Constructor for a Sheet object.
	 *
	 * @param object The object of the Sheet.
	 */
	Sheet(Object object) {
		
		this.from    = CodeFinder.getLocation(STACK_INDEX);
		this.subject = "-";
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Constructor for a Sheet object.
	 *
	 * @param object     The object of the Sheet.
	 * @param stackIndex The stack index of the caller.
	 */
	Sheet(Object object, int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = "-";
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Constructs a new {@code Sheet} with the given subject and object.
	 *
	 * @param subject the subject of the {@code Sheet}
	 * @param object  the object of the {@code Sheet}
	 */
	Sheet(CharSequence subject, Object object) {
		
		this.from    = CodeFinder.getLocation(STACK_INDEX);
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	
	Sheet(CharSequence subject, Object object, Label... labels) {
		
		this.from    = CodeFinder.getLocation(STACK_INDEX);
		this.subject = subject;
		this.object  = object;
		setLabels(new HashSet<>(Arrays.asList(labels)));
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Creates a new Sheet with the given subject and object.
	 *
	 * @param subject    the subject of the Sheet
	 * @param object     the object of the Sheet
	 * @param stackIndex the index of the stack trace element to use as the location
	 *                   of the Sheet
	 */
	Sheet(CharSequence subject, Object object, int stackIndex) {
		
		this.from    = CodeFinder.getLocation(stackIndex);
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * Creates a new {@link Sheet} with the given {@link CodeLocation}, subject and object.
	 *
	 * @param from    The {@link CodeLocation} where the {@link Sheet} was created.
	 * @param subject The subject of the {@link Sheet}.
	 * @param object  The object of the {@link Sheet}.
	 */
	Sheet(CodeLocation from, CharSequence subject, Object object) {
		
		this.from    = from;
		this.subject = subject;
		this.object  = object;
		setTime(System.currentTimeMillis());
	}
	
	/**
	 * @return {@link CodeLocation} as news source
	 */
	@NotNull
	public CodeLocation getFrom() {
		
		return from;
	}
	
	/**
	 * @return The value of the subject, or {@code -} sign if it is not set.
	 */
	@NotNull
	public CharSequence getSubject() {
		
		return subject;
	}
	
	/**
	 * @return The value of the object, or null if it is not set.
	 */
	@Nullable
	public Object getObject() {
		
		return object;
	}
	
	/**
	 * @return String representation of the Sheet object
	 */
	@NotNull
	@Override
	public String toString() {
		
		return "Sheet{" +
		       "from=" + from +
		       ", subject=" + subject +
		       ", object=" + object +
		       ", labels=" + (getLabels() == null ? "[]" : getLabels()) +
		       '}';
	}
	
	@NotNull
	public static Sheet create(CharSequence subject, Object object, Label... labels) {
		
		return new Sheet(subject, object, labels);
	}
}
