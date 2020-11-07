package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.events.Event;

public class HostGameEvent extends Event {
    private String gameCode;
    private final int maxPlayers;
    private final int impostorCount;
    private final Map map;
    private final Language[] languages;

    public HostGameEvent(String gameCode, int maxPlayers, int impostorCount, Map map, Language[] languages) {
        this.gameCode = gameCode;
        this.maxPlayers = maxPlayers;
        this.impostorCount = impostorCount;
        this.map = map;
        this.languages = languages;
    }

    public void setGameCode(String gameCode) throws IllegalArgumentException {
        if (gameCode.length() != 4 && gameCode.length() != 6)
            throw new IllegalArgumentException(Main.getTranslation("gamecode_wrong_length"));

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

    public Language[] getLanguages() {
        return languages;
    }

    public Language getLanguage() {
        return getLanguages()[0];
    }
}
