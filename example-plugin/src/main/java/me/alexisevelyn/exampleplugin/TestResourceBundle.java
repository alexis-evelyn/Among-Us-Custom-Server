package me.alexisevelyn.exampleplugin;

import java.util.ListResourceBundle;

public class TestResourceBundle extends ListResourceBundle {
	// https://docs.oracle.com/javase/tutorial/i18n/resbundle/list.html

	@Override
	protected Object[][] getContents() {
		return contents;
	}

	private final Object[][] contents = {
			{ "example_test", 100},
			{ "example_hello", "Hello World!!!"},
			{ "example_random", Math.random()},
			{ "example_random", Math.random()},
			{ "example_plugin_enabled", "Example Plugin Enabled!!!"},
			{ "example_plugin_disabled", "Example Plugin Disabled!!!"},
	};
}
