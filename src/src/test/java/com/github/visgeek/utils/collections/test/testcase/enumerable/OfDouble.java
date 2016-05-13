package com.github.visgeek.utils.collections.test.testcase.enumerable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.visgeek.utils.collections.Enumerable;
import com.github.visgeek.utils.collections.IEnumerable;
import com.github.visgeek.utils.testing.Assert2;

public class OfDouble {
	@Test
	public void iEnumerable_empty() {
		IEnumerable<Double> source = Enumerable.ofDouble(Arrays.asList());
		IEnumerable<Double> actual = Enumerable.ofDouble(source);
		Assert.assertSame(source, actual);
	}

	@Test
	public void iEnumerable_normal() {
		IEnumerable<Double> source = Enumerable.of(1.0, 2.0, 3.0);
		IEnumerable<Double> actual = Enumerable.ofDouble(source);
		Assert.assertSame(source, actual);
	}

	@Test
	public void primitiveArray_empty() {
		double[] values = new double[] {};
		IEnumerable<Double> actual = Enumerable.of(values);

		Assert2.assertSequanceEquals(actual);
	}

	@Test
	public void primitiveArray_normal() {
		double[] values = new double[] { 1.0, 2.0, 3.0 };
		IEnumerable<Double> actual = Enumerable.of(values);

		Assert2.assertSequanceEquals(actual, 1.0, 2.0, 3.0);
	}

	@Test
	public void objectArray_empty() {
		Double[] values = new Double[] {};
		IEnumerable<Double> actual = Enumerable.of(values);

		Assert2.assertSequanceEquals(actual);
	}

	@Test
	public void objectArray_normal() {
		Double[] values = new Double[] { 1.0, 2.0, 3.0 };
		IEnumerable<Double> actual = Enumerable.of(values);

		Assert2.assertSequanceEquals(actual, 1.0, 2.0, 3.0);
	}

	@Test
	public void randomAccesss_empty() {
		List<Double> values = new ArrayList<>();
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual);
	}

	@Test
	public void randomAccesss_normal() {
		List<Double> values = new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0));
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual, 1.0, 2.0, 3.0);
	}

	@Test
	public void collection_empty() {
		List<Double> values = new LinkedList<>();
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual);
	}

	@Test
	public void collection_normal() {
		List<Double> values = new LinkedList<>(Arrays.asList(1.0, 2.0, 3.0));
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual, 1.0, 2.0, 3.0);
	}

	@Test
	public void iterable_empty() {
		Iterable<Double> values = () -> new ArrayList<Double>().iterator();
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual);
	}

	@Test
	public void iterable_normal() {
		Iterable<Double> values = () -> new ArrayList<>(Arrays.asList(1.0, 2.0, 3.0)).iterator();
		IEnumerable<Double> actual = Enumerable.ofDouble(values);

		Assert2.assertSequanceEquals(actual, 1.0, 2.0, 3.0);
	}
}