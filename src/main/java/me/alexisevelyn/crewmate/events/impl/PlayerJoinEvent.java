package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.events.EventCancellable;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.net.InetAddress;

public class PlayerJoinEvent extends EventCancellable {

    private final InetAddress address;
    private final int port;
    private final String fullAddress;
    private final String gameCode;
    private final byte[] gameCodeBytes;

    // TODO: Add more data (when player id system is implemented.)
    public PlayerJoinEvent(String gameCode, InetAddress address, int port) throws InvalidGameCodeException {
        this.address = address;
        this.port = port;
        this.fullAddress = address.getHostAddress() + ":" + port;
        this.gameCode = gameCode;
        this.gameCodeBytes = GameCodeHelper.generateGameCodeBytes(gameCode);
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public byte[] getGameCodeBytes() {
        return gameCodeBytes;
    }

    public String getGameCode() {
        return gameCode;
    }

}
