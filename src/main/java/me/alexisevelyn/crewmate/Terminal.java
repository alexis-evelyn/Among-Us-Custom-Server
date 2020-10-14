package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.handlers.CommandHandler;

import java.util.Scanner;

public class Terminal extends Thread {
	// This prefix will be changeable via commands in the future
	private String prefix = "# ";
	private boolean running = false;
	Scanner input;

	@Override
	public void run() {
		// For Cleaning Up When Shutdown
		// this.setupShutdownHook(); // This currently blocks

		input = new Scanner(System.in);
		running = true;

		String command;
		while (this.isRunning()) {
			try {
				// This is useless as it doesn't stop Input Scanner
				if (this.isInterrupted())
					throw new InterruptedException();

				LogHelper.print(this.prefix);

				// To Keep From Blocking
				if (!input.hasNextLine())
					continue;

				// Ensures Loop Runs Every Line
				command = input.nextLine();

				// Exit Thread If Server Not Running
				if (Main.getServer() == null || !Main.getServer().isRunning()) {
					this.exit();
					return;
				}

				CommandHandler.handleCommand(command, this);
			} catch (InterruptedException e) {
				// This is due to recommendations to rethrow the interrupt after catching it
				// https://stackoverflow.com/a/1087504/6828099
				Thread.currentThread().interrupt();

				this.exit();
			}
		}

		input.close();
	}

	// Isn't this supposed to be overridable from Thread?
	public void exit() {
		if (this.running)
			this.shutdown();
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
