package me.alexisevelyn.crewmate.packethandler;

import java.util.ArrayList;
import java.util.Arrays;

public class PacketHelper {
	/**
	 * Helper utility to merge byte arrays together without wreaking havoc in the packet creation code
	 *
	 * @param bytes byte[][] of byte[] to merge together as if one giant byte[]
	 * @return the merged results of the array of byte arrays as a byte[]
	 */
	public static byte[] mergeBytes(byte[] ...bytes) {
		// Don't Bother Merging If Nothing To Merge
		if (bytes == null)
			return new byte[0];

		ArrayList<Byte> byteList = new ArrayList<>();

		for (byte[] byteArray : bytes) {
			// You'd think people would know better than to not pass in null values, but just in case, skip this byte[]
			if (byteArray == null)
				continue;

			for (byte bite : byteArray) {
				byteList.add(bite);
			}
		}

		Byte[] mergedBytes = byteList.toArray(new Byte[0]);
		byte[] mergedBytesPrimitive = new byte[mergedBytes.length];

		for (int i = 0; i < mergedBytesPrimitive.length; i++) {
			mergedBytesPrimitive[i] = mergedBytes[i]; // Apparently Unboxing Is Automatic
		}

		return mergedBytesPrimitive;
	}

	/**
	 * Helper utility to grab the second part of the byte array for passing into more refined packet parsing functions
	 *
	 * @param bytes the byte array to split
	 * @param pos the byte to split on. so, to drop only the first byte, specify 0.
	 * @return the second part of the byte array
	 */
	public static byte[] extractBytes(byte[] bytes, int pos) {
		if ((bytes.length - 1) < pos)
			return bytes;

		return Arrays.copyOfRange(bytes, pos, bytes.length - 1);
	}

	/**
	 * Helper utility to convert short into a little endian ordered byte array
	 *
	 * @param value The short to be converted
	 * @return The byte array to be used as you wish
	 */
	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte)(value & 0xff), (byte)((value >> 8) & 0xff)};
	}
}
