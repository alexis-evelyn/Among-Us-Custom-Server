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

			for (byte bite : byteArray)
				byteList.add(bite);
		}

		Byte[] mergedBytes = byteList.toArray(new Byte[0]);
		byte[] mergedBytesPrimitive = new byte[mergedBytes.length];

		// Sadly, native auto-unboxing Byte[] to byte[] does not seem to exist. Could be wrong though.
		for (int i = 0; i < mergedBytesPrimitive.length; i++)
			mergedBytesPrimitive[i] = mergedBytes[i]; // Apparently Unboxing Is Automatic

		return mergedBytesPrimitive;
	}

  /**
   * Helper utility to merge byte arrays together without having to manually create a new byte[] for
   * single byte values. Unsupported data will just be dropped silently.
   * <br><br>
   *
   * This method internally creates a new byte[][] and then calls {@link #mergeBytes(byte[]...)}.
   * <br><br>
   *
   * Example 1:
   * <br>
   * {@code
   * PacketHelper.mergeBytes(new byte[] {-1, -2, -3, 0}, (byte) 1, new byte[] {2, 3, 4, 5});
   * }
   * <br><br>
   *
   * Example 2:
   * <br>
   * {@code
   * byte[] test = PacketHelper.mergeBytes(new byte[] {-1, -2, -3, 0}, new Byte((byte) 1), new byte[] {2, 3, 4, 5});
   * }
   *
   * @param bytes byte[] (Not Byte[]) and or byte of values to merge together into one giant byte[].
   * @return merged values in a byte[]
   */
	@NotNull
	public static byte[] mergeBytes(@NotNull Object... bytes) {
		// Separate Out Bytes From Byte Arrays And Convert Bytes To Byte Arrays
		ArrayList<byte[]> byteList = new ArrayList<>();
		for (Object bite : bytes) {
			// If Byte or byte (Is autoboxed as Byte if originally a byte), then create new byte[] and add to ArrayList
			if (bite instanceof Byte)
				byteList.add(new byte[] {(byte) bite});

			// If byte[], then just simply add to ArrayList
			else if (bite instanceof byte[])
				byteList.add((byte[]) bite);
		}

		// Convert byte[] ArrayList to byte[][] for further processing
		return mergeBytes(byteList.toArray(new byte[0][0]));
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
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (bytes.length <= pos)
			return bytes;

		return Arrays.copyOfRange(bytes, pos, bytes.length);
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
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (pos <= 0)
			return bytes;

		if (bytes.length <= pos)
			return bytes;

		return Arrays.copyOfRange(bytes, 0, pos);
	}

	/**
	 * Helper utility to convert int into a little endian ordered byte array
	 *
	 * @param value The int to be converted
	 * @return The byte array to be used as you wish
	 */
	@NotNull
	public static byte[] convertIntToBE(int value) {
		return new byte[] {(byte) ((value >> 24) & 0xff), (byte) ((value >> 16) & 0xff), (byte) ((value >> 8) & 0xff), (byte) (value & 0xff)};
	}

	/**
	 * Helper utility to convert int into a little endian ordered byte array
	 *
	 * @param value The int to be converted
	 * @return The byte array to be used as you wish
	 */
	@NotNull
	public static byte[] convertIntToLE(int value) {
		return new byte[] {(byte) (value & 0xff), (byte) ((value >> 8) & 0xff), (byte) ((value >> 16) & 0xff), (byte) ((value >> 24) & 0xff)};
	}

	/**
	 * Helper utility to convert short into a big endian ordered byte array
	 *
	 * @param value The short to be converted
	 * @return The byte array to be used as you wish
	 */
	@NotNull
	public static byte[] convertShortToBE(short value) {
		return new byte[] {(byte) (value >> 8 & 0xff), (byte) ((value) & 0xff)};
	}

	/**
	 * Helper utility to convert short into a little endian ordered byte array
	 *
	 * @param value The short to be converted
	 * @return The byte array to be used as you wish
	 */
	@NotNull
	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte) (value & 0xff), (byte) ((value >> 8) & 0xff)};
	}

	/**
	 * Flips bytes and internally calls {@link #getUnsignedShortLE(byte...)}
	 *
	 * @param shortBytes 2 byte array in Big Endian form
	 * @return unsigned short formatted as a signed int
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static int getUnsignedShortBE(@NotNull byte... shortBytes) throws InvalidBytesException {
		if (shortBytes == null)
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (shortBytes.length != 2)
			throw new InvalidBytesException(String.format(Main.getTranslation("invalid_number_of_bytes_exact"), 2));

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
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (shortBytes.length != 2)
			throw new InvalidBytesException(String.format(Main.getTranslation("invalid_number_of_bytes_exact"), 2));

		// Tutorial: https://stackoverflow.com/a/28580238/6828099
		return (shortBytes[0] & 0xff) | ((shortBytes[1] & 0xff) << 8);
	}

	/**
	 * Flips bytes and internally calls {@link #getUnsignedIntLE(byte...)}
	 *
	 * @param intBytes 4 byte array in Big Endian form
	 * @return unsigned int formatted as a signed long
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static long getUnsignedIntBE(@NotNull byte... intBytes) throws InvalidBytesException {
		if (intBytes == null)
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (intBytes.length != 4)
			throw new InvalidBytesException(String.format(Main.getTranslation("invalid_number_of_bytes_exact"), 4));

		return getUnsignedIntLE(intBytes[3], intBytes[2], intBytes[1], intBytes[0]);
	}

	/**
	 * Retrieves an unsigned int and stores as a signed int.
	 *
	 * An int is 4 bytes or is otherwise known as being 32 bit.
	 *
	 * Java doesn't have unsigned data types except for char.
	 *
	 * See <a href="https://stackoverflow.com/a/6850755/6828099">this Stackoverflow Answer</a> for more details.
	 * Also see <a href="https://www.darksleep.com/player/JavaAndUnsignedTypes.html">this page</a> for an in depth explanation on handling signed/unsigned data types.
	 *
	 * @param intBytes 4 byte array in Little Endian form
	 * @return unsigned int formatted as a signed long
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static long getUnsignedIntLE(@NotNull byte... intBytes) throws InvalidBytesException {
		if (intBytes == null)
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

		if (intBytes.length != 4)
			throw new InvalidBytesException(String.format(Main.getTranslation("invalid_number_of_bytes_exact"), 4));

		// Tutorial: https://stackoverflow.com/a/28580238/6828099
		return (intBytes[0] & 0xff) | ((intBytes[1] & 0xff) << 8) | ((intBytes[2] & 0xff) << 16) | ((intBytes[3] & 0xff) << 24) & (-1L >>> 32);
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
			throw new InvalidBytesException(Main.getTranslation("invalid_bytes_null_exception"));

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

	/**
	 * Just a wrapper that internally calls {@link #getUnsignedIntLE(byte...)}
	 *
	 * @param floatBytes 4 byte array in Little Endian form
	 * @return unsigned float formatted as a signed long
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static long getUnsignedFloatLE(@NotNull byte... floatBytes) throws InvalidBytesException {
		return getUnsignedIntLE(floatBytes);
	}

	/**
	 * Just a wrapper that internally calls {@link #getUnsignedIntBE(byte...)}
	 *
	 * @param floatBytes 4 byte array in Big Endian form
	 * @return unsigned float formatted as a signed long
	 * @throws InvalidBytesException for not providing the correct number of bytes
	 */
	public static long getUnsignedFloatBE(@NotNull byte... floatBytes) throws InvalidBytesException {
		return getUnsignedIntBE(floatBytes);
	}
}