package com.github.visgeek.utils.collections.test.testcase.ienumerable.instances.ilistenumerable;

import org.junit.Assert;
import org.junit.Test;

import com.github.visgeek.utils.Func0;
import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.IEnumerable;

public class ElementAtOrDefault03 {
	@Test
	public void defaultValueExists_success() {
		IEnumerable<Integer> source = Enumerable.of(1, 2, 3);
		int index = 1;
		Func0<Integer> defaultValue = () -> 0;
		Integer expected = 2;

		Integer actual = source.elementAtOrDefault(index, defaultValue);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void defaultValueExists_fail_01() {
		IEnumerable<Integer> source = Enumerable.of(1, 2, 3);
		int index = -1;
		Func0<Integer> defaultValue = () -> 0;
		Integer expected = defaultValue.func();

		Integer actual = source.elementAtOrDefault(index, defaultValue);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void defaultValueExists_fail_02() {
		IEnumerable<Integer> source = Enumerable.of(1, 2, 3);
		int index = 3;
		Func0<Integer> defaultValue = () -> 0;
		Integer expected = defaultValue.func();

		Integer actual = source.elementAtOrDefault(index, defaultValue);
		Assert.assertEquals(expected, actual);
	}
}
