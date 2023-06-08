package com.tr.hsyn.watch;


import com.tr.hsyn.time.Time;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;


public class WatchMaker {
	
	protected final AtomicBoolean watching = new AtomicBoolean(false);
	private final   ActionList    actList  = new ActionList((x, y) -> Long.compare(y.getRunTime(), x.getRunTime()));
	protected       long          leftToWakeUp;
	
	
	public WatchMaker() {
		
		actList.listenForAdd(this::onNewWatch);
	}
	
	protected void onNewWatch(Watch watch) {
		
		if (actList.isEmpty()) {
			
			leftToWakeUp = Time.now() - watch.getRunTime();
			
		}
	}
	
	protected void watchOut() {
		
		
	}
	
	protected final int runAfter(long value, TimeUnit timeUnit, Runnable runnable) {
		
		long  now = Time.now();
		Watch watch;
		
		if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
			
			watch = Watch.that(runnable, now + value);
		}
		else {
			
			watch = Watch.that(runnable, now + TimeUnit.MILLISECONDS.convert(value, timeUnit));
		}
		
		actList.add(watch);
		return watch.getId();
	}
	
	protected static final class ActionList extends ConcurrentSkipListSet<Watch> {
		
		private Consumer<Watch> onNewWatch;
		
		public ActionList(Comparator<? super Watch> comparator) {
			
			super(comparator);
		}
		
		public void listenForAdd(Consumer<Watch> onNewWatch) {
			
			this.onNewWatch = onNewWatch;
		}
		
		@Override
		public boolean add(Watch watch) {
			
			boolean r = super.add(watch);
			
			if (r && onNewWatch != null) onNewWatch.accept(watch);
			
			return r;
		}
	}
	
}