package com.github.visgeek.utils.collections.test.testcase.ienumerable.instances;

import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

import com.github.visgeek.utils.Action0;
import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.IEnumerable;
import com.github.visgeek.utils.testing.Assert2;

public class IndexOf02 {
	@Test
	public void argIsNull() {
		IEnumerable<Integer> source = () -> Enumerable.of(1, 2, 3).iterator();
		Predicate<Integer> match = null;
		Action0 action = () -> source.findIndex(match);
		Assert2.assertNullPointerExceptionThrown("match", action);
	}

	@Test
	public void found() {
		IEnumerable<Integer> source = () -> Enumerable.of(1, 2, 3, 2).iterator();
		Predicate<Integer> match = n -> n == 2;
		int expected = 1;
		int actual = source.findIndex(match);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void notFound01() {
		IEnumerable<Integer> source = () -> Enumerable.of(1, 2, 3).iterator();
		Predicate<Integer> match = n -> n == 0;
		int expected = -1;
		int actual = source.findIndex(match);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void notFound02() {
		IEnumerable<Integer> source = () -> Enumerable.of(1, 2, 3).iterator();
		Predicate<Integer> match = n -> n == 4;
		int expected = -1;
		int actual = source.findIndex(match);
		Assert.assertEquals(expected, actual);
	}
}
