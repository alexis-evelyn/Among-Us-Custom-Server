package me.alexisevelyn.crewmate.packethandler;

import java.util.ArrayList;

public class PacketHelper {
	public static byte[] mergeBytes(byte[] ...bytes) {
		// TODO: Make this a standard function for creating packets

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

	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte)(value & 0xff), (byte)((value >> 8) & 0xff)};
	}
}
