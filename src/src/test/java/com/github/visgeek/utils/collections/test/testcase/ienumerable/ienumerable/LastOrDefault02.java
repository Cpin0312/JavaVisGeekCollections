package com.github.visgeek.utils.collections.test.testcase.ienumerable.ienumerable;

import org.junit.Assert;
import org.junit.Test;

import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.IEnumerable;

public class LastOrDefault02 {
	@Test
	public void empty() {
		IEnumerable<Integer> source = Enumerable.empty();
		Integer defaultValue = 0;
		Integer expected = defaultValue;

		Integer actual = source.lastOrDefault(defaultValue);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void any() {
		IEnumerable<Integer> source = () -> Enumerable.of(1, 2, 3).iterator();
		Integer defaultValue = 0;
		Integer expected = 3;

		Integer actual = source.lastOrDefault(defaultValue);
		Assert.assertEquals(expected, actual);
	}
}
