package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.events.bus.EventHandler;
import me.alexisevelyn.crewmate.events.impl.*;
import me.alexisevelyn.crewmate.handlers.PlayerManager;

public class TestEventListener {

    public TestEventListener(Server server) {
        server.getEventBus().register(this);
    }

    @EventHandler
    public void onPreJoin(PlayerPreJoinEvent event) {
        //event.setCancelled(true, "Kicked during PreJoin");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //event.setCancelled(true, "Kicked during Join");
        //PlayerManager.getPlayerByAddress(event.getAddress(), event.getPort()).kick("funi");
        for (Player player : PlayerManager.getPlayers()) {
            LogHelper.printLine(player.getName());
        }
    }

    @EventHandler
    public void onHostGame(HostGameEvent event) {
        event.setGameCode("TEST");
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        LogHelper.printLine(event.getMessage());
    }

    @EventHandler
    public void onChangeColour(PlayerChangeColorEvent event) {
        LogHelper.printLine(event.getColor());
    }

    @EventHandler
    public void onChangeHat(PlayerChangeHatEvent event) {
        LogHelper.printLine(event.getHat());
    }

    @EventHandler
    public void onChangePet(PlayerChangePetEvent event) {
        LogHelper.printLine(event.getPet());
    }

    @EventHandler
    public void onChangeSkin(PlayerChangeSkinEvent event) {
        LogHelper.printLine(event.getSkin());
    }

    @EventHandler
    public void onChangeVisibility(ChangeVisibilityEvent event) {
        event.setVisible(false);
    }

    @EventHandler
    public void onRequestGamesList(GameSearchEvent event) {
        LogHelper.printLine(event.getLanguage());
    }

}
