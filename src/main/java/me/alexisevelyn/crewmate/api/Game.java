package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {

    private final String code;
    private final byte[] codeBytes;
    private final List<Player> players = new ArrayList<>();

    public Game(String code) throws InvalidGameCodeException {
        this.code = code;
        this.codeBytes = GameCodeHelper.generateGameCodeBytes(code);
    }

    public Game(byte[] code) throws InvalidGameCodeException, InvalidBytesException {
        this.codeBytes = code;
        this.code = GameCodeHelper.parseGameCode(code);
    }

    public String getCode() {
        return code;
    }

    public byte[] getCodeBytes() {
        return codeBytes;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (players.size() == 0) {
            GameManager.removeGame(this);
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

}
