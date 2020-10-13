package me.alexisevelyn.crewmate;

import java.net.SocketException;

public class Main {
	// TODO: Add a region file generator - Same Format As https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

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