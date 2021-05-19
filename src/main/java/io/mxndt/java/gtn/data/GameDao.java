package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;

import java.util.List;

/**
 * @author mxndt
 */
public interface GameDao {

    /**
     * starts a game
     *
     * @return - Game: game added with full data
     */
    Game createGame();

    /**
     * get all games
     *
     * @return - List<Game>: a list of all games
     */
    List<Game> getAllGames();

    /**
     * gets a game by id
     *
     * @param gameId - int: id of the game to be fetched
     * @return - Game: the game found under that id
     */
    Game getGame(int gameId);

    /**
     * sets a game's status
     *
     * @param gameId - int: the id of the game
     * @param inProgress - boolean: the status to be set
     * @return - boolean: true if item exists and is updated
     */
    boolean updateStatus(int gameId, boolean inProgress);
}