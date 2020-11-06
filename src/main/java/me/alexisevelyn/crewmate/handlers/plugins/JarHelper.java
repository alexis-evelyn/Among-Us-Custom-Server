package me.alexisevelyn.crewmate.handlers.plugins;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarHelper {
	public static String readTextJarEntry(JarFile jarFile, JarEntry jarEntry) throws IOException {
		return readTextJarEntry(jarFile, jarEntry, StandardCharsets.UTF_8);
	}

	public static String readTextJarEntry(JarFile jarFile, JarEntry jarEntry, Charset charset) throws IOException {
		return new String(readJarEntry(jarFile, jarEntry), charset);
	}

	@NotNull
	public static byte[] readJarEntry(JarFile jarFile, JarEntry jarEntry) throws IOException {
		InputStream inputStream = jarFile.getInputStream(jarEntry);
		long entrySize = jarEntry.getSize();

		if (entrySize == -1)
			return readJarEntryBytesUnknownSize(inputStream);

		return readJarEntryBytes(inputStream, entrySize);
	}

	// Testing With a File of Byte Size 21790432 (21 MB), this is at least 16 times faster than the unknown entrySize version (tested with M.2 Drive on Macbook Air)
	private static byte[] readJarEntryBytes(InputStream inputStream, long entrySize) throws IOException {
		byte[] entryBytes = new byte[(int) entrySize];

		//noinspection ResultOfMethodCallIgnored
		inputStream.read(entryBytes, 0, (int) entrySize);

		return entryBytes;
	}

	private static byte[] readJarEntryBytesUnknownSize(InputStream inputStream) throws IOException {
		return inputStream.readAllBytes();
	}
}
