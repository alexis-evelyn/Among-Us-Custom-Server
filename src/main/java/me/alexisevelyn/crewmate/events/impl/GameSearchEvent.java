package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.events.Event;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.SearchGamePacket;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.UnknownHostException;

public class GameSearchEvent extends Event {
    private final Language[] languages;
    private final Map[] maps;
    private final int impostors;
    private byte[] games;

    public GameSearchEvent(Language[] languages, int impostors, Map... maps) {
        this.languages = languages;
        this.maps = maps;
        this.impostors = impostors;

        try {
            this.games = SearchGamePacket.getFakeSearchBytes(impostors, getLanguage().getUnsignedInt(), maps);
        } catch (UnknownHostException e) {
            LogHelper.printLineErr(Main.getTranslationBundle().getString("search_unknown_host"));
            e.printStackTrace();

            games = ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("search_unknown_host"));
        }
    }

    public byte[] getGames() {
        return games;
    }

    public void setGames(byte... games) {
        // TODO: Change to a ArrayList of Games
        this.games = games;
    }

    public Language getLanguage() {
        return getLanguages()[0];
    }

    public Language[] getLanguages() {
        return languages;
    }

    public int getImpostors() {
        return impostors;
    }

    public Map[] getMaps() {
        return maps;
    }

}
