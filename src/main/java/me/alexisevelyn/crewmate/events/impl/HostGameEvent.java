package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.events.Event;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;

public class HostGameEvent extends Event {
    private String gameCode;
    private final int maxPlayers;
    private final int impostorCount;
    private final Map map;
    private final Language language;

    public HostGameEvent(String gameCode, int maxPlayers, int impostorCount, Map map, Language language) {
        this.gameCode = gameCode;
        this.maxPlayers = maxPlayers;
        this.impostorCount = impostorCount;
        this.map = map;
        this.language = language;
    }

    public void setGameCode(String gameCode) throws IllegalArgumentException {
        if (gameCode.length() != 4 && gameCode.length() != 6)
            throw new IllegalArgumentException(Main.getTranslationBundle().getString("gamecode_wrong_length"));

        this.gameCode = gameCode;
    }

    public String getGameCode() {
        return gameCode;
    }

    public Map getMap() {
        return map;
    }

    public int getImpostorCount() {
        return impostorCount;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Language getLanguage() {
        return language;
    }

}
