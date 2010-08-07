package com.barefoot.seismicdroid.utils;

public class StringUtils {

	public static boolean isNullOrBlank(String valueToCheck) {
		if (null == valueToCheck || valueToCheck.trim().length() == 0)
			return true;
		return false;
	}

	public static String camelCase(String toConvert) {
		return toConvert;
	}
}
