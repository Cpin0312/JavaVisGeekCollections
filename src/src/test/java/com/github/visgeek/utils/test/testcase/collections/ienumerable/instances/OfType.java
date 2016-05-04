package com.github.visgeek.utils.test.testcase.collections.ienumerable.instances;

import org.junit.Test;

import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.IEnumerable;
import com.github.visgeek.utils.testing.Assert2;

public class OfType {
	@Test
	public void test_ofType01() {
		IEnumerable<Integer> actual =
				Enumerable
						.of((Object) 1, 2, "a", null, "b", 3)
						.ofType(Integer.class)
						.toList();

		Assert2.assertSequanceEquals(actual, 1, 2, 3);
	}

	@Test
	public void test_ofType02() {
		IEnumerable<Integer> actual =
				Enumerable
						.of((Object) 1, (Object) "2", (Object) 3, (Object) "4")
						.ofType(Integer.class)
						.toList();

		Assert2.assertSequanceEquals(actual, 1, 3);
	}
}