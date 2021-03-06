package com.github.visgeek.utils.collections;

import java.util.Iterator;

import com.github.visgeek.utils.IEqualityComparator;

class LinqIntersectIterator<T> extends Enumerator<T> {
	public LinqIntersectIterator(Iterable<T> source, Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		this.itr = source.iterator();
		this.second = second;
		this.comparator = comparator;
	}

	private final IEqualityComparator<? super T> comparator;

	private final Iterable<? extends T> second;

	private EquatableSet<T> firstSet;

	private EquatableSet<T> secondSet;

	private final Iterator<T> itr;

	private T currentValue;

	@Override
	public T current() {
		return this.currentValue;
	}

	@Override
	public boolean moveNext() {
		if (this.secondSet == null) {
			this.firstSet = EquatableSet.create(this.comparator);
			this.secondSet = EquatableSet.create(this.comparator);
			for (T item : this.second) {
				this.secondSet.add(item);
			}
		}

		boolean result = false;

		while (this.itr.hasNext()) {
			this.currentValue = this.itr.next();
			if (this.firstSet.add(this.currentValue) && this.secondSet.contains(this.currentValue)) {
				result = true;
				break;
			}
		}

		return result;
	}
}
