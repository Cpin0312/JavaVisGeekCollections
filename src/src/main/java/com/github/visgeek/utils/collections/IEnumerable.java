package com.github.visgeek.utils.collections;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.github.visgeek.utils.ComparatorUtils;
import com.github.visgeek.utils.IEqualityComparator;
import com.github.visgeek.utils.JoinedValue;
import com.github.visgeek.utils.collections.test.testcase.enumerable.of.IBigDecimalEnumerable;
import com.github.visgeek.utils.functions.Func0;
import com.github.visgeek.utils.functions.Func1;
import com.github.visgeek.utils.functions.Func2;
import com.github.visgeek.utils.functions.IndexedAction1;
import com.github.visgeek.utils.functions.IndexedFunc1;
import com.github.visgeek.utils.functions.IndexedPredicate;
import com.github.visgeek.utils.functions.Predicate;

@SuppressWarnings("unchecked")
public interface IEnumerable<T> extends Iterable<T> {
	// メソッド
	default Enumerator<T> getEnumerator() {
		if (this.iterator() instanceof Enumerator<?>) {
			return (Enumerator<T>) this.iterator();
		} else {
			return new Enumerator<T>() {
				private final Iterator<T> itr = IEnumerable.this.iterator();

				private T current;

				@Override
				public T current() {
					return this.current;
				}

				@Override
				public boolean moveNext() {
					boolean result = false;

					if (this.itr.hasNext()) {
						this.current = this.itr.next();
						result = true;
					}

					return result;
				}
			};
		}
	}

	// LINQ メソッド
	default T aggregate(Func2<T, ? super T, T> func) {
		Errors.throwIfNull(func, "func");

		Iterator<T> i = this.iterator();

		if (!i.hasNext()) {
			throw Errors.empty();
		}

		T value = i.next();

		while (i.hasNext()) {
			value = func.func(value, i.next());
		}

		return value;
	}

	default <TAccumulate> TAccumulate aggregate(TAccumulate seed, Func2<TAccumulate, ? super T, TAccumulate> func) {
		Errors.throwIfNull(func, "func");

		TAccumulate value = seed;

		for (T item : this) {
			value = func.func(value, item);
		}

		return value;
	}

	default <TAccumulate, TResult> TResult aggregate(TAccumulate seed, Func2<TAccumulate, ? super T, TAccumulate> func, Func1<TAccumulate, TResult> resultSelector) {
		Errors.throwIfNull(func, "func");
		Errors.throwIfNull(resultSelector, "resultSelector");

		TAccumulate value = seed;

		for (T item : this) {
			value = func.func(value, item);
		}

		return resultSelector.func(value);
	}

