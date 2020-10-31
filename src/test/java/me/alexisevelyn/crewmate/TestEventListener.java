package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.events.bus.EventHandler;
import me.alexisevelyn.crewmate.events.impl.*;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.nio.file.AccessDeniedException;

public class TestEventListener {
    Server server;

    @Test
    public void testEventListener() {
        try {
            this.server = new Server(new Config());
        } catch (SocketException | AccessDeniedException exception) {
            System.err.println(exception.getMessage());

            exception.printStackTrace();
        }

        server.getEventBus().register(this);

        // TODO: Figure Out Best Testing Practices For Event Handlers
        // Test Host
        HostGameEvent hostGameEvent = new HostGameEvent("CODE", 20, 6, Map.STICKMIN, new Language[] {Language.ENGLISH});
        this.onHostGame(hostGameEvent);
    }

    @EventHandler
    public void onPreJoin(PlayerJoinLobbyEvent event) {
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
        System.out.printf("Current Game Code: %s%n", event.getGameCode());

        // Check Initial Game Code
        assert event.getGameCode().equals("CODE");

        event.setGameCode("TEST");

        System.out.printf("New Game Code: %s%n", event.getGameCode());

        // Check Initial Game Code
        assert event.getGameCode().equals("TEST");
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
