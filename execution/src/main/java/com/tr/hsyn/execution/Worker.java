package com.tr.hsyn.execution;


import com.tr.hsyn.executors.MainExecutor;
import com.tr.hsyn.executors.MinExecutor;
import com.tr.hsyn.executors.NormExecutor;
import com.tr.hsyn.xlog.xlog;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;


public class Worker<T> implements Work<T> {

	private final Callable<T>              callable;
	private final Runnable                 runnable;
	private       Consumer<T>              onSuccess;
	private       Consumer<Throwable>      onError;
	private       long                     delayOnWork;
	private       long                     delayOnLast;
	private       boolean                  minPriority;
	private       Runnable                 onLast;
	private       BiConsumer<T, Throwable> onResult;

	public Worker(@NotNull Callable<T> callable) {

		this.callable = callable;
		runnable      = null;
	}

	public Worker(@NotNull Runnable runnable) {

		this.runnable = runnable;
		this.callable = null;
	}

	public static <T> Worker<T> on(Callable<T> callable) {

		return new Worker<>(callable);
	}

	public static <T> Worker<T> on(Runnable runnable) {

		return new Worker<>(runnable);
	}

	@Override
	public Work<T> onResult(BiConsumer<T, Throwable> onResult) {

		this.onResult = onResult;
		return this;
	}

	@Override
	public Work<T> onSuccess(Consumer<T> onSuccess) {

		this.onSuccess = onSuccess;
		return this;
	}

	@Override
	public Work<T> onError(Consumer<Throwable> onError) {

		this.onError = onError;
		return this;
	}

	@Override
	public Work<T> onLast(Runnable onLast) {

		this.onLast = onLast;
		return this;
	}

	@Override
	public Work<T> onLast(Runnable onLast, long delay) {

		this.onLast = onLast;
		delayOnLast = delay;
		return this;
	}

	@Override
	public Work<T> delayOnWork(long delay) {

		this.delayOnWork = delay;
		return this;
	}

	@Override
	public Work<T> delayOnLast(long delay) {

		this.delayOnLast = delay;
		return this;
	}

	@Override
	public Work<T> priority(boolean minPriority) {

		this.minPriority = minPriority;
		return this;
	}

	@Override
	public void execute() {

		if (minPriority) MinExecutor.run(this::executeWork, delayOnWork);
		else NormExecutor.run(this::executeWork, delayOnWork);
	}

	@Override
	public void execute(long delay) {

		delayOnWork = delay;
		execute();
	}

	private void executeWork() {

		T         result = null;
		Exception ex     = null;

		try {

			if (callable != null) result = callable.call();
			else if (runnable != null) runnable.run();
		}
		catch (Exception e) {

			ex = e;
			xlog.e(e);
		}

		T         finalResult = result;
		Exception finalEx     = ex;

		if (onResult != null) {

			MainExecutor.run(() -> onResult.accept(finalResult, finalEx));
		}
		else {

			if (onSuccess != null) {

				MainExecutor.run(() -> onSuccess.accept(finalResult));
			}

			if (finalEx != null && onError != null) {

				MainExecutor.run(() -> onError.accept(finalEx));
			}
		}

		if (onLast != null) MainExecutor.run(onLast, delayOnLast);
	}


}
