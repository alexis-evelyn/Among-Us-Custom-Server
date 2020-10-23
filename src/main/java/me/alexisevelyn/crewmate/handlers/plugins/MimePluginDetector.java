package me.alexisevelyn.crewmate.handlers.plugins;

import me.alexisevelyn.crewmate.LogHelper;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// https://stackoverflow.com/a/32863198/6828099

public class MimePluginDetector extends FileTypeDetector {
	/**
	 * Detect if file is a Crewmate Plugin.
	 * <br><br>
	 *
	 * {@inheritDoc}
	 * <br><br>
	 *
	 * @see Files#probeContentType
	 */
	@Override
	public String probeContentType(Path path) throws IOException {
		if (this.attemptReadJarFile(path))
			return "plugin/x-crewmate";

		return null;
	}

	private boolean attemptReadJarFile(Path path) {
		try {
			JarFile jarFile = new JarFile(path.toFile());

			Enumeration<JarEntry> files = jarFile.entries();

			while (files.hasMoreElements()) {
				JarEntry file = files.nextElement();

				// TODO: Only Check For File In Root
				if (file.getName().equals("crewmate.plugin.json"))
					return true;
			}
		} catch(Exception ignored) {
//			LogHelper.printLineErr("Exception: " + ignored.getMessage());
//			ignored.printStackTrace();
		}

		return false;
	}
}
