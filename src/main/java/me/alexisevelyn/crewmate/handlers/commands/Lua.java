package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class Lua implements Command {
	public void execute(String command, Terminal terminal) {
		// This is setup to capture Lua Output so I can manipulate it and log it how I want

		// Initialize Resources
		Globals globals = JsePlatform.standardGlobals();

		// Setup PrintStream to Capture Output - https://stackoverflow.com/a/47680140/6828099
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(baos, true, StandardCharsets.UTF_8);

		// Change Output to PrintStream (So I Can Capture Output And Handle Logging Myself)
		globals.STDOUT = printStream;

		// Setup Script and Sanitize Translation
		String preGreetingScript = "print '%s'";
		String postGreetingScript = String.format(preGreetingScript, Main.getTranslation("test_lua_greeting").replace("'", "\\'"));

		// Load and Execute Script
		LuaValue chunk = globals.load(postGreetingScript);
		chunk.call();

		// Read Output
		String output = new String(baos.toByteArray(), StandardCharsets.UTF_8).trim();

		// Shutdown Streams
		printStream.close();

		// Output Captured Output
		LogHelper.printLine(output);
	}

	@Override
	public String getCommand() {
		return Main.getTranslation("lua_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslation("lua_command_help");
	}
}
