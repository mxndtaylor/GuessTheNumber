package io.mxndt.java.guessthenumber.data;

import io.mxndt.java.guessthenumber.models.Round;

import java.util.List;

/**
 * @author mxndt
 */
public interface RoundDao {

    /**
     * Adds a Round
     *
     * @param round - Round: the round to be added
     * @return - Round: the round as it is stored
     */
    Round add(Round round);

    /**
     * fetches all rounds for a certain game
     *
     * @param gameId - int: id of the game whose rounds are to be fetched
     * @return - List<Round>: the list of all rounds associated with the game
     */
    List<Round> getRounds(int gameId);
}
