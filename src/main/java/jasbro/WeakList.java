package jasbro;

//Revised from objectweb jac

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableBoolean;

public class WeakList<T> extends AbstractList<T> {

	/**
	 * Lock to prevent modification by garbage collection while list is being copied.
	 */
	private MutableBoolean copyListLock=new MutableBoolean(false);
	
	/**
	 * Reference queue for cleared weak references
	 */
	private final ReferenceQueue<T> queue = new ReferenceQueue<T>();
	
	private final List<ListEntry<T>> list = new ArrayList<ListEntry<T>>();
	
	public boolean add(T o) {
		expungeStaleEntries();
		return list.add(new ListEntry<T>(o, queue));
	}
	
	public T get(int i) {
		expungeStaleEntries();
		return ((ListEntry<T>) list.get(i)).get();
	}
	
	public int size() {
		expungeStaleEntries();
		return list.size();
	}
	
	public T remove(int index) {
		return ((ListEntry<T>) list.remove(index)).get();
	}
	
	@Override
	public void clear() {
		list.clear();
		super.clear();
	}
	
	/**
	 * Creates a copy of the current contents of this list with strong references to its elements.
	 * The copy will contain no null values.
	 */
	public List<T> strongCopy() {
		List<T> copy;
		synchronized (getCopyListLock()) {
			try {
				getCopyListLock().setTrue();
				int size=size();
				copy=new ArrayList<T>(size);
				
				for (int i=0; i<size; i++) {
					T element=get(i);
					if (element!=null) {
						copy.add(element);
					}
				}
			} finally {
				getCopyListLock().setFalse();
			}
		}
		return copy;
	}
	
	/**
	 * Expunge stale entries from the list.
	 */
	private void expungeStaleEntries() {       
		Object r;
		while ((r = queue.poll()) != null) {
			int i = list.indexOf(r);
			if (i != -1) {
				synchronized(getCopyListLock()) {
					if (!getCopyListLock().booleanValue()) {
						list.remove(i);
					}
				}
			}
		}
	}
	
	private synchronized MutableBoolean getCopyListLock() {
		if (copyListLock==null) {
			copyListLock=new MutableBoolean(false);
		}
		return copyListLock;
	}

	private static class ListEntry<T> extends WeakReference<T> {
		String objectString;
		
		public ListEntry(T o, ReferenceQueue<T> queue) {
			super(o, queue);
			objectString = o.toString();
		}
		
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			} else {
				if ((o instanceof ListEntry)) {
					return o.hashCode() == this.hashCode();
				} else {
					if (this.get() == null)
						return false;
					return this.get().equals(o);
				}
			}
		}
		
		public String toString() {
			if (this.get() == null) {
				return "'entry " + objectString + " <GARBAGED>'";
			} else {
				return "'entry " + this.get() + "'";
			}
		}
		
	}
}