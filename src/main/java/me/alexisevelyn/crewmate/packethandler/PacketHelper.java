package me.alexisevelyn.crewmate.packethandler;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class PacketHelper {
	/**
	 * Helper utility to merge byte arrays together without wreaking havoc in the packet creation code
	 *
	 * @param bytes byte[][] of byte[] to merge together as if one giant byte[]
	 * @return the merged results of the array of byte arrays as a byte[]
	 */
	@NotNull
	public static byte[] mergeBytes(@NotNull byte[]... bytes) {
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
	 * @param pos the byte to split on. so, to drop only the first byte, specify 1.
	 * @param bytes the byte array to split
	 * @return the second part of the byte array
	 * @throws InvalidBytesException For null byte array
	 */
	@NotNull
	public static byte[] extractSecondPartBytes(int pos, @NotNull byte... bytes) throws InvalidBytesException {
		if (bytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("invalid_bytes_null_exception"));

		if (bytes.length <= pos)
			return bytes;

		return Arrays.copyOfRange(bytes, pos, bytes.length - 1);
	}

	/**
	 * Helper utility to grab the first part of the byte array for passing into more refined packet parsing functions
	 *
	 * @param pos the byte to split on. so, to keep only the first byte, specify 1.
	 * @param bytes the byte array to split
	 * @return the first part of the byte array
	 * @throws InvalidBytesException For null byte array
	 */
	@NotNull
	public static byte[] extractFirstPartBytes(int pos, @NotNull byte... bytes) {
		if (bytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("invalid_bytes_null_exception"));

		if (bytes.length <= pos)
			return bytes;

		return Arrays.copyOfRange(bytes, 0, pos);
	}

	/**
	 * Helper utility to convert short into a little endian ordered byte array
	 *
	 * @param value The short to be converted
	 * @return The byte array to be used as you wish
	 */
	@NotNull
	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte)(value & 0xff), (byte)((value >> 8) & 0xff)};
	}

	/**
	 * Flips bytes and internally calls {@link #getUnsignedShortLE(byte...)}
	 *
	 * @param shortBytes 2 byte array in Big Endian form
	 * @return unsigned short formatted as a signed int
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	@NotNull
	public static int getUnsignedShortBE(@NotNull byte... shortBytes) throws InvalidBytesException {
		if (shortBytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("invalid_bytes_null_exception"));

		if (shortBytes.length != 2)
			throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_exact"), 2));

		return getUnsignedShortLE(shortBytes[1], shortBytes[0]);
	}

	/**
	 * Retrieves an unsigned short and stores as a signed int.
	 *
	 * A short is 2 bytes or is otherwise known as being 16 bit. An int is 4 bytes or 32 bit.
	 *
	 * Java doesn't have unsigned data types except for char.
	 *
	 * See <a href="https://stackoverflow.com/a/6850755/6828099">this Stackoverflow Answer</a> for more details.
	 * Also see <a href="https://www.darksleep.com/player/JavaAndUnsignedTypes.html">this page</a> for an in depth explanation on handling signed/unsigned data types.
	 *
	 * @param shortBytes 2 byte array in Little Endian form
	 * @return unsigned short formatted as a signed int
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static int getUnsignedShortLE(@NotNull byte... shortBytes) throws InvalidBytesException {
		if (shortBytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("invalid_bytes_null_exception"));

		if (shortBytes.length != 2)
			throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_exact"), 2));

		// Tutorial: https://stackoverflow.com/a/28580238/6828099
		return ((shortBytes[0] & 0xff) | ((shortBytes[1] & 0xff) << 8));
	}

	/**
	 * Public Domain (or Unlicense) Implementation of Packing An Integer For Among Us.
	 *
	 * <br><br>
	 * See <a href="https://wiki.weewoo.net/wiki/Packing">this packed ints explanation</a> and <a href="https://amongus-debugger.vercel.app/tools">this online debugger</a> for more info.
	 *
	 * @param value Integer to pack
	 * @return packed bytes
	 */
	@NotNull
	public static byte[] packInteger(int value) {
		ArrayList<Byte> packedBytes = new ArrayList<>();

		do {
			byte bite = (byte) (value & 0b11111111);

			if (value >= 0b10000000) {
				bite |= 0b10000000;
			}

			value >>>= 7;
			packedBytes.add(bite);
		} while (value > 0);

		// Create Primitive Byte Array
		byte[] packedBytesPrimitive = new byte[packedBytes.size()];

		// Unbox Byte Class to byte
		for (int i = 0; i < packedBytesPrimitive.length; i++)
			packedBytesPrimitive[i] = packedBytes.get(i); // Apparently Unboxing Is Automatic

		return packedBytesPrimitive;
	}

	/**
	 * Public Domain (or Unlicense) Implementation of Unpacking An Integer For Among Us.
	 *
	 * <br><br>
	 * See <a href="https://wiki.weewoo.net/wiki/Packing">this packed ints explanation</a> and <a href="https://amongus-debugger.vercel.app/tools">this online debugger</a> for more info.
	 *
	 * @param packedBytes Packed Bytes to Unpack
	 * @return Signed Integer
	 */
	public static int unpackInteger(@NotNull byte... packedBytes) throws InvalidBytesException {
		if (packedBytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("invalid_bytes_null_exception"));

		boolean readMore = true;
		int shift = 0;
		int value = 0;

		int bite;
		int position = 0;
		while (readMore && (position < packedBytes.length)) {
			bite = (packedBytes[position] & 0xFF); // U-Int 8

			if (bite >= 0x80) {
				readMore = true;

				bite ^= 0x80;
			} else {
				readMore = false;
			}

			value |= bite << shift;
			shift += 7;

			position++;
		}

		return value;
	}
}