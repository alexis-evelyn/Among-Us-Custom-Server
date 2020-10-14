package me.alexisevelyn.crewmate.terminal;

import me.alexisevelyn.crewmate.Main;

import java.util.Scanner;

public class CommandHandler extends Thread {
	private boolean running = false;
	Scanner input;

	@Override
	public void run() {
		input = new Scanner(System.in);
		running = true;

		String command;
		while (running) {
			System.out.print("# ");
			command = input.next();

			if (command.toLowerCase().equals("exit") && Main.getServer().isRunning()) {
				System.out.println("Shutting Down Server!!!");

				// Interrupt Seems To Do Nothing
				// Main.getServer().interrupt();

				// Shutdown Server
				// TODO: Figure out how to unblock thread for closing
				Main.getServer().exit();

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

	public void shutdown() {
		this.running = false;
		this.input.close();
	}

	public boolean isRunning() {
		return this.running;
	}
}
