package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.api.Game;
import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.util.HashMap;
import java.util.List;

public class GameManager {
    private static final HashMap<String, Game> gamesByCode = new HashMap<>();
    private static final HashMap<byte[], Game> gamesByCodeBytes = new HashMap<>();

    public static void addGame(Game game) {
        gamesByCode.put(game.getCode(), game);
        gamesByCodeBytes.put(game.getCodeBytes(), game);

        LogHelper.printLine(String.format(Main.getTranslationBundle().getString("added_game"), game.getCode()));
    }

    public static void removeGame(Game game) {
        try {
            removeGame(game.getCodeBytes());
        } catch (InvalidGameCodeException | InvalidBytesException ignored) {
            // Should never happen.
        }
    }

    public static void removeGame(String code) throws InvalidGameCodeException {
        gamesByCode.remove(code);
        gamesByCodeBytes.remove(GameCodeHelper.generateGameCodeBytes(code));
    }

    public static void removeGame(byte... code) throws InvalidGameCodeException, InvalidBytesException {
        gamesByCodeBytes.remove(code);
        gamesByCode.remove(GameCodeHelper.parseGameCode(code));
    }

    public static boolean existsWithCode(String code) {
        return gamesByCode.containsKey(code);
    }

    public static boolean existsWithCodeBytes(byte... code) {
        return gamesByCodeBytes.containsKey(code);
    }

    public static Game getGameByCode(String code) throws InvalidGameCodeException {
        if (!existsWithCode(code)) {
            addGame(new Game(code));
        }
        return gamesByCode.get(code);
    }

    public static void removePlayer(Player player) {
        for (Game game : gamesByCode.values()) {
            game.removePlayer(player);
        }
    }
}
