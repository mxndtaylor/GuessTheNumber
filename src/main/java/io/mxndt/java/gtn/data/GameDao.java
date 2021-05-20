package io.mxndt.java.gtn.data;

import io.mxndt.java.gtn.models.Game;
import io.mxndt.java.gtn.service.GTNPersistenceException;

import java.util.List;

/**
 * @author mxndt
 */
public interface GameDao {

    /**
     * adds a game
     *
     * @return - Game: the game as represented in the data
     */
    Game add(Game game) throws GTNPersistenceException;

    /**
     * get all games
     *
     * @return - List<Game>: a list of all games
     */
    List<Game> getAllGames() throws GTNPersistenceException;

    /**
     * gets a game by id
     *
     * @param gameId - int: id of the game to be fetched
     * @return - Game: the game found under that id
     */
    Game getGame(int gameId) throws GTNPersistenceException;

    /**
     * sets a game's status
     *
     * @param gameId - int: the id of the game
     * @param inProgress - boolean: the status to be set
     * @return - boolean: true if item exists and is updated
     */
    boolean updateStatus(int gameId, boolean inProgress) throws GTNPersistenceException;
}