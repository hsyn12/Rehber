package com.tr.hsyn.watch.observable;


public class BasicObserver<T> {

	private Observer<T> observer;

	public void setObserver(Observer<T> observer) {

		this.observer = observer;
	}

	protected void notifyChange(T newValue) {

		if (observer != null) {

			observer.onUpdate(newValue);
		}

	}


}
