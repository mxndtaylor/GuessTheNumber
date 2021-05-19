package io.mxndt.java.guessthenumber.service;

import io.mxndt.java.guessthenumber.models.Game;
import io.mxndt.java.guessthenumber.models.Round;

import java.util.List;

/**
 *
 * @author mxndt
 */
public interface GuessTheNumberServiceLayer {

    /**
     * Creates a new game
     *
     * @return - Game: the newly created game
     */
    Game createGame();

    /**
     * fetches a game based on its id, filtering out its answer
     *
     * @param gameId - int: id of the desired game
     * @return - Game: the desired game
     * @throws GuessTheNumberGameNotFoundException - thrown when
     *              the gameId does not match any games
     */
    Game getGame(int gameId) throws GuessTheNumberGameNotFoundException;

    /**
     * fetches all games, filtering out answers for unfinished games
     *
     * @return - List<Game>: the list of all games
     */
    List<Game> getGames();

    /**
     * makes a guess
     *
     * @param round - Round: contains a game id and guess, but not a time or a result
     * @return - Round: the input round, but with the time and result fields filled
     */
    Round guess(Round round);

    /**
     * Fetches all rounds for a game based on its id
     *
     * @param gameId - int: id of the game where the rounds were played
     * @return - List<Round>: list of all rounds in the game
     * @throws GuessTheNumberGameNotFoundException - thrown when
     *              the gameId does not match any games
     */
    List<Round> getGameRounds(int gameId) throws GuessTheNumberGameNotFoundException;
}
