package me.alexisevelyn.crewmate;

import java.util.Scanner;

public class Terminal extends Thread {
	private boolean running = false;
	Scanner input;

	@Override
	public void run() {
		// For Cleaning Up When Shutdown
		// this.setupShutdownHook(); // TODO: Fix hanging before enabling this

		input = new Scanner(System.in);
		running = true;

		String command;
		while (running) {
			LogHelper.print("# ");

			// To Keep From Blocking
			if (!input.hasNextLine())
				continue;

			// Ensures Loop Runs Every Line
			command = input.nextLine();

			if (command.toLowerCase().equals("exit") && Main.getServer() != null && Main.getServer().isRunning()) {
				LogHelper.printLine("Shutting Down Server!!!");

				// Interrupt Seems To Do Nothing
				// Main.getServer().interrupt();

				// Shutdown Server
				// TODO: Figure out how to unblock thread for closing
				// Main.getServer().exit();

				// Shutdown Command Handler
				this.exit();
			}
		}

		input.close();
	}

	// Isn't this supposed to be overridable from Thread?
	public void exit() {
		if (this.running) {
			this.shutdown();
		}
	}

	private void shutdown() {
		this.running = false;
		this.input.close();
	}

	public boolean isRunning() {
		return this.running;
	}

	private void setupShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Main.getTerminal().exit()));
	}
}
