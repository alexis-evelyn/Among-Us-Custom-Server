package me.alexisevelyn.crewmate.api;

import org.apiguardian.api.API;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Not Complete, But Functional
 *
 * <br><br>
 * I plan on doing what Minecraft does and prepending the base game/mod id to the beginning. E.g. crewmate_key for base and example_key for the example plugin. This will be based on the plugin/mod's id.
 * <br><br>
 *
 * I'm also trying to figure out how to get the PropertyKey annotation to work for class files too and not just a hardcoded translation file.
 */
@API(status = API.Status.EXPERIMENTAL)
public final class ResourceBundleHandler {
	private final List<ListResourceBundle> resourceBundleList = new ArrayList<>();

	// Default Bundle
	@NonNls public static final String DEFAULT_BUNDLE_PATH = "translations.Main";
	private final ResourceBundle defaultResourceBundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_PATH);

	@Nullable
	public Object getResource(@NotNull String key) {
		// Check Default Resource Bundle First
		if (defaultResourceBundle.containsKey(key))
			return defaultResourceBundle.getObject(key);

		// If None Contains The Key, Don't Bother Looping
		if (resourceBundleList.stream().noneMatch(resourceBundle -> resourceBundle.containsKey(key)))
			return null;

		// Find First Matching Value For Key
		AtomicReference<Object> value = new AtomicReference<>();
		resourceBundleList.stream().takeWhile(resourceBundle -> value.get() == null).forEach(resourceBundle -> {
			if (resourceBundle.containsKey(key))
				value.set(resourceBundle.getObject(key));
		});

		return value.get();
	}

	@Nullable
	public String getString(@NotNull @PropertyKey(resourceBundle = DEFAULT_BUNDLE_PATH) String key) {
		// Check Default Resource Bundle First
		if (defaultResourceBundle.containsKey(key))
			return defaultResourceBundle.getString(key);

		// If None Contains The Key, Don't Bother Looping
		if (resourceBundleList.stream().noneMatch(resourceBundle -> resourceBundle.containsKey(key)))
			return null;

		// Find First Matching Value For Key
		AtomicReference<String> value = new AtomicReference<>();
		resourceBundleList.stream().takeWhile(resourceBundle -> value.get() == null).forEach(resourceBundle -> {
			if (resourceBundle.containsKey(key) && resourceBundle.getObject(key) instanceof String)
				value.set(resourceBundle.getString(key));
		});

		return value.get();
	}

	/**
	 * Register Resource Bundle With Locale
	 *
	 * @param resourceBundle the resource bundle to register
	 */
	public final void registerResourceBundle(ListResourceBundle resourceBundle) {
		// Append To Existing List If Exists (But Only Once)
		if (!resourceBundleList.contains(resourceBundle))
			resourceBundleList.add(resourceBundle);
	}

	/**
	 * Retrieve list of resource bundles
	 *
	 * @return list of resource bundles
	 */
	public final List<ListResourceBundle> getResourceBundles() {
		return new ArrayList<>(resourceBundleList); // The new ArrayList is to prevent editing original list outside of class
	}

	/**
	 * Remove a Resource Bundle From The Specific Locale
	 *
	 * @param resourceBundle resource bundle to remove
	 */
	public final void unregisterResourceBundle(ListResourceBundle resourceBundle) {
		// Retrieve And Remove Every Instance of the ResourceBundle From The List
		resourceBundleList.removeIf(storedResourceBundle -> storedResourceBundle.equals(resourceBundle));
	}
}
