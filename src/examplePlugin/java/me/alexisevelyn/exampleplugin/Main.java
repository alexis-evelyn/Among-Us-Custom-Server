package me.alexisevelyn.exampleplugin;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.api.Plugin;

public class Main extends Plugin {
	@Override
	public void onEnable() {
		LogHelper.printLine("Example Plugin Enable!!! Will be replaced with translation friendly logging methods later!!!");
	}

	@Override
	public void onDisable() {
		LogHelper.printLine("Example Plugin Disable!!! Will be replaced with translation friendly logging methods later!!!");
	}

	@Override
	public String getID() {
		return "deprecated-load-from-manifest-please";
	}
}