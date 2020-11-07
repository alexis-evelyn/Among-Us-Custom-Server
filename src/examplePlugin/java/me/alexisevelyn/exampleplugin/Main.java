package me.alexisevelyn.exampleplugin;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.api.Plugin;
import me.alexisevelyn.crewmate.api.ResourceBundleHandler;

public class Main extends Plugin {
	ResourceBundleHandler resourceBundleHandler;

	@Override
	public void onEnable() {
		// Not Recommended Method - Unsure How To Use
		resourceBundleHandler = new ResourceBundleHandler();
		resourceBundleHandler.registerResourceBundle(new TestResourceBundle());

		LogHelper.printLine(resourceBundleHandler.getString("example_plugin_enabled"));
	}

	//@Override
	public void onEnable(ResourceBundleHandler resourceBundleHandler) {
		resourceBundleHandler.registerResourceBundle(new TestResourceBundle());
	}

	@Override
	public void onDisable() {
		LogHelper.printLine(resourceBundleHandler.getString("example_plugin_disabled"));
	}

	@Override
	public String getID() {
		return "deprecated-load-from-manifest-please";
	}
}