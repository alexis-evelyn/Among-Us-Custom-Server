package me.alexisevelyn.crewmate.handlers.plugins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.spi.FileTypeDetector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

// https://stackoverflow.com/a/32863198/6828099

public class MimePluginDetector extends FileTypeDetector {
	public static final String manifestFileName = "crewmate.plugin.json";
	public static final String mainClassKey = "main";
	public static final String mimeType = "plugin/x-crewmate";

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
	@Nullable
	public String probeContentType(@NotNull Path path) throws IOException {
		JarFile jarFile;
		JarEntry jarEntry;
		String manifestContents;

		// Try To Open Jar File
		try {
			jarFile = new JarFile(path.toFile());
			jarEntry = jarFile.getJarEntry(manifestFileName);

			manifestContents = JarHelper.readTextJarEntry(jarFile, jarEntry);
		} catch (IOException exception) {
			return null;
		}

		if (!validateManifest(manifestContents))
			return null;

		return mimeType;
	}

	private boolean validateManifest(String manifestContents) {
		JSONObject json;

		try {
			json = new JSONObject(manifestContents);
			json.getString(mainClassKey);
		} catch (JSONException exception) {
			return false;
		}

		// TODO: Validate Main Class Exists and Implements Proper Interface
		// LogHelper.printLine(manifestContents);

		return true;
	}
}
