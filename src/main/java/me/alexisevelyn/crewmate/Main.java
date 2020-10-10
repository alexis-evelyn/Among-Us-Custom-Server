package me.alexisevelyn.crewmate;

import java.net.SocketException;

public class Main {
	public static void main(String[] args) {
		System.out.println("Starting Server!!!");

		try {
			Server server = new Server();
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed To Bind To Socket!!!");
		}
	}
}