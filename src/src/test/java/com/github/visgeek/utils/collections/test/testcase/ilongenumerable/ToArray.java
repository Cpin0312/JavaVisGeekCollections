package com.github.visgeek.utils.collections.test.testcase.ilongenumerable;

import org.junit.Assert;
import org.junit.Test;

import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.ILongEnumerable;

public class ToArray {
	@Test
	public void test() {
		ILongEnumerable source = Enumerable.of(null, 1L, 2L, 3L).selectLong(n -> n);
		long[] expected = new long[] { 0L, 1L, 2L, 3L };

		long[] actual = source.toPrimitiveArray();
		Assert.assertArrayEquals(expected, actual);
	}
}