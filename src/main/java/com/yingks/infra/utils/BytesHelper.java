package com.yingks.infra.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author nathena
 *
 */
public class BytesHelper 
{
	private BytesHelper() {
	}

	public static int toInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	public static short toShort(byte[] bytes) {
		return (short) (((-(short) Byte.MIN_VALUE + (short) bytes[0]) << 8)
				- (short) Byte.MIN_VALUE + (short) bytes[1]);
	}

	public static long toLong(byte[] bytes) {
		if (bytes == null) {
			return 0;
		}
		if (bytes.length != 8) {
			throw new IllegalArgumentException(
					"Expecting 8 byte values to construct a long");
		}
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value = (value << 8) | (bytes[i] & 0xff);
		}
		return value;
	}

	public static byte[] fromInt(int value) {
		byte[] result = new byte[4];
		for (int i = 3; i >= 0; i--) {
			result[i] = (byte) ((0xFFl & value) + Byte.MIN_VALUE);
			value >>>= 8;
		}
		return result;
	}

	public static byte[] fromShort(int shortValue) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (shortValue >> 8);
		bytes[1] = (byte) ((shortValue << 8) >> 8);
		return bytes;
	}

	public static byte[] fromLong(long longValue) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (longValue >> 56);
		bytes[1] = (byte) ((longValue << 8) >> 56);
		bytes[2] = (byte) ((longValue << 16) >> 56);
		bytes[3] = (byte) ((longValue << 24) >> 56);
		bytes[4] = (byte) ((longValue << 32) >> 56);
		bytes[5] = (byte) ((longValue << 40) >> 56);
		bytes[6] = (byte) ((longValue << 48) >> 56);
		bytes[7] = (byte) ((longValue << 56) >> 56);
		return bytes;
	}

	public static String toBinaryString(byte value) {
		String formatted = Integer.toBinaryString(value);
		if (formatted.length() > 8) {
			formatted = formatted.substring(formatted.length() - 8);
		}
		StringBuilder buf = new StringBuilder("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	public static String toBinaryString(int value) {
		String formatted = Long.toBinaryString(value);
		StringBuilder buf = new StringBuilder(StringUtils.repeat('0', 32));
		buf.replace(64 - formatted.length(), 64, formatted);
		return buf.toString();
	}

	public static String toBinaryString(long value) {
		String formatted = Long.toBinaryString(value);
		StringBuilder buf = new StringBuilder(StringUtils.repeat('0', 64));
		buf.replace(64 - formatted.length(), 64, formatted);
		return buf.toString();
	}
}