	/**
	 * シーケンスのすべての要素が条件を満たしているかどうかを判断します。
	 *
	 * @param predicate 要素が条件を満たしているかどうかを判断するメソッド。
	 * @return すべての要素が条件を満たしていれば true。そうでなければ false。
	 */
	default boolean all(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		boolean result = true;

		for (T item : this) {
			if (!predicate.test(item)) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * シーケンスに要素が登録されているかどうかを判断します。
	 * @return
	 */
	default boolean any() {
		return this.iterator().hasNext();
	}

	/**
	 * シーケンスの 1 つ以上の要素が条件を満たしているかどうかを判断します。
	 *
	 * @param predicate
	 * @return
	 */
	default boolean any(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		boolean result = false;

		for (T item : this) {
			if (predicate.test(item)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * IBigDecimalEnumerable に変換します。要素を BigDecimal にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IBigDecimalEnumerable asBigDecimalEnumerable() {
		if (this instanceof IBigDecimalEnumerable) {
			return (IBigDecimalEnumerable) this;
		} else {
			return this.selectBigDecimal(n -> (BigDecimal) n);
		}
	}

	/**
	 * IBooleanEnumerable に変換します。要素を Boolean にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IBooleanEnumerable asBooleanEnumerable() {
		if (this instanceof IBooleanEnumerable) {
			return (IBooleanEnumerable) this;
		} else {
			return this.selectBoolean(n -> (Boolean) n);
		}
	}

	/**
	 * IByteEnumerable に変換します。要素を Byte にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IByteEnumerable asByteEnumerable() {
		if (this instanceof IByteEnumerable) {
			return (IByteEnumerable) this;
		} else {
			return this.selectByte(n -> (Byte) n);
		}
	}

	/**
	 * ICharacterEnumerable に変換します。要素を Character にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default ICharacterEnumerable asCharacterEnumerable() {
		if (this instanceof ICharacterEnumerable) {
			return (ICharacterEnumerable) this;
		} else {
			return this.selectCharacter(n -> (Character) n);
		}
	}

	/**
	 * IDoubleEnumerable に変換します。要素を Double にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IDoubleEnumerable asDoubleEnumerable() {
		if (this instanceof IDoubleEnumerable) {
			return (IDoubleEnumerable) this;
		} else {
			return this.selectDouble(n -> (Double) n);
		}
	}

	/**
	 * IEnumerable&lt;T&gt; に変換します。
	 *
	 * @return
	 */
	default IEnumerable<T> asEnumerable() {
		return this;
	}

	/**
	 * IFloatEnumerable に変換します。要素を Float にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IFloatEnumerable asFloatEnumerable() {
		if (this instanceof IFloatEnumerable) {
			return (IFloatEnumerable) this;
		} else {
			return this.selectFloat(n -> (Float) n);
		}
	}

	/**
	 * IIntegerEnumerable に変換します。要素を Integer にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IIntegerEnumerable asIntegerEnumerable() {
		if (this instanceof IIntegerEnumerable) {
			return (IIntegerEnumerable) this;
		} else {
			return this.selectInteger(n -> (Integer) n);
		}
	}

	/**
	 * ILongEnumerable に変換します。要素を Long にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default ILongEnumerable asLongEnumerable() {
		if (this instanceof ILongEnumerable) {
			return (ILongEnumerable) this;
		} else {
			return this.selectLong(n -> (Long) n);
		}
	}

	/**
	 * IShortEnumerable に変換します。要素を Short にキャストできない場合でも例外の発生は実行時まで延期されます。
	 *
	 * @return
	 */
	default IShortEnumerable asShortEnumerable() {
		if (this instanceof IShortEnumerable) {
			return (IShortEnumerable) this;
		} else {
			return this.selectShort(n -> (Short) n);
		}
	}

	/**
	 * Stream&lt;T&gt; に変換します。
	 * @return
	 */
	default Stream<T> asStream() {
		return StreamSupport.stream(this.spliterator(), false);
	}

	/**
	 * セレクター関数を使用して要素から取得した値の平均値を取得します。セレクター関数の結果が null の場合は 計算の対象外です。
	 *
	 * @param selector
	 * @return null 以外の値の平均値。シーケンスが空、または null のみの場合は null。
	 */
	default Double averageDouble(Func1<? super T, Double> selector) {
		return this.selectDouble(selector).average();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の平均値を取得します。セレクター関数の結果が null の場合は 計算の対象外です。
	 *
	 * @param selector
	 * @return null 以外の値の平均値。シーケンスが空、または null のみの場合は null。
	 */
	default Float averageFloat(Func1<? super T, Float> selector) {
		return this.selectFloat(selector).average();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の平均値を取得します。セレクター関数の結果が null の場合は 計算の対象外です。
	 *
	 * @param selector
	 * @return null 以外の値の平均値。シーケンスが空、または null のみの場合は null。
	 */
	default Double averageInteger(Func1<? super T, Integer> selector) {
		return this.selectInteger(selector).average();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の平均値を取得します。セレクター関数の結果が null の場合は 計算の対象外です。
	 *
	 * @param selector
	 * @return null 以外の値の平均値。シーケンスが空、または null のみの場合は null。
	 */
	default Double averageLong(Func1<? super T, Long> selector) {
		return this.selectLong(selector).average();
	}

//	/***
//	 * 整列済みのシーケンスから指定された要素を二分探索し、見つかった位置のインデックスを取得します。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。要素が整列されていない場合の動作は保証されません。
//	 * @param item 検索する要素
//	 * @return 見つかった位置のインデックス。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。大きい要素が存在しない場合は検索範囲の最後の要素のインデックス + 1 の補数です。
//	 */
//	default int binarySearch(T item) {
//		return this.binarySearch(0, this.count(), item);
//	}
//
//	/***
//	 * 整列済みのシーケンスから指定された要素を二分探索し、見つかった位置のインデックスを取得します。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。要素が整列されていない場合の動作は保証されません。
//	 * @param index 検索開始位置を表すインデックス。
//	 * @param count 検索範囲の要素数。
//	 * @param item 検索する要素
//	 * @return 見つかった位置のインデックス。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。大きい要素が存在しない場合は検索範囲の最後の要素のインデックス + 1 の補数です。
//	 */
//	default int binarySearch(int index, int count, T item) {
//		return this.binarySearch(index, count, item, ComparatorUtils.getDefault());
//	}
//
//	/***
//	 * 整列済みのシーケンスから指定された要素を二分探索し、見つかった位置のインデックスを取得します。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。要素が整列されていない場合の動作は保証されません。
//	 * @param item 検索する要素
//	 * @param comparer 要素を比較するための比較子
//	 * @return 見つかった位置のインデックス。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。大きい要素が存在しない場合は検索範囲の最後の要素のインデックス + 1 の補数です。
//	 */
//	default int binarySearch(T item, Comparator<? super T> comparer) {
//		return this.binarySearch(0, this.count(), item, comparer);
//	}
//
//	/***
//	 * 整列済みのシーケンスから指定された要素を二分探索し、見つかった位置のインデックスを取得します。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。要素が整列されていない場合の動作は保証されません。
//	 * @param index 検索開始位置を表すインデックス。
//	 * @param count 検索範囲の要素数。
//	 * @param item 検索する要素
//	 * @param comparator 要素を比較するための比較子
//	 * @return 見つかった位置のインデックス。見つからなかった場合は検索値の次に大きい要素のインデックスのビットごとの補数です。大きい要素が存在しない場合は検索範囲の最後の要素のインデックス + 1 の補数です。
//	 */
//	default int binarySearch(int index, int count, T item, Comparator<? super T> comparator) {
//		if (comparator == null) {
//			comparator = ComparatorUtils.getDefault();
//		}
//
//		int itemCount;
//
//		if (index < 0) {
//			throw new IllegalArgumentException("index");
//		} else if (count < 0) {
//			throw new IllegalArgumentException("count");
//		} else {
//			itemCount = this.count();
//			if (itemCount < index + count) {
//				throw new IllegalArgumentException();
//			}
//		}
//
//		int result;
//
//		if (itemCount == 0) {
//			result = ~0;
//
//		} else {
//			int startIndex = index;
//			int endIndex = startIndex + count - 1;
//			while (true) {
//				int idx = startIndex + (endIndex - startIndex) / 2;
//				T val = this.elementAt(idx);
//
//				int compareResult = comparator.compare(val, item);
//				if (compareResult == 0) {
//					result = idx;
//					break;
//
//				} else {
//					if (compareResult < 0) {
//						startIndex = idx + 1;
//
//					} else {
//						endIndex = idx - 1;
//					}
//
//					if (endIndex < startIndex) {
//						result = ~startIndex;
//						break;
//					}
//				}
//			}
//		}
//
//		return result;
//	}

	/**
	 * シーケンスの要素を指定した型にキャストします。
	 *
	 * @param elementClass
	 * @return
	 */
	default <TResult> IEnumerable<TResult> cast(Class<TResult> elementClass) {
		Errors.throwIfNull(elementClass, "elementClass");
		return this.<TResult> cast();
	}

	/**
	 * シーケンスの要素を指定した型にキャストします。
	 *
	 * @return
	 */
	default <TResult> IEnumerable<TResult> cast() {
		return (IEnumerable<TResult>) this;
	}

	/**
	 * 指定したシーケンスを連結します。
	 *
	 * @param second
	 * @return
	 */
	default IEnumerable<T> concat(Iterable<? extends T> second) {
		Errors.throwIfNull(second, "second");
		return () -> new LinqConcateIterator<>(this, second);
	}

	/**
	 * 指定したシーケンスを連結します。
	 *
	 * @param second
	 * @return
	 */
	default IEnumerable<T> concat(T... second) {
		Errors.throwIfNull(second, "second");
		return () -> new LinqConcateIterator<>(this, Enumerable.of(second));
	}

	/**
	 * 指定した要素がシーケンスに含まれているかどうかを判断します。
	 *
	 * @param item
	 * @return
	 */
	default boolean contains(T item) {
		return this.contains(item, null);
	}

	/**
	 * 指定した比較方法を使用して、指定した要素がシーケンスに含まれているかどうかを判断します。
	 *
	 * @param item
	 * @param comparator
	 * @return
	 */
	default boolean contains(T item, IEqualityComparator<? super T> comparator) {
		if (comparator == null) {
			comparator = IEqualityComparator.getDefault();
		}

		boolean result = false;

		for (T local : this) {
			if (comparator.equals(local, item)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * シーケンスに指定した要素がすべて含まれているかどうかを判断します。
	 *
	 * @param second
	 * @return
	 */
	default boolean containsAllValues(Iterable<? extends T> second) {
		boolean result = true;

		for (T item : second) {
			if (!this.contains(item)) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * 指定した比較方法を使用して、シーケンスに指定した要素がすべて含まれているかどうかを判断します。
	 *
	 * @param second
	 * @param comparator
	 * @return
	 */
	default boolean containsAllValues(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		boolean result = true;

		for (T item : second) {
			if (!this.contains(item, comparator)) {
				result = false;
				break;
			}
		}

		return result;
	}

	/**
	 * 指定した比較方法を使用して、シーケンスに指定した要素が 1 つ以上含まれているかどうかを判断します。
	 *
	 * @param second
	 * @return
	 */
	default boolean containsAnyValue(Iterable<? extends T> second) {
		boolean result = false;

		for (T item : second) {
			if (this.contains(item)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * シーケンスに指定した要素が 1 つ以上含まれているかどうかを判断します。
	 *
	 * @param second
	 * @param comparator
	 * @return
	 */
	default boolean containsAnyValue(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		boolean result = false;

		for (T item : second) {
			if (this.contains(item, comparator)) {
				result = true;
				break;
			}
		}

		return result;
	}

	/**
	 * シーケンスの要素数を取得します。
	 *
	 * @return
	 */
	default int count() {
		int count = 0;

		Iterator<T> itr = this.iterator();
		while (itr.hasNext()) {
			itr.next();
			count = Math.addExact(count, 1);
		}

		return count;
	}

	/**
	 * 条件を満たす要素の数を取得します。
	 *
	 * @param predicate
	 * @return
	 */
	default int count(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		int count = 0;

		for (T item : this) {
			if (predicate.test(item)) {
				count = Math.addExact(count, 1);
			}
		}

		return count;
	}

	/**
	 * シーケンスに要素が含まれている場合はそれを取得します。空の場合は null を一つだけ含むシーケンスを取得します。
	 *
	 * @return
	 */
	default IEnumerable<T> defaultIfEmpty() {
		T defaultValue = null;
		return this.defaultIfEmpty(defaultValue);
	}

	/**
	 * シーケンスに要素が含まれている場合はそれを取得します。空の場合は指定した値を一つだけ含むシーケンスを取得します。
	 * @param defaultValue
	 * @return
	 */
	default IEnumerable<T> defaultIfEmpty(T defaultValue) {
		IEnumerable<T> defaultValues = Enumerable.of(defaultValue);
		return this.defaultIfEmpty(defaultValues);
	}

	/**
	 * シーケンスに要素が含まれている場合はそれを取得します。空の場合は指定したシーケンスを取得します。
	 *
	 * @param defaultValues
	 * @return
	 */
	default IEnumerable<T> defaultIfEmpty(T... defaultValues) {
		return () -> new LinqDefaultIfEmptyIterator<>(this, Enumerable.of(defaultValues));
	}

	/**
	 * シーケンスに要素が含まれている場合はそれを取得します。空の場合は指定したシーケンスを取得します。
	 *
	 * @param defaultValues
	 * @return
	 */
	default IEnumerable<T> defaultIfEmpty(Iterable<? extends T> defaultValues) {
		return () -> new LinqDefaultIfEmptyIterator<>(this, defaultValues);
	}

	/**
	 * 要素の重複のないシーケンスを取得します。
	 *
	 * @return
	 */
	default IEnumerable<T> distinct() {
		return this.distinct(IEqualityComparator.getDefault());
	}

	/**
	 * 指定した比較方法を使用して、要素の重複のないシーケンスを取得します。
	 *
	 * @param comparator
	 * @return
	 */
	default IEnumerable<T> distinct(IEqualityComparator<? super T> comparator) {
		return () -> new LinqDistinctIterator<>(this, comparator);
	}

	/**
	 * 指定されたインデックス位置にある要素を取得します。
	 *
	 * @param index
	 * @return
	 */
	default T elementAt(int index) {
		if (index < 0) {
			throw Errors.argumentOfOutOfRange("index");
		}

		int i = -1;
		for (T item : this) {
			i++;
			if (i == index) {
				return item;
			}
		}

		throw Errors.argumentOfOutOfRange("index");
	}

	/**
	 * 指定されたインデックス位置にある要素を取得します。インデックスが範囲外の場合は null を取得します。
	 *
	 * @param index
	 * @return
	 */
	default T elementAtOrDefault(int index) {
		return this.elementAtOrDefault(index, () -> null);
	}

	/**
	 * 指定されたインデックス位置にある要素を取得します。インデックスが範囲外の場合は指定した値を取得します。
	 *
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	default T elementAtOrDefault(int index, T defaultValue) {
		return this.elementAtOrDefault(index, () -> defaultValue);
	}

	/**
	 * 指定されたインデックス位置にある要素を取得します。インデックスが範囲外の場合は指定した関数によって返された値を取得します。
	 *
	 * @param index
	 * @param defaultValue
	 * @return
	 */
	default T elementAtOrDefault(int index, Func0<T> defaultValue) {
		if (0 <= index) {
			int i = -1;
			for (T item : this) {
				i++;
				if (i == index) {
					return item;
				}
			}
		}

		return defaultValue.func();
	}

	/**
	 * シーケンスの差集合を取得します。
	 *
	 * @param second
	 * @return
	 */
	default IEnumerable<T> except(Iterable<? extends T> second) {
		return this.except(second, null);
	}

	/**
	 * 指定した比較方法を利用して、シーケンスの差集合を取得します。
	 *
	 * @param second
	 * @param comparator
	 * @return
	 */
	default IEnumerable<T> except(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		return () -> new LinqExceptIterator<>(this, second, comparator);
	}

	/**
	 * シーケンスから指定した要素を検索して、最初に見つかった要素のインデックスを取得します。
	 * @param item
	 * @return
	 */
	default int findIndex(T item) {
		return this.findIndex(val -> Objects.equals(val, item));
	}

	/**
	 * シーケンスから検索条件を満たす要素を検索して、最初に見つかった要素のインデックスを取得します。
	 * @param match
	 * @return
	 */
	default int findIndex(Predicate<? super T> match) {
		Errors.throwIfNull(match, "match");

		int result = -1;

		int idx = -1;
		for (T item : this) {
			idx++;
			if (match.test(item)) {
				result = idx;
				break;
			}
		}

		return result;
	}

	/**
	 * シーケンスの最初の要素を取得します。シーケンスに要素が含まれていない場合はエラーとなります。
	 *
	 * @return
	 */
	default T first() {
		for (T item : this) {
			return item;
		}

		throw Errors.empty();
	}

	/**
	 * 条件を満たす最初の要素を取得します。
	 *
	 * @param predicate
	 * @return
	 */
	default T first(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		for (T item : this) {
			if (predicate.test(item)) {
				return item;
			}
		}

		throw Errors.noMatch();
	}

	/**
	 * シーケンスの最初の要素を取得します。シーケンスに要素が含まれていない場合は null を取得します。
	 *
	 * @return
	 */
	default T firstOrDefault() {
		return this.firstOrDefault((T) null);
	}

	/**
	 * シーケンスの最初の要素を取得します。シーケンスに要素が含まれていない場合は指定した規定値を取得します。
	 *
	 * @param defaultValue
	 * @return
	 */
	default T firstOrDefault(T defaultValue) {
		Iterator<T> itr = this.iterator();
		return itr.hasNext() ? itr.next() : defaultValue;
	}

	/**
	 * 条件を満たす最初の要素を取得します。シーケンスに要素が含まれていない場合は null を取得します。
	 *
	 * @param predicate
	 * @return
	 */
	default T firstOrDefault(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return this.firstOrDefault(predicate, null);
	}

	/**
	 * 条件を満たす最初の要素を取得します。シーケンスに要素が含まれていない場合は指定した規定値を取得します。
	 *
	 * @param predicate
	 * @param defaultValue
	 * @return
	 */
	default T firstOrDefault(Predicate<? super T> predicate, T defaultValue) {
		Errors.throwIfNull(predicate, "predicate");

		T value = defaultValue;

		for (T local : this) {
			if (predicate.test(local)) {
				value = local;
				break;
			}
		}

		return value;
	}

	/**
	 * シーケンスのすべての要素にインデックス位置を利用した処理を実行します。
	 * @param action
	 */
	default void forEach(IndexedAction1<? super T> action) {
		Errors.throwIfNull(action, "action");

		int i = -1;
		for (T item : this) {
			i = Math.addExact(i, 1);
			action.action(item, i);
		}
	}

	default <TKey> IEnumerable<IGrouping<TKey, T>> groupBy(Func1<? super T, TKey> keySelector) {
		return new LinqGroupByIterable.Iterable1<>(this, keySelector, n -> n, null);
	}

	default <TKey> IEnumerable<IGrouping<TKey, T>> groupBy(
			Func1<? super T, TKey> keySelector,
			IEqualityComparator<TKey> comparator) {

		return new LinqGroupByIterable.Iterable1<>(this, keySelector, n -> n, comparator);
	}

	default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(
			Func1<? super T, TKey> keySelector,
			Func1<? super T, TElement> elementSelector) {

		return new LinqGroupByIterable.Iterable1<>(this, keySelector, elementSelector, null);
	}

	default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(
			Func1<? super T, TKey> keySelector,
			Func1<? super T, TElement> elementSelector,
			IEqualityComparator<TKey> comparator) {

		return new LinqGroupByIterable.Iterable1<>(this, keySelector, elementSelector, comparator);
	}

	default <TKey, TResult> IEnumerable<TResult> groupBy(
			Func1<? super T, TKey> keySelector,
			Func2<TKey, ? super IEnumerable<T>, TResult> resultSelector) {

		return new LinqGroupByIterable.Iterable2<>(this, keySelector, n -> n, resultSelector, null);
	}

	default <TKey, TResult> IEnumerable<TResult> groupBy(
			Func1<? super T, TKey> keySelector,
			Func2<TKey, ? super IEnumerable<T>, TResult> resultSelector,
			IEqualityComparator<TKey> comparator) {

		return new LinqGroupByIterable.Iterable2<>(this, keySelector, n -> n, resultSelector, comparator);
	}

	default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(
			Func1<? super T, TKey> keySelector,
			Func1<? super T, TElement> elementSelector,
			Func2<TKey, ? super IEnumerable<TElement>, TResult> resultSelector) {

		return new LinqGroupByIterable.Iterable2<>(this, keySelector, elementSelector, resultSelector, null);
	}

	default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(
			Func1<? super T, TKey> keySelector,
			Func1<? super T, TElement> elementSelector,
			Func2<TKey, ? super IEnumerable<TElement>, TResult> resultSelector,
			IEqualityComparator<TKey> comparator) {

		return new LinqGroupByIterable.Iterable2<>(this, keySelector, elementSelector, resultSelector, comparator);
	}

	default <TInner, TKey> IEnumerable<JoinedValue<T, IEnumerable<TInner>>> groupJoin(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector) {

		Func2<T, Iterable<? super TInner>, JoinedValue<T, IEnumerable<TInner>>> resultSelector =
				(arg1, arg2) -> {
					IEnumerable<TInner> inner_ = (IEnumerable<TInner>) new EnumerableList<>(arg2);
					return new JoinedValue<>(arg1, inner_);
				};

		return this.groupJoin(inner, outerKeySelector, innerKeySelector, resultSelector);
	}

	default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			Func2<? super T, ? super IEnumerable<TInner>, TResult> resultSelector) {

		return this.groupJoin(inner, outerKeySelector, innerKeySelector, resultSelector, null);
	}

	default <TInner, TKey> IEnumerable<JoinedValue<T, IEnumerable<TInner>>> groupJoin(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			IEqualityComparator<? super TKey> comparator) {

		Func2<T, Iterable<? super TInner>, JoinedValue<T, IEnumerable<TInner>>> resultSelector =
				(arg1, arg2) -> {
					IEnumerable<TInner> inner_ = (IEnumerable<TInner>) new EnumerableList<>(arg2);
					return new JoinedValue<>(arg1, inner_);
				};

		return this.groupJoin(inner, outerKeySelector, innerKeySelector, resultSelector, comparator);
	}

	default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			Func2<? super T, ? super IEnumerable<TInner>, TResult> resultSelector,
			IEqualityComparator<? super TKey> comparator) {

		Errors.throwIfNull(inner, "inner");
		Errors.throwIfNull(outerKeySelector, "outerKeySelector");
		Errors.throwIfNull(innerKeySelector, "innerKeySelector");
		Errors.throwIfNull(resultSelector, "resultSelector");

		return () -> new LinqGroupJoinIterator<>(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparator);
	}

	/**
	 * 積集合を作成します。
	 * @param second
	 * @return
	 */
	default IEnumerable<T> intersect(Iterable<? extends T> second) {
		Errors.throwIfNull(second, "second");
		return this.intersect(second, null);
	}

	/**
	 * 積集合を作成します。
	 * @param second
	 * @param comparator
	 * @return
	 */
	default IEnumerable<T> intersect(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		Errors.throwIfNull(second, "second");
		return () -> new LinqIntersectIterator<>(this, second, comparator);
	}

	default <TInner, TKey> IEnumerable<JoinedValue<T, TInner>> join(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector) {

		Func2<T, TInner, JoinedValue<T, TInner>> resultSelector = (arg1, arg2) -> new JoinedValue<>(arg1, arg2);
		return this.join(inner, outerKeySelector, innerKeySelector, resultSelector);
	}

	default <TInner, TKey, TResult> IEnumerable<TResult> join(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			Func2<? super T, ? super TInner, TResult> resultSelector) {

		return this.join(inner, outerKeySelector, innerKeySelector, resultSelector, null);
	}

	default <TInner, TKey> IEnumerable<JoinedValue<T, TInner>> join(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			IEqualityComparator<? super TKey> comparator) {

		Func2<T, TInner, JoinedValue<T, TInner>> resultSelector = (arg1, arg2) -> new JoinedValue<>(arg1, arg2);
		return this.join(inner, outerKeySelector, innerKeySelector, resultSelector, comparator);
	}

	default <TInner, TKey, TResult> IEnumerable<TResult> join(
			Iterable<TInner> inner,
			Func1<? super T, TKey> outerKeySelector,
			Func1<? super TInner, TKey> innerKeySelector,
			Func2<? super T, ? super TInner, TResult> resultSelector,
			IEqualityComparator<? super TKey> comparator) {

		Errors.throwIfNull(inner, "inner");
		Errors.throwIfNull(outerKeySelector, "outerKeySelector");
		Errors.throwIfNull(innerKeySelector, "innerKeySelector");
		Errors.throwIfNull(resultSelector, "resultSelector");

		return () -> new LinqJoinIterator<>(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparator);
	}

	default T last() {
		Iterator<T> itr = this.iterator();
		if (itr.hasNext()) {
			T lastItem;
			do {
				lastItem = itr.next();
			} while (itr.hasNext());
			return lastItem;
		}

		throw Errors.empty();
	}

	default T last(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		T lastItem = null;
		boolean found = false;

		for (T item : this) {
			if (predicate.test(item)) {
				lastItem = item;
				found = true;
			}
		}

		if (found) {
			return lastItem;
		}

		throw Errors.noMatch();
	}

	default T lastOrDefault() {
		return this.lastOrDefault((T) null);
	}

	default T lastOrDefault(T defaultValue) {
		T value = defaultValue;

		for (T tmp : this) {
			value = tmp;
		}

		return value;
	}

	default T lastOrDefault(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return this.lastOrDefault(predicate, null);
	}

	default T lastOrDefault(Predicate<? super T> predicate, T defaultValue) {
		Errors.throwIfNull(predicate, "predicate");

		T value = defaultValue;

		for (T local : this) {
			if (predicate.test(local)) {
				value = local;
			}
		}

		return value;
	}

	/**
	 * シーケンスの要素数を返します。
	 * @return
	 */
	default long longCount() {
		long count = 0;

		Iterator<T> itr = this.iterator();
		while (itr.hasNext()) {
			itr.next();
			count = Math.addExact(count, 1);
		}

		return count;
	}

	/**
	 * 条件を満たす要素の数を返します。
	 * @param predicate
	 * @return
	 */
	default long longCount(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		long count = 0;

		Iterator<T> itr = this.iterator();
		while (itr.hasNext()) {
			T item = itr.next();
			if (predicate.test(item)) {
				count = Math.addExact(count, 1);
			}
		}

		return count;
	}

	default T max() {
		try {
			return this.max(ComparatorUtils.getDefault());
		} catch (ClassCastException e) {
			throw Errors.notComparable();
		}
	}

	default T max(Comparator<? super T> comparator) {
		Errors.throwIfNull(comparator, "comparator");

		T result = null;

		for (T item : this) {
			if (item != null) {
				if (result == null) {
					result = item;
				} else if (comparator.compare(result, item) < 0) {
					result = item;
				}
			}
		}

		return result;
	}

	default <TResult extends Comparable<TResult>> TResult max(Func1<? super T, ? extends TResult> selector) {
		return this.select(selector).max();
	}

	default Double maxDouble(Func1<? super T, Double> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectDouble(selector).max();
	}

	default Float maxFloat(Func1<? super T, Float> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectFloat(selector).max();
	}

	default Integer maxInt(Func1<? super T, Integer> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectInteger(selector).max();
	}

	default Long maxLong(Func1<? super T, Long> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectLong(selector).max();
	}

	default BigDecimal maxBigDecimal(Func1<? super T, BigDecimal> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectBigDecimal(selector).max();
	}

	default T min() {
		try {
			return this.min(ComparatorUtils.getDefault());
		} catch (ClassCastException e) {
			throw Errors.notComparable();
		}
	}

	default T min(Comparator<? super T> comparator) {
		Errors.throwIfNull(comparator, "comparator");

		T result = null;

		for (T item : this) {
			if (item != null) {
				if (result == null) {
					result = item;
				} else if (comparator.compare(result, item) > 0) {
					result = item;
				}
			}
		}

		return result;
	}

	default Double minDouble(Func1<? super T, Double> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectDouble(selector).min();
	}

	default Float minFloat(Func1<? super T, Float> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectFloat(selector).min();
	}

	default Integer minInt(Func1<? super T, Integer> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectInteger(selector).min();
	}

	default Long minLong(Func1<? super T, Long> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectLong(selector).min();
	}

	default BigDecimal minBigDecimal(Func1<? super T, BigDecimal> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectBigDecimal(selector).min();
	}

	default <TResult extends T> IEnumerable<TResult> ofType(Class<TResult> elementClass) {
		Errors.throwIfNull(elementClass, "elementClass");
		return () -> new LinqOfTypeIterator<>(this, elementClass);
	}

	default <TKey extends Comparable<TKey>> IOrderedEnumerable<T> orderBy(Func1<? super T, TKey> keySelector) {
		Errors.throwIfNull(keySelector, "keySelector");

		Comparator<? super T> comparator = Comparator.comparing(keySelector::func);
		comparator = Comparator.nullsFirst(comparator); // null 優先

		return this.orderBy(comparator);
	}

	default <TKey> IOrderedEnumerable<T> orderBy(Func1<? super T, TKey> keySelector, Comparator<? super TKey> comparator) {
		Errors.throwIfNull(keySelector, "keySelector");

		if (comparator == null) {
			comparator = ComparatorUtils.getDefault();
		}

		return this.orderBy(Comparator.comparing(keySelector::func, comparator));
	}

	default IOrderedEnumerable<T> orderBy(Comparator<? super T> comparator) {
		if (comparator == null) {
			comparator = ComparatorUtils.getDefault();
		}

		return new OrderedEnumerable<>(this, comparator);
	}

	default IOrderedEnumerable<T> orderByDefaultKey() {
		Comparator<T> comparator = ComparatorUtils.getDefault();
		return this.orderBy(comparator);
	}

	default IOrderedEnumerable<T> orderByDefaultKeyDescending() {
		Comparator<T> comparator = ComparatorUtils.getDefault();
		return this.orderByDescending(comparator);
	}

	default <TKey extends Comparable<TKey>> IOrderedEnumerable<T> orderByDescending(Func1<? super T, TKey> keySelector) {
		Errors.throwIfNull(keySelector, "keySelector");

		Comparator<? super T> comparator = Comparator.comparing(keySelector::func);
		comparator = Comparator.nullsFirst(comparator); // null 優先

		return this.orderByDescending(comparator);
	}

	default <TKey> IOrderedEnumerable<T> orderByDescending(Func1<? super T, TKey> keySelector, Comparator<? super TKey> comparator) {
		Errors.throwIfNull(keySelector, "keySelector");

		if (comparator == null) {
			comparator = ComparatorUtils.getDefault();
		}

		return this.orderByDescending(Comparator.comparing(keySelector::func, comparator));
	}

	default IOrderedEnumerable<T> orderByDescending(Comparator<? super T> comparator) {
		if (comparator == null) {
			comparator = ComparatorUtils.getDefault();
		}

		return new OrderedEnumerable<>(this, comparator.reversed());
	}

	default IEnumerable<T> reverse() {
		return () -> new LinqReverseIterator<>(this);
	}

	default <TResult> IEnumerable<TResult> select(Func1<? super T, TResult> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default <TResult> IEnumerable<TResult> select(IndexedFunc1<? super T, TResult> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IBigDecimalEnumerable selectBigDecimal(Func1<? super T, BigDecimal> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IBigDecimalEnumerable selectBigDecimal(IndexedFunc1<? super T, BigDecimal> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IBooleanEnumerable selectBoolean(Func1<? super T, Boolean> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IBooleanEnumerable selectBoolean(IndexedFunc1<? super T, Boolean> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IByteEnumerable selectByte(Func1<? super T, Byte> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IByteEnumerable selectByte(IndexedFunc1<? super T, Byte> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default ICharacterEnumerable selectCharacter(Func1<? super T, Character> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default ICharacterEnumerable selectCharacter(IndexedFunc1<? super T, Character> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IDoubleEnumerable selectDouble(Func1<? super T, Double> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IDoubleEnumerable selectDouble(IndexedFunc1<? super T, Double> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IFloatEnumerable selectFloat(Func1<? super T, Float> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IFloatEnumerable selectFloat(IndexedFunc1<? super T, Float> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IIntegerEnumerable selectInteger(Func1<? super T, Integer> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IIntegerEnumerable selectInteger(IndexedFunc1<? super T, Integer> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default ILongEnumerable selectLong(Func1<? super T, Long> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default ILongEnumerable selectLong(IndexedFunc1<? super T, Long> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IShortEnumerable selectShort(Func1<? super T, Short> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default IShortEnumerable selectShort(IndexedFunc1<? super T, Short> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectIterator<>(this, selector);
	}

	default <TResult> IEnumerable<TResult> selectMany(Func1<? super T, Iterable<TResult>> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectMany((item, idx) -> selector.func(item));
	}

	default <TResult> IEnumerable<TResult> selectMany(IndexedFunc1<? super T, Iterable<TResult>> selector) {
		Errors.throwIfNull(selector, "selector");
		return () -> new LinqSelectManyIterator<>(this, selector);
	}

	default boolean sequenceEqual(Iterable<? extends T> second) {
		return this.sequenceEqual(second, null);
	}

	default boolean sequenceEqual(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		Errors.throwIfNull(second, "second");

		if (comparator == null) {
			comparator = IEqualityComparator.getDefault();
		}

		Iterator<T> itr1 = this.iterator();
		Iterator<? extends T> itr2 = second.iterator();

		boolean equal = true;

		while (itr1.hasNext()) {
			if (!itr2.hasNext()) {
				equal = false;
				break;
			} else if (!comparator.equals(itr1.next(), itr2.next())) {
				equal = false;
				break;
			}
		}

		if (equal && itr2.hasNext()) {
			equal = false;
		}

		return equal;
	}

	/**
	 * 指定した数の要素をスキップして残りの要素を列挙したあと、スキップした要素を列挙します。
	 * @param count スキップする要素の数。負数を指定することができます。
	 * @return
	 */
	default IEnumerable<T> shift(int count) {
		return () -> new LinqShiftIterator<>(this, count);
	}

	default IEnumerable<T> shuffle() {
		return this.shuffle(null);
	}

	default IEnumerable<T> shuffle(Random rnd) {
		return () -> new LinqShuffleIterator<>(this, rnd);
	}

	default T single() {
		T value = null;

		Iterator<T> i = this.iterator();

		if (i.hasNext()) {
			value = i.next();
		} else {
			throw Errors.empty();
		}

		if (i.hasNext()) {
			throw Errors.moreThanOneElement();
		} else {
			return value;
		}
	}

	default T single(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");

		T value = null;

		int count = 0;

		for (T local : this) {
			if (predicate.test(local)) {
				value = local;
				count++;
				if (2 <= count) {
					break;
				}
			}
		}

		switch (count) {
			case 0:
				throw Errors.noMatch();

			case 1:
				return value;

			default:
				throw Errors.moreThanOneMatch();
		}
	}

	default T singleOrDefault() {
		return this.singleOrDefault((T) null);
	}

	default T singleOrDefault(T defaultValue) {
		T value = defaultValue;

		Iterator<T> i = this.iterator();

		if (i.hasNext()) {
			value = i.next();
		}

		if (i.hasNext()) {
			return defaultValue;
		} else {
			return value;
		}
	}

	default T singleOrDefault(Predicate<? super T> predicate) {
		return this.singleOrDefault(predicate, null);
	}

	default T singleOrDefault(Predicate<? super T> predicate, T defaultValue) {
		Errors.throwIfNull(predicate, "predicate");

		T value = null;

		int count = 0;

		for (T local : this) {
			if (predicate.test(local)) {
				value = local;
				count++;
				if (2 <= count) {
					break;
				}
			}
		}

		if (count == 1) {
			return value;
		} else {
			return defaultValue;
		}
	}

	default IEnumerable<T> skip(int count) {
		if (count < 0) {
			throw Errors.argumentOfOutOfRange("count");
		}

		return this.skipWhile((item, index) -> index < count);
	}

	default IEnumerable<T> skipWhile(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqSkipWhileIterator<>(this, predicate);
	}

	default IEnumerable<T> skipWhile(IndexedPredicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqSkipWhileIterator<>(this, predicate);
	}

	/**
	 * セレクター関数を使用して要素から取得した値の合計を取得します。セレクター関数の結果が null の場合は 0 として扱います。
	 * @param selector
	 * @return
	 */
	default double sumDouble(Func1<? super T, Double> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectDouble(selector).sum();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の合計を取得します。セレクター関数の結果が null の場合は 0 として扱います。
	 * @param selector
	 * @return
	 */
	default double sumFloat(Func1<? super T, Float> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectFloat(selector).sum();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の合計を取得します。セレクター関数の結果が null の場合は 0 として扱います。
	 * @param selector
	 * @return
	 */
	default int sumInteger(Func1<? super T, Integer> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectInteger(selector).sum();
	}

	/**
	 * セレクター関数を使用して要素から取得した値の合計を取得します。セレクター関数の結果が null の場合は 0 として扱います。
	 * @param selector
	 * @return
	 */
	default long sumLong(Func1<? super T, Long> selector) {
		Errors.throwIfNull(selector, "selector");
		return this.selectLong(selector).sum();
	}

	default IEnumerable<T> take(int count) {
		if (count < 0) {
			throw Errors.argumentOfOutOfRange("count");
		}

		return this.takeWhile((item, index) -> index < count);
	}

	default IEnumerable<T> takeWhile(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqTakeWhileIterator<>(this, predicate);
	}

	default IEnumerable<T> takeWhile(IndexedPredicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqTakeWhileIterator<>(this, predicate);
	}

	default T[] toArray(Class<T> elementClass) {
		Errors.throwIfNull(elementClass, "elementClass");

		List<?> list = this.toList();
		T[] array = (T[]) Array.newInstance(elementClass, list.size());

		return list.toArray(array);
	}

	/**
	 * EnumerableSet&lt;T&gt; を作成します。
	 * @return
	 */
	default EnumerableSet<T> toHashSet() {
		return new EnumerableSet<>(this);
	}

	/**
	 * 要素を連結した文字列を作成します。各要素の間に指定した区切り文字列が挿入されます。
	 * @param separator
	 * @return
	 */
	default String toString(String separator) {
		return this.toString(separator, item -> item);
	}

	/**
	 * セレクター関数で要素から取得した値を連結した文字列を作成します。各要素の間に指定した区切り文字列が挿入されます。
	 * @param separator
	 * @param selector
	 * @return
	 */
	default String toString(String separator, Func1<? super T, ? extends Object> selector) {
		Errors.throwIfNull(selector, "selector");

		if (separator == null) {
			separator = "";
		}

		boolean first = true;
		StringBuilder sb = new StringBuilder();
		for (T item : this) {
			if (first) {
				first = false;
			} else {
				sb.append(separator);
			}

			Object obj = selector.func(item);
			if (obj != null) {
				sb.append(obj);
			}
		}

		return sb.toString();
	}

	/**
	 * EnumerableList&lt;T&gt; を作成します。
	 * @return
	 */
	default EnumerableList<T> toList() {
		return new EnumerableList<>(this);
	}

	default <TKey> EnumerableMap<TKey, T> toMap(Func1<? super T, TKey> keySelector) {
		Errors.throwIfNull(keySelector, "keySelector");

		EnumerableMap<TKey, T> map = new EnumerableMap<>();

		for (T item : this) {
			TKey key = keySelector.func(item);
			map.putOrThrow(key, item);
		}

		return map;
	}

	default <TKey> EnumerableMap<TKey, T> toMap(IndexedFunc1<? super T, TKey> keySelector) {
		Errors.throwIfNull(keySelector, "keySelector");

		EnumerableMap<TKey, T> map = new EnumerableMap<>();

		int i = -1;
		for (T item : this) {
			i = Math.addExact(i, 1);
			TKey key = keySelector.func(item, i);
			map.putOrThrow(key, item);
		}

		return map;
	}

	default <TKey, TValue> EnumerableMap<TKey, TValue> toMap(Func1<? super T, TKey> keySelector, Func1<? super T, TValue> valueSelector) {
		Errors.throwIfNull(keySelector, "keySelector");
		Errors.throwIfNull(valueSelector, "valueSelector");

		EnumerableMap<TKey, TValue> map = new EnumerableMap<>();

		for (T item : this) {
			TKey key = keySelector.func(item);
			TValue value = valueSelector.func(item);
			map.putOrThrow(key, value);
		}

		return map;
	}

	default <TKey, TValue> EnumerableMap<TKey, TValue> toMap(IndexedFunc1<? super T, TKey> keySelector, IndexedFunc1<? super T, TValue> valueSelector) {
		Errors.throwIfNull(keySelector, "keySelector");
		Errors.throwIfNull(valueSelector, "valueSelector");

		EnumerableMap<TKey, TValue> map = new EnumerableMap<>();

		int i = -1;
		for (T item : this) {
			i = Math.addExact(i, 1);
			TKey key = keySelector.func(item, i);
			TValue value = valueSelector.func(item, i);
			map.putOrThrow(key, value);
		}

		return map;
	}

	default <TKey> ILookup<TKey, T> toLookup(Func1<? super T, TKey> keySelector) {
		return Lookup.create(this, keySelector, n -> n, null);
	}

	default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<? super T, TKey> keySelector, Func1<? super T, TElement> elementSelector) {
		return Lookup.create(this, keySelector, elementSelector, null);
	}

	default <TKey> ILookup<TKey, T> toLookup(Func1<? super T, TKey> keySelector, IEqualityComparator<? super TKey> comparator) {
		return Lookup.create(this, keySelector, n -> n, comparator);
	}

	default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<? super T, TKey> keySelector, Func1<? super T, TElement> elementSelector, IEqualityComparator<? super TKey> comparator) {
		return Lookup.create(this, keySelector, elementSelector, comparator);
	}

	default IEnumerable<T> union(Iterable<? extends T> second) {
		Errors.throwIfNull(second, "second");
		return this.union(second, null);
	}

	default IEnumerable<T> union(Iterable<? extends T> second, IEqualityComparator<? super T> comparator) {
		Errors.throwIfNull(second, "second");
		return () -> new LinqUnionIterator<>(this, second, comparator);
	}

	default IEnumerable<T> where(Predicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqWhereIterator<>(this, predicate);
	}

	default IEnumerable<T> where(IndexedPredicate<? super T> predicate) {
		Errors.throwIfNull(predicate, "predicate");
		return () -> new LinqWhereIterator<>(this, predicate);
	}

	default <TSecond, TResult> IEnumerable<TResult> zip(Iterable<TSecond> second, Func2<? super T, ? super TSecond, TResult> resultSelector) {
		Errors.throwIfNull(second, "second");
		return () -> new LinqZipIterator<>(this, second, resultSelector);
	}
}
