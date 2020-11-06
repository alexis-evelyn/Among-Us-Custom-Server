package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.events.EventCancellable;

import java.net.InetAddress;

public class PlayerJoinLobbyEvent extends EventCancellable {
    private final InetAddress address;
    private final int port;

    public PlayerJoinLobbyEvent(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public String getFullAddress() {
        return address.getHostAddress() + ":" + port;
    }
}
