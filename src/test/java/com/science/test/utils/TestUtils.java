package com.science.test.utils;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;

public class TestUtils {
	public static void IsElementsInListIdentical(List<?> list1, List<?> list2) {
		Iterator<?> list1Itr = list1.iterator();
		Iterator<?> list2Itr = list2.iterator();
		while (list1Itr.hasNext() && list2Itr.hasNext()) {
			assertEquals(list1Itr.next(), list2Itr.next());
		}
	}
}
