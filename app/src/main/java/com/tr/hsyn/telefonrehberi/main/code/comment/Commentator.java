package com.tr.hsyn.telefonrehberi.main.code.comment;


import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


/**
 * The <code>Commentator</code> interface defines a contract for
 * classes that can provide comments on a given subject.
 * It is a generic interface that can be implemented for any type of subject.
 */
public interface Commentator<T> {
	
	/**
	 * This method takes a subject and returns a comment on it.
	 *
	 * @param subject the subject to be commented on
	 * @return a CharSequence representing the comment on the subject
	 */
	@NotNull
	CharSequence commentOn(@NotNull T subject);
	
	/**
	 * Comments on the subject.
	 *
	 * @param subject  the subject to be commented on
	 * @param callback the callback to be called when the comment is done
	 */
	void commentOn(@NotNull T subject, @NotNull Consumer<CharSequence> callback);
}
