package com.countriesroute.util;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@UtilityClass
public class MyCollectors {

	public static <T> Collector<T, ?, List<T>> reversing() {
		return Collectors.collectingAndThen(Collectors.toList(), list -> {
			Collections.reverse(list);
			return list;
		});
	}
}
